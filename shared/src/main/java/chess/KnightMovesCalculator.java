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
            checkSides(board, myPosition, color, moves, x, y, "y");
        }
    }

    private void knightBackward(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow();
        int y = myPosition.getColumn() - 2;

        if (y >= 1) {
            checkSides(board, myPosition, color, moves, x, y, "y");
        }
    }



    private void knightLeft(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() + 2;
        int y = myPosition.getColumn();

        if (x <= 8) {
            checkSides(board, myPosition, color, moves, y, x, "x");
        }
    }

    private void knightRight(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() - 2;
        int y = myPosition.getColumn();

        if (x >= 1) {
            checkSides(board, myPosition, color, moves, y, x, "x");
        }
    }

    private static void checkSides(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves,
                                   int side, int forward, String constant) {
        int leftSide = side - 1;
        if (leftSide >= 1) {
            ChessPosition leftEndPosition;
            if (constant.equals("x")) {
                leftEndPosition = new ChessPosition(forward, leftSide);
            } else {
                leftEndPosition = new ChessPosition(leftSide, forward);
            }
            ChessPiece leftEnemy = board.getPiece(leftEndPosition);
            if (leftEnemy == null || leftEnemy.getTeamColor() != color) {
                moves.add(new ChessMove(myPosition, leftEndPosition, null));
            }
        }
        int rightSide = side + 1;
        if (rightSide <= 8) {
            ChessPosition rightEndPosition;
            if (constant.equals("x")) {
                rightEndPosition = new ChessPosition(forward, rightSide);
            } else {
                rightEndPosition = new ChessPosition(rightSide, forward);
            }
            ChessPiece rightEnemy = board.getPiece(rightEndPosition);
            if (rightEnemy == null || rightEnemy.getTeamColor() != color) {
                moves.add(new ChessMove(myPosition, rightEndPosition, null));
            }
        }
    }
}
