package hva;

import hva.config.Config;
import hva.config.ConfigLoader;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OutputFile {

    private static final Config config = ConfigLoader.load();
    private static final String newline = System.lineSeparator();

    private static File file;

    public static void create() {
        String pathname = config.outputFile();
        if (pathname == null || pathname.trim().isEmpty()) {
            pathname = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/hva.txt";
        }
        file = new File(pathname);
        if (!file.exists()) {
            try {
                boolean success = file.createNewFile();
                if (!success) {
                    throw new RuntimeException("Unable to create file " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create file " + file.getAbsolutePath(), e);
            }
        }
    }

    public static void recordEntry(String entry, LocalDateTime last, LocalDateTime time) {
        String buffer = "";

        if (time.getDayOfYear() != last.getDayOfYear()) {
            buffer += "\n";
        }

        String fromTime;
        if (last.getDayOfYear() != LocalDateTime.now().getDayOfYear()) {
            fromTime = "i morges";
        } else {
            fromTime = last.format(DateTimeFormatter.ofPattern(config.timeFormat()));
        }
        String toTime = time.format((DateTimeFormatter.ofPattern(config.timeFormat())));

        buffer = buffer + time.format((DateTimeFormatter.ofPattern(config.dateFormat())));
        buffer = buffer + " " + fromTime;
        buffer = buffer + " - " + toTime;
        buffer = buffer + ": " + entry;
        log(buffer);
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write(buffer + newline);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Feil ved skriving til fil\n" + e);
        }
    }

    public static void openDataFile() {
        try {
            if (Desktop.isDesktopSupported() && file.exists()) {
                Desktop.getDesktop().edit(file);
            }
        } catch (Exception e) {
            log("Error while opening the activity record file. Filename: " + file.getAbsolutePath() + " (" + e.getMessage() + ")");
        }
    }

    private static void log(String s) {
        System.err.println(s);
    }


}
