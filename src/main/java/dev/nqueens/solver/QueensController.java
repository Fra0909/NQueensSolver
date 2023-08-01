package dev.nqueens.solver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/nqueens")
public class QueensController {

    private final QueensService queensService;
    private final int MAX_SIZE = 12;

    @Autowired
    public QueensController(QueensService queensService) {
        this.queensService = queensService;
    }

    @GetMapping(value = "/{size}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getQueensSolutions(@PathVariable int size) {
        if (size <= 0 || size == 2 || size == 3) {
            return ResponseEntity.ok(new QueensSolutionDTO(Collections.emptyList()));
        } else if (size > MAX_SIZE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO("Size too big"));
        }

        var solutions = queensService.solveNQueens(size);
        if (solutions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        var responseDTO = new QueensSolutionDTO(solutions);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping(value = "/{size}/{solutionNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getQueensSolution(@PathVariable int size, @PathVariable int solutionNumber,  Model model) {
        if (size <= 3 || size > MAX_SIZE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid size.");
        }

        var solutions = queensService.solveNQueens(size);

        if (solutionNumber <= 0 || solutionNumber > solutions.size()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solution not found.");
        }

        List<Integer> solution = solutions.get(solutionNumber - 1);

        StringBuilder formattedSolution = new StringBuilder();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (solution.get(row) == col) {
                    formattedSolution.append("1");
                } else {
                    formattedSolution.append("0");
                }
            }
            formattedSolution.append("\n");
        }

        return ResponseEntity.ok(formattedSolution.toString());

    }
}
