package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ChessGame.TeamColor color = piece.getTeamColor();
        Collection<ChessMove> moves = new ArrayList<>();
        bishopMovesUpRight(board, myPosition, color, moves);
        bishopMovesUpLeft(board, myPosition, color, moves);
        bishopMovesDownRight(board, myPosition, color, moves);
        bishopMovesDownLeft(board, myPosition, color, moves);
        return moves;
    }

    private void bishopMovesUpRight(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() + 1;
        int y = myPosition.getColumn() + 1;

        while (x <= 8 && y <= 8 && board.getPiece(new ChessPosition(x, y)) == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
            x++;
            y++;
        }

        if (x < 8 && y < 8) {
            ChessPosition endPosition = new ChessPosition(x, y);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece != null) {
                if (piece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
    }

    private void bishopMovesUpLeft(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() - 1;
        int y = myPosition.getColumn() + 1;

        while (x >= 1 && y <= 8 && board.getPiece(new ChessPosition(x, y)) == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
            x--;
            y++;
        }

        if (x > 1 && y < 8) {
            ChessPosition endPosition = new ChessPosition(x, y);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece != null) {
                if (piece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
    }

    private void bishopMovesDownRight(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() + 1;
        int y = myPosition.getColumn() - 1;

        while (x <= 8 && y >= 1 && board.getPiece(new ChessPosition(x, y)) == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
            x++;
            y--;
        }

        if (x < 8 && y > 1) {
            ChessPosition endPosition = new ChessPosition(x, y);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece != null) {
                if (piece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
    }

    private void bishopMovesDownLeft(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() - 1;
        int y = myPosition.getColumn() - 1;

        while (x >= 1 && y >= 1 && board.getPiece(new ChessPosition(x, y)) == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
            x--;
            y--;
        }

        if (x > 1 && y > 1) {
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
