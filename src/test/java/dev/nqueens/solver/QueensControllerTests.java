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
    public void testGetQueensSolutions_ValidSize_ReturnsOkResponse() throws Exception {
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
        System.out.println("JSON Response: " + jsonResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJsonNode = objectMapper.readTree(jsonResponse);
        JsonNode solutionsJsonNode = responseJsonNode.get("solutions");

        List<List<Integer>> actualSolutions = objectMapper.readValue(
                solutionsJsonNode.traverse(),
                new TypeReference<List<List<Integer>>>() {});

        assertEquals(solutions, actualSolutions);
    }

    @Test
    public void testGetQueensSolutions_InvalidSize_ReturnsBadRequest() throws Exception {
        int size = 0;

        RequestBuilder request = MockMvcRequestBuilders.get("/nqueens/{size}", size);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.solutions").isEmpty());
    }

    @Test
    public void testGetQueensSolutions_SizeTooBig_ReturnsBadRequest() throws Exception {
        int size = 15;

        RequestBuilder request = MockMvcRequestBuilders.get("/nqueens/{size}", size);
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Size too big"));
    }

    @Test
    public void testGetQueensSolution_ValidInput_ReturnsOkResponse() throws Exception {
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
                .andExpect(content().string(containsString("0100\n0001\n1000\n0010\n")));
    }

    @Test
    public void testGetQueensSolution_InvalidSize_ReturnsBadRequest() throws Exception {
        int size = 3;
        int solutionNumber = 1;

        RequestBuilder request = MockMvcRequestBuilders.get("/nqueens/{size}/{solutionNumber}", size, solutionNumber);
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("Invalid size."));
    }

    @Test
    public void testGetQueensSolution_SolutionNotFound_ReturnsNotFound() throws Exception {
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
                .andExpect(content().string("Solution not found."));
    }
}
