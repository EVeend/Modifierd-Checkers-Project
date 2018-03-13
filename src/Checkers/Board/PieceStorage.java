package Checkers.Board;

import java.util.ArrayList;

/**
 *
 * @author edwar
 */
public class PieceStorage {
    
    private PieceType type; 
    private ArrayList<Piece> pieceStorage;

    public PieceStorage(PieceType type) {
        pieceStorage = new ArrayList<Piece>();
        this.type = type;
    }

    public ArrayList<Piece> getPieceStorage() {
        return pieceStorage;
    }

    public void setPieceStorage(ArrayList<Piece> pieceStorage) {
        this.pieceStorage = pieceStorage;
    }
    
    
      
}
