package client;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameBoardDisplay {

    public void showBoard (ChessGame game, ChessGame.TeamColor color) {
        ChessBoard board = game.getBoard();
        if (color == ChessGame.TeamColor.WHITE || color == null) {
            whiteBoard(board);
        } else if (color == ChessGame.TeamColor.BLACK) {
            blackBoard(board);
        }
    }

    private void whiteBoard(ChessBoard board) {
        Collection<String> letterRow = new ArrayList<>(List.of(" ", "a", "b", "c", "d", "e", "f", "g", "h", " "));
        borderRow(letterRow);
        for (int i = 1; i <= 8; i++) {
            Collection<String> pieceRow = new ArrayList<>();
            pieceRow.add(Integer.toString(9 - i));
            for (int j = 1; j <= 8; j++) {
                pieceRow.add(pieceRepresentation(board.getPiece(new ChessPosition(i, j))));
            }
            pieceRow.add(Integer.toString(9 - i));
            if (i % 2 == 0) {
                blackStartRow(pieceRow);
            } else {
                whiteStartRow(pieceRow);
            }
        }
        borderRow(letterRow);
    }

    private void blackBoard(ChessBoard board) {
        Collection<String> letterRow = new ArrayList<>(List.of(" ", "h", "g", "f", "e", "d", "c", "b", "a", " "));
        borderRow(letterRow);
        for (int i = 1; i <= 8; i++) {
            Collection<String> pieceRow = new ArrayList<>();
            pieceRow.add(Integer.toString(i));
            for (int j = 1; j <= 8; j++) {
                pieceRow.add(pieceRepresentation(board.getPiece(new ChessPosition(9 - i, 9 - j))));
            }
            pieceRow.add(Integer.toString(1));
            if (i % 2 == 0) {
                blackStartRow(pieceRow);
            } else {
                whiteStartRow(pieceRow);
            }
        }
        borderRow(letterRow);
    }

    private void borderRow(Collection<String> row) {
        for (String item : row) {
            System.out.print(item);
        }
        System.out.print("\n");
    }

    private void whiteStartRow(Collection<String> row) {
        for (String item : row) {
            System.out.print(item);
        }
        System.out.print("\n");
    }

    private void blackStartRow(Collection<String> row) {
        for (String item : row) {
            System.out.print(item);
        }
        System.out.print("\n");
    }

    private String pieceRepresentation(ChessPiece piece) {
        if (piece == null) return " ";
        ChessPiece.PieceType pieceType = piece.getPieceType();
        if (pieceType.equals(ChessPiece.PieceType.KING)) {
            return "K";
        } else if (pieceType.equals(ChessPiece.PieceType.QUEEN)) {
            return "Q";
        } else if (pieceType.equals(ChessPiece.PieceType.BISHOP)) {
            return "B";
        } else if (pieceType.equals(ChessPiece.PieceType.KNIGHT)) {
            return "N";
        } else if (pieceType.equals(ChessPiece.PieceType.ROOK)) {
            return "R";
        } else if (pieceType.equals(ChessPiece.PieceType.PAWN)) {
            return "P";
        } else {
            return " ";
        }
    }
}
