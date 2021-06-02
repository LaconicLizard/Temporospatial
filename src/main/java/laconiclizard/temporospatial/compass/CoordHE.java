package laconiclizard.temporospatial.compass;

import laconiclizard.hudelements.AlterHudScreen;
import laconiclizard.temporospatial.TSConfig;
import laconiclizard.temporospatial.Temporospatial;
import laconiclizard.temporospatial.util.InstanceTracker;
import laconiclizard.temporospatial.util.TSHudElement;
import laconiclizard.temporospatial.util.Util;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CoordHE extends TSHudElement<CoordHE_Config> {

    public static final InstanceTracker<CoordHE> INSTANCES = new InstanceTracker<>();

    // cached config values
    private float scale;
    private int textColor, backgroundColor, borderColor;
    private float borderThickness;
    private String separator;
    private DecimalFormat format;

    private long lastTime = -1;
    private String lastText = null;

    public CoordHE(CoordHE_Config config) {
        super(config);
        updateFromConfig();
        INSTANCES.add(this);
    }

    private String getText() {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return "";
        long wt = world.getTime();
        if (wt == lastTime && lastText != null) {
            return lastText;
        }
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return "";
        lastTime = wt;
        return lastText = format.format(player.getX()) + separator + format.format(player.getY()) + separator + format.format(player.getZ());
    }

    @Override public void updateFromConfig() {
        setEnabled(config.enabled);
        setX(config.x);
        setY(config.y);
        setZ(config.z);
        scale = config.scale;
        textColor = config.textColor;
        backgroundColor = config.backgroundColor;
        borderColor = config.borderColor;
        borderThickness = config.borderThickness;
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

    @Override public void save() {
        config.x = getX();
        config.y = getY();
        saveAll();
    }

    @Override public void render(MatrixStack matrices, float tickDelta) {
        Util.drawTextWithFrills(matrices, scale, getText(), getX(), getY(), textColor, backgroundColor, borderThickness, borderColor);
    }

    @Override public float getWidth() {
        return MinecraftClient.getInstance().textRenderer.getWidth(getText());
    }

    @Override public float getHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
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
