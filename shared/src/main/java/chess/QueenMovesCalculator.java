package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ChessGame.TeamColor color = piece.getTeamColor();
        Collection<ChessMove> moves = new ArrayList<>();
        queenMovesUp(board, myPosition, color, moves);
        queenMovesDown(board, myPosition, color, moves);
        queenMovesRight(board, myPosition, color, moves);
        queenMovesLeft(board, myPosition, color, moves);
        queenMovesUpRight(board, myPosition, color, moves);
        queenMovesUpLeft(board, myPosition, color, moves);
        queenMovesDownRight(board, myPosition, color, moves);
        queenMovesDownLeft(board, myPosition, color, moves);
        return moves;
    }

    private void queenMovesUp(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow();
        int y = myPosition.getColumn() + 1;

        while (y <= 8 && board.getPiece(new ChessPosition(x, y)) == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
            y++;
        }

        if (y < 8) {
            ChessPosition endPosition = new ChessPosition(x, y);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece != null) {
                if (piece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
    }

    private void queenMovesDown(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow();
        int y = myPosition.getColumn() - 1;

        while (y >= 1 && board.getPiece(new ChessPosition(x, y)) == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
            y--;
        }

        if (y > 1) {
            ChessPosition endPosition = new ChessPosition(x, y);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece != null) {
                if (piece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
    }

    private void queenMovesRight(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() + 1;
        int y = myPosition.getColumn();

        while (x <= 8 && board.getPiece(new ChessPosition(x, y)) == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
            x++;
        }

        if (x < 8) {
            ChessPosition endPosition = new ChessPosition(x, y);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece != null) {
                if (piece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
    }

    private void queenMovesLeft(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() - 1;
        int y = myPosition.getColumn();

        while (x >= 1 && board.getPiece(new ChessPosition(x, y)) == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
            x--;
        }

        if (x > 1) {
            ChessPosition endPosition = new ChessPosition(x, y);
            ChessPiece piece = board.getPiece(endPosition);
            if (piece != null) {
                if (piece.getTeamColor() != color) {
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
    }

    private void queenMovesUpRight(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
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

    private void queenMovesUpLeft(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
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

    private void queenMovesDownRight(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
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

    private void queenMovesDownLeft(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
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
