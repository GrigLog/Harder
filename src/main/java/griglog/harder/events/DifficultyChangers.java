package griglog.harder.events;

import griglog.harder.capability.PlayerDifficulty;
import griglog.harder.config.Config;
import griglog.harder.config.DifficultyTier;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
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
        PlayerDifficulty cap = PlayerDifficulty.get(event.getPlayer());
        for (int t = 1; t < Config.tiers.size(); t++){
            DifficultyTier tier = Config.tiers.get(t);
            if (tier.dimensions.contains(dim) && cap.value < t){
                for (int i = cap.value + 1; i <= t; i++)
                    event.getPlayer().sendMessage(new TextComponent(Config.tiers.get(i).message), Util.NIL_UUID);
                cap.value = t;
                return;
            }
        }
    }

    @SubscribeEvent
    static void onKillBoss(LivingDeathEvent e){
        if (!(e.getSource().getEntity() instanceof ServerPlayer) || e.getEntityLiving().getType().getRegistryName() == null)
            return;
        ServerPlayer player = (ServerPlayer) e.getSource().getEntity();
        PlayerDifficulty cap = PlayerDifficulty.get(player);
        ResourceLocation id = e.getEntityLiving().getType().getRegistryName();
        for (int t = 1; t < Config.tiers.size(); t++){
            DifficultyTier tier = Config.tiers.get(t);
            if (tier.bosses.contains(id) && cap.value < t){
                for (int i = cap.value + 1; i <= t; i++)
                    player.sendMessage(new TextComponent(Config.tiers.get(i).message), Util.NIL_UUID);
                cap.value = t;
            }
        }
    }
}
