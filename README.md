# NQueensSolver

The N-Queens Solver Web Application is a user-friendly tool that allows users to explore and visualize solutions to the classic N-Queens problem. The N-Queens problem involves placing N queens on an N×N chessboard in such a way that no two queens attack each other. A queen can move horizontally, vertically, or diagonally

Main Features:

Solve N-Queens: Users can input a positive integer 'N', and the application will generate all possible solutions to the N-Queens problem for the given board size. The solutions are presented as a list of unique arrangements of queens on the chessboard.
Size Validation: The application ensures that the user provides a valid board size (N > 0) and handles invalid inputs gracefully, displaying a friendly error message.
Solution Limit: To prevent excessive computation, the application limits the maximum board size to a predefined value, preventing users from requesting solutions for very large N values.
Caching: The application intelligently caches previously generated solutions to avoid redundant computations and improve response time for frequently requested board sizes.
Single Solution View: Users can explore individual solutions from the list by providing the board size and the solution number, and the application will display that specific solution in a user-friendly format.

Technologies Used:

- Java 17
- SpringBoot
- JUnit
- Mockito
- FasterXML/Jackson
