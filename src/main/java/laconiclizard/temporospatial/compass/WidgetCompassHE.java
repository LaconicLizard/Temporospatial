package laconiclizard.temporospatial.compass;

import laconiclizard.temporospatial.TSConfig;
import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceTracker;
import laconiclizard.temporospatial.util.TSHudElement;
import laconiclizard.temporospatial.util.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class WidgetCompassHE extends TSHudElement<WidgetCompass_Config> {

    public static final InstanceTracker<WidgetCompassHE> INSTANCES = new InstanceTracker<>();

    private final ItemStack compassStack = new ItemStack(Items.COMPASS);

    public WidgetCompassHE(WidgetCompass_Config config) {
        super(config);
        INSTANCES.add(this);
    }

    @Override public void updateFromConfig() {
        setEnabled(config.enabled);
        setX(config.x);
        setY(config.y);
        setZ(config.z);
        Compass_MPP.PREVENT_SWING.setFlagged(compassStack, config.preventSwing);
        Compass_MPP.WORK_EVERYWHERE.setFlagged(compassStack, config.worksEverywhere);
        Compass_MPP.POINT_NORTH.setFlagged(compassStack, config.pointsNorth);
    }

    @Override public void saveAll() {
        List<WidgetCompass_Config> configs;
        synchronized (INSTANCES.lock) {
            configs = new ArrayList<>(INSTANCES.instances.size());
            for (WidgetCompassHE cw : INSTANCES.instances) {
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
            config.widgetCompasses = configs;
            Temporospatial.CONFIG_HOLDER.value.save();
        }
    }

    @Override public void save() {
        config.enabled = isEnabled();
        config.x = getX();
        config.y = getY();
        saveAll();
    }

    @Override public void render(MatrixStack matrices, float tickDelta) {
        float x = getX(), y = getY();
        float scale = config.scale;
        Util.scaleAbout(x, y, 0, scale, scale, 1);
        MinecraftClient.getInstance().getItemRenderer().renderInGuiWithOverrides(compassStack, (int) x, (int) y);
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
        // todo
    }
}
