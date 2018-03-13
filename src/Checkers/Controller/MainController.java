package Checkers.Controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.stage.Stage;

public class MainController implements Initializable {

    @FXML
    private Menu Help;
    private Button humanVhuman;

    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void humanVShumanBoard(ActionEvent event) {
        try {
            System.out.println("Human vs. HUman");
            Parent humanVShumanBoard = FXMLLoader.load(getClass().getClassLoader().getResource("Checkers/Design/Board.fxml"));
            Scene scene = new Scene(humanVShumanBoard, 725, 522);
            Stage primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();
            primaryStage.setTitle("CheckersApp");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void humanVcomputerBoard(ActionEvent event) {
        try {
            System.out.println("Human vs. Computer");
            Parent humanVcomputerBoard = FXMLLoader.load(getClass().getClassLoader().getResource("Checkers/Design/choose.fxml"));
            Scene scene = new Scene(humanVcomputerBoard, 492, 315);
            Stage primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();
            primaryStage.setTitle("CheckersApp");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
