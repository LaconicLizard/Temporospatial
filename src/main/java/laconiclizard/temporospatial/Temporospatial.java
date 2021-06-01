package laconiclizard.temporospatial;

import laconiclizard.hudelements.api.HudElement;
import laconiclizard.temporospatial.clock.*;
import laconiclizard.temporospatial.mixin.ModelPredicateProviderRegistry_Mixin;
import laconiclizard.temporospatial.util.InstanceConfigSerializer;
import laconiclizard.temporospatial.util.SyncHolder;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class Temporospatial implements ModInitializer {

    public static final String MOD_ID = "temporospatial";
    public static final SyncHolder<ConfigHolder<TSConfig>> CONFIG_HOLDER
            = new SyncHolder<>(AutoConfig.register(TSConfig.class, GsonConfigSerializer::new));

    public static final InstanceConfigSerializer<WidgetClockHE, WidgetClock_Config> WIDGET_CLOCK_CONFIG_SERIALIZER
            = new InstanceConfigSerializer<>(new WidgetClock_Config());
    public static final SyncHolder<ConfigHolder<WidgetClock_Config>> WIDGET_CLOCK_CONFIG_HOLDER
            = new SyncHolder<>(AutoConfig.register(WidgetClock_Config.class, WIDGET_CLOCK_CONFIG_SERIALIZER::registrationFunction));

    public static final InstanceConfigSerializer<NumericClockHE, NumericClock_Config> NUMERIC_CLOCK_CONFIG_SERIALIZER
            = new InstanceConfigSerializer<>(new NumericClock_Config());
    public static final SyncHolder<ConfigHolder<NumericClock_Config>> NUMERIC_CLOCK_CONFIG_HOLDER
            = new SyncHolder<>(AutoConfig.register(NumericClock_Config.class, NUMERIC_CLOCK_CONFIG_SERIALIZER::registrationFunction));

    static {
        // enable all of our hud elements when in the /alterhud screen
        HudElement.PRE_ALTERHUD.connect((unused) -> {
            synchronized (WidgetClockHE.INSTANCES.lock) {
                for (WidgetClockHE cw : WidgetClockHE.INSTANCES.instances) {
                    cw.enterAlterHud();
                }
            }
            synchronized (NumericClockHE.INSTANCES.lock) {
                for (NumericClockHE nc : NumericClockHE.INSTANCES.instances) {
                    nc.enterAlterHud();
                }
            }

        });
        HudElement.POST_ALTERHUD.connect((unused) -> {
            synchronized (WidgetClockHE.INSTANCES.lock) {
                for (WidgetClockHE cw : WidgetClockHE.INSTANCES.instances) {
                    cw.exitAlterHud();
                }
            }
            synchronized (NumericClockHE.INSTANCES.lock) {
                for (NumericClockHE nc : NumericClockHE.INSTANCES.instances) {
                    nc.exitAlterHud();
                }
            }
        });
    }

    @Override public void onInitialize() {
        ModelPredicateProviderRegistry_Mixin.invokeRegister(Items.CLOCK, new Identifier("time"), new Clock_MPP());
        // config stuff
        synchronized (CONFIG_HOLDER.lock) {
            CONFIG_HOLDER.value.registerLoadListener((holder, config) -> {
                // widget clocks
                synchronized (WidgetClockHE.INSTANCES.lock) {  // clear old ones
                    for (WidgetClockHE cw : WidgetClockHE.INSTANCES.instances) {
                        synchronized (cw.lock) {
                            cw.config.enabled = false;
                            cw.updateFromConfig();
                        }
                    }
                    WidgetClockHE.INSTANCES.instances.clear();
                }
                for (WidgetClock_Config cwc : config.widgetClocks) {  // load new ones
                    new WidgetClockHE(cwc);
                }
                synchronized (NumericClockHE.INSTANCES.lock) {
                    for (NumericClockHE nc : NumericClockHE.INSTANCES.instances) {
                        synchronized (nc.lock) {
                            nc.config.enabled = false;
                            nc.updateFromConfig();
                        }
                    }
                    NumericClockHE.INSTANCES.instances.clear();
                }
                for (NumericClock_Config ncc : config.numericClocks) {
                    new NumericClockHE(ncc);
                }
                // clock globals
                Clock_MPP.PREVENT_SWING.setAllFlagged(config.allClocks_preventSwing);
                Clock_MPP.WORK_EVERYWHERE.setAllFlagged(config.allClocks_workEverywhere);
                Clock_MPP.REALTIME.setAllFlagged(config.allClocks_realtime);
                // return pass
                return ActionResult.PASS;
            });
            CONFIG_HOLDER.value.load();
        }
    }

}
