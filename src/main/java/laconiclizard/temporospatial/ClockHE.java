package laconiclizard.temporospatial;

import laconiclizard.hudelements.api.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ClockHE extends HudElement {

    private final ItemStack clockStack = new ItemStack(Items.CLOCK);
    public float scale;

    {
        setPreventSwing(false);
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

    public ClockHE() {
        super(0, 0);
    }

    @Override public void save() {
        TSConfig config = Temporospatial.CONFIG_HOLDER.getConfig();
        config.clockHE_X = getX();
        config.clockHE_Y = getY();
    }

    @Override public void render(MatrixStack matrices, float tickDelta) {
        Util.scaleAbout(getX(), getY(), 0, scale, scale, 1);
        MinecraftClient.getInstance().getItemRenderer().renderInGuiWithOverrides(clockStack, getX(), getY());
        Util.scaleAbout(getX(), getY(), 0, 1 / scale, 1 / scale, 1);
    }

    @Override public int getWidth() {
        return (int) (15 * scale);
    }

    @Override public int getHeight() {
        return (int) (15 * scale);
    }

}
