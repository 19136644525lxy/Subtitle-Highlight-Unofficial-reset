package yeah_zero.subtitle_highlight.Configure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import yeah_zero.subtitle_highlight.SubtitleHighlight;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Manager {
    public static Settings settings = new Settings();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FMLPaths.CONFIGDIR.get().toFile(), "subtitle_highlight.json");

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                settings = GSON.fromJson(reader, Settings.class);
                SubtitleHighlight.LOGGER.info("Loaded config from " + CONFIG_FILE.getAbsolutePath());
            } catch (IOException e) {
                SubtitleHighlight.LOGGER.error("Failed to load config", e);
                settings = new Settings();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(settings, writer);
            SubtitleHighlight.LOGGER.info("Saved config to " + CONFIG_FILE.getAbsolutePath());
        } catch (IOException e) {
            SubtitleHighlight.LOGGER.error("Failed to save config", e);
        }
    }

    public static void export(String path) {
        File exportFile = new File(FMLPaths.GAMEDIR.get().toFile(), path);
        try (FileWriter writer = new FileWriter(exportFile)) {
            GSON.toJson(settings, writer);
            SubtitleHighlight.LOGGER.info("Exported config to " + exportFile.getAbsolutePath());
        } catch (IOException e) {
            SubtitleHighlight.LOGGER.error("Failed to export config", e);
        }
    }

    public static void exportConfig(File exportFile) throws IOException {
        try (FileWriter writer = new FileWriter(exportFile)) {
            GSON.toJson(settings, writer);
            SubtitleHighlight.LOGGER.info("Exported config to " + exportFile.getAbsolutePath());
        }
    }

    public static void importConfig(File importFile) throws IOException {
        try (FileReader reader = new FileReader(importFile)) {
            settings = GSON.fromJson(reader, Settings.class);
            save();
            SubtitleHighlight.LOGGER.info("Imported config from " + importFile.getAbsolutePath());
        }
    }

    public static void importConfig(String path) {
        File importFile = new File(FMLPaths.GAMEDIR.get().toFile(), path);
        if (importFile.exists()) {
            try (FileReader reader = new FileReader(importFile)) {
                settings = GSON.fromJson(reader, Settings.class);
                save();
                SubtitleHighlight.LOGGER.info("Imported config from " + importFile.getAbsolutePath());
            } catch (IOException e) {
                SubtitleHighlight.LOGGER.error("Failed to import config", e);
            }
        }
    }
}
