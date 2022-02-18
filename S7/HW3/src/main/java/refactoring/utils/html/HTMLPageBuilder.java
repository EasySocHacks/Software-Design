package refactoring.utils.html;

import org.jetbrains.annotations.NotNull;

public class HTMLPageBuilder {
    private final @NotNull HTMLPage htmlPage;

    public HTMLPageBuilder() {
        htmlPage = new HTMLPage();
    }

    public HTMLPageBuilder(@NotNull HTMLPage htmlPage) {
        this.htmlPage = htmlPage;
    }

    public @NotNull HTMLPageBuilder addLine(@NotNull String line, Object... args) {
        htmlPage.addLine(line, args);
        return new HTMLPageBuilder(htmlPage);
    }

    public @NotNull HTMLPageBuilder addBlock(@NotNull String blockTag, @NotNull String text, Object... args) {
        htmlPage.addLine(String.format("<%s>%s</%s>", blockTag, text, blockTag), args);
        return new HTMLPageBuilder(htmlPage);
    }

    public @NotNull HTMLPageBuilder addLineWithBr(@NotNull String line, Object... args) {
        htmlPage.addLine(String.format("%s</br>", line), args);
        return new HTMLPageBuilder(htmlPage);
    }

    public @NotNull HTMLPageBuilder build() {
        htmlPage.add("</body></html>");
        return new HTMLPageBuilder(htmlPage);
    }

    public @NotNull HTMLPage getHtmlPage() {
        return htmlPage;
    }
}
