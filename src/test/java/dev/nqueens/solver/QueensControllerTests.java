package dev.nqueens.solver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(QueensController.class)
public class QueensControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueensService queensService;

    @InjectMocks
    private QueensController queensController;


    @Test
    public void getAllSolutionsForSize_ValidSize_ReturnsOkResponse() throws Exception {
        int size = 4;
        List<List<Integer>> solutions = List.of(
                List.of(1, 3, 0, 2),
                List.of(2, 0, 3, 1)
        );

        when(queensService.solveNQueens(size)).thenReturn(solutions);

        RequestBuilder request = MockMvcRequestBuilders.get("/nqueens/{size}", size);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJsonNode = objectMapper.readTree(jsonResponse);
        JsonNode solutionsJsonNode = responseJsonNode.get("solutions");

        List<List<Integer>> actualSolutions = objectMapper.readValue(
                solutionsJsonNode.traverse(),
                new TypeReference<List<List<Integer>>>() {
                });

        assertEquals(solutions, actualSolutions);
    }

    @Test
    public void getAllSolutionsForSize_InvalidSize_ReturnsBadRequest() throws Exception {
        int size = 0;

        RequestBuilder request = MockMvcRequestBuilders.get("/nqueens/{size}", size);
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Size needs to be a positive number greater than zero"));
    }

    @Test
    public void getAllSolutionsForSize_TwoOrThree_ReturnsEmptyArray() throws Exception {
        int size = 2;

        RequestBuilder request = MockMvcRequestBuilders.get("/nqueens/{size}", size);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.solutions").isEmpty())
                .andExpect(jsonPath("$.solutions").isArray());
    }

    @Test
    public void getAllSolutionsForSize_SizeTooBig_ReturnsBadRequest() throws Exception {
        int size = 15;

        RequestBuilder request = MockMvcRequestBuilders.get("/nqueens/{size}", size);
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(String.format("Size cannot be greater than %s", QueensController.MAX_SIZE)));
    }

    @Test
    public void getSingleSolutionForSize_InvalidSize_ReturnsBadRequest() throws Exception {
        int size = 13;
        int solutionNumber = 1;

        RequestBuilder request = MockMvcRequestBuilders.get("/nqueens/{size}/{solutionNumber}", size, solutionNumber);
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Size cannot be greater than 12")));
    }

    @Test
    public void getSingleSolutionForSize_ValidInput_ReturnsOkResponse() throws Exception {
        int size = 4;
        int solutionNumber = 1;
        List<List<Integer>> solutions = List.of(
                List.of(1, 3, 0, 2),
                List.of(2, 0, 3, 1)
        );

        when(queensService.solveNQueens(size)).thenReturn(solutions);

        RequestBuilder request = MockMvcRequestBuilders.get("/nqueens/{size}/{solutionNumber}", size, solutionNumber);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(size))
                .andExpect(jsonPath("$[0]").value(1))
                .andExpect(jsonPath("$[1]").value(3))
                .andExpect(jsonPath("$[2]").value(0))
                .andExpect(jsonPath("$[3]").value(2));
    }

    @Test
    public void getSingleSolutionForSize_SolutionNotFound_ReturnsNotFound() throws Exception {
        int size = 4;
        int solutionNumber = 3;
        List<List<Integer>> solutions = List.of(
                List.of(1, 3, 0, 2),
                List.of(2, 0, 3, 1)
        );

        when(queensService.solveNQueens(size)).thenReturn(solutions);

        RequestBuilder request = MockMvcRequestBuilders.get("/nqueens/{size}/{solutionNumber}", size, solutionNumber);
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("There are only 2 solutions for a board of size 4")));
    }

    @Test
    public void testInvalidInputSize() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/nqueens/{size}/{solutionNumber}/display", -1, 1);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("invalid-size"));
    }

    @Test
    public void testDisplaySingleSolutionForSize_ValidInput_ReturnsCorrectViewAndModelAttributes() throws Exception {
        int size = 4;
        int solutionNumber = 2;
        List<List<Integer>> solutions = List.of(
                List.of(1, 3, 0, 2),
                List.of(2, 0, 3, 1)
        );
        when(queensService.solveNQueens(size)).thenReturn(solutions);

        RequestBuilder request = MockMvcRequestBuilders.get("/nqueens/{size}/{solutionNumber}/display", size, solutionNumber);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("solution"))
                .andExpect(model().attribute("size", size))
                .andExpect(model().attribute("solutionNumber", solutionNumber))
                .andExpect(model().attributeExists("chessboard"));
    }

    @Test
    public void testDisplaySingleSolutionForSize_ValidInputInvalidSolution_ReturnsCorrectViewAndModelAttributes() throws Exception {
        List<List<Integer>> solutions = List.of(List.of(1, 2), List.of(2, 1));
        when(queensService.solveNQueens(4)).thenReturn(solutions);

        RequestBuilder request = MockMvcRequestBuilders.get("/nqueens/{size}/{solutionNumber}/display", 4, 3);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("invalid-solution"))
                .andExpect(model().attribute("maxSolutions", 2));
    }
}
