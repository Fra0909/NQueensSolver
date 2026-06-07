package dev.nqueens.solver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/nqueens")
public class QueensController {

    public static final int MAX_SIZE = 12;
    private final QueensService queensService;

    @Autowired
    public QueensController(QueensService queensService) {
        this.queensService = queensService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("maxSize", MAX_SIZE);
        return "index";
    }

    private boolean isInvalidSize(int size) {
        return size <= 0 || size == 2 || size == 3 || size > MAX_SIZE;
    }

    @GetMapping(value = "/{size}", produces = "application/json")
    public ResponseEntity<?> getAllSolutions(@PathVariable int size) {
        if (isInvalidSize(size)) {
            return handleInvalidSize(size);
        }
        List<List<Integer>> solutions = queensService.solveNQueens(size);
        return ResponseEntity.ok(new QueensSolutionDTO(solutions));
    }

    @GetMapping(value = "/{size}/{solutionNumber}", produces = "application/json")
    public ResponseEntity<?> getSingleSolution(@PathVariable int size, @PathVariable int solutionNumber) {
        if (isInvalidSize(size)) {
            return handleInvalidSize(size);
        }
        List<List<Integer>> solutions = queensService.solveNQueens(size);
        if (solutionNumber < 1 || solutionNumber > solutions.size()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO("Invalid solution number. Max: " + solutions.size()));
        }
        return ResponseEntity.ok(solutions.get(solutionNumber - 1));
    }

    @GetMapping(value = "/{size}/{solutionNumber}/display")
    public ModelAndView displaySolution(@PathVariable int size, @PathVariable int solutionNumber, Model model) {
        if (isInvalidSize(size)) {
            return new ModelAndView("invalid-size");
        }

        List<List<Integer>> solutions = queensService.solveNQueens(size);
        if (solutionNumber < 1 || solutionNumber > solutions.size()) {
            model.addAttribute("maxSolutions", solutions.size());
            model.addAttribute("size", size);
            return new ModelAndView("invalid-solution");
        }

        List<Integer> solution = solutions.get(solutionNumber - 1);
        List<List<Integer>> chessboard = generateChessboard(size, solution);

        model.addAttribute("size", size);
        model.addAttribute("solutionNumber", solutionNumber);
        model.addAttribute("totalSolutions", solutions.size());
        model.addAttribute("chessboard", chessboard);

        return new ModelAndView("solution");
    }

    private ResponseEntity<?> handleInvalidSize(int size) {
        if (size <= 0) {
            return ResponseEntity.badRequest().body(new ErrorResponseDTO("Size must be positive"));
        } else if (size > MAX_SIZE) {
            return ResponseEntity.badRequest().body(new ErrorResponseDTO("Size cannot exceed " + MAX_SIZE));
        } else {
            return ResponseEntity.ok(new QueensSolutionDTO(Collections.emptyList()));
        }
    }

    private List<List<Integer>> generateChessboard(int size, List<Integer> queens) {
        List<List<Integer>> board = new ArrayList<>();
        for (int r = 0; r < size; r++) {
            List<Integer> row = new ArrayList<>();
            for (int c = 0; c < size; c++) {
                row.add(queens.get(r) == c ? 1 : 0);
            }
            board.add(row);
        }
        return board;
    }
}