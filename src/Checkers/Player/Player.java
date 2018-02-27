/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Checkers.Player;

import Checkers.Board.Piece;
import Checkers.Board.PieceType;
import Checkers.Board.Tile;

/**
 *
 * @author edwar
 */
public class Player {

    private PieceType playerType;
    private double playerScore;
    //Kung ilang piece na ang nasa enemy side
    private int numberOfWinningPieces;
    //Number of moves
    private int numberOfMoves;

    public Player(PieceType playerType, int playerScore, int numberOfWinningPieces, int numberOfMoves) {
        this.playerType = playerType;
        this.playerScore = playerScore;
        this.numberOfWinningPieces = numberOfWinningPieces;
        this.numberOfMoves = numberOfMoves;
    }

    public void addOneToMove() {
        ++numberOfMoves;
    }

    public boolean addOneToNumberOfWinningPieces(Piece piece, Tile tile) {
        //this == cinacall niya yung Player object na gumamit ng method
        if (tile.isEnemyTile(this)) {
            int newNumberOfWinningPieces = this.getNumberOfWinningPieces() + 1;
            this.setNumberOfWinningPieces(newNumberOfWinningPieces);
            return true;
        }
        
        return false;
    }

    public void subtractOneToNumberOfWinningPieces() {
        this.setNumberOfWinningPieces(this.getNumberOfWinningPieces() - 1);
    }
    
    public void updatePlayerScore(Double newScore){
        playerScore += newScore;
    }

    public double getPlayerScore() {
        return playerScore;
    }

    public int getNumberOfWinningPieces() {
        return numberOfWinningPieces;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public PieceType getPlayerType() {
        return playerType;
    }

    public void setNumberOfWinningPieces(int numberOfWinningPieces) {
        this.numberOfWinningPieces = numberOfWinningPieces;
    }

    public void setPlayerScore(double playerScore) {
        this.playerScore = playerScore;
    }

    
}
