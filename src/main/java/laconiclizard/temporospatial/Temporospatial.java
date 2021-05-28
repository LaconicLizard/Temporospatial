package laconiclizard.temporospatial;

import laconiclizard.temporospatial.client.ClockHE;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.ActionResult;

public class Temporospatial implements ModInitializer {

    public static final String MOD_ID = "temporospatial";
    public static final ConfigHolder<TSConfig> CONFIG_HOLDER = AutoConfig.register(TSConfig.class, GsonConfigSerializer::new);
    public static final ClockHE CLOCK_HE = new ClockHE();

    @Override public void onInitialize() {
        CONFIG_HOLDER.registerLoadListener((holder, config) -> {
            CLOCK_HE.setPos(config.clockHE_X, config.clockHE_Y);
            CLOCK_HE.setEnabled(config.clockHE_enabled);
            CLOCK_HE.scale = config.clockHE_scale;
            return ActionResult.PASS;
        });
        CONFIG_HOLDER.load();
    }

}
