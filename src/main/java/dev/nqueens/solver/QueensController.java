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
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/nqueens")
public class QueensController {

    public static final int MAX_SIZE = 12;
    private final QueensService queensService;

    @Autowired
    public QueensController(QueensService queensService) {
        this.queensService = queensService;
    }

    private boolean isInvalidInput(int size) {
        if (size <= 0 || size == 2 || size == 3 || size > MAX_SIZE) {
            return true;
        }
        return false;
    }

    private ResponseEntity<?> getResponseForInvalidSize(int size) {
        if (size <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO("Size needs to be a positive number greater than zero"));
        } else if (size == 2 || size == 3) {
            return ResponseEntity.ok(new QueensSolutionDTO(Collections.emptyList()));
        }
        else if (size > 12) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO("Size cannot be greater than " + MAX_SIZE));
        }
        return null;
    }

    @GetMapping(value = "/{size}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllSolutionsForSize(@PathVariable int size) {
        if (isInvalidInput(size)) {
            return getResponseForInvalidSize(size);
        }

        var solutions = queensService.solveNQueens(size);
        var responseDTO = new QueensSolutionDTO(solutions);
        return ResponseEntity.ok(responseDTO);
    }


    @GetMapping(value = "/{size}/{solutionNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSingleSolutionForSize(@PathVariable int size, @PathVariable int solutionNumber, Model model) {
        if (isInvalidInput(size)) {
            return getResponseForInvalidSize(size);
        }

        var solutions = queensService.solveNQueens(size);

        if (solutionNumber > solutions.size()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(String.format("There are only %s solutions for a board of size %s.", solutions.size(), size)));
        }

        List<Integer> solution = solutions.get(solutionNumber - 1);
        return ResponseEntity.ok(solution);
    }

    @GetMapping(value = "/{size}/{solutionNumber}/display", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView displaySingleSolutionForSize(@PathVariable int size, @PathVariable int solutionNumber, Model model) {
        if (isInvalidInput(size)) {
            return new ModelAndView("invalid-size");
        }

        var solutions = queensService.solveNQueens(size);

        if (solutionNumber > solutions.size()) {
            model.addAttribute("maxSolutions", solutions.size());
            return new ModelAndView("invalid-solution");
        }

        List<Integer> solution = solutions.get(solutionNumber - 1);
        List<List<Integer>> chessboard = generateChessboard(size, solution);

        model.addAttribute("size", size);
        model.addAttribute("solutionNumber", solutionNumber);
        model.addAttribute("chessboard", chessboard);

        return new ModelAndView("solution");
    }

    private List<List<Integer>> generateChessboard(int size, List<Integer> queensPositions) {
        List<List<Integer>> chessboard = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            List<Integer> chessboardRow = new ArrayList<>();
            for (int col = 0; col < size; col++) {
                chessboardRow.add(queensPositions.get(row) == col ? 1 : 0);
            }
            chessboard.add(chessboardRow);
        }
        return chessboard;
    }
}
