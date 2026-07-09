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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if <=1.21.1 {
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
//?}

import net.minecraft.client.renderer.LightTexture;

//? if >=1.21.2 {
/*import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;
*///?}

//? if >=1.21.2 && <=1.21.4 {
/*import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.CompiledShaderProgram;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.world.effect.MobEffectInstance;
import java.util.Objects;
*///?}

//? if =1.21.5 {
/*import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import java.util.OptionalInt;
*///?}

//? if >=1.21.6 {
/*import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.GpuBuffer.MappedView;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.RenderPipelines;
import java.util.OptionalInt;
*///?}

//? if >=1.21.6 && <=1.21.8 {
/*import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
*///?}

//? if >=1.21.9 {
/*import net.minecraft.client.renderer.EndFlashState;
*///?}

//? if >=1.21.11 {
/*import net.minecraft.client.Camera;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.EnvironmentAttributes;
*///?}

import grondag.darkness.Darkness;
import grondag.darkness.LightmapAccess;

@Mixin(LightTexture.class)
@Environment(EnvType.CLIENT)
public class MixinLightTexture implements LightmapAccess {
    //? if <=1.21.1 {
    @Shadow
    private NativeImage lightPixels;
    //?}
    @Shadow
    private float blockLightRedFlicker;
    @Shadow
    private boolean updateLightTexture;

    //? if >=1.21.2 {
    /*@Shadow @Final
    private GameRenderer renderer;
    @Shadow @Final
    private Minecraft minecraft;

    @Shadow
    private float calculateDarknessScale(LivingEntity livingEntity, float f, float g) {
        throw new AssertionError();
    }
    *///?}

    //? if >=1.21.2 && <=1.21.4 {
    /*@Shadow @Final
    private TextureTarget target;

    @Shadow
    private float getDarknessGamma(float f) {
        throw new AssertionError();
    }
    *///?}

    //? if =1.21.5 {
    /*@Shadow @Final
    private GpuTexture texture;
    *///?}

    //? if >=1.21.6 {
    /*@Shadow @Final
    private GpuTexture texture;
    @Shadow @Final
    private GpuTextureView textureView;
    @Shadow @Final
    private MappableRingBuffer ubo;
    *///?}

    //? if <=1.21.1 {
    @Inject(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/DynamicTexture;upload()V"))
    private void onUpload(CallbackInfo ci) {
        if (Darkness.enabled && lightPixels != null) {
            for (int b = 0; b < 16; b++) {
                for (int s = 0; s < 16; s++) {
                    final int color = Darkness.darken(lightPixels.getPixelRGBA(b, s), b, s);
                    lightPixels.setPixelRGBA(b, s, color);
                }
            }
        }
    }
    //?}

    //? if >=1.21.2 {
    /*@Inject(method = "updateLightTexture", at = @At("HEAD"), cancellable = true)
    private void darkness_updateLightTexture(float f, CallbackInfo ci) {
        if (!Darkness.enabled) return;

        ci.cancel();

        if (this.updateLightTexture) {
            this.updateLightTexture = false;
            ProfilerFiller profilerFiller = Profiler.get();
            profilerFiller.push("lightTex");
            ClientLevel clientLevel = this.minecraft.level;
            if (clientLevel != null) {
                // --- Compute sky factor ---
                //? if >=1.21.11 {
                /^Camera camera = this.minecraft.gameRenderer.getMainCamera();
                int skyLightColorInt = (Integer) camera.attributeProbe().getValue(EnvironmentAttributes.SKY_LIGHT_COLOR, f);
                float ambientLight = clientLevel.dimensionType().ambientLight();
                float skyFactor = (Float) camera.attributeProbe().getValue(EnvironmentAttributes.SKY_LIGHT_FACTOR, f);
                EndFlashState endFlashState = clientLevel.endFlashState();
                Vector3f flashSkyColor;
                if (endFlashState != null) {
                    flashSkyColor = new Vector3f(0.99F, 1.12F, 1.0F);
                    if (!(Boolean) this.minecraft.options.hideLightningFlash().get()) {
                        float flashIntensity = endFlashState.getIntensity(f);
                        if (this.minecraft.gui.getBossOverlay().shouldCreateWorldFog()) {
                            skyFactor += flashIntensity / 3.0F;
                        } else {
                            skyFactor += flashIntensity;
                        }
                    }
                } else {
                    flashSkyColor = new Vector3f(1.0F, 1.0F, 1.0F);
                }
                ^///?} else if >=1.21.9 {
                /^float skyDarken = clientLevel.getSkyDarken(1.0F);
                float ambientLight = clientLevel.dimensionType().ambientLight();
                float skyFactor;
                Vector3f flashSkyColor;
                if (clientLevel.effects().hasEndFlashes()) {
                    flashSkyColor = new Vector3f(0.99F, 1.12F, 1.0F);
                    EndFlashState endFlashState = clientLevel.endFlashState();
                    if (endFlashState != null && !(Boolean) this.minecraft.options.hideLightningFlash().get()) {
                        float flashIntensity = endFlashState.getIntensity(f);
                        if (this.minecraft.gui.getBossOverlay().shouldCreateWorldFog()) {
                            skyFactor = flashIntensity / 3.0F;
                        } else {
                            skyFactor = flashIntensity;
                        }
                    } else {
                        skyFactor = 0.0F;
                    }
                } else {
                    flashSkyColor = new Vector3f(1.0F, 1.0F, 1.0F);
                    if (clientLevel.getSkyFlashTime() > 0) {
                        skyFactor = 1.0F;
                    } else {
                        skyFactor = skyDarken * 0.95F + 0.05F;
                    }
                }
                Vector3f skyLightColor;
                if (clientLevel.effects().hasEndFlashes()) {
                    skyLightColor = new Vector3f(0.9F, 0.5F, 1.0F);
                } else {
                    skyLightColor = new Vector3f(skyDarken, skyDarken, 1.0F).lerp(new Vector3f(1.0F, 1.0F, 1.0F), 0.35F);
                }
                ^///?} else {
                float skyDarken = clientLevel.getSkyDarken(1.0F);
                float ambientLight = clientLevel.dimensionType().ambientLight();
                float skyFactor;
                if (clientLevel.getSkyFlashTime() > 0) {
                    skyFactor = 1.0F;
                } else {
                    skyFactor = skyDarken * 0.95F + 0.05F;
                }
                Vector3f skyLightColor = new Vector3f(skyDarken, skyDarken, 1.0F).lerp(new Vector3f(1.0F, 1.0F, 1.0F), 0.35F);
                //?}

                // === DARKNESS MODIFICATION: darken sky factor ===
                skyFactor *= Darkness.skyDarkness;

                // === DARKNESS MODIFICATION: darken sky light color ===
                //? if >=1.21.11 {
                /^Vector3f darkSkyLightColor = ARGB.vector3fFromRGB24(skyLightColorInt);
                darkSkyLightColor.mul(Darkness.skyDarkness);
                ^///?} else {
                skyLightColor.mul(Darkness.skyDarkness);
                //?}

                // --- Compute effect factors ---
                float darknessEffectScale = ((Double) this.minecraft.options.darknessEffectScale().get()).floatValue();
                //? if >=1.21.5 {
                /^float darknessGamma = this.minecraft.player.getEffectBlendFactor(MobEffects.DARKNESS, f) * darknessEffectScale;
                ^///?} else {
                float darknessGamma = this.getDarknessGamma(f) * darknessEffectScale;
                //?}
                float darknessScale = this.calculateDarknessScale(this.minecraft.player, darknessGamma, f) * darknessEffectScale;

                // === DARKNESS MODIFICATION: boost DarknessScale to overcome shader floor ===
                darknessScale = Math.max(darknessScale, (1.0f - Darkness.skyDarkness) * 0.15f);

                float waterVision = this.minecraft.player.getWaterVision();
                float nightVisionFactor;
                if (this.minecraft.player.hasEffect(MobEffects.NIGHT_VISION)) {
                    nightVisionFactor = GameRenderer.getNightVisionScale(this.minecraft.player, f);
                } else if (waterVision > 0.0F && this.minecraft.player.hasEffect(MobEffects.CONDUIT_POWER)) {
                    nightVisionFactor = waterVision;
                } else {
                    nightVisionFactor = 0.0F;
                }

                float blockFactor = this.blockLightRedFlicker + 1.5F;
                //? if <=1.21.8 {
                /^boolean useBrightLightmap = clientLevel.effects().forceBrightLightmap();
                ^///?}
                float gamma = ((Double) this.minecraft.options.gamma().get()).floatValue();

                // === DARKNESS MODIFICATION: darken gamma with cave brightness blending ===
                gamma *= Darkness.caveGammaFactor;

                // --- Render lightmap (version-specific) ---
                //? if >=1.21.11 {
                /^// Branch: 1.21.11+ (GpuTexture + GpuTextureView + UBO + EnvironmentAttributes)
                CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
                try (MappedView mappedView = commandEncoder.mapBuffer(this.ubo.currentBuffer(), false, true)) {
                    Std140Builder.intoBuffer(mappedView.data())
                        .putFloat(ambientLight)
                        .putFloat(skyFactor)
                        .putFloat(blockFactor)
                        .putFloat(nightVisionFactor)
                        .putFloat(darknessScale)
                        .putFloat(this.renderer.getDarkenWorldAmount(f))
                        .putFloat(Math.max(0.0F, gamma - darknessGamma))
                        .putVec3(darkSkyLightColor)
                        .putVec3(flashSkyColor);
                }
                try (RenderPass renderPass = commandEncoder.createRenderPass(() -> "Update light", this.textureView, OptionalInt.empty())) {
                    renderPass.setPipeline(RenderPipelines.LIGHTMAP);
                    RenderSystem.bindDefaultUniforms(renderPass);
                    renderPass.setUniform("LightmapInfo", this.ubo.currentBuffer());
                    renderPass.draw(0, 3);
                }
                this.ubo.rotate();
                ^///?} else if >=1.21.9 {
                /^// Branch: 1.21.9-1.21.10 (GpuTexture + GpuTextureView + UBO + EndFlashState)
                CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
                try (MappedView mappedView = commandEncoder.mapBuffer(this.ubo.currentBuffer(), false, true)) {
                    Std140Builder.intoBuffer(mappedView.data())
                        .putFloat(ambientLight)
                        .putFloat(skyFactor)
                        .putFloat(blockFactor)
                        .putFloat(nightVisionFactor)
                        .putFloat(darknessScale)
                        .putFloat(this.renderer.getDarkenWorldAmount(f))
                        .putFloat(Math.max(0.0F, gamma - darknessGamma))
                        .putVec3(skyLightColor)
                        .putVec3(flashSkyColor);
                }
                try (RenderPass renderPass = commandEncoder.createRenderPass(() -> "Update light", this.textureView, OptionalInt.empty())) {
                    renderPass.setPipeline(RenderPipelines.LIGHTMAP);
                    RenderSystem.bindDefaultUniforms(renderPass);
                    renderPass.setUniform("LightmapInfo", this.ubo.currentBuffer());
                    renderPass.draw(0, 3);
                }
                this.ubo.rotate();
                ^///?} else if >=1.21.6 {
                /^// Branch: 1.21.6-1.21.8 (GpuTexture + GpuTextureView + UBO)
                CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
                try (MappedView mappedView = commandEncoder.mapBuffer(this.ubo.currentBuffer(), false, true)) {
                    Std140Builder.intoBuffer(mappedView.data())
                        .putFloat(ambientLight)
                        .putFloat(skyFactor)
                        .putFloat(blockFactor)
                        .putInt(useBrightLightmap ? 1 : 0)
                        .putFloat(nightVisionFactor)
                        .putFloat(darknessScale)
                        .putFloat(this.renderer.getDarkenWorldAmount(f))
                        .putFloat(Math.max(0.0F, gamma - darknessGamma))
                        .putVec3(skyLightColor);
                }
                RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
                GpuBuffer gpuBuffer = autoStorageIndexBuffer.getBuffer(6);
                try (RenderPass renderPass = commandEncoder.createRenderPass(() -> "Update light", this.textureView, OptionalInt.empty())) {
                    renderPass.setPipeline(RenderPipelines.LIGHTMAP);
                    RenderSystem.bindDefaultUniforms(renderPass);
                    renderPass.setUniform("LightmapInfo", this.ubo.currentBuffer());
                    renderPass.setVertexBuffer(0, RenderSystem.getQuadVertexBuffer());
                    renderPass.setIndexBuffer(gpuBuffer, autoStorageIndexBuffer.type());
                    renderPass.drawIndexed(0, 0, 6, 1);
                }
                this.ubo.rotate();
                ^///?} else if =1.21.5 {
                /^// Branch: 1.21.5 (GpuTexture + RenderPass, individual uniforms)
                RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
                GpuBuffer gpuBuffer = autoStorageIndexBuffer.getBuffer(6);
                try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(this.texture, OptionalInt.empty())) {
                    renderPass.setPipeline(RenderPipelines.LIGHTMAP);
                    renderPass.setUniform("AmbientLightFactor", new float[]{ambientLight});
                    renderPass.setUniform("SkyFactor", new float[]{skyFactor});
                    renderPass.setUniform("BlockFactor", new float[]{blockFactor});
                    renderPass.setUniform("UseBrightLightmap", new int[]{useBrightLightmap ? 1 : 0});
                    renderPass.setUniform("SkyLightColor", new float[]{skyLightColor.x, skyLightColor.y, skyLightColor.z});
                    renderPass.setUniform("NightVisionFactor", new float[]{nightVisionFactor});
                    renderPass.setUniform("DarknessScale", new float[]{darknessScale});
                    renderPass.setUniform("DarkenWorldFactor", new float[]{this.renderer.getDarkenWorldAmount(f)});
                    renderPass.setUniform("BrightnessFactor", new float[]{Math.max(0.0F, gamma - darknessGamma)});
                    renderPass.setVertexBuffer(0, RenderSystem.getQuadVertexBuffer());
                    renderPass.setIndexBuffer(gpuBuffer, autoStorageIndexBuffer.type());
                    renderPass.drawIndexed(0, 6);
                }
                ^///?} else {
                // Branch: 1.21.2-1.21.4 (TextureTarget + CompiledShaderProgram)
                CompiledShaderProgram compiledShaderProgram = Objects.requireNonNull(
                    RenderSystem.setShader(CoreShaders.LIGHTMAP), "Lightmap shader not loaded");
                compiledShaderProgram.safeGetUniform("AmbientLightFactor").set(ambientLight);
                compiledShaderProgram.safeGetUniform("SkyFactor").set(skyFactor);
                compiledShaderProgram.safeGetUniform("BlockFactor").set(blockFactor);
                compiledShaderProgram.safeGetUniform("UseBrightLightmap").set(useBrightLightmap ? 1 : 0);
                compiledShaderProgram.safeGetUniform("SkyLightColor").set(skyLightColor);
                compiledShaderProgram.safeGetUniform("NightVisionFactor").set(nightVisionFactor);
                compiledShaderProgram.safeGetUniform("DarknessScale").set(darknessScale);
                compiledShaderProgram.safeGetUniform("DarkenWorldFactor").set(this.renderer.getDarkenWorldAmount(f));
                compiledShaderProgram.safeGetUniform("BrightnessFactor").set(Math.max(0.0F, gamma - darknessGamma));
                this.target.bindWrite(true);
                BufferBuilder bufferBuilder = RenderSystem.renderThreadTesselator().begin(
                    VertexFormat.Mode.QUADS, DefaultVertexFormat.BLIT_SCREEN);
                bufferBuilder.addVertex(0.0F, 0.0F, 0.0F);
                bufferBuilder.addVertex(1.0F, 0.0F, 0.0F);
                bufferBuilder.addVertex(1.0F, 1.0F, 0.0F);
                bufferBuilder.addVertex(0.0F, 1.0F, 0.0F);
                BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
                this.target.unbindWrite();
                //?}

                profilerFiller.pop();
            }
        }
    }
    *///?}

    @Override
    public float darkness_prevFlicker() {
        return blockLightRedFlicker;
    }

    @Override
    public boolean darkness_isDirty() {
        return updateLightTexture;
    }
}
