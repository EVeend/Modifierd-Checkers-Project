/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Checkers.Player;

import Checkers.Board.Piece;

/**
 *
 * @author edwar
 */
public class PossibleMove {
    
    //g(n) == gN - the value of a move
    //h(n) == hN - how many winning piece will it get
    
    private Piece piece;
    private int newXPost;
    private int newYPost;
    private int fValue;

    public PossibleMove(Piece piece, int newXPost, int newYPost) {
        this.piece = piece;
        this.newXPost = newXPost;
        this.newYPost = newYPost;
    }

    public PossibleMove(Piece piece, int newXPost, int newYPost, int fValue) {
        this.piece = piece;
        this.newXPost = newXPost;
        this.newYPost = newYPost;
        this.fValue = fValue;
    }
    
    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getNewXPost() {
        return newXPost;
    }

    public void setNewXPost(int newXPost) {
        this.newXPost = newXPost;
    }

    public int getNewYPost() {
        return newYPost;
    }

    public void setNewYPost(int newYPost) {
        this.newYPost = newYPost;
    }

    public int getfValue() {
        return fValue;
    }

    public void setfValue(int fValue) {
        this.fValue = fValue;
    }

    @Override
    public String toString() {
        return "PossibleMove{" + "newXPost=" + newXPost + ", newYPost=" + newYPost + '}';
    }
    
    
}
