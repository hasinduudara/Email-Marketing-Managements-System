package lk.ijse.groupproject.emms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("/view/SignInPage.fxml"));

        stage.setTitle("Stay Smart");

        Scene scene = new Scene(new Group(load));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/test-img.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}