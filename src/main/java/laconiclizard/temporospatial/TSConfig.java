package laconiclizard.temporospatial;

import laconiclizard.temporospatial.clock.WidgetClock_Config;
import laconiclizard.temporospatial.clock.NumericClock_Config;
import laconiclizard.temporospatial.compass.WidgetCompass_Config;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@Config(name = Temporospatial.MOD_ID)
public class TSConfig implements ConfigData {

    public List<WidgetClock_Config> widgetClocks = new ArrayList<>();
    public List<NumericClock_Config> numericClocks = new ArrayList<>();
    public List<WidgetCompass_Config> widgetCompasses = new ArrayList<>();

    // globals
    public boolean allClocks_preventSwing = false;
    public boolean allClocks_workEverywhere = false;
    public boolean allClocks_realtime = false;

    public boolean allCompasses_preventSwing = false;
    public boolean allCompasses_workEverywhere = false;
    public boolean allCompasses_pointNorth = false;

}
