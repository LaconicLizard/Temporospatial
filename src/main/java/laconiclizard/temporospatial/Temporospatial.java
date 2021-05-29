package laconiclizard.temporospatial;

import laconiclizard.temporospatial.clock.ClockHE;
import laconiclizard.temporospatial.clock.Clock_MPP;
import laconiclizard.temporospatial.clock.NumericClockHE;
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
    public static final ClockHE CLOCK_HE = new ClockHE(false);
    public static final NumericClockHE NUMERIC_CLOCK_HE = new NumericClockHE(false);

    public static final ClockHE REALTIME_CLOCK_HE = new ClockHE(true);
    public static final NumericClockHE NUMERIC_REALTIME_CLOCK_HE = new NumericClockHE(true);

    @Override public void onInitialize() {
        ModelPredicateProviderRegistry_Mixin.invokeRegister(Items.CLOCK, new Identifier("time"), new Clock_MPP());
        // config stuff
        CONFIG_HOLDER.registerLoadListener((holder, config) -> {
            // clock hud element
            CLOCK_HE.setPos(config.clockHE_X, config.clockHE_Y);
            CLOCK_HE.setEnabled(config.clockHE_enabled);
            CLOCK_HE.scale = config.clockHE_scale;
            CLOCK_HE.setPreventSwing(config.clockHE_preventSwing);
            CLOCK_HE.setWorksInNether(config.clockHE_worksInNether);
            CLOCK_HE.setWorksInEnd(config.clockHE_worksInEnd);
            // numeric clock hud element
            NUMERIC_CLOCK_HE.setPos(config.numericClockHE_X, config.numericClockHE_Y);
            NUMERIC_CLOCK_HE.setEnabled(config.numericClockHE_enabled);
            NUMERIC_CLOCK_HE.scale = config.numericClockHE_scale;
            NUMERIC_CLOCK_HE.textColor = config.numericClockHE_textColor;
            NUMERIC_CLOCK_HE.backgroundColor = config.numericClockHE_backgroundColor;
            NUMERIC_CLOCK_HE.borderColor = config.numericClockHE_borderColor;
            NUMERIC_CLOCK_HE.borderThickness = config.numericClockHE_borderThickness;
            // realtime clock hud element
            REALTIME_CLOCK_HE.setPos(config.realtimeClockHE_X, config.realtimeClockHE_Y);
            REALTIME_CLOCK_HE.setEnabled(config.realtimeClockHE_enabled);
            REALTIME_CLOCK_HE.scale = config.realtimeClockHE_scale;
            // numeric realtime clock hud element
            NUMERIC_REALTIME_CLOCK_HE.setPos(config.numericRealtimeClockHE_X, config.numericRealtimeClockHE_Y);
            NUMERIC_REALTIME_CLOCK_HE.setEnabled(config.numericRealtimeClockHE_enabled);
            NUMERIC_REALTIME_CLOCK_HE.scale = config.numericRealtimeClockHE_scale;
            NUMERIC_REALTIME_CLOCK_HE.textColor = config.numericRealtimeClockHE_textColor;
            NUMERIC_REALTIME_CLOCK_HE.backgroundColor = config.numericRealtimeClockHE_backgroundColor;
            NUMERIC_REALTIME_CLOCK_HE.borderColor = config.numericRealtimeClockHE_borderColor;
            NUMERIC_REALTIME_CLOCK_HE.borderThickness = config.numericRealtimeClockHE_borderThickness;
            NUMERIC_REALTIME_CLOCK_HE.setFormatString(config.numericRealtimeClockHE_format);
            // misc
            Clock_MPP.ALL_SWINGLESS = config.allClocks_preventSwing;
            Clock_MPP.ALL_WORK_IN_NETHER = config.allClocks_workInNether;
            Clock_MPP.ALL_WORK_IN_END = config.allClocks_workInEnd;
            Clock_MPP.ALL_WORK_EVERYWHERE = config.allClocks_workEverywhere;
            return ActionResult.PASS;
        });
        CONFIG_HOLDER.load();
    }

}
