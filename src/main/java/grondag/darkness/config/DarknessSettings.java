package grondag.darkness.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class DarknessSettings {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("darkness.json");

    public boolean darkOverworld = true;
    public boolean darkNether = false;
    public boolean darkEnd = false;
    public boolean requireMod = false;
    public int darknessIntensity = 75;

    public static DarknessSettings load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                DarknessSettings loaded = GSON.fromJson(reader, DarknessSettings.class);
                if (loaded != null) {
                    loaded.save();
                    return loaded;
                }
            } catch (IOException | com.google.gson.JsonSyntaxException e) {
                // Use defaults on error
            }
        }
        DarknessSettings settings = new DarknessSettings();
        settings.save();
        return settings;
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            // Silently fail
        }
    }
}
