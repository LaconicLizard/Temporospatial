package laconiclizard.temporospatial;

import laconiclizard.hudelements.api.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class ClockHE extends HudElement {

    public float scale;
    private ItemStack clockStack;

    {
        setPreventSwing(false);
    }

    public void setPreventSwing(boolean preventSwing) {
        if (preventSwing) {
            clockStack = Clock_MPP.SWINGLESS_CLOCK;
        } else {
            clockStack = Clock_MPP.NORMAL_CLOCK;
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
