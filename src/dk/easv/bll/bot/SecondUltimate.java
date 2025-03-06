package dk.easv.bll.bot;

import dk.easv.bll.field.IField;
import dk.easv.bll.game.IGameState;
import dk.easv.bll.move.IMove;

import java.util.List;

    public class SecondUltimate implements IBot {
        private static final String BOTNAME = "Second Ultimate";
        private static final int SIZE = 3; // Size of small boards
        private static final int MAX_DEPTH = 4; // Limits recursion depth

        @Override
        public IMove doMove(IGameState state) {
            List<IMove> availableMoves = state.getField().getAvailableMoves();
            if (availableMoves.isEmpty()) return null;

            int bestScore = Integer.MIN_VALUE;
            IMove bestMove = availableMoves.get(0);

            // Convert board state into a 2D character array
            char[][] board = convertBoard(state);

            for (IMove move : availableMoves) {
                board[move.getX()][move.getY()] = 'O'; // Bot move
                int score = minimax(board, false, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                board[move.getX()][move.getY()] = ' '; // Undo move

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }

            return bestMove;
        }

        /**
         * Minimax algorithm with Alpha-Beta pruning.
         */
        private int minimax(char[][] board, boolean isMaximizing, int depth, int alpha, int beta) {
            Integer result = checkWinner(board);
            if (result != null) return result * (10 - depth); // Favor faster wins

            if (depth >= MAX_DEPTH) return evaluateBoard(board); // Evaluate non-terminal states

            if (isMaximizing) {
                int bestScore = Integer.MIN_VALUE;
                for (int i = 0; i < SIZE * SIZE; i++) {
                    for (int j = 0; j < SIZE * SIZE; j++) {
                        if (board[i][j] == ' ') {
                            board[i][j] = 'O';
                            int score = minimax(board, false, depth + 1, alpha, beta);
                            board[i][j] = ' ';
                            bestScore = Math.max(bestScore, score);
                            alpha = Math.max(alpha, bestScore);
                            if (beta <= alpha) break; // Alpha-beta pruning
                        }
                    }
                }
                return bestScore;
            } else {
                int bestScore = Integer.MAX_VALUE;
                for (int i = 0; i < SIZE * SIZE; i++) {
                    for (int j = 0; j < SIZE * SIZE; j++) {
                        if (board[i][j] == ' ') {
                            board[i][j] = 'X';
                            int score = minimax(board, true, depth + 1, alpha, beta);
                            board[i][j] = ' ';
                            bestScore = Math.min(bestScore, score);
                            beta = Math.min(beta, bestScore);
                            if (beta <= alpha) break; // Alpha-beta pruning
                        }
                    }
                }
                return bestScore;
            }
        }

        /**
         * Converts IGameState board into a char[][] for evaluation.
         */
        private char[][] convertBoard(IGameState state) {
            char[][] board = new char[SIZE * SIZE][SIZE * SIZE];
            String[][] gameBoard = state.getField().getBoard();

            for (int i = 0; i < SIZE * SIZE; i++) {
                for (int j = 0; j < SIZE * SIZE; j++) {
                    board[i][j] = gameBoard[i][j].equals(IField.EMPTY_FIELD) ? ' ' : gameBoard[i][j].equals("0") ? 'X' : 'O';
                }
            }
            return board;
        }

        /**
         * Determines if there is a winner.
         */
        private Integer checkWinner(char[][] board) {
            // Check small 3x3 boards for wins
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    Integer smallBoardResult = checkSmallBoard(board, i * SIZE, j * SIZE);
                    if (smallBoardResult != null) return smallBoardResult;
                }
            }
            return null;
        }

        /**
         * Checks a small 3x3 board for a winner.
         */
        private Integer checkSmallBoard(char[][] board, int startX, int startY) {
            for (int i = 0; i < SIZE; i++) {
                if (board[startX + i][startY] != ' ' &&
                        board[startX + i][startY] == board[startX + i][startY + 1] &&
                        board[startX + i][startY + 1] == board[startX + i][startY + 2])
                    return (board[startX + i][startY] == 'O') ? 1 : -1;

                if (board[startX][startY + i] != ' ' &&
                        board[startX][startY + i] == board[startX + 1][startY + i] &&
                        board[startX + 1][startY + i] == board[startX + 2][startY + i])
                    return (board[startX][startY + i] == 'O') ? 1 : -1;
            }

            if (board[startX][startY] != ' ' &&
                    board[startX][startY] == board[startX + 1][startY + 1] &&
                    board[startX + 1][startY + 1] == board[startX + 2][startY + 2])
                return (board[startX][startY] == 'O') ? 1 : -1;

            if (board[startX][startY + 2] != ' ' &&
                    board[startX][startY + 2] == board[startX + 1][startY + 1] &&
                    board[startX + 1][startY + 1] == board[startX + 2][startY])
                return (board[startX][startY + 2] == 'O') ? 1 : -1;

            return null;
        }

        /**
         * Evaluates the board state (heuristic).
         */
        private int evaluateBoard(char[][] board) {
            int score = 0;
            for (int i = 0; i < SIZE * SIZE; i++) {
                for (int j = 0; j < SIZE * SIZE; j++) {
                    if (board[i][j] == 'O') score++;
                    else if (board[i][j] == 'X') score--;
                }
            }
            return score;
        }

        @Override
        public String getBotName() {
            return BOTNAME;
        }
    }
