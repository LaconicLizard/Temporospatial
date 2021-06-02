package laconiclizard.temporospatial.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;

public abstract class TSTextHudElement<C extends HEConfig<C> & TextConfig<C>> extends TSHudElement<C> {

    protected int cachedConfig_textColor, cachedConfig_backgroundColor, cachedConfig_borderColor;
    protected float cachedConfig_borderThickness;

    private long lastTime;
    private String lastText;

    public TSTextHudElement(C config) {
        super(config);
    }

    /** Generate the text to display. */
    public abstract String generateText();

    /** Get the text to display.  Cached per tick. */
    public String getText() {
        ClientWorld w = MinecraftClient.getInstance().world;
        if (w == null) return "";
        long t = w.getTime();
        if (t == lastTime && lastText != null) {
            return lastText;
        } else {
            lastTime = t;
            return lastText = generateText();
        }
    }

    @Override public void updateFromConfig() {
        super.updateFromConfig();
        TextConfigData tcd = config.textConfigData();
        cachedConfig_textColor = tcd.textColor;
        cachedConfig_backgroundColor = tcd.backgroundColor;
        cachedConfig_borderColor = tcd.borderColor;
        cachedConfig_borderThickness = tcd.borderThickness;
    }

    @Override public void render(MatrixStack matrices, float tickDelta) {
        Util.drawTextWithFrills(matrices, cachedConfig_scale, getText(), getX(), getY(), cachedConfig_textColor,
                cachedConfig_backgroundColor, cachedConfig_borderThickness, cachedConfig_borderColor);
    }

    @Override public float getWidth() {
        return MinecraftClient.getInstance().textRenderer.getWidth(getText());
    }

    @Override public float getHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
    }
}
