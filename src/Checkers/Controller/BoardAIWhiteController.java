/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Checkers.Controller;

import Checkers.AI.AlphaBetaPrunWhite;
import Checkers.Board.MoveResult;
import Checkers.Board.Piece;
import Checkers.Board.PieceStorage;
import Checkers.Board.PieceType;
import Checkers.Board.MoveType;
import Checkers.Board.Tile;
import Checkers.Player.Player;
import Checkers.Player.PlayerMove;
import Checkers.Player.PossibleMove;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author edwar
 */
public class BoardAIWhiteController implements Initializable {

    public static final int tileSize = 100;
    public static final int width = 4;
    public static final int height = 4;

    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();

    public static Tile[][] board = new Tile[width][height];
    public static ArrayList<Player> listOfPlayer = new ArrayList<>();

    public static Player whitePlayer;
    public static Player blackPlayer;

    //If true, white moves, if false black moves
    private boolean moveOrder = true;

    static PieceStorage whitePieceStorage;
    static PieceStorage blackPieceStorage;

    private PieceType playerType;

    @FXML
    Group boardGroup;
    Button newGameOPTN;
    Button forfeitOPTN;
    Button passBTN;

    @FXML
    private Label movesWhite;
    @FXML
    private Label movesBlack;
    @FXML
    private Label pieceTurn;

    public void setPlayerPieceType(PieceType type) {
        playerType = type;
        System.out.println("Setted Piece Type: " + playerType);
        System.out.println(playerType == PieceType.WHITE);
    }

    private Pane createContent() {
        Pane root = new Pane();
        root.setPrefSize(width * tileSize, height * tileSize);
        root.getChildren().addAll(tileGroup, pieceGroup);

        whitePieceStorage = new PieceStorage(PieceType.WHITE);
        blackPieceStorage = new PieceStorage(PieceType.BLACK);

//        whitePlayer = new Player(PieceType.WHITE, 0, 0, 0, whitePieceStorage);
//        blackPlayer = new Player(PieceType.BLACK, 0, 0, 0, whitePieceStorage);
        whitePlayer = new Player(PieceType.WHITE, 0, 0, 0, whitePieceStorage, true);
        blackPlayer = new Player(PieceType.BLACK, 0, 0, 0, blackPieceStorage, false);
        listOfPlayer.add(whitePlayer);
        listOfPlayer.add(blackPlayer);

        //MAY MALI DITO
//        switch (playerType) {
//            //0 if Human; 1 if Computer
//            case WHITE:
//                whitePlayer = new Player(PieceType.WHITE, 0, 0, 0, whitePieceStorage, true);
//                blackPlayer = new Player(PieceType.BLACK, 0, 0, 0, whitePieceStorage, false);
//                listOfPlayer.add(whitePlayer);
//                listOfPlayer.add(blackPlayer);
//                break;
//            case BLACK:
//                whitePlayer = new Player(PieceType.WHITE, 0, 0, 0, whitePieceStorage, false);
//                blackPlayer = new Player(PieceType.BLACK, 0, 0, 0, whitePieceStorage, true);
//                listOfPlayer.add(blackPlayer);
//                listOfPlayer.add(whitePlayer);
//                break;
//        }
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
                pieceTurn.setText("Black's turn!");
                if (doMove(whitePlayer, piece)) {
                    //From this line
                    whitePlayer.addOneToMove();
                    moveOrder = false;
                    System.out.println("Move order now false");
                    System.out.println("White made: " + whitePlayer.getNumberOfMoves() + " moves");
                    movesWhite.setText(Integer.toString(whitePlayer.getNumberOfMoves()));
                    whitePlayer.addOneToNumberOfWinningPieces(piece, board[newX][newY]);
                    System.out.println("White Player Winning Pieces: " + whitePlayer.getNumberOfWinningPieces());
                    //to this line, eto yung ilalagay sa pass method

                    //Iterate through this piece storage
                    //Update the new position of the current piece
                    for (int i = 0; i < whitePlayer.getPiecePositions().getPieceStorage().size(); i++) {
                        if (whitePlayer.getPiecePositions().getPieceStorage().get(i).getOldX() == piece.getOldX() && whitePlayer.getPiecePositions().getPieceStorage().get(i).getOldY() == piece.getOldY()) {
                            whitePlayer.getPiecePositions().getPieceStorage().get(i).movePiece(newX, newY);
                            System.out.println("Position Updated");
                            System.out.println("New Position " + whitePlayer.getPiecePositions().getPieceStorage().get(i).getOldX() + " " + whitePlayer.getPiecePositions().getPieceStorage().get(i).getOldY());
                        }
                    }
                    checkIfHasWinner(e);
                }
                if (!moveOrder) {
                    AIMove();
                    checkIfHasWinner(e);
                }
            } else {
                piece.abortMove();
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
            return new MoveResult(MoveType.NONE);
        }

