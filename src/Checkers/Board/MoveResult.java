/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Checkers.Board;

/**
 *
 * @author edwar
 */
public class MoveResult {
    
    private MoveType moveType;
    private Piece hoppedPiece;

    public MoveResult(MoveType moveType) {
        this.moveType = moveType;
    }
    
    public MoveResult(MoveType moveType, Piece hoppedPiece) {
        this.moveType = moveType;
        this.hoppedPiece = hoppedPiece;
    }
    
    public Piece getHoppedPiece() {
        return hoppedPiece;
    }

    public MoveType getMoveType() {
        return moveType;
    }
    
}
