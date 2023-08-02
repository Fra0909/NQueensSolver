package dev.nqueens.solver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

class QueensServiceTests {

    QueensService queensService;


    @BeforeEach
    void setUp() {
        queensService = new QueensService();
    }

    @Test
    void testSolveNQueens_ValidSize_ReturnsListOfSolutions() {
        int size = 4;
        List<List<Integer>> expectedSolutions = List.of(
                List.of(1, 3, 0, 2),
                List.of(2, 0, 3, 1)
        );

        List<List<Integer>> actualSolutions = queensService.solveNQueens(size);

        assertEquals(expectedSolutions, actualSolutions);
    }
}