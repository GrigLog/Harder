package griglog.harder.config;

import com.google.gson.stream.JsonReader;
import griglog.harder.Harder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
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
            JSONArray arr = new JSONArray(file);
            for (int i = 0; i < arr.length(); i++) {
                DifficultyTier tier = new DifficultyTier();
                JSONObject json = arr.getJSONObject(i);
                tier.damage = json.has("damage") ? json.getFloat("damage") : 0;
                tier.health = json.has("health") ? json.getFloat("health") : 0;
                tier.message = json.getString("message");
                tier.dimensions = json.has("dimensions") ? (List<String>) (Object) json.getJSONArray("dimensions").toList() : new ArrayList<>();
                tier.bosses = json.has("bosses") ? (List<String>) (Object) json.getJSONArray("bosses").toList() : new ArrayList<>();
                tiers.add(tier);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private final static String defaultConfig = "[\n" +
            "  {\"damage\": 1.25, \"health\": 1.2, \"dimensions\": [\"minecraft:the_nether\"], \"bosses\": [], \"message\": \"§6Expert mode entered!\"},\n" +
            "  {\"damage\": 1.5, \"health\": 1.4, \"dimensions\": [\"minecraft:the_end\"], \"bosses\": [\"minecraft:wither\"], \"message\": \"§cMaster mode entered!\"}\n" +
            "]";
}
