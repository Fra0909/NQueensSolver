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
            for (int col : queens) solution.add(col);
            solutions.add(solution);
            return;
        }

        for (int col = 0; col < queens.length; col++) {
            if (isSafe(queens, row, col)) {
                queens[row] = col;
                findSolutions(queens, row + 1, solutions);
            }
        }
    }

    private boolean isSafe(int[] queens, int row, int col) {
        for (int i = 0; i < row; i++) {
            if (queens[i] == col || 
                queens[i] - i == col - row || 
                queens[i] + i == col + row) {
                return false;
            }
        }
        return true;
    }
}