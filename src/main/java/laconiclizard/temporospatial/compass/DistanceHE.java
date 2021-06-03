package laconiclizard.temporospatial.compass;

import laconiclizard.hudelements.AlterHudScreen;
import laconiclizard.temporospatial.TSConfig;
import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceTracker;
import laconiclizard.temporospatial.util.TSTextHudElement;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static laconiclizard.temporospatial.util.Util.THE_END_ID;
import static laconiclizard.temporospatial.util.Util.THE_NETHER_ID;

public class DistanceHE extends TSTextHudElement<DistanceHE_Config> {

    public static final InstanceTracker<DistanceHE> INSTANCES = new InstanceTracker<>();

    protected boolean cachedConfig_toSpawn;
    protected float cachedConfig_targetX, cachedConfig_targetY, cachedConfig_targetZ;
    protected String cachedConfig_numberFormat, cachedConfig_prefix, cachedConfig_suffix;

    protected DecimalFormat numberFormat;

    public DistanceHE(DistanceHE_Config config) {
        super(config);
        updateFromConfig();
        INSTANCES.add(this);
    }

    @Override public void updateFromConfig() {
        super.updateFromConfig();
        cachedConfig_toSpawn = config.toSpawn;
        cachedConfig_targetX = config.targetX;
        cachedConfig_targetY = config.targetY;
        cachedConfig_targetZ = config.targetZ;
        numberFormat = new DecimalFormat(cachedConfig_numberFormat = config.numberFormat);
        cachedConfig_prefix = config.prefix;
        cachedConfig_suffix = config.suffix;
    }

    @Override public String generateText() {
        final MinecraftClient client = MinecraftClient.getInstance();
        final ClientPlayerEntity player = client.player;
        if (player == null) return "";
        final Vec3d playerPos = player.getPos();
        if (cachedConfig_toSpawn) {
            ClientWorld world = client.world;
            if (world == null) return "";
            final Identifier did = world.getRegistryKey().getValue();
            if (world.getDimension().isNatural() && !THE_NETHER_ID.equals(did) && !THE_END_ID.equals(did)) {
                final BlockPos spawn = world.getSpawnPos();
                return cachedConfig_prefix + numberFormat.format(playerPos.distanceTo(
                        new Vec3d(spawn.getX() + .5, playerPos.getY(), spawn.getZ() + .5)))
                        + cachedConfig_suffix;
            } else if (THE_NETHER_ID.equals(did)) {
                final BlockPos spawn = world.getSpawnPos();
                //noinspection IntegerDivisionInFloatingPointContext
                return cachedConfig_prefix + numberFormat.format(playerPos.distanceTo(
                        new Vec3d((spawn.getX() / 8) + .5, playerPos.getY(), (spawn.getZ() / 8) + .5)))
                        + cachedConfig_suffix;
            } else {  // the end
                return cachedConfig_prefix + numberFormat.format(playerPos.distanceTo(new Vec3d(0, playerPos.getY(), 0)))
                        + cachedConfig_suffix;
            }
        } else {
            return cachedConfig_prefix + numberFormat.format(playerPos.distanceTo(
                    new Vec3d(cachedConfig_targetX, cachedConfig_targetY, cachedConfig_targetZ)))
                    + cachedConfig_suffix;
        }
    }

    @Override public void saveAll() {
        List<DistanceHE_Config> configs;
        synchronized (INSTANCES.lock) {
            configs = new ArrayList<>(INSTANCES.instances.size());
            for (DistanceHE he : INSTANCES.instances) {
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
            config.distanceDisplays = configs;
            Temporospatial.CONFIG_HOLDER.value.save();
        }
    }

    @Override public boolean isEditable() {
        return true;
    }

    @Override public void edit() {
        Temporospatial.DISTANCE_HE_CONFIG_SERIALIZER.setBacker(this);
        synchronized (Temporospatial.DISTANCE_HE_CONFIG_HOLDER.lock) {
            Temporospatial.DISTANCE_HE_CONFIG_HOLDER.value.load();
        }
        MinecraftClient.getInstance().openScreen(
                AutoConfig.getConfigScreen(DistanceHE_Config.class,
                        Temporospatial.DISTANCE_HE_CONFIG_SERIALIZER.returnScreen(new AlterHudScreen()))
                        .get());
    }

}
