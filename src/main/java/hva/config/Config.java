package hva.config;

import java.util.Map;

public record Config(
        boolean windows,
        String outputFile,
        String configFile,
        String dateFormat,
        String timeFormat,
        int delay
) {

    Map<String, Object> toMap() {
        return Map.of(
                "outputfile", outputFile,
                "dateformat", dateFormat,
                "timeformat", timeFormat,
                "delay", delay
        );
    }

}
