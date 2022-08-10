package griglog.harder.events;

import griglog.harder.capability.PlayerDifficulty;
import griglog.harder.config.Config;
import griglog.harder.config.DifficultyTier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber
public class EntitySpawnEvent {
    private static final UUID HP_ID = UUID.fromString("4f424060-5b65-4d2d-baf8-df5c705c15ac");
    private static final UUID DMG_ID = UUID.fromString("6ff9ee6b-eb59-449c-893f-ee08d15e6740");

    @SubscribeEvent
    static void onSpawn(EntityJoinWorldEvent event){
        if (!(event.getEntity() instanceof LivingEntity))
            return;
        LivingEntity living = (LivingEntity) event.getEntity();
        if (living.getType().getCategory().isFriendly())  //could as well check (category != MONSTER)
            return;
        PlayerEntity player = event.getWorld().getNearestPlayer(living, Integer.MAX_VALUE);
        if (player == null){
            return;
        }
        PlayerDifficulty cap = PlayerDifficulty.get(player);
        if (cap.value == 0)
            return;
        DifficultyTier tier = Config.tiers.get(cap.value);
        ModifiableAttributeInstance attr;
        AttributeModifier mod;
        if ((attr = living.getAttribute(Attributes.MAX_HEALTH)) != null) {
            mod = new AttributeModifier(HP_ID, "HarderHp", tier.health - 1, AttributeModifier.Operation.MULTIPLY_TOTAL);
            if (!attr.hasModifier(mod)) {
                attr.addPermanentModifier(mod);
                living.setHealth(living.getMaxHealth());
            }
        }
        if ((attr = living.getAttribute(Attributes.ATTACK_DAMAGE)) != null) {
            mod = new AttributeModifier(DMG_ID, "HarderDmg", tier.damage - 1, AttributeModifier.Operation.MULTIPLY_TOTAL);
            if (!attr.hasModifier(mod)){
                attr.addPermanentModifier(mod);
            }
        }

    }
}
