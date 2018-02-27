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
public enum PieceType {

    WHITE(1), BLACK(-1);

    public final int movePiece;

    PieceType(int movePiece) {
        this.movePiece = movePiece;
    }
    
    
}
