/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Checkers.Board;

import Checkers.Controller.BoardController;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

/**
 *
 * @author edwar
 */
public class Piece extends StackPane {

    private PieceType type;
    private double mouseX, mouseY;
    private double oldX, oldY;

    public PieceType getType() {
        return type;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public Piece(PieceType type, int x, int y) {
        this.type = type;
        movePiece(x, y);
//        Ellipse ellipse = new Ellipse(BoardController.tileSize * 0.3125, BoardController.tileSize * 0.26);
        Circle ellipse = new Circle();
        ellipse.setRadius(29);
        ellipse.setFill(type == PieceType.BLACK ? Color.valueOf("#000000") : Color.valueOf("#ffbc89"));
        ellipse.setTranslateX((BoardController.tileSize - BoardController.tileSize * 0.3125 * 2) / 2);
        ellipse.setTranslateY((BoardController.tileSize - BoardController.tileSize * 0.26 * 2) / 2);
        ellipse.setStroke(type == PieceType.BLACK ? Color.valueOf("#fffdfd") : Color.valueOf("#000000"));
        ellipse.setStrokeLineJoin(StrokeLineJoin.MITER);
        ellipse.setStrokeLineCap(StrokeLineCap.SQUARE);
        ellipse.setStrokeType(StrokeType.CENTERED);
        ellipse.setStrokeWidth(5);
        ellipse.setStrokeMiterLimit(10);

        getChildren().addAll(ellipse);

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
            System.out.println(e.getSceneX() + " " + e.getSceneY());
        });

        setOnMouseDragged(e -> {
            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
        });

    }

    public void movePiece(int x, int y) {

        oldX = x * BoardController.tileSize;
        oldY = y * BoardController.tileSize;
        relocate(oldX, oldY);

    }

    public void abortMove() {
        relocate(oldX, oldY);
    }

}
