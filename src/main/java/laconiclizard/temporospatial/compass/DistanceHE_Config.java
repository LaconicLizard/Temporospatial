package laconiclizard.temporospatial.compass;

import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.HEConfig;
import laconiclizard.temporospatial.util.HEConfigData;
import laconiclizard.temporospatial.util.TextConfig;
import laconiclizard.temporospatial.util.TextConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Temporospatial.MOD_ID + "-internal-DistanceHE")
public class DistanceHE_Config implements HEConfig<DistanceHE_Config>, TextConfig<DistanceHE_Config> {

    public HEConfigData heConfigData = new HEConfigData();
    public TextConfigData textConfigData = new TextConfigData();

    public boolean toSpawn = true;
    public float targetX, targetY, targetZ;
    public String numberFormat = "#.000", prefix = "", suffix = "";

    @Override public HEConfigData heConfigData() {
        return heConfigData;
    }

    @Override public TextConfigData textConfigData() {
        return textConfigData;
    }

    @Override public DistanceHE_Config newInstance() {
        return new DistanceHE_Config();
    }

    @Override public void load(DistanceHE_Config src) {
        heConfigData.load(src.heConfigData);
        textConfigData.load(src.textConfigData);
        toSpawn = src.toSpawn;
        targetX = src.targetX;
        targetY = src.targetY;
        targetZ = src.targetZ;
        numberFormat = src.numberFormat;
        prefix = src.prefix;
        suffix = src.suffix;
    }

}
