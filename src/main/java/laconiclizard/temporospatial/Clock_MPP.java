package laconiclizard.temporospatial;

import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// adapted from code in ModelPredicateProviderRegistry for CLOCK
public class Clock_MPP implements ModelPredicateProvider {

    private static final Set<ItemStack> SWINGLESS_CLOCKS = ConcurrentHashMap.newKeySet();

    public static final ItemStack NORMAL_CLOCK = new ItemStack(Items.CLOCK);
    public static final ItemStack SWINGLESS_CLOCK = new ItemStack(Items.CLOCK);

    static {
        preventSwing(SWINGLESS_CLOCK);
    }

    public static void preventSwing(ItemStack stack) {
        SWINGLESS_CLOCKS.add(stack);
    }

    public static void releaseSwing(ItemStack stack) {
        SWINGLESS_CLOCKS.remove(stack);
    }

    // ----- ----- implementation ----- -----

    private double time;
    private double step;
    private long lastTick;

    public float call(ItemStack itemStack, @Nullable ClientWorld clientWorld, @Nullable LivingEntity livingEntity) {
        Entity entity = livingEntity != null ? livingEntity : itemStack.getHolder();
        if (entity == null) {
            return 0.0F;
        } else {
            if (clientWorld == null && entity.world instanceof ClientWorld) {
                clientWorld = (ClientWorld) entity.world;
            }

            if (clientWorld == null) {
                return 0.0F;
            } else {
                double e;
                if (clientWorld.getDimension().isNatural()) {
                    e = clientWorld.getSkyAngle(1.0F);
                } else {
                    e = Math.random();
                }

                e = this.getTime(clientWorld, e, SWINGLESS_CLOCKS.contains(itemStack));
                return (float) e;
            }
        }
    }

    private double getTime(World world, double skyAngle, boolean swingless) {
        long wt = world.getTime();
        if (wt != this.lastTick) {
            this.lastTick = wt;
            double d = skyAngle - this.time;
            d = MathHelper.floorMod(d + 0.5D, 1.0D) - 0.5D;
            if (!swingless) {
                this.step += d * 0.1d;
                this.step *= 0.9D;
            } else {
                this.step = d;
            }
            this.time = MathHelper.floorMod(this.time + this.step, 1.0D);
        }

        return this.time;
    }
}
