package laconiclizard.temporospatial;

import com.mojang.blaze3d.systems.RenderSystem;

public class Util {

    /** Scale the world about x,y,z. */
    public static void scaleAbout(double x, double y, double z, double xs, double ys, double zs) {
        RenderSystem.translated(x, y, z);
        RenderSystem.scaled(xs, ys, zs);
        RenderSystem.translated(-x, -y, -z);
    }

}
