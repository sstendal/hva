package hva;

import java.util.function.BiConsumer;

/**
 * User: Sigurd Stendal
 * Date: 28.02.2017
 */
public class ChangeDelayCommand {

    public static boolean executeCommand(String command, BiConsumer<Integer, String> setDelay) {
        try {
            if (command == null)
                return false;
            int time = parse(command);
            if (time > 0) {
                setDelay.accept(time, command);
                return true;
            }
        } catch (Exception e) {
            Log.error("Failed to parse this string as a command: " + command, e);
        }
        return false;
    }

    private static int parse(String text) {
        try {
            int tall;
            if (text.endsWith("sekunder")) {
                tall = Integer.parseInt(text.substring(0, text.length() - " sekunder".length()));
                return tall;
            }
            if (text.endsWith("minutter")) {
                tall = Integer.parseInt(text.substring(0, text.length() - " minutter".length()));
                return tall * 60;
            }
            if (text.endsWith("timer")) {
                tall = Integer.parseInt(text.substring(0, text.length() - " timer".length()));
                return tall * 60 * 60;
            }
            if (text.equals("1 sekund"))
                return 1;
            if (text.equals("1 minutt"))
                return 60;
            if (text.equals("1 time"))
                return 60 * 60;
        } catch (NumberFormatException e) {
            Log.error("Failed to parse this string as an interval: " + text, e);
        }
        return -1;
    }

}
