package grondag.darkness.compat;


import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

// Imports for Apoli NightVisionPower compat
//? if =1.21.1 {
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.type.NightVisionPowerType;
//?} else if <=1.20.4 {
/*import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.NightVisionPower;
*///?}


public class ApoliCompat {

    public boolean isApoliNightVisionPower(Minecraft client) {
        if(FabricLoader.getInstance().isModLoaded("apoli")) {
            //? if =1.21.1 {
            return PowerHolderComponent.hasPowerType(client.player, NightVisionPowerType.class);
            //?} else if <=1.20.4 {
            /*return PowerHolderComponent.hasPower(client.player, NightVisionPower.class);
            *///?} else {
            /*return false;
            *///?}
        }
        else{
            return false;
        }
    }
}