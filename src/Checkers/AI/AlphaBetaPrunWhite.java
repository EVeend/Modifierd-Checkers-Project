/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Checkers.AI;

import Checkers.Board.Piece;
import Checkers.Controller.BoardAIWhiteController;
import Checkers.Player.Player;
import Checkers.Player.PlayerMove;
import Checkers.Player.PossibleMove;
import java.util.ArrayList;

/**
 *
 * @author edwar
 */
public class AlphaBetaPrunWhite {

    // Initial values of Aplha and Beta
    static int MAX = 1;
    static int MIN = -1;

    public static PlayerMove startAlphaBet(int depth, PossibleMove move, ArrayList<Player> listOfPlayers, int player) {
        PlayerMove newMove = alphaBeta(depth, MAX, MIN, move, listOfPlayers, player);
        System.out.println(newMove.toString());
        System.out.println("Start alpha bet newMoveX: " + newMove.getMove().getNewXPost() + " " + newMove.getMove().getNewYPost());
        return newMove;
    }

    public static PlayerMove alphaBeta(int depth, int alpha, int beta, PossibleMove move, ArrayList<Player> listOfPlayers, int player) {
        //Say that AI is listOfPlayers[1]
        PlayerMove newMove;
        PlayerMove bestMove = null;
        System.out.println("Player: " + player);
        Player currentPlayer = listOfPlayers.get(player);
        ArrayList<PossibleMove> possibleMoveList = BoardAIWhiteController.getPossibleMoves(currentPlayer);
        int bestValue;

        if (isMaxDepth(depth)) {
            Piece newPiece = move.getPiece();
            System.out.println("New moveX: " + move.getNewXPost() + " New move Y: " + move.getNewYPost());
            int newXPost = move.getNewXPost();
            int newYPost = move.getNewYPost();
            System.out.println("NewXPost: " + newXPost + "NewYPost " + newYPost);
            System.out.println("OldXPost: " + move.getPiece().getOldX() + " OldYPost: " + move.getPiece().getOldY());
//            newPiece.setLayoutX(newXPost * 100);
//            newPiece.setLayoutY(newYPost * 100);
            System.out.println(newPiece.getLayoutX());
            System.out.println(newPiece.getLayoutY());
            int fitnessValue = BoardAIWhiteController.evaluatePiece(currentPlayer, newPiece, move.getNewXPost(), move.getNewYPost());
            System.out.println("Fitness value of move is: " + fitnessValue);
            System.out.println(move.getNewXPost() + " " + move.getNewYPost());
            newMove = new PlayerMove(currentPlayer, move, fitnessValue);
//            System.out.println(newMove.toString());
            return newMove;
        }

        for (int i = 0; i < possibleMoveList.size() - 1; i++) {
            //Maximizing player
            if (player == 1) {
                //Initialize current best move
                bestMove = new PlayerMove(currentPlayer, move, MIN);

                System.out.println("Who is the player: " + currentPlayer);
                newMove = alphaBeta(depth - 1, alpha, beta, possibleMoveList.get(i), listOfPlayers, Math.abs(player - 1));
                bestMove.setFitnessValue(Math.max(bestMove.getFitnessValue(), newMove.getFitnessValue()));
                System.out.println("Current Alpha: " + alpha + " Current BestMove: " + bestMove.getFitnessValue() + " Current Beta: " + beta);
               
                alpha = Math.max(alpha, bestMove.getFitnessValue());
                
                //Pruning
                if (beta <= alpha) {
                    System.out.println("Prunned");
                    break;
                }
            } //Minimizing player
            else {
                //Initialize current best move
                bestMove = new PlayerMove(currentPlayer, move, MAX);
                System.out.println("Who is the player: " + currentPlayer);
                newMove = alphaBeta(depth - 1, alpha, beta, possibleMoveList.get(i), listOfPlayers, Math.abs(player + 1));
                bestMove.setFitnessValue(Math.max(bestMove.getFitnessValue(), newMove.getFitnessValue()));
                beta = Math.max(alpha, bestMove.getFitnessValue());

                //Pruning
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return bestMove;

    }

    public static int computeRating() {
        return 0;
    }

    public static boolean isMaxDepth(int depth) {
        if (depth == 0) {
            return true;
        } else {
            return false;
        }
    }

}
