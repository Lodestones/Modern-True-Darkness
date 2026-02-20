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

//? if >=1.21.11 {
/*import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import net.minecraft.client.renderer.CloudRenderer;
*///?}

import grondag.darkness.Darkness;

//? if >=1.21.11 {
/*@Mixin(CloudRenderer.class)
*///?} else {
@Mixin(net.minecraft.client.Minecraft.class)
//?}
@Environment(EnvType.CLIENT)
public class MixinCloudRenderer {

    //? if >=1.21.11 {
    /*@ModifyVariable(method = "render", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private int darkenCloudColor(int color) {
        if (Darkness.enabled && Darkness.skyDarkness < 1.0f) {
            float f = Darkness.skyDarkness;
            int a = (color >> 24) & 0xFF;
            int r = Math.round(((color >> 16) & 0xFF) * f);
            int g = Math.round(((color >> 8) & 0xFF) * f);
            int b = Math.round((color & 0xFF) * f);
            return (a << 24) | (r << 16) | (g << 8) | b;
        }
        return color;
    }
    *///?}
}
