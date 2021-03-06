package laconiclizard.temporospatial;

import laconiclizard.hudelements.api.HudElement;
import laconiclizard.temporospatial.clock.*;
import laconiclizard.temporospatial.compass.*;
import laconiclizard.temporospatial.mixin.ModelPredicateProviderRegistry_Mixin;
import laconiclizard.temporospatial.util.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;

public class Temporospatial implements ModInitializer {

    public static final String MOD_ID = "temporospatial";

    // global config
    public static final SyncHolder<ConfigHolder<TSConfig>> CONFIG_HOLDER
            = new SyncHolder<>(AutoConfig.register(TSConfig.class, GsonConfigSerializer::new));

    // hud element config stuff

    public static final HEConfigSerializer<WidgetClockHE, WidgetClockHE_Config> WIDGET_CLOCK_CONFIG_SERIALIZER
            = new HEConfigSerializer<>(new WidgetClockHE_Config());
    public static final SyncHolder<ConfigHolder<WidgetClockHE_Config>> WIDGET_CLOCK_CONFIG_HOLDER
            = new SyncHolder<>(AutoConfig.register(WidgetClockHE_Config.class, WIDGET_CLOCK_CONFIG_SERIALIZER::registrationFunction));

    public static final HEConfigSerializer<NumericClockHE, NumericClockHE_Config> NUMERIC_CLOCK_CONFIG_SERIALIZER
            = new HEConfigSerializer<>(new NumericClockHE_Config());
    public static final SyncHolder<ConfigHolder<NumericClockHE_Config>> NUMERIC_CLOCK_CONFIG_HOLDER
            = new SyncHolder<>(AutoConfig.register(NumericClockHE_Config.class, NUMERIC_CLOCK_CONFIG_SERIALIZER::registrationFunction));

    public static final HEConfigSerializer<WidgetCompassHE, WidgetCompassHE_Config> WIDGET_COMPASS_CONFIG_SERIALIZER
            = new HEConfigSerializer<>(new WidgetCompassHE_Config());
    public static final SyncHolder<ConfigHolder<WidgetCompassHE_Config>> WIDGET_COMPASS_CONFIG_HOLDER
            = new SyncHolder<>(AutoConfig.register(WidgetCompassHE_Config.class, WIDGET_COMPASS_CONFIG_SERIALIZER::registrationFunction));

    public static final HEConfigSerializer<CoordHE, CoordHE_Config> COORD_HE_CONFIG_SERIALIZER
            = new HEConfigSerializer<>(new CoordHE_Config());
    public static final SyncHolder<ConfigHolder<CoordHE_Config>> COORD_HE_CONFIG_HOLDER
            = new SyncHolder<>(AutoConfig.register(CoordHE_Config.class, COORD_HE_CONFIG_SERIALIZER::registrationFunction));

    public static final HEConfigSerializer<AnglesHE, AnglesHE_Config> ANGLES_HE_CONFIG_SERIALIZER
            = new HEConfigSerializer<>(new AnglesHE_Config());
    public static final SyncHolder<ConfigHolder<AnglesHE_Config>> ANGLES_HE_CONFIG_HOLDER
            = new SyncHolder<>(AutoConfig.register(AnglesHE_Config.class, ANGLES_HE_CONFIG_SERIALIZER::registrationFunction));

    public static final HEConfigSerializer<DistanceHE, DistanceHE_Config> DISTANCE_HE_CONFIG_SERIALIZER
            = new HEConfigSerializer<>(new DistanceHE_Config());
    public static final SyncHolder<ConfigHolder<DistanceHE_Config>> DISTANCE_HE_CONFIG_HOLDER
            = new SyncHolder<>(AutoConfig.register(DistanceHE_Config.class, DISTANCE_HE_CONFIG_SERIALIZER::registrationFunction));

    public static final List<InstanceTracker<? extends TSHudElement<?>>> INSTANCE_TRACKERS
            = Arrays.asList(WidgetClockHE.INSTANCES, NumericClockHE.INSTANCES, WidgetCompassHE.INSTANCES,
            CoordHE.INSTANCES, AnglesHE.INSTANCES, DistanceHE.INSTANCES);

    static {
        // enable all of our hud elements when in the /alterhud screen
        HudElement.PRE_ALTERHUD.connect((unused) -> {
            for (InstanceTracker<? extends TSHudElement<?>> tracker : INSTANCE_TRACKERS) {
                synchronized (tracker.lock) {
                    for (TSHudElement<?> he : tracker.instances) {
                        he.enterAlterHud();
                    }
                }
            }
        });
        HudElement.POST_ALTERHUD.connect((unused) -> {
            for (InstanceTracker<? extends TSHudElement<?>> tracker : INSTANCE_TRACKERS) {
                synchronized (tracker.lock) {
                    for (TSHudElement<?> he : tracker.instances) {
                        he.exitAlterHud();
                    }
                }
            }
        });
    }

    private static void loadStateFromConfig(TSConfig config) {
        // deactivate and delete old stuff
        for (InstanceTracker<? extends TSHudElement<?>> tracker : INSTANCE_TRACKERS) {
            synchronized (tracker.lock) {
                for (TSHudElement<?> he : tracker.instances) {
                    he.config.heConfigData().enabled = false;
                    he.updateFromConfig();
                }
            }
            tracker.instances.clear();
        }
        // load new hud elements
        for (WidgetClockHE_Config c : config.widgetClocks) {
            new WidgetClockHE(c);
        }
        for (NumericClockHE_Config c : config.numericClocks) {
            new NumericClockHE(c);
        }
        for (WidgetCompassHE_Config c : config.widgetCompasses) {
            new WidgetCompassHE(c);
        }
        for (CoordHE_Config c : config.coordDisplays) {
            new CoordHE(c);
        }
        for (AnglesHE_Config c : config.anglesDisplays) {
            new AnglesHE(c);
        }
        for (DistanceHE_Config c : config.distanceDisplays) {
            new DistanceHE(c);
        }
        // globals
        Clock_MPP.PREVENT_SWING.setAllFlagged(config.allClocks_preventSwing);
        Clock_MPP.WORK_EVERYWHERE.setAllFlagged(config.allClocks_workEverywhere);
        Clock_MPP.REALTIME.setAllFlagged(config.allClocks_realtime);
        Compass_MPP.PREVENT_SWING.setAllFlagged(config.allCompasses_preventSwing);
        Compass_MPP.WORK_EVERYWHERE.setAllFlagged(config.allCompasses_workEverywhere);
        Compass_MPP.POINT_NORTH.setAllFlagged(config.allCompasses_pointNorth);
    }

    @Override public void onInitialize() {
        ModelPredicateProviderRegistry_Mixin.invokeRegister(Items.CLOCK, new Identifier("time"), new Clock_MPP());
        ModelPredicateProviderRegistry_Mixin.invokeRegister(Items.COMPASS, new Identifier("angle"), new Compass_MPP());
        // config stuff
        synchronized (CONFIG_HOLDER.lock) {
            CONFIG_HOLDER.value.registerLoadListener((holder, config) -> {
                loadStateFromConfig(config);
                return ActionResult.PASS;
            });
            CONFIG_HOLDER.value.registerSaveListener((holder, config) -> {
                loadStateFromConfig(config);
                return ActionResult.PASS;
            });
            CONFIG_HOLDER.value.load();
            DistanceHE he = new DistanceHE(new DistanceHE_Config());
            he.config.heConfigData.enabled = true;
            he.updateFromConfig();
            he.save();
            CONFIG_HOLDER.value.load();
        }
    }

}
