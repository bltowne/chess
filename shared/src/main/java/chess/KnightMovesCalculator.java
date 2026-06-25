package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
            ChessPosition leftEndPosition = new ChessPosition(x - 1, y);
            if (x >= 1) {
                ChessPiece leftEnemy = board.getPiece(leftEndPosition);
                if (leftEnemy == null || leftEnemy.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, leftEndPosition, null));
                }
            }
            ChessPosition rightEndPosition = new ChessPosition(x + 1, y);
            if (x <= 8) {
                ChessPiece rightEnemy = board.getPiece(leftEndPosition);
                if (rightEnemy == null || rightEnemy.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, rightEndPosition, null));
                }
            }
        }
    }

    private void knightBackward(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow();
        int y = myPosition.getColumn() - 2;

        if (y >= 1) {
            ChessPosition leftEndPosition = new ChessPosition(x - 1, y);
            if (x >= 1) {
                ChessPiece leftEnemy = board.getPiece(leftEndPosition);
                if (leftEnemy == null || leftEnemy.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, leftEndPosition, null));
                }
            }
            ChessPosition rightEndPosition = new ChessPosition(x + 1, y);
            if (x <= 8) {
                ChessPiece rightEnemy = board.getPiece(leftEndPosition);
                if (rightEnemy == null || rightEnemy.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, rightEndPosition, null));
                }
            }
        }
    }

    private void knightLeft(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() + 2;
        int y = myPosition.getColumn();

        if (x <= 8) {
            ChessPosition leftEndPosition = new ChessPosition(x, y - 1);
            if (y >= 1) {
                ChessPiece leftEnemy = board.getPiece(leftEndPosition);
                if (leftEnemy == null || leftEnemy.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, leftEndPosition, null));
                }
            }
            ChessPosition rightEndPosition = new ChessPosition(x, y + 1);
            if (y <= 8) {
                ChessPiece rightEnemy = board.getPiece(leftEndPosition);
                if (rightEnemy == null || rightEnemy.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, rightEndPosition, null));
                }
            }
        }
    }

    private void knightRight(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() - 2;
        int y = myPosition.getColumn();

        if (x >= 1) {
            ChessPosition leftEndPosition = new ChessPosition(x, y - 1);
            if (y >= 1) {
                ChessPiece leftEnemy = board.getPiece(leftEndPosition);
                if (leftEnemy == null || leftEnemy.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, leftEndPosition, null));
                }
            }
            ChessPosition rightEndPosition = new ChessPosition(x, y + 1);
            if (y <= 8) {
                ChessPiece rightEnemy = board.getPiece(leftEndPosition);
                if (rightEnemy == null || rightEnemy.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, rightEndPosition, null));
                }
            }
        }
    }

}
