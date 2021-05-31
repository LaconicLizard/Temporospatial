package laconiclizard.temporospatial.clock;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.ConfigSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

public class ClockWidget_ConfigSerializer implements ConfigSerializer<ClockWidget_Config> {

    private static final Object BACKER_LOCK = new Object();
    private static ClockWidgetHE BACKER;  // the ClockWidget_Config to read/write from

    public ClockWidget_ConfigSerializer(@SuppressWarnings("unused") Config definition,
                                        @SuppressWarnings("unused") Class<ClockWidget_Config> configClass) {
        // nothing here
    }

    public static void setBacker(ClockWidgetHE backer) {
        synchronized (BACKER_LOCK) {
            if (BACKER != null && backer != null) {
                throw new AssertionError("Concurrent modification of multiple ClockWidget_Config s: "
                        + BACKER + " and " + backer);
            }
            BACKER = backer;
        }
    }

    @Override public void serialize(ClockWidget_Config src) {
        synchronized (BACKER_LOCK) {
            if (BACKER != null) {
                synchronized (BACKER.lock) {
                    BACKER.config.load(src);
                    BACKER.updateFromConfig();
                }
                ClockWidgetHE.saveAll(null);
            }
        }
    }

    @Override public ClockWidget_Config deserialize() {
        synchronized (BACKER_LOCK) {
            if (BACKER == null) {
                return createDefault();
            }
            return new ClockWidget_Config(BACKER.config);
        }
    }

    @Override public ClockWidget_Config createDefault() {
        return new ClockWidget_Config();
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
