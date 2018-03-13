/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Checkers.Player;


/**
 *
 * @author edwar
 */
public class PlayerMove {
    
    private Player playerType;
    private PossibleMove move;
    private int fitnessValue;
    
    public PlayerMove(Player playerType, PossibleMove move) {
        this.playerType = playerType;
        this.move = move;
    }

    public PlayerMove(Player playerType, PossibleMove move, int fitnessValue) {
        this.playerType = playerType;
        this.move = move;
        this.fitnessValue = fitnessValue;
    }
    
    
    public Player getPlayerType() {
        return playerType;
    }

    public void setPlayerType(Player playerType) {
        this.playerType = playerType;
    }

    public PossibleMove getMove() {
        return move;
    }

    public void setMove(PossibleMove move) {
        this.move = move;
    }

    public int getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(int fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    @Override
    public String toString() {
        return "PlayerMove{" + "playerType=" + playerType + ", move=" + move + ", piece=" + move.getPiece() + " fitnessValue=" + fitnessValue + '}';
    }

 
    
    
    
}
