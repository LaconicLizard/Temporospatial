package laconiclizard.temporospatial;

import laconiclizard.temporospatial.clock.WidgetClockHE_Config;
import laconiclizard.temporospatial.clock.NumericClockHE_Config;
import laconiclizard.temporospatial.compass.CoordHE_Config;
import laconiclizard.temporospatial.compass.WidgetCompassHE_Config;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@Config(name = Temporospatial.MOD_ID)
public class TSConfig implements ConfigData {

    public List<WidgetClockHE_Config> widgetClocks = new ArrayList<>();
    public List<NumericClockHE_Config> numericClocks = new ArrayList<>();
    public List<WidgetCompassHE_Config> widgetCompasses = new ArrayList<>();
    public List<CoordHE_Config> coordDisplays = new ArrayList<>();

    // globals
    public boolean allClocks_preventSwing = false;
    public boolean allClocks_workEverywhere = false;
    public boolean allClocks_realtime = false;

    public boolean allCompasses_preventSwing = false;
    public boolean allCompasses_workEverywhere = false;
    public boolean allCompasses_pointNorth = false;

}
