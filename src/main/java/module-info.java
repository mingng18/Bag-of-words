module com.tntco.bagofwordsmavenfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.httpserver;

    opens com.tntco.bagofwordsmavenfx to javafx.fxml;
    exports com.tntco.bagofwordsmavenfx;
}
