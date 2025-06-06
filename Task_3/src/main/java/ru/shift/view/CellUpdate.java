package ru.shift.view;

import ru.shift.model.CellState;

public record CellUpdate(
        int x,
        int y,
        CellState state,
        int adjacentMines,
        boolean hasMine
) {}
