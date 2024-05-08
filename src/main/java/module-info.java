module com.tntco.bagofwordsmavenfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.tntco.bagofwordsmavenfx to javafx.fxml;
    exports com.tntco.bagofwordsmavenfx;
}
