package FXX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static String home = "StartFXML.fxml";
    static Stage stage;
    static Parent root;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        root = FXMLLoader.load(Main.class.getResource("FXML/" + home));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Main.class.getResource("CSS/MyStyle.css").toExternalForm());
        primaryStage.setMinHeight(620);
        primaryStage.setMinWidth(1000);
        primaryStage.setTitle("Projet 2017-2018");
        primaryStage.setScene(scene);

        try {
            stage = primaryStage;
            stage.show();
        } catch (Exception ex) {
        }
    }

    public static void refresh() throws Exception {
        root = FXMLLoader.load(Main.class.getResource("FXML/" + home));
        stage.getScene().setRoot(root);
    }

}
