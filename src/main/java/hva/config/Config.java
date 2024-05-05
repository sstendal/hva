package hva.config;

public record Config(
        boolean windows,
        String outputFile,
        String configFile,
        String dateFormat,
        String timeFormat,
        int delay
) {
}
