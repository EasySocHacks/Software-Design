package refactoring.utils.html;

public class HTMLPage {
    private final StringBuilder lines = new StringBuilder("<html><body>\n");

    public void addLine(String line, Object... args) {
        add(line, args);
        lines.append(System.lineSeparator());
    }

    public void add(String text, Object... args) {
        lines.append(String.format(text, args));
    }

    public String getLines() {
        return lines.toString();
    }
}
