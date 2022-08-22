package griglog.harder.events;

import griglog.harder.config.DifficultyTier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
public class DifficultyChangers {
    @SubscribeEvent
    static void onChangedDim(PlayerEvent.PlayerChangedDimensionEvent event){
        ResourceLocation dim = event.getTo().location();
        DifficultyTier.tryIncrease(event.getPlayer(), dim, (tier) -> tier.dimensions);
    }

    @SubscribeEvent
    static void onKillBoss(LivingDeathEvent e){
        if (!(e.getSource().getEntity() instanceof ServerPlayer) || e.getEntityLiving().getType().getRegistryName() == null)
            return;
        ServerPlayer player = (ServerPlayer) e.getSource().getEntity();
        ResourceLocation id = e.getEntityLiving().getType().getRegistryName();
        DifficultyTier.tryIncrease(player, id, (tier) -> tier.mobs);
    }

    @SubscribeEvent()
    static void onItemPickup(PlayerEvent.ItemPickupEvent event){
        if (event.getPlayer().level.isClientSide)
            return;
        ResourceLocation id = event.getStack().getItem().getRegistryName();
        DifficultyTier.tryIncrease(event.getPlayer(), id, (tier) -> tier.items);
    }
}