        //Invalid Moves
        if (board[newX][newY].hasPiece()) {
            if (!validSwapMove(piece, newX, newY)) {
                System.out.println("Invalid Move 1");
                return new MoveResult(MoveType.NONE);
            } else {
                return new MoveResult(MoveType.SWAP);
            }

        }

        if (((piece.getOldX() + piece.getOldY()) / tileSize) % 2 == 0 && (newX + newY) % 2 == 0) {
            if (!validHopMove(piece, newX, newY)) {
                System.out.println(piece.getOldX() + piece.getOldY());
                System.out.println((piece.getOldX() + piece.getOldY()) % 2 == 0);
                System.out.println("Invalid Move 2");
                return new MoveResult(MoveType.NONE);
            } else {
                return new MoveResult(MoveType.NORMAL);
            }

        }

        if (((piece.getOldX() + piece.getOldY()) / tileSize) % 2 == 1 && (newX + newY) % 2 == 1) {
            if (!validHopMove(piece, newX, newY)) {
                System.out.println(piece.getOldX() + piece.getOldY());
                System.out.println((piece.getOldX() + piece.getOldY()) % 2 == 0);
                System.out.println("Invalid Move 3");
                return new MoveResult(MoveType.NONE);
            } else {
                return new MoveResult(MoveType.NORMAL);
            }
        }

