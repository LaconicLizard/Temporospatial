package laconiclizard.temporospatial.compass;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceConfig;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Temporospatial.MOD_ID + "-internal-WidgetCompass")
public class WidgetCompassHE_Config extends InstanceConfig<WidgetCompassHE_Config> {

    public boolean enabled;
    public float x, y, z;
    public float scale = 1f;
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
        enabled = src.enabled;
        x = src.x;
        y = src.y;
        z = src.z;
        scale = src.scale;
        preventSwing = src.preventSwing;
        worksEverywhere = src.worksEverywhere;
        targetMode = src.targetMode;
        targetDimension = src.targetDimension;
        targetX = src.targetX;
        targetY = src.targetY;
        targetZ = src.targetZ;
    }

}
