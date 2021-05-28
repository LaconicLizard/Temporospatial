package laconiclizard.temporospatial;

import com.mojang.blaze3d.systems.RenderSystem;

import java.util.function.Supplier;

public class Util {

    /** Scale the world about x,y,z. */
    public static void scaleAbout(double x, double y, double z, double xs, double ys, double zs) {
        RenderSystem.translated(x, y, z);
        RenderSystem.scaled(xs, ys, zs);
        RenderSystem.translated(-x, -y, -z);
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

}
