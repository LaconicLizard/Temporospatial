package laconiclizard.temporospatial.clock;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.HEConfig;
import laconiclizard.temporospatial.util.HEConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

/** Config for a WidgetClockHE. */
@Config(name = Temporospatial.MOD_ID + "-internal-WidgetClock")
public class WidgetClockHE_Config implements HEConfig<WidgetClockHE_Config> {

    @ConfigEntry.Gui.TransitiveObject
    public HEConfigData heConfigData = new HEConfigData();

    public boolean realTime = false;
    public boolean preventSwing = true;
    public boolean worksEverywhere = true;

    @Override public HEConfigData heConfigData() {
        return heConfigData;
    }

    @Override public WidgetClockHE_Config newInstance() {
        return new WidgetClockHE_Config();
    }

    public void load(WidgetClockHE_Config src) {
        heConfigData.load(src.heConfigData);
        realTime = src.realTime;
        preventSwing = src.preventSwing;
        worksEverywhere = src.worksEverywhere;
    }


}
