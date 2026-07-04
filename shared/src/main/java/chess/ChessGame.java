package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard board;
    ChessBoard prevBoard;
    TeamColor color;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        color = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return color;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        color = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && Objects.equals(prevBoard, chessGame.prevBoard) && color == chessGame.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, prevBoard, color);
    }

    private ChessBoard copyBoard(ChessBoard board) {
        ChessBoard newBoard = new ChessBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                newBoard.addPiece(position, board.getPiece(position));
            }
        }
        return newBoard;
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        return compileMoves(startPosition, board);
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        moving(move, board);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return checkKing(teamColor, board);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return checkKingCheckmate(teamColor, board);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return checkKingStalemate(teamColor, board);
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    private ChessPosition findKing(TeamColor teamColor, ChessBoard testBoard) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPosition = new ChessPosition(i, j);
                ChessPiece testPiece = testBoard.getPiece(testPosition);
                if (testPiece != null && testPiece.getPieceType() == ChessPiece.PieceType.KING && testPiece.getTeamColor() == teamColor) {
                    return testPosition;
                }
            }
        }
        return null;
    }

    private Collection<ChessMove> compileMoves(ChessPosition position, ChessBoard testBoard) {
        ChessPiece piece = testBoard.getPiece(position);
        if (piece == null) {
            return null;
        }
        TeamColor color = piece.getTeamColor();
        Collection<ChessMove> moves = piece.pieceMoves(testBoard, position);
        Collection<ChessMove> removeMoves = new ArrayList<>();
        for (ChessMove move : moves) {
            ChessBoard tempBoard = copyBoard(testBoard);
            moving(move, tempBoard);
            if (checkKing(color, tempBoard)) {
                removeMoves.add(move);
            }
        }
        for (ChessMove move : removeMoves) {
            moves.remove(move);
        }
        return moves;
    }

    private boolean checkKing(TeamColor teamColor, ChessBoard testBoard) {
        ChessPosition kingPosition = findKing(teamColor, testBoard);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPosition = new ChessPosition(i, j);
                ChessPiece testPiece = testBoard.getPiece(testPosition);
                if (testPiece != null && testPiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = testPiece.pieceMoves(testBoard, testPosition);
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean checkKingCheckmate(TeamColor teamColor, ChessBoard testBoard) {
        if (checkKing(teamColor, testBoard)) {
            return allMovesMeanCheck(teamColor, testBoard);
        }
        return false;
    }

    private boolean checkKingStalemate(TeamColor teamColor, ChessBoard testBoard) {
        if (!checkKing(teamColor, testBoard)) {
            return allMovesMeanCheck(teamColor, testBoard);
        }
        return false;
    }

    private boolean allMovesMeanCheck(TeamColor teamColor, ChessBoard testBoard) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition testPosition = new ChessPosition (i, j);
                ChessPiece testPiece = testBoard.getPiece(testPosition);
                if (testPiece != null && testPiece.getTeamColor() == teamColor) {
                    if (!compileMoves(testPosition, testBoard).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void moving(ChessMove move, ChessBoard board) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = board.getPiece(startPosition);
        board.addPiece(startPosition, null);
        board.addPiece(endPosition, piece);
    }
}
