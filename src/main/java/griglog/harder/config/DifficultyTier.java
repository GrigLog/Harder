package griglog.harder.config;

import java.util.ArrayList;
import java.util.List;

public class DifficultyTier {
    public float damage = 1, health = 1, exp = 1;
    public String message = "§cDifficulty increased.";
    public List<String> dimensions = new ArrayList<>();
    public List<String> bosses = new ArrayList<>();
}
