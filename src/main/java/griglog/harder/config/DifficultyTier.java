package griglog.harder.config;

import griglog.harder.capability.PlayerDifficulty;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class DifficultyTier {
    public float damage = 1, health = 1, exp = 1;
    public String message = "§cDifficulty increased.";
    public Set<ResourceLocation> dimensions = new HashSet<>();
    public Set<ResourceLocation> mobs = new HashSet<>();
    public Set<ResourceLocation> items = new HashSet<>();

    public static void tryIncrease(Player player, ResourceLocation id, Function<DifficultyTier, Set<ResourceLocation>> setGetter) {
        PlayerDifficulty cap = PlayerDifficulty.get(player);
        for (int t = 1; t < Config.tiers.size(); t++) {
            DifficultyTier tier = Config.tiers.get(t);
            if (setGetter.apply(tier).contains(id) && cap.value < t) {
                for (int i = cap.value + 1; i <= t; i++)
                    player.sendMessage(new TextComponent(Config.tiers.get(i).message), Util.NIL_UUID);
                cap.value = t;
                break;
            }
        }
    }
}
