package laconiclizard.temporospatial.clock;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceConfig_HE;
import me.shedaniel.autoconfig.annotation.Config;

/** Config for a WidgetClockHE. */
@Config(name = Temporospatial.MOD_ID + "-internal-WidgetClock")
public class WidgetClockHE_Config extends InstanceConfig_HE<WidgetClockHE_Config> {

    public boolean realTime = false;
    public boolean preventSwing = true;
    public boolean worksEverywhere = true;

    @Override public WidgetClockHE_Config newInstance() {
        return new WidgetClockHE_Config();
    }

    public void load(WidgetClockHE_Config src) {
        super.load(src);
        realTime = src.realTime;
        preventSwing = src.preventSwing;
        worksEverywhere = src.worksEverywhere;
    }

}
