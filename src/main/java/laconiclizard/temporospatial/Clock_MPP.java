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

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

// adapted from code in ModelPredicateProviderRegistry for CLOCK
public class Clock_MPP implements ModelPredicateProvider {

    private static final Set<ItemStack> SWINGLESS_CLOCKS = Collections.synchronizedSet(
            Collections.newSetFromMap(new IdentityHashMap<>()));
    public static boolean ALL_SWINGLESS = false;  // if true, then all clocks will be swingless

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

    // track swinging and non-swinging values separately
    // (not in separate classes bc. most code is identical)
    private double timeSwinging, timeSwingless;
    private double step;  // only relevant to swinging clocks
    private long lastTickSwinging, lastTickSwingless;

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

                e = this.getTime(clientWorld, e, ALL_SWINGLESS || SWINGLESS_CLOCKS.contains(itemStack));
                return (float) e;
            }
        }
    }

    private double getTime(World world, double skyAngle, boolean swingless) {
        long wt = world.getTime();
        if (swingless) {
            if (wt != this.lastTickSwingless) {
                this.lastTickSwingless = wt;
                double d = skyAngle - this.timeSwingless;
                d = MathHelper.floorMod(d + .5, 1) - .5;
                this.timeSwingless = MathHelper.floorMod(this.timeSwingless + d, 1);
            }
            return this.timeSwingless;
        } else {
            if (wt != this.lastTickSwinging) {
                this.lastTickSwinging = wt;
                double d = skyAngle - this.timeSwinging;
                d = MathHelper.floorMod(d + .5, 1) - .5;
                this.step += d * .1;
                this.step *= .9;
                this.timeSwinging = MathHelper.floorMod(this.timeSwinging + this.step, 1);
            }
            return this.timeSwinging;
        }
    }
}
