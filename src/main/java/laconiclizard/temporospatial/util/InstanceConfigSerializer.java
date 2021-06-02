package laconiclizard.temporospatial.util;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.ConfigSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

public class InstanceConfigSerializer<T extends TSHudElement<C>, C extends InstanceConfig_HE<C>> implements ConfigSerializer<C> {

    private final Object lock = new Object();
    private T backer = null;
    private final C defaultConfig;

    public InstanceConfigSerializer(C defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    /** Function to pass into AutoConfig.register(...). */
    public InstanceConfigSerializer<T, C> registrationFunction(@SuppressWarnings("unused") Config definition,
                                                               @SuppressWarnings("unused") Class<C> configClass) {
        return this;
    }

    /** Set the config backing this serializer. */
    public void setBacker(T backer) {
        synchronized (lock) {
            if (this.backer != null && backer != null) {
                throw new AssertionError("Concurrent modification of InstanceConfigSerializer.backer: "
                        + this + " // " + this.backer + "to" + backer);
            }
            this.backer = backer;
        }
    }

    /** A screen that invokes .setBacker(null) before immediately redirecting to dest. */
    public Screen returnScreen(Screen dest) {
        return new Screen(new LiteralText("")) {

            @Override protected void init() {
                super.init();
                setBacker(null);
                MinecraftClient.getInstance().openScreen(dest);
            }

        };
    }

    @Override public void serialize(C c) {
        synchronized (lock) {
            if (backer != null) {
                synchronized (backer.lock) {
                    backer.config.load(c);
                    backer.updateFromConfig();
                    backer.saveAll();
                }
            }
            // do nothing if saving without backer; this is invoked upon registration
        }
    }

    @Override public C deserialize() {
        synchronized (lock) {
            if (this.backer == null) {
                return createDefault();
            } else {
                return backer.config.copy();
            }
        }
    }

    @Override public C createDefault() {
        return defaultConfig.copy();
    }

}
