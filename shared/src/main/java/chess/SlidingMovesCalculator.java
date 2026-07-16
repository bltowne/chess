package chess;

import java.util.ArrayList;
import java.util.Collection;

public class SlidingMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ChessGame.TeamColor color = piece.getTeamColor();
        Collection<ChessMove> moves = new ArrayList<>();
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK || piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            moveUp(board, myPosition, color, moves);
            moveDown(board, myPosition, color, moves);
            moveRight(board, myPosition, color, moves);
            moveLeft(board, myPosition, color, moves);
        }
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP || piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            moveUpRight(board, myPosition, color, moves);
            moveUpLeft(board, myPosition, color, moves);
            moveDownRight(board, myPosition, color, moves);
            moveDownLeft(board, myPosition, color, moves);
        }
        return moves;
    }

    private void moveUp(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow();
        int y = myPosition.getColumn() + 1;
        straightSlide(board, myPosition, color, moves, x, y, "y++");

    }

    private void moveDown(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow();
        int y = myPosition.getColumn() - 1;
        straightSlide(board, myPosition, color, moves, x, y, "y--");

    }

    private void moveRight(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() + 1;
        int y = myPosition.getColumn();
        straightSlide(board, myPosition, color, moves, x, y, "x++");

    }

    private void moveLeft(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() - 1;
        int y = myPosition.getColumn();
        straightSlide(board, myPosition, color, moves, x, y, "x--");
    }

    private static void straightSlide(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves,
                                      int x, int y, String change) {
        while (x >= 1 && x <= 8 && y >= 1 && y <= 8 && board.getPiece(new ChessPosition(x, y)) == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
            switch (change) {
                case "x++" -> x++;
                case "x--" -> x--;
                case "y++" -> y++;
                case "y--" -> y--;
            }
        }
        checkEndCases(board, myPosition, color, moves, x, y);
    }

    private void moveUpRight(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() + 1;
        int y = myPosition.getColumn() + 1;
        diagonalSlide(board, myPosition, color, moves, x, y, "positive", "positive");

    }

    private void moveUpLeft(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() - 1;
        int y = myPosition.getColumn() + 1;
        diagonalSlide(board, myPosition, color, moves, x, y, "negative", "positive");
    }

    private void moveDownRight(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() + 1;
        int y = myPosition.getColumn() - 1;
        diagonalSlide(board, myPosition, color, moves, x, y, "positive", "negative");
    }

    private void moveDownLeft(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() - 1;
        int y = myPosition.getColumn() - 1;
        diagonalSlide(board, myPosition, color, moves, x, y, "negative", "negative");
    }

    private static void diagonalSlide(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves,
                                      int x, int y, String xChange, String yChange) {
        while (x >= 1 && x <= 8 && y >= 1 && y <= 8 && board.getPiece(new ChessPosition(x, y)) == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
            if (xChange.equals("positive")) {
                x++;
            } else if (xChange.equals("negative")) {
                x--;
            }
            if (yChange.equals("positive")) {
                y++;
            } else if (yChange.equals("negative")) {
                y--;
            }
        }
        checkEndCases(board, myPosition, color, moves, x, y);
    }

    private static void checkEndCases(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves,
                                      int x, int y) {
        if (x >= 1 && x <= 8 && y >= 1 && y <= 8) {
            ChessPosition endPosition = new ChessPosition(x, y);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece != null) {
                if (piece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
    }

}
