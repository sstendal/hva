package hva;

import java.util.ArrayList;
import java.util.Optional;

/**
 * User: Sigurd Stendal
 * Date: 01.03.2017
 */
public class History {

    private ArrayList<String> entries = new ArrayList<>(5);
    private int historyPosition = -1;

    public void add(String entry) {
        entries.add(0, entry);
        for (int t = 1; t < entries.size(); t++)
            if (entry.equals(entries.get(t))) {
                entries.remove(t);
                t--;
            }
        historyPosition = -1;
    }

    public void setPositionToLast() {
        historyPosition = -1;
    }

    public void next() {
        if (historyPosition < entries.size() - 1) {
            historyPosition++;
        }
    }

    public void previous() {
        if (historyPosition > - 1) {
            historyPosition--;
        }
    }

    public Optional<String> get() {
        if(historyPosition > -1) {
            return Optional.of(entries.get(historyPosition));
        } else {
            return Optional.empty();
        }
    }
}
