package dev.nqueens.solver;

import java.util.List;

public class QueensSolutionDTO {
    private List<List<Integer>> solutions;
    private String message;

    public QueensSolutionDTO(List<List<Integer>> solutions) {
        this.solutions = solutions;
    }

    public List<List<Integer>> getSolutions() {
        return solutions;

    }
}
