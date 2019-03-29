/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Checkers.Controller;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author edwar
 */
public class AlertBox {

    public static void display(String title, String message, double whiteScore, double blacScore, ActionEvent ae) {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(200);

        Label label = new Label();
        label.setText(message);
        Label whiteScoreLabel = new Label();
        Label blackScoreLabel = new Label();
        whiteScoreLabel.setText("White's Score: " + whiteScore);
        blackScoreLabel.setText("Black's Score: " + blacScore);
        Button closeButton = new Button("New Game");

        Node source = (Node) ae.getSource();
        Stage theStage = (Stage)source.getScene().getWindow();

        closeButton.setOnAction(e -> {
            window.close();
            theStage.close();
            try {
                BoardController bc = new BoardController();
                bc.newGame(e);
            } catch (Exception exc) {
                exc.printStackTrace();
            }

        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, whiteScoreLabel, blackScoreLabel, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
    
        public static void display(String title, String message, double whiteScore, double blacScore, MouseEvent ae) {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(200);

        Label label = new Label();
        label.setText(message);
        Label whiteScoreLabel = new Label();
        Label blackScoreLabel = new Label();
        whiteScoreLabel.setText("White's Score: " + whiteScore);
        blackScoreLabel.setText("Black's Score: " + blacScore);
        Button closeButton = new Button("New Game");

        Node source = (Node) ae.getSource();
        Stage theStage = (Stage)source.getScene().getWindow();

        closeButton.setOnAction(e -> {
            window.close();
            theStage.close();
            try {
                BoardController bc = new BoardController();
                bc.newGame(e);
            } catch (Exception exc) {
                exc.printStackTrace();
            }

        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, whiteScoreLabel, blackScoreLabel, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
