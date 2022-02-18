module easy.soc.hacks.hw5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires java.desktop;


    opens easy.soc.hacks.hw5.visualizer to javafx.fxml;
    exports easy.soc.hacks.hw5.visualizer;
}