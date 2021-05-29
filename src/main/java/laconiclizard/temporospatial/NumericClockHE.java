package laconiclizard.temporospatial;

import laconiclizard.hudelements.api.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;

public class NumericClockHE extends HudElement {

    public int textColor;
    public float scale;
    public int backgroundColor;
    public int borderThickness;
    public int borderColor;

    public NumericClockHE() {
        super(0, 0);
    }

    private String getTime() {
        ClientWorld w = MinecraftClient.getInstance().world;
        if (w == null) return null;
        return String.valueOf((w.getTimeOfDay() + 24000) % 24000);
    }

    @Override public void save() {
        TSConfig config = Temporospatial.CONFIG_HOLDER.getConfig();
        config.numericClockHE_X = getX();
        config.numericClockHE_Y = getY();
        Temporospatial.CONFIG_HOLDER.save();
    }

    @Override public void render(MatrixStack matrices, float tickDelta) {
        int x = getX(), y = getY(), w = getWidth(), h = getHeight();
        Util.scaleAbout(x, y, 0, scale, scale, 1);
        DrawableHelper.fill(matrices, x, y, x + w, y + w, backgroundColor);  // background
        MinecraftClient.getInstance().textRenderer.draw(matrices, getTime(), x, y, textColor);  // text
        Util.drawBorder(matrices, x, y, w, h, borderThickness, borderColor);  // border
        Util.scaleAbout(x, y, 0, 1 / scale, 1 / scale, 1);
    }

    @Override public int getWidth() {
        return MinecraftClient.getInstance().textRenderer.getWidth("24000");
    }

    @Override public int getHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
    }

}