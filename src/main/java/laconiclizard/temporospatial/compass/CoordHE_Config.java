package laconiclizard.temporospatial.compass;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceConfig_TextHE;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Temporospatial.MOD_ID + "-internal-CoordHE")
public class CoordHE_Config extends InstanceConfig_TextHE<CoordHE_Config> {

    public String separator = "/";
    public String prefix = "", suffix = "";
    public String numberFormat = "#.000";

    @Override public CoordHE_Config newInstance() {
        return new CoordHE_Config();
    }

    @Override public void load(CoordHE_Config src) {
        super.load(src);
        separator = src.separator;
        numberFormat = src.numberFormat;
    }

}
