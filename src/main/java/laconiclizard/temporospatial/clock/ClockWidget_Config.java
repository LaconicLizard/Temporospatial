package laconiclizard.temporospatial.clock;

import laconiclizard.temporospatial.Temporospatial;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

/** Config for a ClockWidgetHE. */
@Config(name = Temporospatial.MOD_ID + "-internal-ClockWidget")
public class ClockWidget_Config implements ConfigData {

    public boolean enabled = true;
    public float x, y, z;
    public float scale = 1f;
    public boolean realTime = false;
    public boolean preventSwing = true;
    public boolean worksEverywhere = true;

    public ClockWidget_Config() {
    }

    public ClockWidget_Config(ClockWidget_Config src) {
        load(src);
    }

    public void load(ClockWidget_Config src) {
        enabled = src.enabled;
        x = src.x;
        y = src.y;
        z = src.z;
        scale = src.scale;
        realTime = src.realTime;
        preventSwing = src.preventSwing;
        worksEverywhere = src.worksEverywhere;
    }

}
