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
    
    private PossibleMoves moveType;
    private Piece hoppedPiece;

    public MoveResult(PossibleMoves moveType) {
        this.moveType = moveType;
    }
    
    public MoveResult(PossibleMoves moveType, Piece hoppedPiece) {
        this.moveType = moveType;
        this.hoppedPiece = hoppedPiece;
    }
    
    public Piece getHoppedPiece() {
        return hoppedPiece;
    }

    public PossibleMoves getMoveType() {
        return moveType;
    }
    
}
