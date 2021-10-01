package refactoring.utils.html;

import org.jetbrains.annotations.NotNull;

public class HTMLPage {
    private final StringBuilder lines = new StringBuilder("<html><body>\n");

    public void addLine(@NotNull String line, Object... args) {
        add(line, args);
        lines.append(System.lineSeparator());
    }

    public void add(@NotNull String text, Object... args) {
        lines.append(String.format(text, args));
    }

    public @NotNull String getLines() {
        return lines.toString();
    }
}
