package dk.easv.bll.bot;
import dk.easv.bll.field.IField;
import dk.easv.bll.game.IGameState;
import dk.easv.bll.move.IMove;

import java.util.List;


public class UltimateBot implements IBot  {
    private static final String BOTNAME = "UltimateBot";
    private static final int SIZE = 3;
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
            int score = minimax(board, false, 0);
            board[move.getX()][move.getY()] = ' '; // Undo move

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    /**
     * Minimax algorithm to evaluate the best move.
     */
    private int minimax(char[][] board, boolean isMaximizing, int depth) {
        if (depth >= MAX_DEPTH) return 0; // Limit recursion depth

        Integer result = checkWinner(board);
        if (result != null) return result;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < SIZE * SIZE; i++) {
                int x = i / SIZE;
                int y = i % SIZE;
                if (board[x][y] == ' ') {
                    board[x][y] = 'O';
                    bestScore = Math.max(bestScore, minimax(board, false, depth + 1));
                    board[x][y] = ' ';
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < SIZE * SIZE; i++) {
                int x = i / SIZE;
                int y = i % SIZE;
                if (board[x][y] == ' ') {
                    board[x][y] = 'X';
                    bestScore = Math.min(bestScore, minimax(board, true, depth + 1));
                    board[x][y] = ' ';
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
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2])
                return (board[i][0] == 'O') ? 1 : -1;
            if (board[0][i] != ' ' && board[0][i] == board[1][i] && board[1][i] == board[2][i])
                return (board[0][i] == 'O') ? 1 : -1;
        }
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2])
            return (board[0][0] == 'O') ? 1 : -1;
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0])
            return (board[0][2] == 'O') ? 1 : -1;

        return null;
    }

    @Override
    public String getBotName() {
        return BOTNAME;
    }

}