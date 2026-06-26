package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ChessGame.TeamColor color = piece.getTeamColor();
        Collection<ChessMove> moves = new ArrayList<>();
        if (color == ChessGame.TeamColor.WHITE) {
            whiteCheckForEnemies(board, myPosition, color, moves);
            whiteMoveForward(board, myPosition, moves);
        } else if (color == ChessGame.TeamColor.BLACK) {
            blackCheckForEnemies(board, myPosition, color, moves);
            blackMoveForward(board, myPosition, moves);
        }
        return moves;
    }

    private void whiteCheckForEnemies(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() + 1;
        int y = myPosition.getColumn();

        int lowY = y - 1;
        int highY = y + 1;

        if (lowY >= 1) {
            ChessPosition leftEndPosition = new ChessPosition(x, lowY);
            ChessPiece leftEnemy = board.getPiece(leftEndPosition);
            if (leftEnemy != null && leftEnemy.getTeamColor() != color) {
                promotionCheck(moves, myPosition, leftEndPosition, color);
            }
        }

        if (highY <= 8) {
            ChessPosition rightEndPosition = new ChessPosition(x, highY);
            ChessPiece rightEnemy = board.getPiece(rightEndPosition);
            if (rightEnemy != null && rightEnemy.getTeamColor() != color) {
                promotionCheck(moves, myPosition, rightEndPosition, color);
            }
        }
    }

    private void blackCheckForEnemies(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() - 1;
        int y = myPosition.getColumn();

        int lowY = y - 1;
        int highY = y + 1;

        if (lowY >= 1) {
            ChessPosition leftEndPosition = new ChessPosition(x, lowY);
            ChessPiece leftEnemy = board.getPiece(leftEndPosition);
            if (leftEnemy != null && leftEnemy.getTeamColor() != color) {
                promotionCheck(moves, myPosition, leftEndPosition, color);
            }
        }

        if (highY <= 8) {
            ChessPosition rightEndPosition = new ChessPosition(x, highY);
            ChessPiece rightEnemy = board.getPiece(rightEndPosition);
            if (rightEnemy != null && rightEnemy.getTeamColor() != color) {
                promotionCheck(moves, myPosition, rightEndPosition, color);
            }
        }
    }

    private void whiteMoveForward(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {
        int x = myPosition.getRow();
        int y = myPosition.getColumn();

        if (x == 2) {
            int singleX = x + 1;
            int doubleX = x + 2;
            ChessPosition oneForward = new ChessPosition(singleX, y);
            ChessPosition twoForward = new ChessPosition(doubleX, y);
            if (board.getPiece(oneForward) == null && board.getPiece(twoForward) == null) {
                promotionCheck(moves, myPosition, twoForward, ChessGame.TeamColor.WHITE);
            }
        }

        x++;

        if (x <= 8) {
            ChessPosition newPosition = new ChessPosition(x, y);
            if (board.getPiece(newPosition) == null) {
                promotionCheck(moves, myPosition, newPosition, ChessGame.TeamColor.WHITE);
            }
        }
    }

    private void blackMoveForward(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {
        int x = myPosition.getRow();
        int y = myPosition.getColumn();

        if (x == 7) {
            int singleX = x - 1;
            int doubleX = x - 2;
            ChessPosition oneBackward = new ChessPosition(singleX, y);
            ChessPosition twoBackward = new ChessPosition(doubleX, y);
            if (board.getPiece(oneBackward) == null && board.getPiece(twoBackward) == null) {
                promotionCheck(moves, myPosition, twoBackward, ChessGame.TeamColor.BLACK);
            }
        }

        x--;

        if (x >= 1) {
            ChessPosition newPosition = new ChessPosition(x, y);
            if (board.getPiece(newPosition) == null) {
                promotionCheck(moves, myPosition, newPosition, ChessGame.TeamColor.BLACK);
            }
        }
    }

    private void promotionCheck(Collection<ChessMove> moves, ChessPosition myPosition, ChessPosition endPosition, ChessGame.TeamColor color) {
        int y = endPosition.getRow();
        if ((y == 8 && color == ChessGame.TeamColor.WHITE) || (y == 1 && color == ChessGame.TeamColor.BLACK)) {
            moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.ROOK));
        } else {
            moves.add(new ChessMove(myPosition, endPosition, null));
        }

    }
}
