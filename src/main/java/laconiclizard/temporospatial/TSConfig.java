package laconiclizard.temporospatial;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Temporospatial.MOD_ID)
public class TSConfig implements ConfigData {

    public boolean clockHE_enabled = true;
    public float clockHE_scale = 1f;
    @ConfigEntry.Gui.Excluded
    public int clockHE_X, clockHE_Y;
    public boolean clockHE_preventSwing = true;
    public boolean clockHE_worksInNether = true;
    public boolean clockHE_worksInEnd = true;

    public boolean allClocks_preventSwing = false;
    public boolean allClocks_workInNether = false;
    public boolean allClocks_workInEnd = false;

    public boolean numericClockHE_enabled = true;
    public float numericClockHE_scale = 1f;
    @ConfigEntry.Gui.Excluded
    public int numericClockHE_X, numericClockHE_Y;
    public int numericClockHE_textColor = 0xffffff;
    public int numericClockHE_backgroundColor = 0x40000000;
    public int numericClockHE_borderColor = 0x40ffffff;
    public int numericClockHE_borderThickness = 1;

}
