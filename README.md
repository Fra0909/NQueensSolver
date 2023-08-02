# NQueensSolver

The N-Queens Solver Web Application is a user-friendly tool that allows users to explore and visualize solutions to the classic N-Queens problem. The N-Queens problem involves placing N queens on an N×N chessboard in such a way that no two queens attack each other. A queen can move horizontally, vertically, or diagonally.

## Main Features:

- **Solve N-Queens:** Users can input a positive integer 'N', and the application will generate all possible solutions to the N-Queens problem for the given board size. The solutions are presented as a list of unique arrangements of queens on the chessboard.
- **Size Validation:** The application ensures that the user provides a valid board size (N > 0) and handles invalid inputs gracefully, displaying a friendly error message.
- **Solution Limit:** To prevent excessive computation, the application limits the maximum board size to a predefined value, preventing users from requesting solutions for very large N values.
- **Caching:** The application intelligently caches previously generated solutions to avoid redundant computations and improve response time for frequently requested board sizes.
- **Single Solution View:** Users can explore individual solutions from the list by providing the board size and the solution number, and the application will display that specific solution in a user-friendly format.

## Technologies Used:

- Java 17
- Spring Boot
- JUnit
- Mockito
- FasterXML/Jackson

##SpringBoot endpoints:

**GET /nqueens/{size}**
Retrieve all possible solutions to the N-Queens problem for a given board size.

- **URL Parameters:**
  - `size` (integer): The board size (N) for which to find the solutions.

- **Response:**
  - If the size is not valid <= 0 it returns a 400 bad request.
  - If the size is 2 or 3 (e.g. no solutions available) it returns an empty list.
  - If the size is too large (greater than the maximum defined size 12), it returns a 400 bad request.
  - Otherwise, it returns a JSON array containing all the solutions.

#### Get Single N-Queens Solution

**GET /nqueens/{size}/{solutionNumber}**

Retrieve a specific solution to the N-Queens problem for a given board size and solution number.

- **URL Parameters:**
  - `size` (integer): The board size (N) for which to find the solution.
  - `solutionNumber` (integer): The index of the solution in the list of solutions.

- **Response:**
  - If the size is not valid (<= 3 or greater than the maximum defined size), it returns a plain text "Invalid size." error.
  - If the solution number is not valid (<= 0 or greater than the total number of solutions), it returns a 404 not found error.
  - Otherwise, it returns an array where the index corresponds to the row and the current element to the column the queen is in.

**GET /nqueens/{size}/{solutionNumber}/display**

Retrieve a specific solution to the N-Queens problem for a given board size and solution number.

- **URL Parameters:**
  - `size` (integer): The board size (N) for which to find the solution.
  - `solutionNumber` (integer): The index of the solution in the list of solutions.

- **Response:**
  - If the size is not valid (<= 0, 2 or 3 or greater than the maximum defined size), it shows the user an error page.
  - If the solution number is not valid (<= 0 or greater than the total number of solutions), it shows the user an error page.
  - Otherwise, it takes the user to an interactive HTML page that shows the chess board and when hovering over any queen, it highlights their vertical, horizontal, and diagonal lines.
