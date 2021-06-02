package laconiclizard.temporospatial.compass;

import laconiclizard.hudelements.AlterHudScreen;
import laconiclizard.temporospatial.TSConfig;
import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceTracker;
import laconiclizard.temporospatial.util.TSHudElement;
import laconiclizard.temporospatial.util.Util;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class WidgetCompassHE extends TSHudElement<WidgetCompassHE_Config> {

    public static final InstanceTracker<WidgetCompassHE> INSTANCES = new InstanceTracker<>();

    private final ItemStack compassStack = new ItemStack(Items.COMPASS);

    public WidgetCompassHE(WidgetCompassHE_Config config) {
        super(config);
        updateFromConfig();
        INSTANCES.add(this);
    }

    @Override public void updateFromConfig() {
        super.updateFromConfig();
        Compass_MPP.PREVENT_SWING.setFlagged(compassStack, config.preventSwing);
        Compass_MPP.WORK_EVERYWHERE.setFlagged(compassStack, config.worksEverywhere);
        Compass_MPP.POINT_NORTH.setFlagged(compassStack, config.targetMode == CompassTargetMode.NORTH);

        CompoundTag tag = compassStack.getOrCreateTag();
        if (config.targetMode == CompassTargetMode.CUSTOM_POINT) {  // set lodestone target
            String targetDimension = config.targetDimension;
            if (!targetDimension.contains(":")) {  // prefix if reasonable
                targetDimension = "minecraft:" + targetDimension;
            }
            tag.putString("LodestoneDimension", targetDimension);
            tag.put("LodestonePos",
                    NbtHelper.fromBlockPos(new BlockPos(config.targetX, config.targetY, config.targetZ)));
        } else {  // unset lodestone target
            tag.remove("LodestonePos");
            tag.remove("LodestoneDimension");
        }
    }

    @Override public void saveAll() {
        List<WidgetCompassHE_Config> configs;
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

    @Override public void render(MatrixStack matrices, float tickDelta) {
        float x = getX(), y = getY();
        float scale = cachedConfig_scale;
        Util.scaleAbout(x, y, 0, scale, scale, 1);
        MinecraftClient.getInstance().getItemRenderer().renderInGuiWithOverrides(compassStack, (int) x, (int) y);
        Util.scaleAbout(x, y, 0, 1 / scale, 1 / scale, 1);
    }

    @Override public float getWidth() {
        return 16 * cachedConfig_scale;
    }

    @Override public float getHeight() {
        return 16 * cachedConfig_scale;
    }

    @Override public boolean isEditable() {
        return true;
    }

    @Override public void edit() {
        Temporospatial.WIDGET_COMPASS_CONFIG_SERIALIZER.setBacker(this);
        synchronized (Temporospatial.WIDGET_COMPASS_CONFIG_HOLDER.lock) {
            Temporospatial.WIDGET_COMPASS_CONFIG_HOLDER.value.load();
        }
        MinecraftClient.getInstance().openScreen(
                AutoConfig.getConfigScreen(WidgetCompassHE_Config.class,
                        Temporospatial.WIDGET_COMPASS_CONFIG_SERIALIZER.returnScreen(new AlterHudScreen()))
                        .get());
    }

}
