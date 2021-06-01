package laconiclizard.temporospatial.compass;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceConfig;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Temporospatial.MOD_ID + "-internal-WidgetCompass")
public class WidgetCompass_Config extends InstanceConfig<WidgetCompass_Config> {

    public boolean enabled;
    public float x, y, z;
    public float scale = 1f;
    public boolean preventSwing = true;
    public boolean worksEverywhere = true;
    public CompassTargetMode targetMode = CompassTargetMode.SPAWN;
    public String targetDimension = "";
    public int targetX, targetY, targetZ;

    public WidgetCompass_Config() {
    }

    @Override public WidgetCompass_Config newInstance() {
        return new WidgetCompass_Config();
    }

    public void load(WidgetCompass_Config src) {
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
