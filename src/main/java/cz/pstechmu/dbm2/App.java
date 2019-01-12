package cz.pstechmu.dbm2;

import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) throws Exception {
        Locale.setDefault(new Locale("cs", "CZ"));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Semestralni prace pro predmet DBM2");
        primaryStage.setWidth(640);
        primaryStage.setHeight(480);
        primaryStage.show();
    }
}
