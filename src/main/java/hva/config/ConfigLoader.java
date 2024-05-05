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

        String configFilePath = appDataDir + "/hva.cfg";
        File file = new File(configFilePath);
        if (!file.exists()) {

            Map<String, Object> data = Map.of(
                    "outputfile", appDataDir + "/hva.txt",
                    "configfile", configFilePath,
                    "dateformat", "dd.MM.yyyy",
                    "timeformat", "HH:mm",
                    "delay", 1800
            );

            try (FileWriter writer = new FileWriter(file.getAbsolutePath())) {
                // Configure SnakeYAML with options for pretty-printing
                DumperOptions options = new DumperOptions();
                options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                options.setIndent(2);
                options.setPrettyFlow(true);

                Yaml yaml = new Yaml(options);
                yaml.dump(data, writer);
                Log.info("Config file written successfully to " + file.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException("Error writing config file to " + file.getAbsolutePath() + ": " + e.getMessage());
            }
        }

        try (FileReader reader = new FileReader(file.getAbsolutePath())) {
            Yaml yaml = new Yaml();
            Map<String, Object> loadedData = yaml.load(reader);
            Log.info("Loaded config file from " + file.getAbsolutePath());

            return new Config(isWindows,
                    (String) loadedData.get("outputfile"),
                    (String) loadedData.get("configfile"),
                    (String) loadedData.get("dateformat"),
                    (String) loadedData.get("timeformat"),
                    (int) loadedData.get("delay"));
        } catch (IOException e) {
            throw new RuntimeException("Error reading config file from " + file.getAbsolutePath() + ": " + e.getMessage());
        }

    }

    public static void openConfigFile() {
        load();
        File file = new File(load().configFile());
        try {
            // Specify the path to Notepad executable
            String notepadPath = "C:\\Windows\\System32\\notepad.exe";

            // Start Notepad with the file path as an argument
            ProcessBuilder pb = new ProcessBuilder(notepadPath, file.getAbsolutePath());
            pb.start();
        } catch (Exception e) {
            Log.error("Error while opening the config file. Filename: " + file.getAbsolutePath() + " (" + e.getMessage() + ")");
        }
    }



}
