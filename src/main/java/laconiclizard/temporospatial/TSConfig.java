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

}
