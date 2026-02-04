package grondag.darkness;

import grondag.darkness.config.DarknessConfig;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
//? if >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.resources.ResourceLocation;
//?}
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DarknessInit implements ModInitializer {
    public static final String MOD_ID = "darkness";
    public static final String MOD_NAME = "TrueDarknessRefabricated";
    public static final DarknessConfig CONFIG = DarknessConfig.createAndLoad();

    @Override
    public void onInitialize() {

        if (CONFIG.requireMod()) {
            int protocolVersion = 0;

            ServerLoginConnectionEvents.QUERY_START.register((handler, server, sender, synchronizer) -> {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer(5));
                buf.writeVarInt(protocolVersion);
                //? if >=1.21.11 {
                /*sender.sendPacket(Identifier.parse(MOD_ID), buf);
                *///?} else if >=1.21 {
                sender.sendPacket(ResourceLocation.parse(MOD_ID), buf);
                //?} else if <1.21 {
                /*sender.sendPacket(ResourceLocation.tryParse(MOD_ID), buf);
                *///?}
            });
            //? if >=1.21.11 {
            /*ServerLoginNetworking.registerGlobalReceiver(Identifier.parse(MOD_ID),
            *///?} else if >=1.21 {
             ServerLoginNetworking.registerGlobalReceiver(ResourceLocation.parse(MOD_ID),
             //?} else if <1.21 {
            /*ServerLoginNetworking.registerGlobalReceiver(ResourceLocation.tryParse(MOD_ID),
            *///?}
                    (server, handler, understood, buf, synchronizer, sender) -> {
                        if (!understood) {
                            handler.disconnect(Component.literal("You are missing the mod: " + MOD_NAME));
                        }
                    });
        }
    }
}
