package laconiclizard.temporospatial.clock;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceConfig_TextHE;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Temporospatial.MOD_ID + "-internal-NumericClock")
public class NumericClockHE_Config extends InstanceConfig_TextHE<NumericClockHE_Config> {

    public boolean enabled = false;
    public float x, y, z;
    public float scale = 1f;
    public boolean worksEverywhere = false;
    public boolean realTime = false;
    public String realTimeFormat = "HH:mm:ssaa";
    public boolean minecraftTime_isAbsolute = false;
    public boolean absoluteMinecraftTime_separateDays = true;

    @Override public NumericClockHE_Config newInstance() {
        return new NumericClockHE_Config();
    }

    public void load(NumericClockHE_Config src) {
        super.load(src);
        enabled = src.enabled;
        x = src.x;
        y = src.y;
        z = src.z;
        scale = src.scale;
        realTime = src.realTime;
        realTimeFormat = src.realTimeFormat;
        minecraftTime_isAbsolute = src.minecraftTime_isAbsolute;
        absoluteMinecraftTime_separateDays = src.absoluteMinecraftTime_separateDays;
    }

}