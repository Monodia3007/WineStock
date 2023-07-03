module eu.lilithmonodia.winestock {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;

    opens eu.lilithmonodia.winestock to javafx.fxml;
    opens eu.lilithmonodia.winestock.app to java.base;
    exports eu.lilithmonodia.winestock;
}