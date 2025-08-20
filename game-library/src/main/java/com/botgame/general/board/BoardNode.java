package com.botgame.general.board;

import java.util.Optional;

public record BoardNode<Piece>(Optional<Piece> piece, int row, int column) {
}
