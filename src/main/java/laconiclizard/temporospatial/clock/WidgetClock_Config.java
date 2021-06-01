package laconiclizard.temporospatial.clock;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceConfig;
import me.shedaniel.autoconfig.annotation.Config;

/** Config for a WidgetClockHE. */
@Config(name = Temporospatial.MOD_ID + "-internal-WidgetClock")
public class WidgetClock_Config extends InstanceConfig<WidgetClock_Config> {

    public boolean enabled = false;
    public float x, y, z;
    public float scale = 1f;
    public boolean realTime = false;
    public boolean preventSwing = true;
    public boolean worksEverywhere = true;

    @Override public WidgetClock_Config newInstance() {
        return new WidgetClock_Config();
    }

    public void load(WidgetClock_Config src) {
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
