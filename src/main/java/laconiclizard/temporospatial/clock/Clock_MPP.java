package laconiclizard.temporospatial.clock;

import laconiclizard.temporospatial.Util;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.time.LocalTime;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

// adapted from code in ModelPredicateProviderRegistry for CLOCK
public class Clock_MPP implements ModelPredicateProvider {

    public static final ModelPredicateProvider ORIGINAL_MPP = Util.waitUntilNotNull(
            () -> ModelPredicateProviderRegistry.get(Items.CLOCK, new Identifier("time")), 1);

    private static final Set<ItemStack> SWINGLESS_CLOCKS = Collections.synchronizedSet(
            Collections.newSetFromMap(new IdentityHashMap<>()));
    public static boolean ALL_SWINGLESS = false;  // if true, then all clocks will be swingless
    private static final Set<ItemStack> NETHER_CLOCKS = Collections.synchronizedSet(
            Collections.newSetFromMap(new IdentityHashMap<>()));
    public static boolean ALL_WORK_IN_NETHER;
    private static final Set<ItemStack> END_CLOCKS = Collections.synchronizedSet(
            Collections.newSetFromMap(new IdentityHashMap<>()));
    public static boolean ALL_WORK_IN_END;
    private static final Set<ItemStack> REALTIME_CLOCKS = Collections.synchronizedSet(
            Collections.newSetFromMap(new IdentityHashMap<>()));
    // no global "ALL_REALTIME" variable, as that doesn't really make sense
    public static boolean ALL_WORK_EVERYWHERE;

    public static final ItemStack NORMAL_CLOCK = new ItemStack(Items.CLOCK);
    public static final ItemStack PERFECT_CLOCK = new ItemStack(Items.CLOCK);

    static {
        preventSwing(PERFECT_CLOCK);
        makeWorkInNether(PERFECT_CLOCK);
        makeWorkInEnd(PERFECT_CLOCK);
    }

    public static void preventSwing(ItemStack stack) {
        SWINGLESS_CLOCKS.add(stack);
    }

    public static void releaseSwing(ItemStack stack) {
        SWINGLESS_CLOCKS.remove(stack);
    }

    public static void makeWorkInNether(ItemStack stack) {
        NETHER_CLOCKS.add(stack);
    }

    public static void freeFromWorkingInNether(ItemStack stack) {
        NETHER_CLOCKS.remove(stack);
    }

    public static void makeWorkInEnd(ItemStack stack) {
        END_CLOCKS.add(stack);
    }

    public static void freeFromWorkingInEnd(ItemStack stack) {
        END_CLOCKS.remove(stack);
    }

    public static void makeRealtime(ItemStack stack) {
        REALTIME_CLOCKS.add(stack);
    }

    public static void stopRealtime(ItemStack stack) {
        REALTIME_CLOCKS.remove(stack);
    }

    // ----- ----- implementation ----- -----

    public static boolean swings(ItemStack stack) {
        return ALL_SWINGLESS || SWINGLESS_CLOCKS.contains(stack);
    }

    public static boolean worksInNether(ItemStack stack) {
        return ALL_WORK_IN_NETHER || NETHER_CLOCKS.contains(stack);
    }

    public static boolean worksInEnd(ItemStack stack) {
        return ALL_WORK_IN_END || END_CLOCKS.contains(stack);
    }

    public static boolean isRealtime(ItemStack stack) {
        return REALTIME_CLOCKS.contains(stack);
    }

    public static boolean isNormal(ItemStack stack) {
        return swings(stack) && !worksInNether(stack) && !worksInEnd(stack) && !isRealtime(stack);
    }

    // track swinging and non-swinging values separately
    // (not in separate classes bc. most code is identical)
    private double timeSwinging, timeSwingless;
    private double step;  // only relevant to swinging clocks
    private long lastTickSwinging, lastTickSwingless;

    public float call(ItemStack itemStack, @Nullable ClientWorld clientWorld, @Nullable LivingEntity livingEntity) {
        if (isNormal(itemStack)) {  // if normal, fall back on default
            return ORIGINAL_MPP.call(itemStack, clientWorld, livingEntity);
        }
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
                boolean isRealtime = isRealtime(itemStack);
                if (isRealtime) {
                    LocalTime now = LocalTime.now();
                    int t = now.getHour() * (60 * 60) + now.getMinute() * 60 + now.getSecond();
                    e = t / (24 * 60 * 60d);
                } else {
                    if (clientWorld.getDimension().isNatural()) {
                        e = clientWorld.getSkyAngle(1.0F);
                    } else {
                        Identifier did = clientWorld.getRegistryKey().getValue();
                        if (ALL_WORK_EVERYWHERE || (did.equals(Util.THE_NETHER_ID) && worksInNether(itemStack))
                                || (did.equals(Util.THE_END_ID) && worksInEnd(itemStack))) {
                            e = Util.unfixedSkyAngle(clientWorld.getLunarTime());
                        } else {
                            e = Math.random();
                        }
                    }
                }

                e = this.getTime(clientWorld, e, swings(itemStack), isRealtime);
                return (float) e;
            }
        }
    }

    private double getTime(World world, double skyAngle, boolean swingless, boolean isRealtime) {
        if (isRealtime) {
            return MathHelper.floorMod(skyAngle - .5, 1);
        }
        // minecraft time
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
