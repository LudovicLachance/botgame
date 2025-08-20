package com.botgame.general;

import java.util.Optional;

public record BoardNode<Piece>(Optional<Piece> piece, int row, int column) {
}
