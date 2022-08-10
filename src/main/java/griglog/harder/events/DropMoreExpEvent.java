package griglog.harder.events;

import griglog.harder.capability.PlayerDifficulty;
import griglog.harder.config.Config;
import griglog.harder.config.DifficultyTier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DropMoreExpEvent {
    @SubscribeEvent
    static void onDeath(LivingExperienceDropEvent event){
        Player player = event.getAttackingPlayer();
        if (player == null)
            return;
        PlayerDifficulty cap = PlayerDifficulty.get(player);
        if (cap.value == 0)
            return;
        DifficultyTier tier = Config.tiers.get(cap.value);
        event.setDroppedExperience((int) (event.getDroppedExperience() * tier.exp));
    }
}