        //Possible moves
        if ((oldPlaceOnBoardX + oldPlaceOnBoardY) % 2 == 0 && (newX + newY) % 2 == 1 && Math.abs(newX - oldPlaceOnBoardX) <= 1 && Math.abs(newY - oldPlaceOnBoardY) <= 1) {
            System.out.println("New Move 1");
            return new MoveResult(MoveType.NORMAL);
        }
        if ((oldPlaceOnBoardX + oldPlaceOnBoardY) % 2 == 1 && (newX + newY) % 2 == 0 && Math.abs(newX - oldPlaceOnBoardX) <= 1 && Math.abs(newY - oldPlaceOnBoardY) <= 1) {
            System.out.println("New Move 2");
            return new MoveResult(MoveType.NORMAL);
        }
        if (isValidDoubleHop(piece, newX, newY)) {
            return new MoveResult(MoveType.NORMAL);
        }
        System.out.println("Invalid move 4");
        return new MoveResult(MoveType.NONE);
    }

    //Do move
    private boolean doMove(Player player, Piece piece) {
        int newX = toBoard(piece.getLayoutX());
        int newY = toBoard(piece.getLayoutY());
        System.out.println("NewXLayout " + newX + " NewYLayout " + piece.getLayoutY());
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
                System.out.println("Move Piece");
                piece.movePiece(newX, newY);
                board[newX][newY].setPiece(piece);
                System.out.println("Returns true");
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
            if (isHopMove(piece, newX, newY)) {
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
            if (isSwap(piece, newX, newY)) {
                return true;
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
            if (hasNoForwardMove(storedPiece)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkAllMoves(boolean moveOrder) {
        ArrayList<Piece> pieceStorage = new ArrayList<>();
        if (moveOrder) {
            pieceStorage = whitePieceStorage.getPieceStorage();
        } else {
            pieceStorage = blackPieceStorage.getPieceStorage();
        }
        for (Piece storedPiece : pieceStorage) {
            if (hasNoForwardMove(storedPiece)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasNoForwardMove(Piece piece) {
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
        if (hasNoForwardMove(piece)) {
            if (isDoubleHop(piece, newX, newY)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static ArrayList<PossibleMove> getPossibleMoves(Player player) {

        ArrayList<PossibleMove> possibleMoveList = new ArrayList<>();
        for (Piece storedPiece : player.getPiecePositions().getPieceStorage()) {
//            PossibleMove move = new PossibleMove(storedPiece.getOldX(), storedPiece.getOldY());
//            possibleMoveList.add(move);
            PossibleMove move;
            System.out.println("Piece position: " + storedPiece.getOldX() + " " + storedPiece.getOldY());
            int oldX = toBoard(storedPiece.getOldX());
            int oldY = toBoard(storedPiece.getOldY());
            if (oldX == 0) {
                System.out.println("X == 0");
                if (oldY == 0) {
                    if (!board[oldX][oldY + 1].hasPiece()) {
                        move = new PossibleMove(storedPiece, oldX, oldY + 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX + 1][oldY].hasPiece()) {
                        move = new PossibleMove(storedPiece, oldX + 1, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    //Start of swap
                    if (board[oldX][oldY + 1].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX][oldY + 1].getPiece().getType() != storedPiece.getType()) {
                        System.out.println("Swap");
                        move = new PossibleMove(storedPiece, oldX, oldY + 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (board[oldX + 1][oldY].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX + 1][oldY].getPiece().getType() != storedPiece.getType()) {
                        System.out.println("Swap");
                        move = new PossibleMove(storedPiece, oldX + 1, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    } //Start of hop
                    if (!board[oldX][oldY + 2].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("Hop");
                        move = new PossibleMove(storedPiece, oldX, oldY + 2);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX + 2][oldY].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("Hop");
                        move = new PossibleMove(storedPiece, oldX + 2, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    } //Start of double hop
                    if (!board[oldX + 3][oldY].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("Double Hop");
                        move = new PossibleMove(storedPiece, oldX + 3, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX][oldY + 3].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("Double Hop");
                        move = new PossibleMove(storedPiece, oldX, oldY + 3);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }

                } //X == 0
                else if (oldY == 3) {
                    if (!board[oldX + 1][oldY].hasPiece()) {
                        move = new PossibleMove(storedPiece, oldX + 1, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX][oldY - 1].hasPiece()) {
                        move = new PossibleMove(storedPiece, oldX, oldY - 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    } //Start of Swap
                    if (board[oldX + 1][oldY].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX + 1][oldY].getPiece().getType() != storedPiece.getType()) {
                        System.out.println("Swap");
                        move = new PossibleMove(storedPiece, oldX + 1, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (board[oldX][oldY - 1].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX][oldY - 1].getPiece().getType() != storedPiece.getType()) {
                        System.out.println("Swap");
                        move = new PossibleMove(storedPiece, oldX, oldY - 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    } //Start pf hop
                    if (!board[oldX][oldY - 2].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("Hop");
                        move = new PossibleMove(storedPiece, oldX, oldY - 2);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX + 2][oldY].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("Hop");
                        move = new PossibleMove(storedPiece, oldX + 2, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    } //Start of double hop
                    if (!board[oldX + 3][oldY].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("Double Hop");
                        move = new PossibleMove(storedPiece, oldX + 3, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX][oldY - 3].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("Double Hop");
                        move = new PossibleMove(storedPiece, oldX, oldY - 3);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }

                } //X == 0
                else if (oldY == 1 || oldY == 2) {
                    if (!board[oldX][oldY - 1].hasPiece()) {
                        move = new PossibleMove(storedPiece, oldX, oldY - 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX + 1][oldY].hasPiece()) {
                        move = new PossibleMove(storedPiece, oldX + 1, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX][oldY + 1].hasPiece()) {
                        move = new PossibleMove(storedPiece, oldX, oldY + 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    } //Start of swap
                    if (board[oldX][oldY - 1].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX][oldY - 1].getPiece().getType() != storedPiece.getType()) {
                        System.out.println("Swap");
                        move = new PossibleMove(storedPiece, oldX, oldY - 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (board[oldX + 1][oldY].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX + 1][oldY].getPiece().getType() != storedPiece.getType()) {
                        System.out.println("Swap");
                        move = new PossibleMove(storedPiece, oldX + 1, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (board[oldX][oldY + 1].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX][oldY + 1].getPiece().getType() != storedPiece.getType()) {
                        System.out.println("Swap");
                        move = new PossibleMove(storedPiece, oldX, oldY + 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    } //Start of hop
                    if (!board[oldX + 2][oldY].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("Hop");
                        move = new PossibleMove(storedPiece, oldX, oldY + 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX + 3][oldY].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("double hop");
                        move = new PossibleMove(storedPiece, oldX, oldY + 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                }

            }
            if (oldX == 3) {
                System.out.println("X == 3");
                if (oldY == 0) {
                    if (!board[oldX - 1][oldY].hasPiece()) {
                        move = new PossibleMove(storedPiece, oldX - 1, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX][oldY + 1].hasPiece()) {
                        move = new PossibleMove(storedPiece, oldX, oldY + 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    } //Start of Swap
                    if (board[oldX - 1][oldY].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX - 1][oldY].getPiece().getType() != storedPiece.getType()) {
                        System.out.println("Swap");
                        move = new PossibleMove(storedPiece, oldX - 1, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (board[oldX][oldY + 1].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX][oldY + 1].getPiece().getType() != storedPiece.getType()) {
                        System.out.println("Swap");
                        move = new PossibleMove(storedPiece, oldX, oldY + 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    } //Start of hops
                    if (!board[oldX - 2][oldY].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("hop");
                        move = new PossibleMove(storedPiece, oldX - 2, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX][oldY + 2].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("hop");
                        move = new PossibleMove(storedPiece, oldX, oldY + 2);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    } //Start of double hop
                    if (!board[oldX][oldY + 3].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("double hop");
                        move = new PossibleMove(storedPiece, oldX, oldY + 3);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX - 3][oldY].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("double hop");
                        move = new PossibleMove(storedPiece, oldX - 3, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }

                } //X == 3
                else if (oldY == 3) {
                    if (!board[oldX - 1][oldY].hasPiece()) {
                        move = new PossibleMove(storedPiece, oldX - 1, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX][oldY - 1].hasPiece()) {
                        move = new PossibleMove(storedPiece, oldX, oldY - 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    } //Swap
                    if (board[oldX - 1][oldY].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX - 1][oldY].getPiece().getType() != storedPiece.getType()) {
                        System.out.println("Swap");
                        move = new PossibleMove(storedPiece, oldX - 1, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (board[oldX][oldY - 1].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX][oldY - 1].getPiece().getType() != storedPiece.getType()) {
                        System.out.println("Swap");
                        move = new PossibleMove(storedPiece, oldX, oldY - 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    } //Hop
                    if (!board[oldX][oldY - 2].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("Hop");
                        move = new PossibleMove(storedPiece, oldX, oldY - 2);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX - 2][oldY].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("Hop");
                        move = new PossibleMove(storedPiece, oldX - 2, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    } //Double hop
                    if (!board[oldX - 3][oldY].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("Double hop");
                        move = new PossibleMove(storedPiece, oldX - 3, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX][oldY - 3].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("Double hop");
                        move = new PossibleMove(storedPiece, oldX, oldY - 3);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                } //X == 3
                else if (oldY == 1 || oldY == 2) {
                    if (!board[oldX][oldY - 1].hasPiece()) {
                        move = new PossibleMove(storedPiece, oldX, oldY - 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX - 1][oldY].hasPiece()) {
                        move = new PossibleMove(storedPiece, oldX - 1, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (!board[oldX][oldY + 1].hasPiece()) {
                        move = new PossibleMove(storedPiece, oldX, oldY + 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    } //Swap
                    if (board[oldX][oldY - 1].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX][oldY - 1].getPiece().getType() != storedPiece.getType()) {
                        System.out.println("Swap");
                        move = new PossibleMove(storedPiece, oldX, oldY - 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (board[oldX - 1][oldY].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX - 1][oldY].getPiece().getType() != storedPiece.getType()) {
                        System.out.println("Swap");
                        move = new PossibleMove(storedPiece, oldX - 1, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                    if (board[oldX][oldY + 1].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX][oldY + 1].getPiece().getType() != storedPiece.getType()) {
                        System.out.println("Swap");
                        move = new PossibleMove(storedPiece, oldX, oldY + 1);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    } //Start of Hop
                    if (!board[oldX - 2][oldY].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("Hop");
                        move = new PossibleMove(storedPiece, oldX - 2, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    } //Start of Double hop
                    if (!board[oldX - 3][oldY].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("Double Hop");
                        move = new PossibleMove(storedPiece, oldX - 3, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                }
            }

            if (oldY == 0 && oldX > 0 && oldX < 3) {
                System.out.println("Y == 0");
                if (!board[oldX - 1][oldY].hasPiece()) {
                    move = new PossibleMove(storedPiece, oldX - 1, oldY);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }
                if (!board[oldX][oldY + 1].hasPiece()) {
                    move = new PossibleMove(storedPiece, oldX, oldY + 1);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }
                if (!board[oldX + 1][oldY].hasPiece()) {
                    move = new PossibleMove(storedPiece, oldX + 1, oldY);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                } //Start of Swap
                if (board[oldX - 1][oldY].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX - 1][oldY].getPiece().getType() != storedPiece.getType()) {
                    System.out.println("Swap");
                    move = new PossibleMove(storedPiece, oldX - 1, oldY);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }
                if (board[oldX][oldY + 1].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX][oldY + 1].getPiece().getType() != storedPiece.getType()) {
                    System.out.println("Swap");
                    move = new PossibleMove(storedPiece, oldX, oldY + 1);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }
                if (board[oldX + 1][oldY].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX + 1][oldY].getPiece().getType() != storedPiece.getType()) {
                    System.out.println("Swap");
                    move = new PossibleMove(storedPiece, oldX + 1, oldY);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                } //Hop
                if (!board[oldX][oldY + 2].hasPiece() && hasNoForwardMove(storedPiece)) {
                    System.out.println("Hop");
                    move = new PossibleMove(storedPiece, oldX, oldY + 2);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                } //Double hop
                if (!board[oldX][oldY + 2].hasPiece() && hasNoForwardMove(storedPiece)) {
                    System.out.println("Double Hop");
                    move = new PossibleMove(storedPiece, oldX, oldY + 3);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }

            }
            if (oldY == 3 && oldX < 3 && oldX > 0) {
                System.out.println("Y == 3");
                if (!board[oldX - 1][oldY].hasPiece()) {
                    move = new PossibleMove(storedPiece, oldX - 1, oldY);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }
                if (!board[oldX][oldY - 1].hasPiece()) {
                    move = new PossibleMove(storedPiece, oldX, oldY - 1);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }
                if (board[oldX + 1][oldY].hasPiece()) {
                    move = new PossibleMove(storedPiece, oldX + 1, oldY);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                } //Swap
                if (board[oldX - 1][oldY].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX - 1][oldY].getPiece().getType() != storedPiece.getType()) {
                    System.out.println("Swap");
                    move = new PossibleMove(storedPiece, oldX - 1, oldY);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }
                if (board[oldX][oldY - 1].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX][oldY - 1].getPiece().getType() != storedPiece.getType()) {
                    System.out.println("Swap");
                    move = new PossibleMove(storedPiece, oldX, oldY - 1);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }
                if (board[oldX + 1][oldY].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX + 1][oldY].getPiece().getType() != storedPiece.getType()) {
                    System.out.println("Swap");
                    move = new PossibleMove(storedPiece, oldX + 1, oldY);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                } //Hop
                if (board[oldX][oldY - 2].hasPiece() && hasNoForwardMove(storedPiece)) {
                    System.out.println("Hop");
                    move = new PossibleMove(storedPiece, oldX - 2, oldY);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                } //Double Hop
                if (board[oldX][oldY - 3].hasPiece() && hasNoForwardMove(storedPiece)) {
                    System.out.println("Double");
                    move = new PossibleMove(storedPiece, oldX - 3, oldY);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }
            }
            if ((oldX == 1 || oldX == 2) && (oldY == 1 || oldY == 2)) {
                System.out.println("Center part");
                if (!board[oldX - 1][oldY].hasPiece()) {
                    move = new PossibleMove(storedPiece, oldX - 1, oldY);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }
                if (!board[oldX + 1][oldY].hasPiece()) {
                    move = new PossibleMove(storedPiece, oldX + 1, oldY);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }
                if (!board[oldX][oldY - 1].hasPiece()) {
                    move = new PossibleMove(storedPiece, oldX, oldY - 1);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }
                if (!board[oldX][oldY + 1].hasPiece()) {
                    move = new PossibleMove(storedPiece, oldX, oldY + 1);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }
                //Swap
                if (board[oldX - 1][oldY].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX - 1][oldY].getPiece().getType() != storedPiece.getType()) {
                    System.out.println("Swap");
                    move = new PossibleMove(storedPiece, oldX - 1, oldY);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }
                if (board[oldX + 1][oldY].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX + 1][oldY].getPiece().getType() != storedPiece.getType()) {
                    System.out.println("Swap");
                    move = new PossibleMove(storedPiece, oldX + 1, oldY);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }
                if (board[oldX][oldY - 1].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX][oldY - 1].getPiece().getType() != storedPiece.getType()) {
                    System.out.println("Swap");
                    move = new PossibleMove(storedPiece, oldX, oldY - 1);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                }
                if (board[oldX][oldY + 1].hasPiece() && hasNoForwardMove(storedPiece) && board[oldX][oldY + 1].getPiece().getType() != storedPiece.getType()) {
                    System.out.println("Swap");
                    move = new PossibleMove(storedPiece, oldX, oldY + 1);
                    System.out.println(move.toString());
                    possibleMoveList.add(move);

                } //Hop 
                if ((oldX == 1 && oldY == 2) || (oldX == 1 && oldY == 1)) {
                    if (board[oldX + 2][oldY].hasPiece() && hasNoForwardMove(storedPiece)) {
                        System.out.println("Hop");
                        move = new PossibleMove(storedPiece, oldX + 2, oldY);
                        System.out.println(move.toString());
                        possibleMoveList.add(move);

                    }
                }
            }
        }
        return possibleMoveList;
    }

    public static boolean isForwardMove(Piece piece, int newX, int newY) {
        int oldXPiece = toBoard(piece.getOldX());
        int oldYPiece = toBoard(piece.getOldY());
        switch (piece.getType()) {

            case WHITE:
                if ((oldXPiece == newX && newY - oldYPiece == 1) || (newX - oldXPiece == 1 && oldYPiece == newY)) {
                    return true;
                }
                break;
            //EDWARD EDIT MO PA TO at pangit ka
            case BLACK:
                if ((oldXPiece == newX && oldYPiece - newY == 1) || (oldXPiece - newX == 1 && oldYPiece == newY)) {
                    return true;
                }
                break;
        }
        return false;
    }

    public static boolean isBackMove(Piece piece, int newX, int newY) {
        int oldXPiece = toBoard(piece.getOldX());
        int oldYPiece = toBoard(piece.getOldY());
        switch (piece.getType()) {

            case WHITE:
                if ((oldXPiece == newX && oldYPiece - newY == 1) || (oldXPiece - newX == 1 && oldYPiece == newY)) {
                    return true;
                }
                break;
            //EDWARD EDIT MO PA TO at pangit ka
            case BLACK:
                if ((oldXPiece == newX && oldYPiece - newY == 1) || (oldXPiece - newX == 1 && oldYPiece == newY)) {
                    return true;
                }
                break;
        }
        return false;
    }

    public static boolean isHopMove(Piece piece, int newX, int newY) {
        int x0 = toBoard(piece.getOldX());
        int y0 = toBoard(piece.getOldY());
        int differenceX0 = Math.abs(x0 - newX);
        int differenceY0 = Math.abs(y0 - newY);
        System.out.println("No possible moves");
//            System.out.println("X0: " + differenceX0 + " Y0: " + differenceY0);
        if ((differenceX0 == 0 || differenceX0 == 2) && (differenceY0 == 0 || differenceY0 == 2)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSwap(Piece piece, int newX, int newY) {
        int x0 = toBoard(piece.getOldX());
        int y0 = toBoard(piece.getOldY());
        int differenceX0 = Math.abs(x0 - newX);
        int differenceY0 = Math.abs(y0 - newY);
//            System.out.println("X0: " + differenceX0 + " Y0: " + differenceY0);
        //Check if adjacent
        if ((differenceX0 == 0 || differenceX0 == 1) && (differenceY0 == 0 || differenceY0 == 1)) {
            //Check if enemy piece
            System.out.println("Something: " + newX + " " + newY);
            System.out.println(board[newX][newY].getPiece().getType());
            if (board[newX][newY].getPiece().getType() != board[x0][y0].getPiece().getType()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isDoubleHop(Piece piece, int newX, int newY) {
        int x0 = toBoard(piece.getOldX());
        int y0 = toBoard(piece.getOldY());
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

    public static int evaluatePiece(Player player, Piece piece, int newX, int newY) {
        int gN = 0;
        int hN = 0;

        if (isForwardMove(piece, newX, newY)) {
            System.out.println("ForwardMove");
            gN = 2;
        } else if (isBackMove(piece, newX, newY)) {
            System.out.println("Back Move");
            gN = 0;
        } else if (isHopMove(piece, newX, newY)) {
            System.out.println("Hop move");
            gN = 1;
        } else if (isDoubleHop(piece, newX, newY)) {
            System.out.println("Double Hop Move");
            gN = 1;
        } else if (isSwap(piece, newX, newY)) {
            System.out.println("Swap Move");
            gN = 1;
        }

        if (board[newX][newY].isEnemyTile(player)) {
            hN = player.getNumberOfWinningPieces() + 1;
        }

        return gN + hN;
    }

    public void AIMove() {
        //AI Turn to move
        System.out.println("Start alpha beta prun");
        ArrayList<PossibleMove> possibleMoveList = getPossibleMoves(blackPlayer);
        PlayerMove newPlayerMove = null;
        //gets first possible forward move
        for (int i = 0; i < possibleMoveList.size(); i++) {
            Piece currentPiece = possibleMoveList.get(i).getPiece();
            int newPostX = possibleMoveList.get(i).getNewXPost();
            int newPostY = possibleMoveList.get(i).getNewYPost();
            if (isForwardMove(currentPiece, newPostX, newPostY)) {
                newPlayerMove = AlphaBetaPrunWhite.startAlphaBet(4, possibleMoveList.get(i), listOfPlayer, 1);
                break;
            }
        }
        System.out.println(newPlayerMove.toString());
//                System.out.println("OldX: " + newPlayerMove.getMove().getPiece().getOldX() + "OldY :" + newPlayerMove.getMove().getPiece().getOldY());
//                System.out.println("LayoutX: " + newPlayerMove.getMove().getPiece().getLayoutX());
//                System.out.println("LayoutY: " + newPlayerMove.getMove().getPiece().getLayoutY());
//                if (doMove(newPlayerMove.getPlayerType(), newPlayerMove.getMove().getPiece())) {
//
//                }
        Piece newPiece = newPlayerMove.getMove().getPiece();
//                System.out.println(newPlayerMove.getMove().getNewXPost() * 100 + " " + newPlayerMove.getMove().getNewYPost() * 100);
        double newPieceLayoutX = newPlayerMove.getMove().getNewXPost() * 100;
        double newPieceLayoutY = newPlayerMove.getMove().getNewYPost() * 100;
        System.out.println("NewPieceLayoutX: " + newPieceLayoutX + " NewPieceLayoutY: " + newPieceLayoutY);
        newPiece.setLayoutX(newPlayerMove.getMove().getNewXPost() * 100);
        newPiece.setLayoutY(newPlayerMove.getMove().getNewYPost() * 100);

        System.out.println("DIvision");
        double oldXPost = newPiece.getOldX();
        double oldYPost = newPiece.getOldY();
        int newX = toBoard(newPiece.getLayoutX());
        int newY = toBoard(newPiece.getLayoutY());
        System.out.println("OldXPost: " + oldXPost + " OldYPost: " + oldYPost);
        if (doMove(newPlayerMove.getPlayerType(), newPiece)) {
            System.out.println("PlayerMove OldX: ");
            blackPlayer.addOneToMove();
            moveOrder = true;
            System.out.println("Move order now true");
            pieceTurn.setText("White's turn!");
            System.out.println("Black made: " + blackPlayer.getNumberOfMoves() + " moves");
            movesBlack.setText(Integer.toString(blackPlayer.getNumberOfMoves()));
            blackPlayer.addOneToNumberOfWinningPieces(newPiece, board[newX][newY]);
            System.out.println("Black Player Winning Pieces: " + blackPlayer.getNumberOfWinningPieces());
            System.out.println("NewPiece OldX: " + newPiece.getOldX());
            System.out.println("NewPiece OldY: " + newPiece.getOldY());
            //Iterate through this piece storage
            ArrayList<Piece> pieceStorage = blackPieceStorage.getPieceStorage();

            //Update the new position of the current piece
            System.out.println("Update Position");
            for (int i = 0; i < blackPieceStorage.getPieceStorage().size(); i++) {
                System.out.println("Stored Old Positions: " + blackPlayer.getPiecePositions().getPieceStorage().get(i).getOldX() + " " + blackPlayer.getPiecePositions().getPieceStorage().get(i).getOldY());
                System.out.println("Old Positions: " + oldXPost + " " + oldYPost);
                System.out.println(blackPlayer.getPiecePositions().getPieceStorage().get(i).getOldX() == oldXPost);
                System.out.println(blackPlayer.getPiecePositions().getPieceStorage().get(i).getOldY() == oldYPost);
                if (blackPlayer.getPiecePositions().getPieceStorage().get(i).getOldX() == oldXPost && blackPlayer.getPiecePositions().getPieceStorage().get(i).getOldY() == oldYPost) {
                    blackPlayer.getPiecePositions().getPieceStorage().get(i).movePiece(newPlayerMove.getMove().getNewXPost(), newPlayerMove.getMove().getNewYPost());
                    System.out.println("Position Updated");
                    System.out.println("New Position " + blackPlayer.getPiecePositions().getPieceStorage().get(i).getOldX() + " " + blackPlayer.getPiecePositions().getPieceStorage().get(i).getOldY());
                }
            }
        }
    }

    public void checkIfHasWinner(MouseEvent e) {
        //Check if there is already a winner
        if (whitePlayer.getNumberOfWinningPieces() > 0 && blackPlayer.getNumberOfWinningPieces() > 0) {
            if (whitePlayer.getNumberOfWinningPieces() == 6) {
                System.out.println("WHITE WINS!");
                whitePlayer.updatePlayerScore(1 - (whitePlayer.getNumberOfMoves() * .01));
                blackPlayer.updatePlayerScore(0 + (blackPlayer.getNumberOfMoves() * .01));
                AlertBox.display("Congratulations!", "White wins!", whitePlayer.getPlayerScore(), blackPlayer.getPlayerScore(), e);

            } else if (blackPlayer.getNumberOfWinningPieces() == 6) {
                System.out.println("BLACK WINS!");
                whitePlayer.updatePlayerScore(0 + (whitePlayer.getNumberOfMoves() * .01));
                blackPlayer.updatePlayerScore(1 - (blackPlayer.getNumberOfMoves() * .01));
                AlertBox.display("Congratulations!", "Black wins!", whitePlayer.getPlayerScore(), blackPlayer.getPlayerScore(), e);
            }
            System.out.println("White Player Score: " + whitePlayer.getPlayerScore());
            System.out.println("Black Player Score: " + blackPlayer.getPlayerScore());
        }
    }

    //new game button
    public void newGame(ActionEvent event) throws IOException {
        try {
            System.out.println("Human vs. Human");
            Parent newGame = FXMLLoader.load(getClass().getClassLoader().getResource("Checkers/Design/main.fxml"));
            Scene scene = new Scene(newGame);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setTitle("CheckersApp");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //forfeit game button
    public void forfeitGame(ActionEvent event) throws IOException {
        if (moveOrder = false) {
            System.out.println("WHITE WINS!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Congratulations!");
            alert.setHeaderText(null);
            alert.setContentText("White wins!");
            alert.showAndWait();
            whitePlayer.updatePlayerScore(1 - (whitePlayer.getNumberOfMoves() * .01));
            blackPlayer.updatePlayerScore(0 + (blackPlayer.getNumberOfMoves() * .01));

        } else {
            System.out.println("BLACK WINS!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Congratulations!");
            alert.setHeaderText(null);
            alert.setContentText("Black wins!");
            alert.showAndWait();
            whitePlayer.updatePlayerScore(0 + (whitePlayer.getNumberOfMoves() * .01));
            blackPlayer.updatePlayerScore(1 - (blackPlayer.getNumberOfMoves() * .01));

        }
    }

    //Edit mo to
    public void pass(ActionEvent event) {
        System.out.println("PASS");
        //Check kung wala ng forward
        if (moveOrder) {
            whitePlayer.addOneToMove();
            movesWhite.setText(Integer.toString(whitePlayer.getNumberOfMoves()));
            moveOrder = false;
            pieceTurn.setText("Black's turn!");
        } //Check kung black
        else if (!moveOrder) {
            blackPlayer.addOneToMove();
            movesBlack.setText(Integer.toString(blackPlayer.getNumberOfMoves()));
            moveOrder = true;
            pieceTurn.setText("White's turn!");
        }
        AIMove();
    }

    private static int toBoard(double pixel) {
        return (int) (pixel + tileSize / 2) / tileSize;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("White!");
        boardGroup.getChildren().addAll(createContent());
    }
}
