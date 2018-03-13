/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Checkers.Board;

import Checkers.Controller.BoardController;
import Checkers.Player.Player;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author edwar
 */
public class Tile extends Rectangle {

    private Piece piece;
    //Kung anung piece ba ang nakalagay dito from the start
    //1 = white; 2 = black; 3 = null
    private PieceType originalPieceType;
    private double mouseX, mouseY;

    public PieceType getOriginalPieceType() {
        return originalPieceType;
    }

    public void setOriginalPieceType(PieceType originalPieceType) {
        this.originalPieceType = originalPieceType;
    }

    
    public boolean hasPiece() {
        if(piece != null){
            return true;
        }
        else{
            return false;
        }
//        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    //x and y are board coordinates
    public Tile(boolean light, int x, int y) {
        setWidth(BoardController.tileSize);
        setHeight(BoardController.tileSize);

        relocate(x * BoardController.tileSize, y * BoardController.tileSize);
        setFill(light ? Color.valueOf("#005b3b") : Color.valueOf("#ffffff"));

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
            System.out.println(e.getSceneX() + " " + e.getSceneY());
            System.out.println("In Board: " + toBoard(e.getSceneX()) + " " + toBoard(e.getSceneY()));
            System.out.println("Original Piece: " + getOriginalPieceType());
        });
    }

    //Delete Later
    private int toBoard(double pixel) {
        return (int) (pixel + BoardController.tileSize / 2) / BoardController.tileSize;
    }
    
    public boolean isEnemyTile(Player player) {
        
        //this == cinacall niya yung Tile Object na tumawag sa method
        if (player.getPlayerType() == PieceType.WHITE && this.getOriginalPieceType() == PieceType.BLACK) {
            return true;
        } else if (player.getPlayerType() == PieceType.BLACK && this.getOriginalPieceType() == PieceType.WHITE) {
            return true;
        }
        return false;
    }

}
