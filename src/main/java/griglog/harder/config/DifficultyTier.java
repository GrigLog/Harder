package griglog.harder.config;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DifficultyTier {
    public float damage = 1, health = 1, exp = 1;
    public String message = "§cDifficulty increased.";
    public Set<ResourceLocation> dimensions = new HashSet<>();
    public Set<ResourceLocation> bosses = new HashSet<>();
}
