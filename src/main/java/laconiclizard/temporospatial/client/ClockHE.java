package laconiclizard.temporospatial.client;

import laconiclizard.hudelements.api.HudElement;
import laconiclizard.temporospatial.TSConfig;
import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ClockHE extends HudElement {

    public static final ItemStack CLOCK_ITEMSTACK = new ItemStack(Items.CLOCK);

    public float scale;

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
        MinecraftClient.getInstance().getItemRenderer().renderInGuiWithOverrides(CLOCK_ITEMSTACK, getX(), getY());
        Util.scaleAbout(getX(), getY(), 0, 1 / scale, 1 / scale, 1);
    }

    @Override public int getWidth() {
        return (int) (16 * scale);
    }

    @Override public int getHeight() {
        return (int) (16 * scale);
    }

}
