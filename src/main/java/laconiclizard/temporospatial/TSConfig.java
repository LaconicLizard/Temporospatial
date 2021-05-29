package laconiclizard.temporospatial;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Temporospatial.MOD_ID)
public class TSConfig implements ConfigData {

    // clock HE
    public boolean clockHE_enabled = true;
    public float clockHE_scale = 1f;
    @ConfigEntry.Gui.Excluded
    public int clockHE_X, clockHE_Y;
    public boolean clockHE_preventSwing = true;
    public boolean clockHE_worksInNether = true;
    public boolean clockHE_worksInEnd = true;

    // numeric clock HE
    public boolean numericClockHE_enabled = true;
    public float numericClockHE_scale = 1f;
    @ConfigEntry.Gui.Excluded
    public int numericClockHE_X, numericClockHE_Y;
    public int numericClockHE_textColor = 0xffffff;
    public int numericClockHE_backgroundColor = 0x40000000;
    public int numericClockHE_borderColor = 0x40ffffff;
    public int numericClockHE_borderThickness = 1;

    // realtime clock HE
    public boolean realtimeClockHE_enabled = true;
    public float realtimeClockHE_scale = 1f;
    @ConfigEntry.Gui.Excluded
    public int realtimeClockHE_X, realtimeClockHE_Y;

    // numeric realtime clock HE
    public boolean numericRealtimeClockHE_enabled = true;
    public float numericRealtimeClockHE_scale = 1f;
    @ConfigEntry.Gui.Excluded
    public int numericRealtimeClockHE_X, numericRealtimeClockHE_Y;
    public int numericRealtimeClockHE_textColor = 0xffffff;
    public int numericRealtimeClockHE_backgroundColor = 0x40000000;
    public int numericRealtimeClockHE_borderColor = 0x40ffffff;
    public int numericRealtimeClockHE_borderThickness = 1;
    public String numericRealtimeClockHE_format = "hh:mm:ssaa";

    // all clocks
    public boolean allClocks_preventSwing = false;
    public boolean allClocks_workInNether = false;
    public boolean allClocks_workInEnd = false;
    public boolean allClocks_workEverywhere = false;

}
