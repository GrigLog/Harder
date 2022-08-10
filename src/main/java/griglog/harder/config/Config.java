package griglog.harder.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Config {
    public static List<DifficultyTier> tiers;
    private static final String path = "config/harder.json";
    public static void reload(){
        try {
            tiers = new ArrayList<>();
            if (!Files.exists(Paths.get(path))) {
                FileWriter w = new FileWriter(path);
                w.write(defaultConfig);
                w.close();
            }
            String file = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            JsonArray arr = new Gson().fromJson(file, JsonArray.class);
            tiers.add(new DifficultyTier());
            for (JsonElement el : arr) {
                DifficultyTier tier = new DifficultyTier();
                JsonObject json = el.getAsJsonObject();
                if (json.has("damage"))
                    tier.damage = json.get("damage").getAsFloat();
                if (json.has("health"))
                    tier.health = json.get("health").getAsFloat();
                if (json.has("exp"))
                    tier.exp = json.get("exp").getAsFloat();
                if (json.has("message"))
                    tier.message = json.get("message").getAsString();
                if (json.has("dimensions"))
                    json.get("dimensions").getAsJsonArray().forEach(e -> tier.dimensions.add(new ResourceLocation(e.getAsString())));
                if (json.has("bosses"))
                    json.get("bosses").getAsJsonArray().forEach(e -> tier.bosses.add(new ResourceLocation(e.getAsString())));
                tiers.add(tier);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private final static String defaultConfig = "[\n" +
        "  {\"damage\": 1.25, \"health\": 1.2, \"exp\": 1.5, \"dimensions\": [\"minecraft:the_nether\"], \"message\": \"§6Expert mode entered!\"},\n" +
        "  {\"damage\": 1.5, \"health\": 1.4, \"exp\": 2.0, \"dimensions\": [\"minecraft:the_end\"], \"bosses\": [\"minecraft:wither\"], \"message\": \"§cMaster mode entered!\"}\n" +
        "]";
}
