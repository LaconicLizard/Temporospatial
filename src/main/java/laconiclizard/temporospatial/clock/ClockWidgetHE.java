package laconiclizard.temporospatial.clock;

import laconiclizard.hudelements.AlterHudScreen;
import laconiclizard.hudelements.api.HudElement;
import laconiclizard.temporospatial.TSConfig;
import laconiclizard.temporospatial.TSHudElement;
import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.Util;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ClockWidgetHE extends TSHudElement {

    public static final Object INSTANCES_LOCK = new Object();
    public static final List<ClockWidgetHE> INSTANCES = new ArrayList<>();

    public final ClockWidget_Config config;  // note: use .lock to synchronize any reads/writes on this
    private final ItemStack clockStack = new ItemStack(Items.CLOCK);

    public ClockWidgetHE(ClockWidget_Config config) {
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
     * Updates the behavior of this ClockWidgetHE to reflect the current state of its config.
     * Assumes .lock has already been acquired.
     */
    public void updateFromConfig() {
        setEnabled(config.enabled);
        setX(config.x);
        setY(config.y);
        setZ(config.z);
        Clock_MPP.setRealtime(clockStack, config.realTime);
        Clock_MPP.setPreventSwing(clockStack, config.preventSwing);
        Clock_MPP.setWorksEverywhere(clockStack, config.worksEverywhere);
    }

    /**
     * Saves all ClockWidgetHE s.
     *
     * @param noSync a ClockWidgetHE that has already had its .lock acquired, or null if no such ClockWidgetHE exists.
     */
    public static void saveAll(ClockWidgetHE noSync) {
        List<ClockWidget_Config> configs;
        synchronized (INSTANCES_LOCK) {
            configs = new ArrayList<>(INSTANCES.size());
            for (ClockWidgetHE cw : INSTANCES) {
                if (cw == noSync) {
                    configs.add(new ClockWidget_Config(cw.config));
                } else {
                    synchronized (cw.lock) {
                        configs.add(new ClockWidget_Config(cw.config));  // copy
                    }
                }
            }
        }
        synchronized (Temporospatial.CONFIG_HOLDER_LOCK) {
            TSConfig config = Temporospatial.CONFIG_HOLDER.getConfig();
            config.clockWidgets = configs;
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
        float x = getX(), y = getY();
        float scale = config.scale;
        Util.scaleAbout(x, y, 0, scale, scale, 1);
        MinecraftClient.getInstance().getItemRenderer().renderInGuiWithOverrides(clockStack, (int) x, (int) y);
        Util.scaleAbout(x, y, 0, 1 / scale, 1 / scale, 1);
    }

    @Override public int getWidth() {
        return (int) (15 * config.scale);
    }

    @Override public int getHeight() {
        return (int) (15 * config.scale);
    }

    @Override public boolean isEditable() {
        return true;
    }

    @Override public void edit() {
        synchronized (ClockWidget_ConfigSerializer.BACKER_LOCK) {
            ClockWidget_ConfigSerializer.BACKER = this;
        }
        synchronized (Temporospatial.CLOCK_WIDGET_CONFIG_HOLDER_LOCK) {
            Temporospatial.CLOCK_WIDGET_CONFIG_HOLDER.load();
        }
        MinecraftClient.getInstance().openScreen(
                AutoConfig.getConfigScreen(ClockWidget_Config.class,
                        new ClockWidget_ConfigSerializer.ReturnScreen(new AlterHudScreen())
                ).get());
    }

}
