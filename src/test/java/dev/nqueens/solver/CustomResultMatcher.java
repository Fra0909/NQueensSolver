package dev.nqueens.solver;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

public class CustomResultMatcher implements ResultMatcher {

    @Override
    public void match(MvcResult result) throws Exception {
        System.out.println(result.getResponse().getContentAsByteArray());
    }
}