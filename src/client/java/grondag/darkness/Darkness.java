/*
 * This file is part of True Darkness and is licensed to the project under
 * terms that are compatible with the GNU Lesser General Public License.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership and licensing.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package grondag.darkness;

import grondag.darkness.compat.ApoliCompat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

import net.minecraft.util.Mth;

import static grondag.darkness.DarknessInit.CONFIG;

@Environment(EnvType.CLIENT)
public class Darkness {

    public static double darkNetherFog() {
        return CONFIG.darkNether ? 0.5 : 1.0;
    }

    public static double darkEndFog() {
        return CONFIG.darkEnd ? 0.0 : 1.0;
    }

    private static boolean isDark(Level world) {
        final ResourceKey<Level> dimType = world.dimension();

        if (dimType == Level.OVERWORLD) {
            return CONFIG.darkOverworld;
        } else if (dimType == Level.NETHER) {
            return CONFIG.darkNether;
        } else if (dimType == Level.END) {
            return CONFIG.darkEnd;
        } else {
            return true;
        }
    }

    //? if >=1.21.11 {
    /*private static float computeTimeOfDay(Level world) {
        long dayTime = world.getDayTime();
        double d = Mth.frac((double)dayTime / 24000.0 - 0.25);
        double e = 0.5 - Math.cos(d * Math.PI) / 2.0;
        return (float)(d * 2.0 + e) / 3.0f;
    }
    *///?}

    private static float skyFactor(Level world) {
        if (isDark(world)) {
            if (world.dimensionType().hasSkyLight()) {
                //? if >=1.21.11 {
                /*final float angle = computeTimeOfDay(world);
                *///?} else {
                final float angle = world.getTimeOfDay(0);
                //?}

                if (angle > 0.25f && angle < 0.75f) {
                    final float oldWeight = Math.max(0, (Math.abs(angle - 0.5f) - 0.2f)) * 20;
                    return Mth.lerp(oldWeight * oldWeight * oldWeight, 0f, 1.0f);
                } else {
                    return 1;
                }
            } else {
                return 0;
            }
        } else {
            return 1;
        }
    }

    public static boolean enabled = false;
    public static float skyDarkness = 1.0f;
    private static final float[][] LUMINANCE = new float[16][16];

    public static int darken(int c, int blockIndex, int skyIndex) {
        final float lTarget = LUMINANCE[blockIndex][skyIndex];
        //? if >=1.21.2 {
        /*final float r = ((c >> 16) & 0xFF) / 255f;
        final float g = ((c >> 8) & 0xFF) / 255f;
        final float b = (c & 0xFF) / 255f;
        final float l = luminance(r, g, b);
        final float f = l > 0 ? Math.min(1, lTarget / l) : 0;

        return f == 1f ? c
                : 0xFF000000 | (Math.round(f * r * 255) << 16) | (Math.round(f * g * 255) << 8)
                        | Math.round(f * b * 255);
        *///?} else {
        final float r = (c & 0xFF) / 255f;
        final float g = ((c >> 8) & 0xFF) / 255f;
        final float b = ((c >> 16) & 0xFF) / 255f;
        final float l = luminance(r, g, b);
        final float f = l > 0 ? Math.min(1, lTarget / l) : 0;

        return f == 1f ? c
                : 0xFF000000 | Math.round(f * r * 255) | (Math.round(f * g * 255) << 8)
                        | (Math.round(f * b * 255) << 16);
        //?}
    }

    public static float luminance(float r, float g, float b) {
        return r * 0.2126f + g * 0.7152f + b * 0.0722f;
    }

    public static void updateLuminance(float tickDelta, Minecraft client, GameRenderer worldRenderer,
            float prevFlicker) {
        final ClientLevel world = client.level;
        ApoliCompat apoliCompat = new ApoliCompat();

        if (world != null) {
            if (!isDark(world) || client.player.hasEffect(MobEffects.NIGHT_VISION)
                    || (client.player.hasEffect(MobEffects.CONDUIT_POWER) && client.player.getWaterVision() > 0)
                    //? if >=1.21.11 {
                    /*|| world.getThunderLevel(0) > 0.9F
                    *///?} else {
                    || world.getSkyFlashTime() > 0
                    //?}
                    //? if <=1.21.1 && !(=1.20.5 || =1.20.6) {
                    || apoliCompat.isApoliNightVisionPower(client)
                    //?}
            ) {
                enabled = false;
                skyDarkness = 1.0f;
                return;
            } else {
                enabled = true;
            }

            final float dimSkyFactor = Darkness.skyFactor(world);
            skyDarkness = dimSkyFactor;
            //? if >=1.21.11 {
            /*final float timeAngle = computeTimeOfDay(world);
            float ambient = 1.0F - (Mth.cos(timeAngle * ((float)Math.PI * 2F)) * 2.0F + 0.5F);
            ambient = Mth.clamp(ambient, 0.0F, 1.0F);
            ambient = 1.0F - ambient;
            ambient *= 1.0F - world.getRainLevel(0) * 5.0F / 16.0F;
            ambient *= 1.0F - world.getThunderLevel(0) * 5.0F / 16.0F;
            *///?} else {
            final float ambient = world.getSkyDarken(1.0F);
            //?}
            final DimensionType dim = world.dimensionType();
            final boolean blockAmbient = !Darkness.isDark(world);

            for (int skyIndex = 0; skyIndex < 16; ++skyIndex) {
                float skyFactor = 1f - skyIndex / 15f;
                skyFactor = 1 - skyFactor * skyFactor * skyFactor * skyFactor;
                skyFactor *= dimSkyFactor;

                float min = skyFactor * 0.05f;
                final float rawAmbient = ambient * skyFactor;
                final float minAmbient = rawAmbient * (1 - min) + min;
                final float skyBase = LightTexture.getBrightness(dim, skyIndex) * minAmbient;

                min = 0.35f * skyFactor;
                float skyRed = skyBase * (rawAmbient * (1 - min) + min);
                float skyGreen = skyBase * (rawAmbient * (1 - min) + min);
                float skyBlue = skyBase;

                if (worldRenderer.getDarkenWorldAmount(tickDelta) > 0.0F) {
                    final float skyDarkness = worldRenderer.getDarkenWorldAmount(tickDelta);
                    skyRed = skyRed * (1.0F - skyDarkness) + skyRed * 0.7F * skyDarkness;
                    skyGreen = skyGreen * (1.0F - skyDarkness) + skyGreen * 0.6F * skyDarkness;
                    skyBlue = skyBlue * (1.0F - skyDarkness) + skyBlue * 0.6F * skyDarkness;
                }

                for (int blockIndex = 0; blockIndex < 16; ++blockIndex) {
                    float blockFactor = 1f;

                    if (!blockAmbient) {
                        blockFactor = 1f - blockIndex / 15f;
                        blockFactor = 1 - blockFactor * blockFactor * blockFactor * blockFactor;
                    }

                    final float blockBase = blockFactor * LightTexture.getBrightness(dim, blockIndex)
                            * (prevFlicker * 0.1F + 1.5F);
                    min = 0.4f * blockFactor;
                    final float blockGreen = blockBase * ((blockBase * (1 - min) + min) * (1 - min) + min);
                    final float blockBlue = blockBase * (blockBase * blockBase * (1 - min) + min);

                    float red = skyRed + blockBase;
                    float green = skyGreen + blockGreen;
                    float blue = skyBlue + blockBlue;

                    final float f = Math.max(skyFactor, blockFactor);
                    min = 0.03f * f;
                    red = red * (0.99F - min) + min;
                    green = green * (0.99F - min) + min;
                    blue = blue * (0.99F - min) + min;

                    if (world.dimension() == Level.END) {
                        red = skyFactor * 0.22F + blockBase * 0.75f;
                        green = skyFactor * 0.28F + blockGreen * 0.75f;
                        blue = skyFactor * 0.25F + blockBlue * 0.75f;
                    }

                    if (red > 1.0F) {
                        red = 1.0F;
                    }

                    if (green > 1.0F) {
                        green = 1.0F;
                    }

                    if (blue > 1.0F) {
                        blue = 1.0F;
                    }

                    final float gamma = client.options.gamma().get().floatValue() * f;
                    float invRed = 1.0F - red;
                    float invGreen = 1.0F - green;
                    float invBlue = 1.0F - blue;
                    invRed = 1.0F - invRed * invRed * invRed * invRed;
                    invGreen = 1.0F - invGreen * invGreen * invGreen * invGreen;
                    invBlue = 1.0F - invBlue * invBlue * invBlue * invBlue;
                    red = red * (1.0F - gamma) + invRed * gamma;
                    green = green * (1.0F - gamma) + invGreen * gamma;
                    blue = blue * (1.0F - gamma) + invBlue * gamma;

                    min = 0.03f * f;
                    red = red * (0.99F - min) + min;
                    green = green * (0.99F - min) + min;
                    blue = blue * (0.99F - min) + min;

                    if (red > 1.0F) {
                        red = 1.0F;
                    }

                    if (green > 1.0F) {
                        green = 1.0F;
                    }

                    if (blue > 1.0F) {
                        blue = 1.0F;
                    }

                    if (red < 0.0F) {
                        red = 0.0F;
                    }

                    if (green < 0.0F) {
                        green = 0.0F;
                    }

                    if (blue < 0.0F) {
                        blue = 0.0F;
                    }

                    LUMINANCE[blockIndex][skyIndex] = Darkness.luminance(red, green, blue);
                }
            }
        }
    }
}
