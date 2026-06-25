package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ChessGame.TeamColor color = piece.getTeamColor();
        Collection<ChessMove> moves = new ArrayList<>();
        kingMovesUpRight(board, myPosition, color, moves);
        kingMovesLeftUp(board, myPosition, color, moves);
        kingMovesRightDown(board, myPosition, color, moves);
        kingMovesDownLeft(board, myPosition, color, moves);
        return moves;
    }

    // y++; y++ x++
    private void kingMovesUpRight(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow();
        int y = myPosition.getColumn() + 1;

        if (y <= 8) {
            ChessPosition frontEndPosition = new ChessPosition(x, y);
            ChessPiece frontEnemy = board.getPiece(frontEndPosition);
            if (frontEnemy == null || frontEnemy.getTeamColor() != color) {
                moves.add(new ChessMove(myPosition, frontEndPosition, null));
            }
            x++;
            if (x <= 8) {
                ChessPosition rightEndPosition = new ChessPosition(x, y);
                ChessPiece rightEnemy = board.getPiece(rightEndPosition);
                if (rightEnemy == null || rightEnemy.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, rightEndPosition, null));
                }
            }
        }
    }

    // x--; x-- y++
    private void kingMovesLeftUp(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() - 1;
        int y = myPosition.getColumn();

        if (x >= 1) {
            ChessPosition leftEndPosition = new ChessPosition(x, y);
            ChessPiece leftEnemy = board.getPiece(leftEndPosition);
            if (leftEnemy == null || leftEnemy.getTeamColor() != color) {
                moves.add(new ChessMove(myPosition, leftEndPosition, null));
            }
            y++;
            if (y <= 8) {
                ChessPosition frontEndPosition = new ChessPosition(x, y);
                ChessPiece frontEnemy = board.getPiece(frontEndPosition);
                if (frontEnemy == null || frontEnemy.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, frontEndPosition, null));
                }
            }
        }
    }

    // x++; x++ y--
    private void kingMovesRightDown(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() + 1;
        int y = myPosition.getColumn();

        if (x <= 8) {
            ChessPosition rightEndPosition = new ChessPosition(x, y);
            ChessPiece rightEnemy = board.getPiece(rightEndPosition);
            if (rightEnemy == null || rightEnemy.getTeamColor() != color) {
                moves.add(new ChessMove(myPosition, rightEndPosition, null));
            }
            y--;
            if (y >= 1) {
                ChessPosition backEndPosition = new ChessPosition(x, y);
                ChessPiece backEnemy = board.getPiece(backEndPosition);
                if (backEnemy == null || backEnemy.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, backEndPosition, null));
                }
            }
        }
    }

    // y--; y-- x--
    private void kingMovesDownLeft(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow();
        int y = myPosition.getColumn() - 1;

        if (y >= 1) {
            ChessPosition backEndPosition = new ChessPosition(x, y);
            ChessPiece backEnemy = board.getPiece(backEndPosition);
            if (backEnemy == null || backEnemy.getTeamColor() != color) {
                moves.add(new ChessMove(myPosition, backEndPosition, null));
            }
            x--;
            if (x >= 1) {
                ChessPosition leftEndPosition = new ChessPosition(x, y);
                ChessPiece leftEnemy = board.getPiece(leftEndPosition);
                if (leftEnemy == null || leftEnemy.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, leftEndPosition, null));
                }
            }
        }
    }

}
