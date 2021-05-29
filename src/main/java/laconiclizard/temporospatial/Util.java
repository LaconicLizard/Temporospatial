package laconiclizard.temporospatial;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.function.Supplier;

public class Util {

    public static final Identifier OVERWORLD_ID = new Identifier("minecraft:overworld");
    public static final Identifier THE_NETHER_ID = new Identifier("minecraft:the_nether");
    public static final Identifier THE_END_ID = new Identifier("minecraft:the_end");

    /** Scale the world about x,y,z. */
    public static void scaleAbout(double x, double y, double z, double xs, double ys, double zs) {
        //noinspection deprecation
        RenderSystem.translated(x, y, z);
        //noinspection deprecation
        RenderSystem.scaled(xs, ys, zs);
        //noinspection deprecation
        RenderSystem.translated(-x, -y, -z);
    }

    /** Scale stack about x,y,z. */
    public static void scaleAbout(MatrixStack stack, double x, double y, double z, float xs, float ys, float zs) {
        stack.translate(x, y, z);
        stack.scale(xs, ys, zs);
        stack.translate(-x, -y, -z);
    }

    /** Sky angle of a world, bypassing the fixedTime check. */
    public static float unfixedSkyAngle(long time) {
        // code adapted from DimensionType#getSkyAngle
        double d = MathHelper.fractionalPart((double) time / 24000.0D - 0.25D);
        double e = 0.5D - Math.cos(d * 3.141592653589793D) / 2.0D;
        return (float) (d * 2.0D + e) / 3.0F;
    }

    /** Repeatedly invokes s until the result is not null, then returns it.  Waits in between. */
    public static <T> T waitUntilNotNull(Supplier<T> s, int interval) {
        T result;
        while ((result = s.get()) == null) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException ignored) {
            }
        }
        return result;
    }

    /** Draw a border around the defined rectangle. */
    public static void drawBorder(MatrixStack matrices, int x, int y, int w, int h, int thickness, int color) {
        DrawableHelper.fill(matrices, x - thickness, y - thickness, x, y + h + thickness, color);
        DrawableHelper.fill(matrices, x + w, y - thickness, x + w + thickness, y + h + thickness, color);
        DrawableHelper.fill(matrices, x, y - thickness, x + w, y, color);
        DrawableHelper.fill(matrices, x, y + h, x + w, y + h + thickness, color);
    }

}
