package kanotLiveUploader;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import kanotLiveUploader.controller.MainController;
import org.fuin.utils4j.Utils4J;

import java.io.File;

public class MainApp extends Application {

    MainController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Utils4J.addToClasspath("file:///" + System.getProperty("java.home") + File.separator + "lib" + File.separator + "jfxrt.jar");

        String fxmlFile = "/fxml/GUI.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        controller = (MainController) loader.getController();
        controller.setStageAndSetupListeners(primaryStage);
        primaryStage.setTitle("Live stream");
        primaryStage.setScene(new Scene(root, 650, 400));
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.out.println("Stage is closing");
        Platform.exit();
        controller.endProgram();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
