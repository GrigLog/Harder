package griglog.harder.events;

import griglog.harder.capability.PlayerDifficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CapabilityEvents {
    @SubscribeEvent
    static void playerCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(PlayerDifficulty.ID, new PlayerDifficulty.Provider());
        }
    }
    @SubscribeEvent
    static void copyPlayerDataOnRespawn(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps(); //without this Forge does not allow accessing capabilities
        PlayerDifficulty cap = PlayerDifficulty.get(event.getOriginal());
        PlayerDifficulty capNew = PlayerDifficulty.get(event.getPlayer());
        if (cap != null && capNew != null)
            capNew.setNbt(cap.getNbt());
    }
}