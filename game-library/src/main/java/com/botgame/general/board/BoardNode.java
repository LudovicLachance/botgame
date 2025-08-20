package com.botgame.general.board;

import java.util.Optional;

public record BoardNode<Piece extends BoardPiece>(Optional<Piece> piece, int row, int column) {
}
