package laconiclizard.temporospatial.compass;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceConfig_HE;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Temporospatial.MOD_ID + "-internal-WidgetCompass")
public class WidgetCompassHE_Config extends InstanceConfig_HE<WidgetCompassHE_Config> {

    public boolean preventSwing = true;
    public boolean worksEverywhere = true;
    public CompassTargetMode targetMode = CompassTargetMode.SPAWN;
    public String targetDimension = "";
    public int targetX, targetY, targetZ;

    public WidgetCompassHE_Config() {
    }

    @Override public WidgetCompassHE_Config newInstance() {
        return new WidgetCompassHE_Config();
    }

    public void load(WidgetCompassHE_Config src) {
        super.load(src);
        preventSwing = src.preventSwing;
        worksEverywhere = src.worksEverywhere;
        targetMode = src.targetMode;
        targetDimension = src.targetDimension;
        targetX = src.targetX;
        targetY = src.targetY;
        targetZ = src.targetZ;
    }

}
