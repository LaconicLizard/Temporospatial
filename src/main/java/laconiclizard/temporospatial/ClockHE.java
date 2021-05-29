package laconiclizard.temporospatial;

import laconiclizard.hudelements.api.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ClockHE extends HudElement {

    private final ItemStack clockStack = new ItemStack(Items.CLOCK);
    public float scale;

    public ClockHE() {
        super(0, 0);
    }

    public void setPreventSwing(boolean preventSwing) {
        if (preventSwing) {
            Clock_MPP.preventSwing(clockStack);
        } else {
            Clock_MPP.releaseSwing(clockStack);
        }
    }

    public void setWorksInNether(boolean worksInNether) {
        if (worksInNether) {
            Clock_MPP.makeWorkInNether(clockStack);
        } else {
            Clock_MPP.freeFromWorkingInNether(clockStack);
        }
    }

    public void setWorksInEnd(boolean worksInEnd) {
        if (worksInEnd) {
            Clock_MPP.makeWorkInEnd(clockStack);
        } else {
            Clock_MPP.freeFromWorkingInEnd(clockStack);
        }
    }

    @Override public void save() {
        TSConfig config = Temporospatial.CONFIG_HOLDER.getConfig();
        config.clockHE_X = getX();
        config.clockHE_Y = getY();
        Temporospatial.CONFIG_HOLDER.save();
    }

    @Override public void render(MatrixStack matrices, float tickDelta) {
        int x = getX(), y = getY();
        Util.scaleAbout(x, y, 0, scale, scale, 1);
        MinecraftClient.getInstance().getItemRenderer().renderInGuiWithOverrides(clockStack, x, y);
        Util.scaleAbout(x, y, 0, 1 / scale, 1 / scale, 1);
    }

    @Override public int getWidth() {
        return (int) (15 * scale);
    }

    @Override public int getHeight() {
        return (int) (15 * scale);
    }

}
