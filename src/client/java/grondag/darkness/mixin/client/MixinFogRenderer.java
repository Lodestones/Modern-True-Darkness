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

package grondag.darkness.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if <=1.21.1 {
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.systems.RenderSystem;
//?}

//? if >=1.21.2 {
/*import org.joml.Vector4f;
*///?}

//? if <=1.21.5 {
import net.minecraft.client.renderer.FogRenderer;
//?} else {
/*import net.minecraft.client.renderer.fog.FogRenderer;
*///?}

import grondag.darkness.Darkness;

@Mixin(FogRenderer.class)
@Environment(EnvType.CLIENT)
public class MixinFogRenderer {
    //? if <=1.21.1 {
    @Shadow
    private static float fogRed;
    @Shadow
    private static float fogGreen;
    @Shadow
    private static float fogBlue;

    @Inject(method = "setupColor", at = @At("TAIL"))
    private static void darkenFogColor(CallbackInfo ci) {
        if (Darkness.enabled && Darkness.skyDarkness < 1.0f) {
            fogRed *= Darkness.skyDarkness;
            fogGreen *= Darkness.skyDarkness;
            fogBlue *= Darkness.skyDarkness;
            RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);
        }
    }
    //?} else {
    /*@Inject(method = "computeFogColor", at = @At("RETURN"), cancellable = true)
    //? if >=1.21.6 && <=1.21.10 {
    /^private void darkenFogColor(CallbackInfoReturnable<Vector4f> cir) {
    ^///?} else if >=1.21.11 {
    /^private void darkenFogColor(CallbackInfoReturnable<Vector4f> cir) {
    ^///?} else {
    private static void darkenFogColor(CallbackInfoReturnable<Vector4f> cir) {
    //?}
        if (Darkness.enabled && Darkness.skyDarkness < 1.0f) {
            Vector4f color = cir.getReturnValue();
            cir.setReturnValue(new Vector4f(
                color.x * Darkness.skyDarkness,
                color.y * Darkness.skyDarkness,
                color.z * Darkness.skyDarkness,
                color.w
            ));
        }
    }
    *///?}
}
