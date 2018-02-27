/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Checkers;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

/**
 *
 * @author edwar
 */
public class App extends Application {


    @Override
    public void start(Stage primaryStage) {
//        Scene scene = new Scene(createContent());

        try {
            //Main.class.getResource("/packagename/LoginGUI.fxml")
//            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Checkers/Design/main.fxml"));
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Checkers/Design/main.fxml"));
            Scene scene = new Scene(root, 492, 315);
            primaryStage.setTitle("CheckersApp");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
