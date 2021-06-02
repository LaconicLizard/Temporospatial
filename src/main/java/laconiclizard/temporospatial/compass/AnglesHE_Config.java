package laconiclizard.temporospatial.compass;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.HEConfig;
import laconiclizard.temporospatial.util.HEConfigData;
import laconiclizard.temporospatial.util.TextConfig;
import laconiclizard.temporospatial.util.TextConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Temporospatial.MOD_ID + "-internal-AnglesHE")
public class AnglesHE_Config implements HEConfig<AnglesHE_Config>, TextConfig<AnglesHE_Config> {

    @ConfigEntry.Gui.TransitiveObject
    public HEConfigData heConfigData = new HEConfigData();
    @ConfigEntry.Gui.TransitiveObject
    public TextConfigData textConfigData = new TextConfigData();

    public String separator = " / ", prefix = "", suffix = "";
    public String numberFormat = "#.000";

    @Override public AnglesHE_Config newInstance() {
        return new AnglesHE_Config();
    }

    @Override public void load(AnglesHE_Config src) {
        heConfigData.load(src.heConfigData);
        textConfigData.load(src.textConfigData);
        separator = src.separator;
        prefix = src.prefix;
        suffix = src.suffix;
        numberFormat = src.numberFormat;
    }

    @Override public HEConfigData heConfigData() {
        return heConfigData;
    }

    @Override public TextConfigData textConfigData() {
        return textConfigData;
    }

}
