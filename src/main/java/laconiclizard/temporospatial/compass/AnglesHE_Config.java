package laconiclizard.temporospatial.compass;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceConfig_TextHE;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Temporospatial.MOD_ID + "-internal-AnglesHE")
public class AnglesHE_Config extends InstanceConfig_TextHE<AnglesHE_Config> {

    public String separator = " / ", prefix = "", suffix = "";
    public String numberFormat = "#.000";

    @Override public AnglesHE_Config newInstance() {
        return new AnglesHE_Config();
    }

    @Override public void load(AnglesHE_Config src) {
        super.load(src);
        separator = src.separator;
        prefix = src.prefix;
        suffix = src.suffix;
        numberFormat = src.numberFormat;
    }
}
