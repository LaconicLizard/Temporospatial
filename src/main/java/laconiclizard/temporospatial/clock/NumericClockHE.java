package laconiclizard.temporospatial.clock;

import laconiclizard.hudelements.AlterHudScreen;
import laconiclizard.temporospatial.TSConfig;
import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceTracker;
import laconiclizard.temporospatial.util.TSTextHudElement;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.world.ClientWorld;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class NumericClockHE extends TSTextHudElement<NumericClockHE_Config> {

    public static final InstanceTracker<NumericClockHE> INSTANCES = new InstanceTracker<>();
    private static final long RANDOM_DATE_LOW, RANDOM_DATE_HIGH;

    static {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            RANDOM_DATE_LOW = f.parse("0000-01-01").getTime();
            RANDOM_DATE_HIGH = f.parse("9999-12-31").getTime();
        } catch (ParseException e) {
            throw new AssertionError("Invalid initialization of RANDOM_DATE_*.");
        }
    }

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private boolean cachedConfig_realTime;
    private String cachedConfig_realTimeFormatString;
    private boolean cachedConfig_minecraftTime_isAbsolute;
    private boolean cachedConfig_absoluteMinecraftTime_separateDays;
    private boolean cachedConfig_worksEverywhere;

    private DateFormat realTimeFormat;

    public NumericClockHE(NumericClockHE_Config config) {
        super(config);
        updateFromConfig();
        INSTANCES.add(this);
    }

    /**
     * Updates the behavior of this NumericClockHE to reflect the current state of its config.
     * Assumes .lock has already been acquired.
     */
    public void updateFromConfig() {
        super.updateFromConfig();
        cachedConfig_realTime = config.realTime;
        cachedConfig_realTimeFormatString = config.realTimeFormat;
        realTimeFormat = new SimpleDateFormat(cachedConfig_realTimeFormatString);
        cachedConfig_minecraftTime_isAbsolute = config.minecraftTime_isAbsolute;
        cachedConfig_absoluteMinecraftTime_separateDays = config.absoluteMinecraftTime_separateDays;
        cachedConfig_worksEverywhere = config.worksEverywhere;
    }

    @Override public String generateText() {
        ClientWorld w = MinecraftClient.getInstance().world;
        if (w == null) return "";
        long currentTime = w.getTimeOfDay();
        final boolean works = w.getDimension().isNatural() || cachedConfig_worksEverywhere;

        if (cachedConfig_realTime) {
            Date d;
            if (works) {
                d = new Date();
            } else {
                d = new Date(RANDOM.nextLong(RANDOM_DATE_LOW, RANDOM_DATE_HIGH));
            }
            return realTimeFormat.format(d);
        } else {
            if (!works) {
                currentTime = RANDOM.nextInt(24000 * 100);
            }
            if (cachedConfig_minecraftTime_isAbsolute) {
                if (cachedConfig_absoluteMinecraftTime_separateDays) {
                    return currentTime / 24000 + "d " + currentTime % 24000 + "t";
                } else {
                    return String.valueOf(currentTime);
                }
            } else {
                return String.valueOf((currentTime + 24000) % 24000);
            }
        }
    }

    @Override public void saveAll() {
        List<NumericClockHE_Config> configs;
        synchronized (INSTANCES.lock) {
            configs = new ArrayList<>(INSTANCES.instances.size());
            for (NumericClockHE cw : INSTANCES.instances) {
                if (cw == this) {
                    configs.add(cw.config.copy());
                } else {
                    synchronized (cw.lock) {
                        configs.add(cw.config.copy());
                    }
                }
            }
        }
        synchronized (Temporospatial.CONFIG_HOLDER.lock) {
            TSConfig config = Temporospatial.CONFIG_HOLDER.value.getConfig();
            config.numericClocks = configs;
            Temporospatial.CONFIG_HOLDER.value.save();
        }
    }

    @Override public float getWidth() {
        // dev note: override so times like "128" keep left-padded space without zeros
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        if (cachedConfig_realTime) {
            return tr.getWidth(cachedConfig_realTimeFormatString);
        } else {
            if (cachedConfig_minecraftTime_isAbsolute) {
                return tr.getWidth(getText());
            } else {
                return tr.getWidth("23999");
            }
        }
    }

    @Override public boolean isEditable() {
        return true;
    }

    @Override public void edit() {
        Temporospatial.NUMERIC_CLOCK_CONFIG_SERIALIZER.setBacker(this);
        synchronized (Temporospatial.NUMERIC_CLOCK_CONFIG_HOLDER.lock) {
            Temporospatial.NUMERIC_CLOCK_CONFIG_HOLDER.value.load();
        }
        MinecraftClient.getInstance().openScreen(
                AutoConfig.getConfigScreen(NumericClockHE_Config.class,
                        Temporospatial.NUMERIC_CLOCK_CONFIG_SERIALIZER.returnScreen(new AlterHudScreen()))
                        .get());
    }

}
