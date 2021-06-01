package laconiclizard.temporospatial.compass;

import laconiclizard.temporospatial.util.FlagTracker;
import laconiclizard.temporospatial.util.Util;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

// adapted from code for Compass in ModelPredicateProviderRegistry
public class Compass_MPP implements ModelPredicateProvider {

    public static final ModelPredicateProvider ORIGINAL_MPP = Util.waitUntilNotNull(
            () -> ModelPredicateProviderRegistry.get(Items.COMPASS, new Identifier("angle")), 1);

    public static final FlagTracker<ItemStack> PREVENT_SWING = new FlagTracker<>(true, false),
            WORK_EVERYWHERE = new FlagTracker<>(true, false),
            POINT_NORTH = new FlagTracker<>(true, false);

    /**
     * Whether the given compass is normal, ie. should perform the default behavior.
     *
     * @param stack compass of interest
     * @return whether the given compass is normal
     */
    public static boolean isNormal(ItemStack stack) {
        return !PREVENT_SWING.isFlagged(stack) && !WORK_EVERYWHERE.isFlagged(stack) && !POINT_NORTH.isFlagged(stack);
    }

    // ----- ----- predicate ----- -----

    private final ModelPredicateProviderRegistry.AngleInterpolator value = new ModelPredicateProviderRegistry.AngleInterpolator();
    private final ModelPredicateProviderRegistry.AngleInterpolator speed = new ModelPredicateProviderRegistry.AngleInterpolator();

    // todo support prevent swing
    // todo support work everywhere
    // todo support point north

    public float call(ItemStack itemStack, @Nullable ClientWorld clientWorld, @Nullable LivingEntity livingEntity) {
        if (isNormal(itemStack)) {  // if normal, fall back onto default
            return ORIGINAL_MPP.call(itemStack, clientWorld, livingEntity);
        }

        // actual calculations
        Entity entity = livingEntity != null ? livingEntity : itemStack.getHolder();
        if (entity == null) {
            return 0;
        } else {
            if (clientWorld == null && entity.world instanceof ClientWorld) {
                clientWorld = (ClientWorld) entity.world;
            }
            assert clientWorld != null;  // null check to satisfy static analysis

            BlockPos blockPos = CompassItem.hasLodestone(itemStack) ?
                    this.getLodestonePos(clientWorld, itemStack.getOrCreateTag())
                    : this.getSpawnPos(clientWorld);
            long l = clientWorld.getTime();
            if (blockPos != null && !(entity.getPos().squaredDistanceTo((double) blockPos.getX() + 0.5D, entity.getPos().getY(), (double) blockPos.getZ() + 0.5D) < 9.999999747378752E-6D)) {
                boolean bl = livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).isMainPlayer();
                double e = 0.0D;
                if (bl) {
                    e = livingEntity.yaw;
                } else if (entity instanceof ItemFrameEntity) {
                    e = this.getItemFrameAngleOffset((ItemFrameEntity) entity);
                } else if (entity instanceof ItemEntity) {
                    e = 180.0F - ((ItemEntity) entity).method_27314(0.5F) / 6.2831855F * 360.0F;
                } else if (livingEntity != null) {
                    e = livingEntity.bodyYaw;
                }

                e = MathHelper.floorMod(e / 360.0D, 1.0D);
                double f = this.getAngleToPos(Vec3d.ofCenter(blockPos), entity) / 6.2831854820251465D;
                double h;
                if (bl) {
                    if (this.value.shouldUpdate(l)) {
                        this.value.update(l, 0.5D - (e - 0.25D));
                    }

                    h = f + this.value.value;
                } else {
                    h = 0.5D - (e - 0.25D - f);
                }

                return MathHelper.floorMod((float) h, 1.0F);
            } else {
                if (this.speed.shouldUpdate(l)) {
                    this.speed.update(l, Math.random());
                }

                double d = this.speed.value + (double) ((float) itemStack.hashCode() / 2.14748365E9F);
                return MathHelper.floorMod((float) d, 1.0F);
            }
        }
    }

    @Nullable
    private BlockPos getSpawnPos(ClientWorld world) {
        return world.getDimension().isNatural() ? world.getSpawnPos() : null;
    }

    @Nullable
    private BlockPos getLodestonePos(World world, CompoundTag tag) {
        boolean bl = tag.contains("LodestonePos");
        boolean bl2 = tag.contains("LodestoneDimension");
        if (bl && bl2) {
            Optional<RegistryKey<World>> optional = CompassItem.getLodestoneDimension(tag);
            if (optional.isPresent() && world.getRegistryKey() == optional.get()) {
                return NbtHelper.toBlockPos(tag.getCompound("LodestonePos"));
            }
        }
        return null;
    }

    private double getItemFrameAngleOffset(ItemFrameEntity itemFrame) {
        Direction direction = itemFrame.getHorizontalFacing();
        int i = direction.getAxis().isVertical() ? 90 * direction.getDirection().offset() : 0;
        return MathHelper.wrapDegrees(180 + direction.getHorizontal() * 90 + itemFrame.getRotation() * 45 + i);
    }

    private double getAngleToPos(Vec3d pos, Entity entity) {
        return Math.atan2(pos.getZ() - entity.getZ(), pos.getX() - entity.getX());
    }

}
