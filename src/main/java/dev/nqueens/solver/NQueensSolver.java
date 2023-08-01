package dev.nqueens.solver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NQueensSolver {
    public static void main(String[] args) {
        SpringApplication.run(NQueensSolver.class, args);
    }
}
