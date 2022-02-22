package griglog.harder.events;

import griglog.harder.capability.PlayerDifficulty;
import griglog.harder.config.Config;
import griglog.harder.config.DifficultyTier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber
public class DifficultyChangers {
    @SubscribeEvent
    static void onChangedDim(PlayerEvent.PlayerChangedDimensionEvent event){
        String dim = event.getTo().location().toString();
        PlayerDifficulty cap = PlayerDifficulty.get(event.getPlayer());
        for (int t = 0; t < Config.tiers.size(); t++){
            DifficultyTier tier = Config.tiers.get(t);
            if (tier.dimensions.contains(dim) && cap.value < t + 1){
                for (int i = cap.value; i <= t; i++)
                    event.getPlayer().sendMessage(new StringTextComponent(Config.tiers.get(i).message), Util.NIL_UUID);
                cap.value = t + 1;
                return;
            }
        }
    }

    @SubscribeEvent
    static void onKillBoss(LivingDeathEvent e){
        if (!(e.getSource().getEntity() instanceof ServerPlayerEntity) || e.getEntityLiving().getType().getRegistryName() == null)
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) e.getSource().getEntity();
        PlayerDifficulty cap = PlayerDifficulty.get(player);
        String id = e.getEntityLiving().getType().getRegistryName().toString();
        for (int t = 0; t < Config.tiers.size(); t++){
            DifficultyTier tier = Config.tiers.get(t);
            if (tier.bosses.contains(id) && cap.value < t + 1){
                for (int i = cap.value; i <= t; i++)
                    player.sendMessage(new StringTextComponent(Config.tiers.get(i).message), Util.NIL_UUID);
                cap.value = t + 1;
            }
        }
    }
}
