package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ChessGame.TeamColor color = piece.getTeamColor();
        Collection<ChessMove> moves = new ArrayList<>();
        rookMovesUp(board, myPosition, color, moves);
        rookMovesDown(board, myPosition, color, moves);
        rookMovesRight(board, myPosition, color, moves);
        rookMovesLeft(board, myPosition, color, moves);
        return moves;
    }

    private void rookMovesUp(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow();
        int y = myPosition.getColumn() + 1;

        while (y <= 8 && board.getPiece(new ChessPosition(x, y)) == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
            y++;
        }

        if (y <= 8) {
            ChessPosition endPosition = new ChessPosition(x, y);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece != null) {
                if (piece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
    }

    private void rookMovesDown(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow();
        int y = myPosition.getColumn() - 1;

        while (y >= 1 && board.getPiece(new ChessPosition(x, y)) == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
            y--;
        }

        if (y >= 1) {
            ChessPosition endPosition = new ChessPosition(x, y);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece != null) {
                if (piece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
    }

    private void rookMovesRight(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() + 1;
        int y = myPosition.getColumn();

        while (x <= 8 && board.getPiece(new ChessPosition(x, y)) == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
            x++;
        }

        if (x <= 8) {
            ChessPosition endPosition = new ChessPosition(x, y);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece != null) {
                if (piece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
    }

    private void rookMovesLeft(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() - 1;
        int y = myPosition.getColumn();

        while (x >= 1 && board.getPiece(new ChessPosition(x, y)) == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
            x--;
        }

        if (x >= 1) {
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
