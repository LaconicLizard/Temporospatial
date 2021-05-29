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

}
