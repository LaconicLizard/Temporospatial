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

public class CoordHE extends TSTextHudElement<CoordHE_Config> {

    public static final InstanceTracker<CoordHE> INSTANCES = new InstanceTracker<>();

    private String separator;
    private DecimalFormat format;

    public CoordHE(CoordHE_Config config) {
        super(config);
        updateFromConfig();
        INSTANCES.add(this);
    }

    @Override public String generateText() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return "";
        return format.format(player.getX()) + separator + format.format(player.getY()) + separator + format.format(player.getZ());
    }

    @Override public void updateFromConfig() {
        super.updateFromConfig();
        separator = config.separator;
        format = new DecimalFormat(config.numberFormat);
    }

    @Override public void saveAll() {
        List<CoordHE_Config> configs;
        synchronized (INSTANCES.lock) {
            configs = new ArrayList<>(INSTANCES.instances.size());
            for (CoordHE he : INSTANCES.instances) {
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
            config.coordDisplays = configs;
            Temporospatial.CONFIG_HOLDER.value.save();
        }
    }

    @Override public boolean isEditable() {
        return true;
    }

    @Override public void edit() {
        Temporospatial.COORD_HE_CONFIG_SERIALIZER.setBacker(this);
        synchronized (Temporospatial.COORD_HE_CONFIG_HOLDER.lock) {
            Temporospatial.COORD_HE_CONFIG_HOLDER.value.load();
        }
        MinecraftClient.getInstance().openScreen(
                AutoConfig.getConfigScreen(CoordHE_Config.class,
                        Temporospatial.COORD_HE_CONFIG_SERIALIZER.returnScreen(new AlterHudScreen()))
                        .get());
    }

}
