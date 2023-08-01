package dev.nqueens.solver;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "queensSolutions")
public class QueensService {

    @Cacheable(key = "#size")
    public List<List<Integer>> solveNQueens(int size) {
        List<List<Integer>> solutions = new ArrayList<>();
        int[] queens = new int[size];
        findSolutions(queens, 0, solutions);
        return solutions;
    }

    private void findSolutions(int[] queens, int row, List<List<Integer>> solutions) {
        if (row == queens.length) {
            List<Integer> solution = new ArrayList<>();
            for (int queenColumn : queens) {
                solution.add(queenColumn);
            }
            solutions.add(solution);
            return;
        }

        for (int column = 0; column < queens.length; column++) {
            if (isClash(queens, row, column)) {
                queens[row] = column;
                findSolutions(queens, row + 1, solutions);
            }
        }
    }

    private boolean isClash(int[] queens, int row, int column) {
        for (int i = 0; i < row; i++) {
            if (queens[i] == column || column + row == queens[i] + i || column - row == queens[i] - i) {
                return false;
            }
        }
        return true;
    }
}
