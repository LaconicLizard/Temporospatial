package laconiclizard.temporospatial.util;

/** General onfig options for a HudElement. */
public abstract class InstanceConfig_HE<T extends InstanceConfig_HE<T>> extends InstanceConfig<T> {

    public boolean enabled;
    public float x, y, z;
    public float scale;

    @Override public void load(T src) {
        enabled = src.enabled;
        x = src.x;
        y = src.y;
        z = src.z;
        scale = src.scale;
    }

}
