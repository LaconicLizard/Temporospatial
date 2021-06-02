package laconiclizard.temporospatial.compass;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.HEConfig;
import laconiclizard.temporospatial.util.HEConfigData;
import laconiclizard.temporospatial.util.TextConfig;
import laconiclizard.temporospatial.util.TextConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Temporospatial.MOD_ID + "-internal-CoordHE")
public class CoordHE_Config implements HEConfig<CoordHE_Config>, TextConfig<CoordHE_Config> {

    @ConfigEntry.Gui.TransitiveObject
    public HEConfigData heConfigData = new HEConfigData();
    @ConfigEntry.Gui.TransitiveObject
    public TextConfigData textConfigData = new TextConfigData();

    public String separator = "/";
    public String prefix = "", suffix = "";
    public String numberFormat = "#.000";

    @Override public CoordHE_Config newInstance() {
        return new CoordHE_Config();
    }

    @Override public HEConfigData heConfigData() {
        return heConfigData;
    }

    @Override public TextConfigData textConfigData() {
        return textConfigData;
    }

    @Override public void load(CoordHE_Config src) {
        heConfigData.load(src.heConfigData);
        textConfigData.load(src.textConfigData);
        separator = src.separator;
        numberFormat = src.numberFormat;
    }

}
