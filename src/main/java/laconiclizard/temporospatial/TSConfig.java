package laconiclizard.temporospatial;

import laconiclizard.temporospatial.clock.WidgetClock_Config;
import laconiclizard.temporospatial.clock.NumericClock_Config;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@Config(name = Temporospatial.MOD_ID)
public class TSConfig implements ConfigData {

    public List<WidgetClock_Config> widgetClocks = new ArrayList<>();
    public List<NumericClock_Config> numericClocks = new ArrayList<>();

    // all clocks
    public boolean allClocks_preventSwing = false;
    public boolean allClocks_workEverywhere = false;
    public boolean allClocks_realtime = false;

}
