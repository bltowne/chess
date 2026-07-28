package client;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;

public class GameBoardDisplay {

    public void showBoard (ChessGame game, ChessGame.TeamColor color) {
        ChessBoard board = game.getBoard();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(SET_TEXT_BOLD);
        if (color == ChessGame.TeamColor.WHITE || color == null) {
            whiteBoard(board, out);
        } else if (color == ChessGame.TeamColor.BLACK) {
            blackBoard(board, out);
        }
        defaultColors(out);
        out.print(RESET_TEXT_BOLD_FAINT);
    }

    private void whiteBoard(ChessBoard board, PrintStream out) {
        List<String> letterRow = new ArrayList<>(
                List.of("   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "));
        borderRow(letterRow, out);
        for (int i = 1; i <= 8; i++) {
            borderColors(out, " " + (9 - i) + " ");
            List<ChessPiece> pieceRow = new ArrayList<>();
            for (int j = 1; j <= 8; j++) {
                pieceRow.add(board.getPiece(new ChessPosition(i, j)));
            }
            if (i % 2 == 0) {
                whiteStartRow(pieceRow, out);
            } else {
                blackStartRow(pieceRow, out);
            }
            borderColors(out, " " + (9 - i) + " ");
            defaultColors(out);
            out.print("\n");
        }
        borderRow(letterRow, out);
    }

    private void blackBoard(ChessBoard board, PrintStream out) {
        List<String> letterRow = new ArrayList<>(
                List.of("   ", " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", "   "));
        borderRow(letterRow, out);
        for (int i = 1; i <= 8; i++) {
            borderColors(out, " " + i + " ");
            List<ChessPiece> pieceRow = new ArrayList<>();
            for (int j = 1; j <= 8; j++) {
                pieceRow.add(board.getPiece(new ChessPosition(9 - i, 9 - j)));
            }
            if (i % 2 == 0) {
                whiteStartRow(pieceRow, out);
            } else {
                blackStartRow(pieceRow, out);
            }
            borderColors(out, " " + i + " ");
            defaultColors(out);
            out.print("\n");
        }
        borderRow(letterRow, out);
    }

    private void borderRow(List<String> row, PrintStream out) {
        for (String item : row) {
            borderColors(out, item);
        }
        defaultColors(out);
        out.print("\n");
    }

    private void whiteStartRow(List<ChessPiece> row, PrintStream out) {
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                blackSquare(out, row.get(i));
            } else {
                whiteSquare(out, row.get(i));
            }
        }
    }

    private void blackStartRow(List<ChessPiece> row, PrintStream out) {
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                whiteSquare(out, row.get(i));
            } else {
                blackSquare(out, row.get(i));
            }
        }
    }

    private String pieceRepresentation(ChessPiece piece) {
        if (piece == null) return "   ";
        ChessPiece.PieceType pieceType = piece.getPieceType();
        if (pieceType.equals(ChessPiece.PieceType.KING)) {
            return " K ";
        } else if (pieceType.equals(ChessPiece.PieceType.QUEEN)) {
            return " Q ";
        } else if (pieceType.equals(ChessPiece.PieceType.BISHOP)) {
            return " B ";
        } else if (pieceType.equals(ChessPiece.PieceType.KNIGHT)) {
            return " N ";
        } else if (pieceType.equals(ChessPiece.PieceType.ROOK)) {
            return " R ";
        } else if (pieceType.equals(ChessPiece.PieceType.PAWN)) {
            return " P ";
        }
        return "   ";
    }

    private void defaultColors(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private void borderColors(PrintStream out, String item) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(item);
    }

    private void whiteSquare(PrintStream out, ChessPiece piece) {
        out.print(SET_BG_COLOR_WHITE);
        setTeamColor(out, piece);
        out.print(pieceRepresentation(piece));
    }

    private void blackSquare(PrintStream out, ChessPiece piece) {
        out.print(SET_BG_COLOR_BLUE);
        setTeamColor(out, piece);
        out.print(pieceRepresentation(piece));
    }

    private void setTeamColor(PrintStream out, ChessPiece piece) {
        if (piece != null && piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            out.print(SET_TEXT_COLOR_BLACK);
        } else if (piece != null && piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            out.print(SET_TEXT_COLOR_RED);
        }
    }
}
