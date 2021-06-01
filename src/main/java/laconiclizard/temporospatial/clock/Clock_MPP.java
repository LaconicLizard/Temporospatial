package laconiclizard.temporospatial.clock;

import laconiclizard.temporospatial.util.FlagTracker;
import laconiclizard.temporospatial.util.Util;
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

// adapted from code in ModelPredicateProviderRegistry for CLOCK
public class Clock_MPP implements ModelPredicateProvider {

    public static final ModelPredicateProvider ORIGINAL_MPP = Util.waitUntilNotNull(
            () -> ModelPredicateProviderRegistry.get(Items.CLOCK, new Identifier("time")), 1);

    public static final FlagTracker<ItemStack> PREVENT_SWING = new FlagTracker<>(true, false),
            WORK_EVERYWHERE = new FlagTracker<>(true, false),
            REALTIME = new FlagTracker<>(true, false);

    public static boolean isNormal(ItemStack stack) {
        return !PREVENT_SWING.isFlagged(stack) && !WORK_EVERYWHERE.isFlagged(stack) && !REALTIME.isFlagged(stack);
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
                boolean isNatural = clientWorld.getDimension().isNatural();
                boolean isRealtime = REALTIME.isFlagged(itemStack);
                if (isNatural || WORK_EVERYWHERE.isFlagged(itemStack)) {
                    if (isRealtime) {
                        LocalTime now = LocalTime.now();
                        int t = now.getHour() * (60 * 60) + now.getMinute() * 60 + now.getSecond();
                        e = t / (24 * 60 * 60d);
                    } else {
                        if (isNatural) {
                            e = clientWorld.getSkyAngle(1.0F);
                        } else {
                            e = Util.unfixedSkyAngle(clientWorld.getLunarTime());
                        }
                    }
                } else {
                    e = Math.random();
                }
                e = this.getTime(clientWorld, e, PREVENT_SWING.isFlagged(itemStack), isRealtime);
                return (float) e;
            }
        }
    }

    private double getTime(World world, double skyAngle, boolean preventSwing, boolean isRealtime) {
        if (isRealtime) {
            return MathHelper.floorMod(skyAngle - .5, 1);
        }
        // minecraft time
        long wt = world.getTime();
        if (preventSwing) {
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
