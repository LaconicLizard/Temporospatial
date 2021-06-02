package laconiclizard.temporospatial.compass;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceConfig_TextHE;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Temporospatial.MOD_ID + "-internal-CoordHE")
public class CoordHE_Config extends InstanceConfig_TextHE<CoordHE_Config> {

    public boolean enabled;
    public float x, y, z;
    public float scale = 1f;
    public String separator = "/";
    public String numberFormat = "#.000";

    @Override public CoordHE_Config newInstance() {
        return new CoordHE_Config();
    }

    @Override public void load(CoordHE_Config src) {
        super.load(src);
        enabled = src.enabled;
        x = src.x;
        y = src.y;
        z = src.z;
        scale = src.scale;
        separator = src.separator;
    }

}