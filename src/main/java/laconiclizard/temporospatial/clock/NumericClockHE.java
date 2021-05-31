package laconiclizard.temporospatial.clock;

import laconiclizard.hudelements.AlterHudScreen;
import laconiclizard.hudelements.api.HudElement;
import laconiclizard.temporospatial.TSConfig;
import laconiclizard.temporospatial.TSHudElement;
import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.Util;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NumericClockHE extends TSHudElement {

    public static final Object INSTANCES_LOCK = new Object();
    public static final List<NumericClockHE> INSTANCES = new ArrayList<>();

    public final NumericClock_Config config;
    // local copies of config settings
    private float scale;
    private int textColor, backgroundColor, borderColor;
    private float borderThickness;
    private boolean realTime;
    private String realTimeFormatString;
    private boolean minecraftTime_isAbsolute;
    private boolean absoluteMinecraftTime_separateDays;

    private DateFormat realTimeFormat;
    private long lastTime = -1;
    private String lastTimeString = null;

    public NumericClockHE(NumericClock_Config config) {
        super();
        synchronized (lock) {
            this.config = config;
            updateFromConfig();
        }
        synchronized (INSTANCES_LOCK) {
            INSTANCES.add(this);
        }
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
        if (realTime) {
            return realTimeFormat.format(new Date());
        } else {
            ClientWorld w = MinecraftClient.getInstance().world;
            if (w == null) return "";
            long t = w.getTimeOfDay();
            if (t == lastTime && lastTimeString != null) {
                return lastTimeString;
            }
            lastTimeString = null;
            if (minecraftTime_isAbsolute) {
                if (absoluteMinecraftTime_separateDays) {
                    return lastTimeString = ((t / 24000) + "d " + t % 24000 + "t");
                } else {
                    return lastTimeString = (String.valueOf(t));
                }
            } else {
                return lastTimeString = (String.valueOf((t + 24000) % 24000));
            }
        }
    }

    /**
     * Saves all NumericClockHE s.
     *
     * @param noSync a NumericClockHE that has already had its .lock acquired, or null if no such NumericClockHE exists.
     */
    public static void saveAll(NumericClockHE noSync) {
        List<NumericClock_Config> configs;
        synchronized (INSTANCES_LOCK) {
            configs = new ArrayList<>(INSTANCES.size());
            for (NumericClockHE cw : INSTANCES) {
                if (cw == noSync) {
                    configs.add(new NumericClock_Config(cw.config));
                } else {
                    synchronized (cw.lock) {
                        configs.add(new NumericClock_Config(cw.config));  // copy
                    }
                }
            }
        }
        synchronized (Temporospatial.CONFIG_HOLDER_LOCK) {
            TSConfig config = Temporospatial.CONFIG_HOLDER.getConfig();
            config.numericClocks = configs;
            Temporospatial.CONFIG_HOLDER.save();
        }
    }

    @Override public void save() {
        config.enabled = isEnabled();
        config.x = getX();
        config.y = getY();
        config.z = getZ();
        saveAll(this);
    }

    @Override public void render(MatrixStack matrices, float tickDelta) {
        float x = getX(), y = getY(), w = getWidth(), h = getHeight();
        Util.scaleAbout(x, y, 0, scale, scale, 1);
        laconiclizard.hudelements.Util.fill(matrices.peek().getModel(), x, y, x + w, y + h, backgroundColor);  // background
        MinecraftClient.getInstance().textRenderer.draw(matrices, getTime(), x, y, textColor);  // text
        laconiclizard.hudelements.Util.drawBorder(matrices, x, y, x + w, y + h, borderThickness, borderColor);  // border
        Util.scaleAbout(x, y, 0, 1 / scale, 1 / scale, 1);
    }

    @Override public int getWidth() {
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

    @Override public int getHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
    }

    @Override public boolean isEditable() {
        return true;
    }

    @Override public void edit() {
        synchronized (NumericClock_ConfigSerializer.BACKER_LOCK) {
            NumericClock_ConfigSerializer.BACKER = this;
        }
        synchronized (Temporospatial.NUMERIC_CLOCK_CONFIG_HOLDER_LOCK) {
            Temporospatial.NUMERIC_CLOCK_CONFIG_HOLDER.load();
        }
        MinecraftClient.getInstance().openScreen(
                AutoConfig.getConfigScreen(NumericClock_Config.class,
                        new NumericClock_ConfigSerializer.ReturnScreen(new AlterHudScreen())
                ).get());
    }

}
