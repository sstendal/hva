package hva.config;

import hva.Log;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * User: Sigurd Stendal
 * Date: 28.02.2017
 */
public class ConfigLoader {

    public static Config load() {
        Config defaultConfig = getDefaultConfig();

        File file = new File(defaultConfig.configFile());
        if (!file.exists()) {
            Log.info("Config file not found at " + file.getAbsolutePath() + ". Writing default config.");
            writeConfig(file, defaultConfig);
            return defaultConfig;
        }

        try {
            Log.info("Reading config file from " + file.getAbsolutePath());
            return readConfig(file, defaultConfig);
        } catch (Exception e) {
            // If we can't read the config file, we should probably just try to overwrite it
            Log.info("Failed to read config file from " + file.getAbsolutePath() + ". Overwriting it with default config.");
            writeConfig(file, defaultConfig);
            return defaultConfig;
        }

    }

    private static Config getDefaultConfig() {
        String osName = System.getProperty("os.name").toLowerCase();
        boolean isWindows = osName.contains("windows");

        String appDataDir;

        if (isWindows) {
            appDataDir = System.getenv("APPDATA") + "/Hva";
        } else {
            appDataDir = System.getProperty("user.home");
        }

        if (!new File(appDataDir).exists()) {
            boolean created = new File(appDataDir).mkdir();
            if (!created) {
                throw new RuntimeException("Failed while creating config directory " + appDataDir);
            }
        }

        String configFilePath = appDataDir + (isWindows ? "/hva.cfg" : "/.hva.cfg");

        return new Config(isWindows,
                appDataDir + "/hva.txt",
                configFilePath,
                "dd.MM.yyyy",
                "HH:mm",
                1800);
    }

    private static Config readConfig(File file, Config config) {
        try (FileReader reader = new FileReader(file.getAbsolutePath())) {
            Yaml yaml = new Yaml();
            Map<String, Object> loadedData = yaml.load(reader);
            return new Config(config.windows(),
                    readEntry(loadedData, "outputfile", config.outputFile()),
                    config.configFile(),
                    readEntry(loadedData, "dateformat", config.dateFormat()),
                    readEntry(loadedData, "timeformat", config.timeFormat()),
                    readEntry(loadedData, "delay", config.delay()));
        } catch (IOException e) {
            Log.error("Error reading config file from " + file.getAbsolutePath() + ": " + e.getMessage());
            throw new RuntimeException("Error reading config file from " + file.getAbsolutePath() + ": " + e.getMessage());
        }
    }

    private static <T> T readEntry(Map<String, Object> loadedData, String name, T defaultValue) {
        T value = (T) loadedData.get(name);
        return value != null ? value : defaultValue;
    }

    private static void writeConfig(File file, Config config) {
        try (FileWriter writer = new FileWriter(file.getAbsolutePath())) {
            // Configure SnakeYAML with options for pretty-printing
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setIndent(2);
            options.setPrettyFlow(true);

            Yaml yaml = new Yaml(options);
            yaml.dump(config.toMap(), writer);
        } catch (IOException e) {
            throw new RuntimeException("Error writing config file to " + file.getAbsolutePath() + ": " + e.getMessage());
        }
    }

    public static void openConfigFile() {
        Config config = load();
        File file = new File(config.configFile());
        if (config.windows()) {
            try {
                // Specify the path to Notepad executable
                String notepadPath = "C:\\Windows\\System32\\notepad.exe";

                // Start Notepad with the file path as an argument
                ProcessBuilder pb = new ProcessBuilder(notepadPath, file.getAbsolutePath());
                pb.start();
            } catch (Exception e) {
                Log.error("Error while opening the config file. Filename: " + file.getAbsolutePath() + " (" + e.getMessage() + ")");
            }
        } else {
            Log.error("Opening config file is not supported on this platform. The config file is located at " + file.getAbsolutePath() + ".");
        }
    }


    public static void setDelay(int delay) {
        Config config = load();
        Config newConfig = new Config(config.windows(), config.outputFile(), config.configFile(), config.dateFormat(), config.timeFormat(), delay);
        writeConfig(new File(config.configFile()), newConfig);
    }
}
