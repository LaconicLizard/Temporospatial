package laconiclizard.temporospatial.clock;

import laconiclizard.hudelements.AlterHudScreen;
import laconiclizard.temporospatial.TSConfig;
import laconiclizard.temporospatial.util.InstanceTracker;
import laconiclizard.temporospatial.util.TSHudElement;
import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.Util;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class NumericClockHE extends TSHudElement<NumericClock_Config> {

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

    // local copies of config settings
    private float scale;
    private int textColor, backgroundColor, borderColor;
    private float borderThickness;
    private boolean realTime;
    private String realTimeFormatString;
    private boolean minecraftTime_isAbsolute;
    private boolean absoluteMinecraftTime_separateDays;
    private boolean worksEverywhere;

    private DateFormat realTimeFormat;
    private long lastTime = -1;
    private String lastTimeString = null;

    public NumericClockHE(NumericClock_Config config) {
        super(config);
        updateFromConfig();
        INSTANCES.add(this);
    }

    /**
     * Updates the behavior of this NumericClockHE to reflect the current state of its config.
     * Assumes .lock has already been acquired.
     */
    public void updateFromConfig() {
        setEnabled(config.enabled);
        setX(config.x);
        setY(config.y);
        setZ(config.z);
        scale = config.scale;
        textColor = config.textColor;
        backgroundColor = config.backgroundColor;
        borderColor = config.borderColor;
        borderThickness = config.borderThickness;
        realTime = config.realTime;
        realTimeFormatString = config.realTimeFormat;
        realTimeFormat = new SimpleDateFormat(realTimeFormatString);
        minecraftTime_isAbsolute = config.minecraftTime_isAbsolute;
        absoluteMinecraftTime_separateDays = config.absoluteMinecraftTime_separateDays;
        worksEverywhere = config.worksEverywhere;
        // clear cache
        lastTime = -1;
        lastTimeString = null;
    }

    /**
     * Get representation of the current time as a string.
     * Assumes that .lock as already been acquired.
     *
     * @return string representation of the current time
     */
    private String getTime() {
        ClientWorld w = MinecraftClient.getInstance().world;
        if (w == null) return "";
        long currentTime = w.getTimeOfDay();
        // return cached answer if we are still in the same tick
        if (currentTime == lastTime && lastTimeString != null) {
            return lastTimeString;
        }
        lastTime = currentTime;
        lastTimeString = null;

        final boolean works = w.getDimension().isNatural() || worksEverywhere;

        if (realTime) {
            Date d;
            if (works) {
                d = new Date();
            } else {
                d = new Date(RANDOM.nextLong(RANDOM_DATE_LOW, RANDOM_DATE_HIGH));
            }
            return lastTimeString = realTimeFormat.format(d);
        } else {
            if (!works) {
                currentTime = RANDOM.nextInt(24000 * 100);
            }
            if (minecraftTime_isAbsolute) {
                if (absoluteMinecraftTime_separateDays) {
                    return lastTimeString = ((currentTime / 24000) + "d " + currentTime % 24000 + "t");
                } else {
                    return lastTimeString = String.valueOf(currentTime);
                }
            } else {
                return lastTimeString = String.valueOf((currentTime + 24000) % 24000);
            }
        }
    }

    @Override public void saveAll() {
        List<NumericClock_Config> configs;
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

    @Override public void save() {
        config.x = getX();
        config.y = getY();
        this.saveAll();
    }

    @Override public void render(MatrixStack matrices, float tickDelta) {
        float x = getX(), y = getY(), w = getWidth(), h = getHeight();
        Util.scaleAbout(x, y, 0, scale, scale, 1);
        laconiclizard.hudelements.Util.fill(matrices.peek().getModel(), x, y, x + w, y + h, backgroundColor);  // background
        MinecraftClient.getInstance().textRenderer.draw(matrices, getTime(), x, y, textColor);  // text
        laconiclizard.hudelements.Util.drawBorder(matrices, x, y, x + w, y + h, borderThickness, borderColor);  // border
        Util.scaleAbout(x, y, 0, 1 / scale, 1 / scale, 1);
    }

    @Override public float getWidth() {
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        if (realTime) {
            return tr.getWidth(realTimeFormatString);
        } else {
            if (minecraftTime_isAbsolute) {
                return tr.getWidth(getTime());
            } else {
                return tr.getWidth("23999");
            }
        }
    }

    @Override public float getHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
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
                AutoConfig.getConfigScreen(NumericClock_Config.class,
                        Temporospatial.NUMERIC_CLOCK_CONFIG_SERIALIZER.returnScreen(new AlterHudScreen()))
                        .get());
    }

}
