package refactoring.utils.html;

public class HTMLPageBuilder {
    private final HTMLPage htmlPage;

    public HTMLPageBuilder() {
        htmlPage = new HTMLPage();
    }

    public HTMLPageBuilder(HTMLPage htmlPage) {
        this.htmlPage = htmlPage;
    }

    public HTMLPageBuilder addLine(String line, Object... args) {
        htmlPage.addLine(line, args);
        return new HTMLPageBuilder(htmlPage);
    }

    public HTMLPageBuilder build() {
        htmlPage.add("</body></html>");
        return new HTMLPageBuilder(htmlPage);
    }

    public HTMLPage getHtmlPage() {
        return htmlPage;
    }
}
