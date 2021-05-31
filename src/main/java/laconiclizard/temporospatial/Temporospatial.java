package laconiclizard.temporospatial;

import laconiclizard.temporospatial.clock.*;
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
    public static final Object CONFIG_HOLDER_LOCK = new Object();
    public static final ConfigHolder<TSConfig> CONFIG_HOLDER = AutoConfig.register(TSConfig.class, GsonConfigSerializer::new);

    public static final Object CLOCK_WIDGET_CONFIG_HOLDER_LOCK = new Object();
    public static final ConfigHolder<ClockWidget_Config> CLOCK_WIDGET_CONFIG_HOLDER
            = AutoConfig.register(ClockWidget_Config.class, ClockWidget_ConfigSerializer::new);
    public static final Object NUMERIC_CLOCK_CONFIG_HOLDER_LOCK = new Object();
    public static final ConfigHolder<NumericClock_Config> NUMERIC_CLOCK_CONFIG_HOLDER
            = AutoConfig.register(NumericClock_Config.class, NumericClock_ConfigSerializer::new);

    @Override public void onInitialize() {
        ModelPredicateProviderRegistry_Mixin.invokeRegister(Items.CLOCK, new Identifier("time"), new Clock_MPP());
        // config stuff
        synchronized (CONFIG_HOLDER_LOCK) {
            CONFIG_HOLDER.registerLoadListener((holder, config) -> {
                // ClockWidgetHEs
                synchronized (ClockWidgetHE.INSTANCES_LOCK) {  // clear old ones
                    for (ClockWidgetHE cw : ClockWidgetHE.INSTANCES) {
                        synchronized (cw.lock) {
                            if (cw.isEnabled()) {
                                cw.disableStrict();
                                cw.config.enabled = false;
                            }
                            cw.updateFromConfig();
                        }
                    }
                    ClockWidgetHE.INSTANCES.clear();
                }
                for (ClockWidget_Config cwc : config.clockWidgets) {  // load new ones
                    new ClockWidgetHE(cwc);
                }
                synchronized (NumericClockHE.INSTANCES_LOCK) {
                    for (NumericClockHE nc : NumericClockHE.INSTANCES) {
                        synchronized (nc.lock) {
                            if (nc.isEnabled()) {
                                nc.disableStrict();
                                nc.config.enabled = false;
                            }
                            nc.updateFromConfig();
                        }
                    }
                    NumericClockHE.INSTANCES.clear();
                }
                for (NumericClock_Config ncc : config.numericClocks) {
                    new NumericClockHE(ncc);
                }
                // clock globals
                Clock_MPP.ALL_WORK_EVERYWHERE = config.allClocks_workEverywhere;
                Clock_MPP.ALL_SWINGLESS = config.allClocks_preventSwing;
                // return pass
                return ActionResult.PASS;
            });
            CONFIG_HOLDER.load();
//            new ClockWidgetHE(new ClockWidget_Config()).enable();
//            new NumericClockHE(new NumericClock_Config()).enable();
        }
    }

}
