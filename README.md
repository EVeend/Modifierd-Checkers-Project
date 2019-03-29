# Modifierd-Checkers-Project

## Mechanics
Given:
  A 4x4 board initially has 9 white pieces on upper left cells and 9 black pieces on bottom right
  cells (leftmost diagram below). Columns are labeled A to D from the left while rows are labeled
  1 to 4 from the top. One player plays white while another plays black. 
  
  ![Board Image](https://github.com/EVeend/Modifierd-Checkers-Project/blob/master/CheckersBoardImage.png)

Objective: Be the first player to move all of own pieces to opposite corner. Middle diagram shows
white pieces when white wins. Rightmost diagram shows black pieces when black wins. 

Rules:

1. Players alternate turns moving one piece at a time. White takes the first turn.
2. A move is identified by a pair of cell coordinates: “(source cell, destination cell)”. E.g. (C2,B2).
3. Pieces may move up, down, left, or right only. **Diagonal and other moves are not allowed.**
4. Forward moves are right and down for white and left and up for black.
5. Backward moves are right and down for black and left and up for white.
6. A piece may be moved to an adjacent cell only if it is not occupied.
7. A hop moves a piece over one or two pieces (of either color) in the same direction to the
nearest unoccupied cell. Hopping over pieces is usually not allowed.
8. **Only when a player has no forward move may the player hop, swap two adjacent (up, down,
left, or right) pieces of different colors, or pass.**
9. The game must count each move (including passes) made by each player. 

## Alpha Beta Pruning Algorithm

Alpha Beta Pruning is an optimized version of the Minimax algorithm where it cuts of branches in the tree that is not needed to be search because there are already better branches available. This algorithm takes in two extra parameters which are Alpha and Beta. 

**Alpha** - the maximum value or the best highest value.
**Beta** - the minimum value or the best lowest value. 

### Evaluation function for the Modified Checkers game

The evaluation function that I used for this game is **f(n) = h(n) + g(n)** where:
- h(n) - number of winning pieces or the number of allied pieces that is in the opponents side.
- g(n) - score of a move, the score being as follows:
  - Forward Move = 2
  - Backward Move = 0
  - Hope Move = 1
  - Double Hop Move = 1
  - Swap Move = 1
