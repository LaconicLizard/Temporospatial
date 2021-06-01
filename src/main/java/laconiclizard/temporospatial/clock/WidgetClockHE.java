package laconiclizard.temporospatial.clock;

import laconiclizard.hudelements.AlterHudScreen;
import laconiclizard.temporospatial.TSConfig;
import laconiclizard.temporospatial.util.InstanceTracker;
import laconiclizard.temporospatial.util.TSHudElement;
import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.Util;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class WidgetClockHE extends TSHudElement<WidgetClock_Config> {

    public static final InstanceTracker<WidgetClockHE> INSTANCES = new InstanceTracker<>();

    private final ItemStack clockStack = new ItemStack(Items.CLOCK);

    public WidgetClockHE(WidgetClock_Config config) {
        super(config);
        updateFromConfig();
        INSTANCES.add(this);
    }

    /**
     * Updates the behavior of this WidgetClockHE to reflect the current state of its config.
     * Assumes .lock has already been acquired.
     */
    @Override public void updateFromConfig() {
        setEnabled(config.enabled);
        setX(config.x);
        setY(config.y);
        setZ(config.z);
        // don't need to set scale
        Clock_MPP.PREVENT_SWING.setFlagged(clockStack, config.preventSwing);
        Clock_MPP.WORK_EVERYWHERE.setFlagged(clockStack, config.worksEverywhere);
        Clock_MPP.REALTIME.setFlagged(clockStack, config.realTime);
    }

    @Override public void saveAll() {
        List<WidgetClock_Config> configs;
        synchronized (INSTANCES.lock) {
            configs = new ArrayList<>(INSTANCES.instances.size());
            for (WidgetClockHE cw : INSTANCES.instances) {
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
            config.widgetClocks = configs;
            Temporospatial.CONFIG_HOLDER.value.save();
        }
    }

    @Override public void save() {
        config.x = getX();
        config.y = getY();
        saveAll();
    }

    @Override public void render(MatrixStack matrices, float tickDelta) {
        float x = getX(), y = getY();
        float scale = config.scale;
        Util.scaleAbout(x, y, 0, scale, scale, 1);
        MinecraftClient.getInstance().getItemRenderer().renderInGuiWithOverrides(clockStack, (int) x, (int) y);
        Util.scaleAbout(x, y, 0, 1 / scale, 1 / scale, 1);
    }

    @Override public float getWidth() {
        return 16 * config.scale;
    }

    @Override public float getHeight() {
        return 16 * config.scale;
    }

    @Override public boolean isEditable() {
        return true;
    }

    @Override public void edit() {
        Temporospatial.WIDGET_CLOCK_CONFIG_SERIALIZER.setBacker(this);
        synchronized (Temporospatial.WIDGET_CLOCK_CONFIG_HOLDER.lock) {
            Temporospatial.WIDGET_CLOCK_CONFIG_HOLDER.value.load();
        }
        MinecraftClient.getInstance().openScreen(
                AutoConfig.getConfigScreen(WidgetClock_Config.class,
                        Temporospatial.WIDGET_CLOCK_CONFIG_SERIALIZER.returnScreen(new AlterHudScreen()))
                        .get());
    }

}
