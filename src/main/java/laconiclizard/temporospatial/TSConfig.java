package laconiclizard.temporospatial;

import laconiclizard.temporospatial.clock.WidgetClockHE_Config;
import laconiclizard.temporospatial.clock.NumericClockHE_Config;
import laconiclizard.temporospatial.compass.AnglesHE_Config;
import laconiclizard.temporospatial.compass.CoordHE_Config;
import laconiclizard.temporospatial.compass.DistanceHE_Config;
import laconiclizard.temporospatial.compass.WidgetCompassHE_Config;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

@Config(name = Temporospatial.MOD_ID)
public class TSConfig implements ConfigData {

    @ConfigEntry.Gui.Excluded
    public List<WidgetClockHE_Config> widgetClocks = new ArrayList<>();
    @ConfigEntry.Gui.Excluded
    public List<NumericClockHE_Config> numericClocks = new ArrayList<>();
    @ConfigEntry.Gui.Excluded
    public List<WidgetCompassHE_Config> widgetCompasses = new ArrayList<>();
    @ConfigEntry.Gui.Excluded
    public List<CoordHE_Config> coordDisplays = new ArrayList<>();
    @ConfigEntry.Gui.Excluded
    public List<AnglesHE_Config> anglesDisplays = new ArrayList<>();
    @ConfigEntry.Gui.Excluded
    public List<DistanceHE_Config> distanceDisplays = new ArrayList<>();

    // globals
    public boolean allClocks_preventSwing = false;
    public boolean allClocks_workEverywhere = false;
    public boolean allClocks_realtime = false;

    public boolean allCompasses_preventSwing = false;
    public boolean allCompasses_workEverywhere = false;
    public boolean allCompasses_pointNorth = false;

}
