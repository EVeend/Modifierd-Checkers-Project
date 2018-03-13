/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Checkers.Controller;

import Checkers.Board.PieceType;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author edwar
 */
public class ChooseController {

    public void playWhite(ActionEvent event) {
        try {
            System.out.println("White vs. Computer");
            Parent humanWhitevsCompBoard = FXMLLoader.load(getClass().getClassLoader().getResource("Checkers/Design/BoardAIWhite.fxml"));
            Scene scene = new Scene(humanWhitevsCompBoard, 725, 522);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setTitle("CheckersApp");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playBlack(ActionEvent event) {
        try {
            System.out.println("Black vs. Computer");
            Parent humanBlackvsCompBoard = FXMLLoader.load(getClass().getClassLoader().getResource("Checkers/Design/BoardAIBlack.fxml"));
            Scene scene = new Scene(humanBlackvsCompBoard, 725, 522);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setTitle("CheckersApp");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void back(ActionEvent event) {
        System.out.println("Back");
        try {

            Parent mainMenu = FXMLLoader.load(getClass().getClassLoader().getResource("Checkers/Design/main.fxml"));
            Scene scene = new Scene(mainMenu, 492, 315);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setTitle("CheckersApp");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
