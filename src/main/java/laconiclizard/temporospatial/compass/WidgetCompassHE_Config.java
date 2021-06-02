package laconiclizard.temporospatial.compass;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.HEConfig;
import laconiclizard.temporospatial.util.HEConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Temporospatial.MOD_ID + "-internal-WidgetCompass")
public class WidgetCompassHE_Config implements HEConfig<WidgetCompassHE_Config> {

    @ConfigEntry.Gui.TransitiveObject
    public HEConfigData heConfigData = new HEConfigData();

    public boolean preventSwing = true;
    public boolean worksEverywhere = true;
    public CompassTargetMode targetMode = CompassTargetMode.SPAWN;
    public String targetDimension = "";
    public int targetX, targetY, targetZ;

    @Override public HEConfigData heConfigData() {
        return heConfigData;
    }

    @Override public WidgetCompassHE_Config newInstance() {
        return new WidgetCompassHE_Config();
    }

    public void load(WidgetCompassHE_Config src) {
        heConfigData.load(src.heConfigData);
        preventSwing = src.preventSwing;
        worksEverywhere = src.worksEverywhere;
        targetMode = src.targetMode;
        targetDimension = src.targetDimension;
        targetX = src.targetX;
        targetY = src.targetY;
        targetZ = src.targetZ;
    }

}
