package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ChessGame.TeamColor color = piece.getTeamColor();
        Collection<ChessMove> moves = new ArrayList<>();
        checkForEnemies(board, myPosition, color, moves);
        pawnMoveForward(board, myPosition, moves);
        return moves;
    }

    private void checkForEnemies(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, Collection<ChessMove> moves) {
        int x = myPosition.getRow() + 1;
        int y = myPosition.getColumn();

        ChessPosition leftEndPosition = new ChessPosition(x, y - 1);
        ChessPosition rightEndPosition = new ChessPosition(x, y + 1);

        ChessPiece leftEnemy = board.getPiece(leftEndPosition);
        ChessPiece rightEnemy = board.getPiece(rightEndPosition);

        if (leftEnemy != null && leftEnemy.getTeamColor() != color) {
            moves.add(new ChessMove(myPosition, leftEndPosition, null));
        }
        if (rightEnemy != null && rightEnemy.getTeamColor() != color) {
            moves.add(new ChessMove(myPosition, rightEndPosition, null));
        }
    }

    private void pawnMoveForward(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {
        int x = myPosition.getRow() + 1;
        int y = myPosition.getColumn();

        ChessPosition newPosition = new ChessPosition(x, y);

        if (x <= 8 && board.getPiece(newPosition) == null) {
            moves.add(new ChessMove(myPosition, newPosition, null));
        }
    }

}
