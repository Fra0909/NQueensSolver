import java.util.List;

public class QueensSolutionDTO {
    private List<List<Integer>> solutions;

    public QueensSolutionDTO(List<List<Integer>> solutions) {
        this.solutions = solutions;
    }

    public List<List<Integer>> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<List<Integer>> solutions) {
        this.solutions = solutions;
    }
}
