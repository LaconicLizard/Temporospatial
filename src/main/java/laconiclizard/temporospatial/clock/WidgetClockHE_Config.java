package laconiclizard.temporospatial.clock;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceConfig;
import me.shedaniel.autoconfig.annotation.Config;

/** Config for a WidgetClockHE. */
@Config(name = Temporospatial.MOD_ID + "-internal-WidgetClock")
public class WidgetClockHE_Config extends InstanceConfig<WidgetClockHE_Config> {

    public boolean enabled = false;
    public float x, y, z;
    public float scale = 1f;
    public boolean realTime = false;
    public boolean preventSwing = true;
    public boolean worksEverywhere = true;

    @Override public WidgetClockHE_Config newInstance() {
        return new WidgetClockHE_Config();
    }

    public void load(WidgetClockHE_Config src) {
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
