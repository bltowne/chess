package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ChessGame.TeamColor color = piece.getTeamColor();
        Collection<ChessMove> moves = new ArrayList<>();
        knightForward(board, myPosition, color, moves);
        knightBackward(board, myPosition, color, moves);
        knightLeft(board, myPosition, color, moves);
        knightRight(board, myPosition, color, moves);
        return moves;
    }

    private void knightForward(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow();
        int y = myPosition.getColumn() + 2;

        if (y <= 8) {
            checkX(board, myPosition, color, moves, x, y);
        }
    }

    private void knightBackward(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow();
        int y = myPosition.getColumn() - 2;

        if (y >= 1) {
            checkX(board, myPosition, color, moves, x, y);
        }
    }

    private static void checkX(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves, int x, int y) {
        int lowX = x - 1;
        if (lowX >= 1) {
            ChessPosition leftEndPosition = new ChessPosition(lowX, y);
            ChessPiece leftEnemy = board.getPiece(leftEndPosition);
            if (leftEnemy == null || leftEnemy.getTeamColor() != color) {
                moves.add(new ChessMove(myPosition, leftEndPosition, null));
            }
        }
        int highX = x + 1;
        if (highX <= 8) {
            ChessPosition rightEndPosition = new ChessPosition(highX, y);
            ChessPiece rightEnemy = board.getPiece(rightEndPosition);
            if (rightEnemy == null || rightEnemy.getTeamColor() != color) {
                moves.add(new ChessMove(myPosition, rightEndPosition, null));
            }
        }
    }

    private void knightLeft(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() + 2;
        int y = myPosition.getColumn();

        if (x <= 8) {
            checkY(board, myPosition, color, moves, y, x);
        }
    }

    private void knightRight(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() - 2;
        int y = myPosition.getColumn();

        if (x >= 1) {
            checkY(board, myPosition, color, moves, y, x);
        }
    }

    private static void checkY(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves, int y, int x) {
        int lowY = y - 1;
        if (lowY >= 1) {
            ChessPosition leftEndPosition = new ChessPosition(x, lowY);
            ChessPiece leftEnemy = board.getPiece(leftEndPosition);
            if (leftEnemy == null || leftEnemy.getTeamColor() != color) {
                moves.add(new ChessMove(myPosition, leftEndPosition, null));
            }
        }
        int highY = y + 1;
        if (highY <= 8) {
            ChessPosition rightEndPosition = new ChessPosition(x, highY);
            ChessPiece rightEnemy = board.getPiece(rightEndPosition);
            if (rightEnemy == null || rightEnemy.getTeamColor() != color) {
                moves.add(new ChessMove(myPosition, rightEndPosition, null));
            }
        }
    }
}
