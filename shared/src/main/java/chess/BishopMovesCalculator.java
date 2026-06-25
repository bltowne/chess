package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
//        return List.of(new ChessMove(new ChessPosition(5,4), new ChessPosition(1, 7), null));
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
        ChessPosition endPosition = new ChessPosition(x-1, y-1);
        ChessPiece piece = board.getPiece(endPosition);
        if (piece != null) {
            if (piece.getTeamColor() != color) {
                moves.add(new ChessMove(myPosition, endPosition, null));
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
        ChessPosition endPosition = new ChessPosition(x+1, y-1);
        ChessPiece piece = board.getPiece(endPosition);
        if (piece != null) {
            if (piece.getTeamColor() != color) {
                moves.add(new ChessMove(myPosition, endPosition, null));
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
        ChessPosition endPosition = new ChessPosition(x-1, y+1);
        ChessPiece piece = board.getPiece(endPosition);
        if (piece != null) {
            if (piece.getTeamColor() != color) {
                moves.add(new ChessMove(myPosition, endPosition, null));
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
        ChessPosition endPosition = new ChessPosition(x+1, y+1);
        ChessPiece piece = board.getPiece(endPosition);
        if (piece != null) {
            if (piece.getTeamColor() != color) {
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
    }

//    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, ChessPosition originalPosition, Collection<ChessMove> moves) {
//        ChessPiece piece = board.getPiece(position);
//        if (piece != null && position != originalPosition) {
//            if (piece.getTeamColor() != color) {
//                moves.add(new ChessMove(originalPosition, position, null));
//            }
//            return moves;
//        }
//
//        moves.add(new ChessMove(originalPosition, position, null));
//        int x = position.getRow();
//        int y = position.getColumn();
//
//        // Check move up one right one
//        if (x < 8 && y < 8) {
//            bishopMoves(board, new ChessPosition(x + 1, y + 1), color, originalPosition, moves);
//        }
//
//        // Check move up one left one
//        if (x > 1 && y < 8) {
//            bishopMoves(board, new ChessPosition(x - 1, y + 1), color, originalPosition, moves);
//        }
//
//        // Check move down one right one
//        if (x < 8 && y > 1) {
//            bishopMoves(board, new ChessPosition(x + 1, y - 1), color, originalPosition, moves);
//        }
//
//        // Check move down one left one
//        if (x > 1 && y > 1) {
//            bishopMoves(board, new ChessPosition(x - 1, y - 1), color, originalPosition, moves);
//        }
//
//        return moves;
//    }

}
