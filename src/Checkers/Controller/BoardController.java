/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Checkers.Controller;

import Checkers.Board.MoveResult;
import Checkers.Board.Piece;
import Checkers.Board.PieceStorage;
import Checkers.Board.PieceType;
import Checkers.Board.PossibleMoves;
import Checkers.Board.Tile;
import Checkers.Player.Player;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.layout.Pane;

/**
 *
 * @author edwar
 */
public class BoardController implements Initializable {

    public static final int tileSize = 100;
    public static final int width = 4;
    public static final int height = 4;

    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();

    public static Tile[][] board = new Tile[width][height];

    public static Player whitePlayer;
    public static Player blackPlayer;

    //If true, white moves, if false black moves
    private boolean moveOrder = true;

    PieceStorage whitePieceStorage;
    PieceStorage blackPieceStorage;

    @FXML
    Group boardGroup;

    private Pane createContent() {
        Pane root = new Pane();
        root.setPrefSize(width * tileSize, height * tileSize);
        root.getChildren().addAll(tileGroup, pieceGroup);

        whitePieceStorage = new PieceStorage(PieceType.WHITE);
        blackPieceStorage = new PieceStorage(PieceType.BLACK);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Tile tile = new Tile((j + i) % 2 == 0, j, i);
                board[j][i] = tile;
                tileGroup.getChildren().add(tile);
                Piece piece = null;
                if (i == 0 && j < 3) {
                    piece = makePiece(PieceType.WHITE, j, i);
                }
                if (i == 1 && j < 2) {
                    piece = makePiece(PieceType.WHITE, j, i);
                }
                if (i == 1 && j == 3) {
                    piece = makePiece(PieceType.BLACK, j, i);
                }
                if (i == 2 && j == 0) {
                    piece = makePiece(PieceType.WHITE, j, i);
                }
                if (i == 2 && j > 1) {
                    piece = makePiece(PieceType.BLACK, j, i);
                }
                if (i == 3 && j > 0) {
                    piece = makePiece(PieceType.BLACK, j, i);
                }
                if (piece != null) {
                    switch (piece.getType()) {
                        case WHITE:
                            tile.setOriginalPieceType(PieceType.WHITE);
                            whitePieceStorage.getPieceStorage().add(piece);
                            break;
                        case BLACK:
                            blackPieceStorage.getPieceStorage().add(piece);
                            tile.setOriginalPieceType(PieceType.BLACK);
                            break;
                    }
                    System.out.println(piece.getOldX() + " " + piece.getOldY());
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece);

                }
            }
        }

        whitePlayer = new Player(PieceType.WHITE, 0, 0, 0);
        blackPlayer = new Player(PieceType.BLACK, 0, 0, 0);
        root.setPadding(new Insets(0, 0, 100, 0));
        return root;
    }

    private Piece makePiece(PieceType type, int x, int y) {
        Piece piece = new Piece(type, x, y);

        piece.setOnMouseReleased(e -> {
            int newX = toBoard(piece.getLayoutX());
            int newY = toBoard(piece.getLayoutY());
            if (piece.getType() == PieceType.WHITE && moveOrder) {
                System.out.println("White made a move");
                if (doMove(whitePlayer, piece)) {
                    whitePlayer.addOneToMove();
                    moveOrder = false;
                    System.out.println("Move order now false");
                    System.out.println("White made: " + whitePlayer.getNumberOfMoves() + " moves");
                    whitePlayer.addOneToNumberOfWinningPieces(piece, board[newX][newY]);
                    System.out.println("White Player Winning Pieces: " + whitePlayer.getNumberOfWinningPieces());

                    //Iterate through this piece storage
                    ArrayList<Piece> pieceStorage = whitePieceStorage.getPieceStorage();
                    //Update the new position of the current piece
                    for (int i = 0; i < pieceStorage.size(); i++) {
                        if (pieceStorage.get(i).getOldX() == piece.getOldX() && pieceStorage.get(i).getOldY() == piece.getOldY()) {
                            pieceStorage.get(i).movePiece(newX, newY);
                            System.out.println("Position Updated");
                            System.out.println("New Position " + pieceStorage.get(i).getOldX() + " " + pieceStorage.get(i).getOldY());
                        }
                    }
                }

            } else if (piece.getType() == PieceType.BLACK && !moveOrder) {
                System.out.println("Black made a move");
                if (doMove(blackPlayer, piece)) {
                    blackPlayer.addOneToMove();
                    moveOrder = true;
                    System.out.println("Move order now true");
                    System.out.println("Black made: " + blackPlayer.getNumberOfMoves() + " moves");
                    blackPlayer.addOneToNumberOfWinningPieces(piece, board[newX][newY]);
                    System.out.println("Black Player Winning Pieces: " + blackPlayer.getNumberOfWinningPieces());

                    //Iterate through this piece storage
                    ArrayList<Piece> pieceStorage = blackPieceStorage.getPieceStorage();

                    //Update the new position of the current piece
                    for (int i = 0; i < pieceStorage.size(); i++) {
                        if (pieceStorage.get(i).getOldX() == piece.getOldX() && pieceStorage.get(i).getOldY() == piece.getOldY()) {
                            pieceStorage.get(i).movePiece(newX, newY);
                            System.out.println("Position Updated");
                            System.out.println("New Position " + pieceStorage.get(i).getOldX() + " " + pieceStorage.get(i).getOldY());
                        }
                    }

                }
            } else {
                piece.abortMove();
            }

            //Check if there is already a winner
            if (whitePlayer.getNumberOfWinningPieces() > 0 && blackPlayer.getNumberOfWinningPieces() > 0) {
                if (whitePlayer.getNumberOfWinningPieces() == 6) {
                    System.out.println("WHITE WINS!");
                    whitePlayer.updatePlayerScore(1 - (whitePlayer.getNumberOfMoves() * .01));
                    blackPlayer.updatePlayerScore(0 + (blackPlayer.getNumberOfMoves() * .01));

                } else if (blackPlayer.getNumberOfWinningPieces() == 6) {
                    System.out.println("BLACK WINS!");
                    whitePlayer.updatePlayerScore(0 + (whitePlayer.getNumberOfMoves() * .01));
                    blackPlayer.updatePlayerScore(1 - (blackPlayer.getNumberOfMoves() * .01));
                }
                System.out.println("White Player Score: " + whitePlayer.getPlayerScore());
                System.out.println("Black Player Score: " + blackPlayer.getPlayerScore());
            }
        });
        return piece;
    }

    //Check if move is Valid
    private MoveResult testMove(Piece piece, int newX, int newY) {
        int oldPlaceOnBoardX = toBoard(piece.getOldX());
        int oldPlaceOnBoardY = toBoard(piece.getOldY());
        System.out.println("New Place: " + newX + " " + newY);
        System.out.println("Old Place in Pixels: " + piece.getOldX() + " " + piece.getOldY());
        System.out.println("Old Place Board Place: " + oldPlaceOnBoardX + " " + oldPlaceOnBoardY);

        //Check if out of bounds
        if (newX > 3 || newY > 3 || newX < 0 || newY < 0) {
            return new MoveResult(PossibleMoves.NONE);
        }

        //Invalid Moves
        if (board[newX][newY].hasPiece()) {
            if (!validSwapMove(piece, newX, newY)) {
                System.out.println("Invalid Move 1");
                return new MoveResult(PossibleMoves.NONE);
            } else {
                return new MoveResult(PossibleMoves.SWAP);
            }

        }

        if (((piece.getOldX() + piece.getOldY()) / tileSize) % 2 == 0 && (newX + newY) % 2 == 0) {
            if (!validHopMove(piece, newX, newY)) {
                System.out.println(piece.getOldX() + piece.getOldY());
                System.out.println((piece.getOldX() + piece.getOldY()) % 2 == 0);
                System.out.println("Invalid Move 2");
                return new MoveResult(PossibleMoves.NONE);
            } else {
                return new MoveResult(PossibleMoves.NORMAL);
            }

        }

        if (((piece.getOldX() + piece.getOldY()) / tileSize) % 2 == 1 && (newX + newY) % 2 == 1) {
            if (!validHopMove(piece, newX, newY)) {
                System.out.println(piece.getOldX() + piece.getOldY());
                System.out.println((piece.getOldX() + piece.getOldY()) % 2 == 0);
                System.out.println("Invalid Move 3");
                return new MoveResult(PossibleMoves.NONE);
            } else {
                return new MoveResult(PossibleMoves.NORMAL);
            }
        }

        //Possible moves
        if ((oldPlaceOnBoardX + oldPlaceOnBoardY) % 2 == 0 && (newX + newY) % 2 == 1 && Math.abs(newX - oldPlaceOnBoardX) <= 1 && Math.abs(newY - oldPlaceOnBoardY) <= 1) {
            System.out.println("New Move 1");
            return new MoveResult(PossibleMoves.NORMAL);
        }
        if ((oldPlaceOnBoardX + oldPlaceOnBoardY) % 2 == 1 && (newX + newY) % 2 == 0 && Math.abs(newX - oldPlaceOnBoardX) <= 1 && Math.abs(newY - oldPlaceOnBoardY) <= 1) {
            System.out.println("New Move 2");
            return new MoveResult(PossibleMoves.NORMAL);
        }
        if (isValidDoubleHop(piece, newX, newY)) {
            return new MoveResult(PossibleMoves.NORMAL);
        }
        System.out.println("Invalid move 4");
        return new MoveResult(PossibleMoves.NONE);
    }

    //Do move
    private boolean doMove(Player player, Piece piece) {
        int newX = toBoard(piece.getLayoutX());
        int newY = toBoard(piece.getLayoutY());
        System.out.println("DO MOVE");
        MoveResult result = testMove(piece, newX, newY);

        int x0 = toBoard(piece.getOldX());
        int y0 = toBoard(piece.getOldY());
//        System.out.println("Previous board has piece: " + board[x0][y0].hasPiece());
//        System.out.println("Previous board in enemy tile: " + board[x0][y0].isEnemyTile(player));
//        if(/*!board[newX][newY].isEnemyTile(player) && */board[x0][y0].isEnemyTile(player)){
//            player.subtractOneToNumberOfWinningPieces(piece, board[x0][y0]);
//            System.out.println("Number of winning pieces: " + player.getNumberOfWinningPieces());
//        }

        switch (result.getMoveType()) {
            case NONE:
                piece.abortMove();
                return false;
            case NORMAL:
                piece.movePiece(newX, newY);
                board[x0][y0].setPiece(null);
                board[newX][newY].setPiece(piece);

                if (board[x0][y0].isEnemyTile(player)) {
                    player.subtractOneToNumberOfWinningPieces();
                }
                return true;
            case SWAP:
                System.out.println("Swap");
                System.out.println(piece.getType());
                Piece newPiece = null;
//                board[x0][y0].setPiece(null);
                switch (piece.getType()) {
                    case WHITE:
                        newPiece = board[newX][newY].getPiece();
                        newPiece.movePiece(x0, y0);
                        blackPlayer.addOneToNumberOfWinningPieces(newPiece, board[x0][y0]);
                        board[x0][y0].setPiece(newPiece);

                        if (board[newX][newY].isEnemyTile(blackPlayer)) {
                            blackPlayer.subtractOneToNumberOfWinningPieces();
                        }
                        if (board[x0][y0].isEnemyTile(whitePlayer)) {
                            whitePlayer.subtractOneToNumberOfWinningPieces();
                        }
                        break;
                    case BLACK:
                        newPiece = board[newX][newY].getPiece();
                        newPiece.movePiece(x0, y0);
                        whitePlayer.addOneToNumberOfWinningPieces(newPiece, board[x0][y0]);
                        board[x0][y0].setPiece(newPiece);

                        if (board[newX][newY].isEnemyTile(whitePlayer)) {
                            whitePlayer.subtractOneToNumberOfWinningPieces();
                        }
                        if (board[x0][y0].isEnemyTile(blackPlayer)) {
                            blackPlayer.subtractOneToNumberOfWinningPieces();
                        }
                        break;
                }
                piece.movePiece(newX, newY);
                board[newX][newY].setPiece(piece);

                return true;

        }
        return false;
    }

    private boolean validHopMove(Piece piece, int newX, int newY) {
        int x0 = toBoard(piece.getOldX());
        int y0 = toBoard(piece.getOldY());

        System.out.println("Position on Board: " + x0 + " " + y0);
        //Check if there are no more possible moves
        if (checkAllMoves(piece)) {
            int differenceX0 = Math.abs(x0 - newX);
            int differenceY0 = Math.abs(y0 - newY);
            System.out.println("No possible moves");
//            System.out.println("X0: " + differenceX0 + " Y0: " + differenceY0);
            if ((differenceX0 == 0 || differenceX0 == 2) && (differenceY0 == 0 || differenceY0 == 2)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public boolean validSwapMove(Piece piece, int newX, int newY) {
        int x0 = toBoard(piece.getOldX());
        int y0 = toBoard(piece.getOldY());

        System.out.println("Position on Board: " + x0 + " " + y0);
        //Check if there are no more possible moves
        if (checkAllMoves(piece)) {
            int differenceX0 = Math.abs(x0 - newX);
            int differenceY0 = Math.abs(y0 - newY);
//            System.out.println("X0: " + differenceX0 + " Y0: " + differenceY0);
            //Check if adjacent
            if ((differenceX0 == 0 || differenceX0 == 1) && (differenceY0 == 0 || differenceY0 == 1)) {
                //Check if enemy piece
                if (board[newX][newY].getPiece().getType() != board[x0][y0].getPiece().getType()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean checkAllMoves(Piece piece) {

        ArrayList<Piece> pieceStorage = new ArrayList<>();
        switch (piece.getType()) {
            case WHITE:
                pieceStorage = whitePieceStorage.getPieceStorage();
                break;
            case BLACK:
                pieceStorage = blackPieceStorage.getPieceStorage();
                break;
        }

        for (Piece storedPiece : pieceStorage) {
            if (isNoForwardMove(storedPiece)) {
                return true;
            }
        }
        //Check if there are no more possible moves
//        if (x0 - 1 != -1 && !board[x0 - 1][y0].hasPiece()) {
//            System.out.println("Invalid Hope Move 1");
//            return false;
//        } else if (x0 + 1 < 4 && !board[x0 + 1][y0].hasPiece()) {
//            System.out.println("Invalid Hope Move 2");
//            return false;
//        } else if (y0 - 1 != -1 && !board[x0][y0 - 1].hasPiece()) {
//            System.out.println("Invalid Hope Move 3");
//            return false;
//        } else if (y0 + 1 < 4 && !board[x0][y0 + 1].hasPiece()) {
//            System.out.println("Invalid Hope Move 4");
//            return false;
//        } else {
//            return true;
//        }

        return false;
    }

    private boolean isNoForwardMove(Piece piece) {
        switch (piece.getType()) {

            case WHITE:
                for (Piece storedPiece : whitePieceStorage.getPieceStorage()) {
                    int oldXStoredPiece = toBoard(storedPiece.getOldX());
                    int oldYStoredPiece = toBoard(storedPiece.getOldY());
                    if ((oldXStoredPiece != 3 && !board[oldXStoredPiece + 1][oldYStoredPiece].hasPiece()) || (oldYStoredPiece != 3 && !board[oldXStoredPiece][oldYStoredPiece + 1].hasPiece())) {
                        return false;
                    }
                }
                break;
            //EDWARD EDIT MO PA TO at pangit ka
            case BLACK:
                for (Piece storedPiece : blackPieceStorage.getPieceStorage()) {
                    int oldXStoredPiece = toBoard(storedPiece.getOldX());
                    int oldYStoredPiece = toBoard(storedPiece.getOldY());
                    if ((oldXStoredPiece != 0 && !board[oldXStoredPiece - 1][oldYStoredPiece].hasPiece()) || (oldYStoredPiece != 0 && !board[oldXStoredPiece][oldYStoredPiece - 1].hasPiece())) {
                        return false;
                    }
                }
                break;
        }
        return true;
    }

    public boolean isValidDoubleHop(Piece piece, int newX, int newY) {
        int x0 = toBoard(piece.getOldX());
        int y0 = toBoard(piece.getOldY());

        System.out.println("Position on Board: " + x0 + " " + y0);
        //Check if double hop is valid
        if (isNoForwardMove(piece)) {
            if ((y0 == 0 && board[x0][y0 + 2].hasPiece()) && (newX == x0 && newY == y0 + 3)) {
                return true;
            } else if ((x0 == 0 && board[x0 + 2][y0].hasPiece()) && (newX == x0 + 3 && newY == y0)) {
                return true;
            } else if (y0 == 3 && board[x0][y0 - 2].hasPiece() && (newX == x0 && newY == y0 - 3)) {
                return true;
            } else if (x0 == 3 && board[x0 - 2][y0].hasPiece() && (newX == x0 - 3 && newY == y0)) {
                return true;
            } else {
                return false;
            }
        }
        else{
            return false;
        }
    }

    private int toBoard(double pixel) {
        return (int) (pixel + tileSize / 2) / tileSize;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardGroup.getChildren().addAll(createContent());
    }
}
