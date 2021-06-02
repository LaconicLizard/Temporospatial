package laconiclizard.temporospatial.clock;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.HEConfig;
import laconiclizard.temporospatial.util.HEConfigData;
import laconiclizard.temporospatial.util.TextConfig;
import laconiclizard.temporospatial.util.TextConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Temporospatial.MOD_ID + "-internal-NumericClock")
public class NumericClockHE_Config implements HEConfig<NumericClockHE_Config>, TextConfig<NumericClockHE_Config> {

    @ConfigEntry.Gui.TransitiveObject
    public HEConfigData heConfigData = new HEConfigData();
    @ConfigEntry.Gui.TransitiveObject
    public TextConfigData textConfigData = new TextConfigData();

    public boolean worksEverywhere = false;
    public boolean realTime = false;
    public String realTimeFormat = "HH:mm:ssaa";
    public boolean minecraftTime_isAbsolute = false;
    public boolean absoluteMinecraftTime_separateDays = true;

    @Override public HEConfigData heConfigData() {
        return heConfigData;
    }

    @Override public TextConfigData textConfigData() {
        return textConfigData;
    }

    @Override public NumericClockHE_Config newInstance() {
        return new NumericClockHE_Config();
    }

    public void load(NumericClockHE_Config src) {
        heConfigData.load(src.heConfigData);
        textConfigData.load(src.textConfigData);
        realTime = src.realTime;
        realTimeFormat = src.realTimeFormat;
        minecraftTime_isAbsolute = src.minecraftTime_isAbsolute;
        absoluteMinecraftTime_separateDays = src.absoluteMinecraftTime_separateDays;
    }

}
