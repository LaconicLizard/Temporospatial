package laconiclizard.temporospatial.clock;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceConfig;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Temporospatial.MOD_ID + "-internal-NumericClock")
public class NumericClock_Config extends InstanceConfig<NumericClock_Config> {

    public boolean enabled = false;
    public float x, y, z;
    public float scale = 1f;
    public int textColor = 0xffffff, backgroundColor = 0x40000000, borderColor = 0x40ffffff;
    public float borderThickness = 1;
    public boolean realTime = false;
    public String realTimeFormat = "HH:mm:ssaa";
    public boolean minecraftTime_isAbsolute = false;
    public boolean absoluteMinecraftTime_separateDays = true;

    @Override public NumericClock_Config newInstance() {
        return new NumericClock_Config();
    }

    public void load(NumericClock_Config src) {
        enabled = src.enabled;
        x = src.x;
        y = src.y;
        z = src.z;
        scale = src.scale;
        textColor = src.textColor;
        backgroundColor = src.backgroundColor;
        borderColor = src.borderColor;
        borderThickness = src.borderThickness;
        realTime = src.realTime;
        realTimeFormat = src.realTimeFormat;
        minecraftTime_isAbsolute = src.minecraftTime_isAbsolute;
        absoluteMinecraftTime_separateDays = src.absoluteMinecraftTime_separateDays;
    }

}
