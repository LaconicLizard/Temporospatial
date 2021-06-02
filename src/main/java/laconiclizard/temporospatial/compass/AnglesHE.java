package laconiclizard.temporospatial.compass;

import laconiclizard.hudelements.AlterHudScreen;
import laconiclizard.temporospatial.TSConfig;
import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceTracker;
import laconiclizard.temporospatial.util.TSTextHudElement;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AnglesHE extends TSTextHudElement<AnglesHE_Config> {

    public static final InstanceTracker<AnglesHE> INSTANCES = new InstanceTracker<>();

    private String cachedConfig_separator, cachedConfig_prefix, cachedConfig_suffix;
    private DecimalFormat format;

    public AnglesHE(AnglesHE_Config config) {
        super(config);
        updateFromConfig();
        INSTANCES.add(this);
    }

    @Override public void updateFromConfig() {
        super.updateFromConfig();
        cachedConfig_separator = config.separator;
        cachedConfig_prefix = config.prefix;
        cachedConfig_suffix = config.suffix;
        format = new DecimalFormat(config.numberFormat);
    }

    @Override public String generateText() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return "";
        return cachedConfig_prefix
                + format.format(player.getYaw(0)) + cachedConfig_separator
                + format.format(player.getPitch(0)) + cachedConfig_suffix;
    }

    @Override public void saveAll() {
        List<AnglesHE_Config> configs;
        synchronized (INSTANCES.lock) {
            configs = new ArrayList<>(INSTANCES.instances.size());
            for (AnglesHE he : INSTANCES.instances) {
                if (he == this) {
                    configs.add(he.config.copy());
                } else {
                    synchronized (he.lock) {
                        configs.add(he.config.copy());
                    }
                }
            }
        }
        synchronized (Temporospatial.CONFIG_HOLDER.lock) {
            TSConfig config = Temporospatial.CONFIG_HOLDER.value.getConfig();
            config.anglesDisplays = configs;
            Temporospatial.CONFIG_HOLDER.value.save();
        }
    }

    @Override public boolean isEditable() {
        return true;
    }

    @Override public void edit() {
        Temporospatial.ANGLES_HE_CONFIG_SERIALIZER.setBacker(this);
        synchronized (Temporospatial.ANGLES_HE_CONFIG_HOLDER.lock) {
            Temporospatial.ANGLES_HE_CONFIG_HOLDER.value.load();
        }
        MinecraftClient.getInstance().openScreen(
                AutoConfig.getConfigScreen(AnglesHE_Config.class,
                        Temporospatial.ANGLES_HE_CONFIG_SERIALIZER.returnScreen(new AlterHudScreen()))
                        .get());
    }

}
