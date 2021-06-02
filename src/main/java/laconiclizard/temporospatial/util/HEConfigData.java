package laconiclizard.temporospatial.util;

/** General onfig options for a HudElement. */
public class HEConfigData implements GenericConfig<HEConfigData> {

    public boolean enabled;
    public float x, y, z;
    public float scale = 1f;

    @Override public HEConfigData newInstance() {
        return new HEConfigData();
    }

    @Override public void load(HEConfigData src) {
        enabled = src.enabled;
        x = src.x;
        y = src.y;
        z = src.z;
        scale = src.scale;
    }

    @Override public void validatePostLoad() {
        if (scale <= 0) {
            scale = 1f;
        }
    }

}
