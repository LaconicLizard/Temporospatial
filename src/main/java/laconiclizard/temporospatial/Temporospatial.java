package laconiclizard.temporospatial;

import laconiclizard.temporospatial.mixin.ModelPredicateProviderRegistry_Mixin;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class Temporospatial implements ModInitializer {

    public static final String MOD_ID = "temporospatial";
    public static final ConfigHolder<TSConfig> CONFIG_HOLDER = AutoConfig.register(TSConfig.class, GsonConfigSerializer::new);
    public static final ClockHE CLOCK_HE = new ClockHE();

    @Override public void onInitialize() {
        ModelPredicateProviderRegistry_Mixin.invokeRegister(Items.CLOCK, new Identifier("time"), new Clock_MPP());
        // config stuff
        CONFIG_HOLDER.registerLoadListener((holder, config) -> {
            CLOCK_HE.setPos(config.clockHE_X, config.clockHE_Y);
            CLOCK_HE.setEnabled(config.clockHE_enabled);
            CLOCK_HE.scale = config.clockHE_scale;
            CLOCK_HE.setPreventSwing(config.clockHE_preventSwing);
            return ActionResult.PASS;
        });
        CONFIG_HOLDER.load();
    }

}
