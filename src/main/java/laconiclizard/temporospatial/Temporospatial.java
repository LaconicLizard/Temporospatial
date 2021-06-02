package laconiclizard.temporospatial;

import laconiclizard.hudelements.api.HudElement;
import laconiclizard.temporospatial.clock.*;
import laconiclizard.temporospatial.compass.*;
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

    public static final InstanceConfigSerializer<WidgetClockHE, WidgetClockHE_Config> WIDGET_CLOCK_CONFIG_SERIALIZER
            = new InstanceConfigSerializer<>(new WidgetClockHE_Config());
    public static final SyncHolder<ConfigHolder<WidgetClockHE_Config>> WIDGET_CLOCK_CONFIG_HOLDER
            = new SyncHolder<>(AutoConfig.register(WidgetClockHE_Config.class, WIDGET_CLOCK_CONFIG_SERIALIZER::registrationFunction));

    public static final InstanceConfigSerializer<NumericClockHE, NumericClockHE_Config> NUMERIC_CLOCK_CONFIG_SERIALIZER
            = new InstanceConfigSerializer<>(new NumericClockHE_Config());
    public static final SyncHolder<ConfigHolder<NumericClockHE_Config>> NUMERIC_CLOCK_CONFIG_HOLDER
            = new SyncHolder<>(AutoConfig.register(NumericClockHE_Config.class, NUMERIC_CLOCK_CONFIG_SERIALIZER::registrationFunction));

    public static final InstanceConfigSerializer<WidgetCompassHE, WidgetCompassHE_Config> WIDGET_COMPASS_CONFIG_SERIALIZER
            = new InstanceConfigSerializer<>(new WidgetCompassHE_Config());
    public static final SyncHolder<ConfigHolder<WidgetCompassHE_Config>> WIDGET_COMPASS_CONFIG_HOLDER
            = new SyncHolder<>(AutoConfig.register(WidgetCompassHE_Config.class, WIDGET_COMPASS_CONFIG_SERIALIZER::registrationFunction));

    public static final InstanceConfigSerializer<CoordHE, CoordHE_Config> COORD_HE_CONFIG_SERIALIZER
            = new InstanceConfigSerializer<>(new CoordHE_Config());
    public static final SyncHolder<ConfigHolder<CoordHE_Config>> COORD_HE_CONFIG_HOLDER
            = new SyncHolder<>(AutoConfig.register(CoordHE_Config.class, COORD_HE_CONFIG_SERIALIZER::registrationFunction));

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
            synchronized (WidgetCompassHE.INSTANCES.lock) {
                for (WidgetCompassHE wc : WidgetCompassHE.INSTANCES.instances) {
                    wc.enterAlterHud();
                }
            }
            synchronized (CoordHE.INSTANCES.lock) {
                for (CoordHE he : CoordHE.INSTANCES.instances) {
                    he.enterAlterHud();
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
            synchronized (WidgetCompassHE.INSTANCES.lock) {
                for (WidgetCompassHE wc : WidgetCompassHE.INSTANCES.instances) {
                    wc.exitAlterHud();
                }
            }
            synchronized (CoordHE.INSTANCES.lock) {
                for (CoordHE he : CoordHE.INSTANCES.instances) {
                    he.exitAlterHud();
                }
            }
        });
    }

    @Override public void onInitialize() {
        ModelPredicateProviderRegistry_Mixin.invokeRegister(Items.CLOCK, new Identifier("time"), new Clock_MPP());
        ModelPredicateProviderRegistry_Mixin.invokeRegister(Items.COMPASS, new Identifier("angle"), new Compass_MPP());
        // config stuff
        synchronized (CONFIG_HOLDER.lock) {
            CONFIG_HOLDER.value.registerLoadListener((holder, config) -> {
                // clocks
                synchronized (WidgetClockHE.INSTANCES.lock) {  // clear old ones
                    for (WidgetClockHE cw : WidgetClockHE.INSTANCES.instances) {
                        synchronized (cw.lock) {
                            cw.config.enabled = false;
                            cw.updateFromConfig();
                        }
                    }
                    WidgetClockHE.INSTANCES.instances.clear();
                }
                for (WidgetClockHE_Config cwc : config.widgetClocks) {  // load new ones
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
                for (NumericClockHE_Config ncc : config.numericClocks) {
                    new NumericClockHE(ncc);
                }
                // compasses
                synchronized (WidgetCompassHE.INSTANCES.lock) {
                    for (WidgetCompassHE wc : WidgetCompassHE.INSTANCES.instances) {
                        synchronized (wc.lock) {
                            wc.config.enabled = false;
                            wc.updateFromConfig();
                        }
                    }
                    WidgetCompassHE.INSTANCES.instances.clear();
                }
                for (WidgetCompassHE_Config c : config.widgetCompasses) {
                    new WidgetCompassHE(c);
                }
                synchronized (CoordHE.INSTANCES.lock) {
                    for (CoordHE he : CoordHE.INSTANCES.instances) {
                        synchronized (he.lock) {
                            he.config.enabled = false;
                            he.updateFromConfig();
                        }
                    }
                    CoordHE.INSTANCES.instances.clear();
                }
                for (CoordHE_Config c : config.coordDisplays) {
                    new CoordHE(c);
                }
                // globals
                Clock_MPP.PREVENT_SWING.setAllFlagged(config.allClocks_preventSwing);
                Clock_MPP.WORK_EVERYWHERE.setAllFlagged(config.allClocks_workEverywhere);
                Clock_MPP.REALTIME.setAllFlagged(config.allClocks_realtime);
                Compass_MPP.PREVENT_SWING.setAllFlagged(config.allCompasses_preventSwing);
                Compass_MPP.WORK_EVERYWHERE.setAllFlagged(config.allCompasses_workEverywhere);
                Compass_MPP.POINT_NORTH.setAllFlagged(config.allCompasses_pointNorth);
                // return pass
                return ActionResult.PASS;
            });
            CONFIG_HOLDER.value.load();
//            CoordHE he = new CoordHE(new CoordHE_Config());
//            he.config.enabled = true;
//            he.updateFromConfig();
//            he.save();
//            CONFIG_HOLDER.value.load();
        }
    }

}
