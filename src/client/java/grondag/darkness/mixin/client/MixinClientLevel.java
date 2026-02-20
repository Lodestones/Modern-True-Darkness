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
import net.minecraft.world.phys.Vec3;
//?}

import net.minecraft.client.multiplayer.ClientLevel;
import grondag.darkness.Darkness;

@Mixin(ClientLevel.class)
@Environment(EnvType.CLIENT)
public class MixinClientLevel {

    //? if <=1.21.1 {
    @Inject(method = "getCloudColor", at = @At("RETURN"), cancellable = true)
    private void darkenCloudColor(float tickDelta, CallbackInfoReturnable<Vec3> cir) {
        if (Darkness.enabled && Darkness.skyDarkness < 1.0f) {
            Vec3 c = cir.getReturnValue();
            cir.setReturnValue(new Vec3(
                c.x * Darkness.skyDarkness,
                c.y * Darkness.skyDarkness,
                c.z * Darkness.skyDarkness
            ));
        }
    }
    //?} else if <=1.21.10 {
    /*@Inject(method = "getCloudColor", at = @At("RETURN"), cancellable = true)
    private void darkenCloudColor(float tickDelta, CallbackInfoReturnable<Integer> cir) {
        if (Darkness.enabled && Darkness.skyDarkness < 1.0f) {
            int c = cir.getReturnValue();
            float f = Darkness.skyDarkness;
            int r = Math.round(((c >> 16) & 0xFF) * f);
            int g = Math.round(((c >> 8) & 0xFF) * f);
            int b = Math.round((c & 0xFF) * f);
            cir.setReturnValue((c & 0xFF000000) | (r << 16) | (g << 8) | b);
        }
    }
    *///?}
}
