package laconiclizard.temporospatial.clock;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.ConfigSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

public class NumericClock_ConfigSerializer implements ConfigSerializer<NumericClock_Config> {

    public static final Object BACKER_LOCK = new Object();
    public static NumericClockHE BACKER;  // the NumericClock_Config to read/write from

    public NumericClock_ConfigSerializer(@SuppressWarnings("unused") Config definition,
                                         @SuppressWarnings("unused") Class<NumericClock_Config> configClass) {
        // nothing here
    }

    public static void setBacker(NumericClockHE backer) {
        synchronized (BACKER_LOCK) {
            if (BACKER != null && backer != null) {
                throw new AssertionError("Concurrent serialization of multiple NumericClock_Config s: "
                        + BACKER + " and " + backer);
            }
            BACKER = backer;
        }
    }

    @Override public void serialize(NumericClock_Config src) {
        synchronized (BACKER_LOCK) {
            if (BACKER != null) {
                synchronized (BACKER.lock) {
                    BACKER.config.load(src);
                    BACKER.updateFromConfig();
                }
                NumericClockHE.saveAll(null);
            }
        }
    }

    @Override public NumericClock_Config deserialize() {
        synchronized (BACKER_LOCK) {
            if (BACKER == null) {
                return createDefault();
            }
            return new NumericClock_Config(BACKER.config);
        }
    }

    @Override public NumericClock_Config createDefault() {
        return new NumericClock_Config();
    }

    /** Clears BACKER before redirecting to the given screen. */
    public static class ReturnScreen extends Screen {

        private final Screen dest;

        public ReturnScreen(Screen dest) {
            super(new LiteralText(""));
            this.dest = dest;
        }

        @Override protected void init() {
            super.init();
            setBacker(null);
            MinecraftClient.getInstance().openScreen(dest);
        }

    }

}
