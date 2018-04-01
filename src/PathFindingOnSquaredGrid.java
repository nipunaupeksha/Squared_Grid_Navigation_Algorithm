
import java.util.Iterator;
import java.util.Scanner;
import java.util.*;

/**
 * ***********************************************************************
 * Author: Dr E Kapetanios Last update: 22-02-2017
 *
 ************************************************************************
 */
public class PathFindingOnSquaredGrid {

    //////////////////////////////////////////////////////////////////////
    public static final int DIAGONAL_COST = 14;
    public static final int V_H_COST = 10;

    static class Cell {

        int heuristicCost = 0; //Heuristic cost
        int finalCost = 0; //G+H
        int i, j;
        Cell parent;

        Cell(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public String toString() {
            return "[" + this.i + ", " + this.j + "]";
        }
    }

    //Blocked cells are just null Cell values in grid
    static Cell[][] grid = new Cell[10][10];

    static PriorityQueue<Cell> open;

    static boolean closed[][];
    static int startI, startJ;
    static int endI, endJ;

    public static void setBlocked(int i, int j) {
        grid[i][j] = null;
    }

    public static void setStartCell(int i, int j) {
        startI = i;
        startJ = j;
    }

    public static void setEndCell(int i, int j) {
        endI = i;
        endJ = j;
    }

    static void checkAndUpdateCost(Cell current, Cell t, int cost) {
        if (t == null || closed[t.i][t.j]) {
            return;
        }
        int t_final_cost = t.heuristicCost + cost;

        boolean inOpen = open.contains(t);
        if (!inOpen || t_final_cost < t.finalCost) {
            t.finalCost = t_final_cost;
            t.parent = current;
            if (!inOpen) {
                open.add(t);
            }
        }
    }

    public static void AStar() {

        //add the start location to open list.
        open.add(grid[startI][startJ]);

        Cell current;

        while (true) {
            current = open.poll();
            if (current == null) {
                break;
            }
            closed[current.i][current.j] = true;

            if (current.equals(grid[endI][endJ])) {
                return;
            }

            Cell t;
            if (current.i - 1 >= 0) {
                t = grid[current.i - 1][current.j];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);

                if (current.j - 1 >= 0) {
                    t = grid[current.i - 1][current.j - 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }

                if (current.j + 1 < grid[0].length) {
                    t = grid[current.i - 1][current.j + 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }
            }

            if (current.j - 1 >= 0) {
                t = grid[current.i][current.j - 1];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
            }

            if (current.j + 1 < grid[0].length) {
                t = grid[current.i][current.j + 1];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
            }

            if (current.i + 1 < grid.length) {
                t = grid[current.i + 1][current.j];
                checkAndUpdateCost(current, t, current.finalCost + V_H_COST);

                if (current.j - 1 >= 0) {
                    t = grid[current.i + 1][current.j - 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }

                if (current.j + 1 < grid[0].length) {
                    t = grid[current.i + 1][current.j + 1];
                    checkAndUpdateCost(current, t, current.finalCost + DIAGONAL_COST);
                }
            }
        }
    }

    /*
     Params :
     tCase = test case No.
     x, y = Board's dimensions
     si, sj = start location's x and y coordinates
     ei, ej = end location's x and y coordinates
     int[][] blocked = array containing inaccessible cell coordinates
     */
    public static void test(int tCase, int x, int y, int si, int sj, int ei, int ej, int[][] blocked, boolean[][] a) {
//        System.out.println("\n\nTest Case #" + tCase);
        //Reset
        grid = new Cell[x][y];
        closed = new boolean[x][y];
        open = new PriorityQueue<>((Object o1, Object o2) -> {
            Cell c1 = (Cell) o1;
            Cell c2 = (Cell) o2;

            return c1.finalCost < c2.finalCost ? -1
                    : c1.finalCost > c2.finalCost ? 1 : 0;
        });
        //Set start position
        setStartCell(si, sj);  //Setting to 0,0 by default. Will be useful for the UI part

        //Set End Location
        setEndCell(ei, ej);

        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                grid[i][j] = new Cell(i, j);
                grid[i][j].heuristicCost = Math.abs(i - endI) + Math.abs(j - endJ);
//                  System.out.print(grid[i][j].heuristicCost+" ");
            }
//              System.out.println();
        }
        grid[si][sj].finalCost = 0;

        /*
         Set blocked cells. Simply set the cell values to null
         for blocked cells.
         */
        for (int i = 0; i < blocked.length; ++i) {
            setBlocked(blocked[i][0], blocked[i][1]);
        }

//Display initial map
//        System.out.println("Grid: ");
//        for (int i = 0; i < x; ++i) {
//            for (int j = 0; j < y; ++j) {
//                if (i == si && j == sj) {
//                    System.out.print("SO  "); //Source
//                } else if (i == ei && j == ej) {
//                    System.out.print("DE  ");  //Destination
//                } else if (grid[i][j] != null) {
//                    System.out.printf("%-3d ", 0);
//                } else {
//                    System.out.print("BL  ");
//                }
//            }
//            System.out.println();
//        }
//        System.out.println();
        AStar();
//Scores for cells        
//        System.out.println("\nScores for cells: ");
//        for (int i = 0; i < x; ++i) {
//            for (int j = 0; j < x; ++j) {
//                if (grid[i][j] != null) {
//                    System.out.printf("%-3d ", grid[i][j].finalCost);
//                } else {
//                    System.out.print("BL  ");
//                }
//            }
//            System.out.println();
//        }
//        System.out.println();

        if (closed[endI][endJ]) {
            //Trace back the path 
            System.out.println("Path: ");
            Cell current = grid[endI][endJ];
            StdDraw.circle(endJ, a.length - 1 - endI, .05);
            System.out.print(current);
            while (current.parent != null) {
                StdDraw.circle(current.parent.j, a.length - 1 - current.parent.i, .05);
                System.out.print(" -> " + current.parent);
                current = current.parent;
            }
            System.out.println();
        } else {
            System.out.println("No possible path");
        }
    }
    //////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    // given an N-by-N matrix of open cells, return an N-by-N matrix
    // of cells reachable from the top
    public static boolean[][] flow(boolean[][] open) {
        int N = open.length;

        boolean[][] full = new boolean[N][N];
        for (int j = 0; j < N; j++) {
            flow(open, full, 0, j);
        }

        return full;
    }

    // determine set of open/blocked cells using depth first search
    public static void flow(boolean[][] open, boolean[][] full, int i, int j) {
        int N = open.length;

        // base cases
        if (i < 0 || i >= N) {
            return;    // invalid row
        }
        if (j < 0 || j >= N) {
            return;    // invalid column
        }
        if (!open[i][j]) {
            return;        // not an open cell
        }
        if (full[i][j]) {
            return;         // already marked as open
        }
        full[i][j] = true;

        flow(open, full, i + 1, j);   // down
        flow(open, full, i, j + 1);   // right
        flow(open, full, i, j - 1);   // left
        flow(open, full, i - 1, j);   // up
    }

    // does the system percolate?
    public static boolean percolates(boolean[][] open) {
        int N = open.length;

        boolean[][] full = flow(open);
        for (int j = 0; j < N; j++) {
            if (full[N - 1][j]) {
                return true;
            }
        }

        return false;
    }

    // does the system percolate vertically in a direct way?
    public static boolean percolatesDirect(boolean[][] open) {
        int N = open.length;

        boolean[][] full = flow(open);
        int directPerc = 0;
        for (int j = 0; j < N; j++) {
            if (full[N - 1][j]) {
                // StdOut.println("Hello");
                directPerc = 1;
                int rowabove = N - 2;
                for (int i = rowabove; i >= 0; i--) {
                    if (full[i][j]) {
                        // StdOut.println("i: " + i + " j: " + j + " " + full[i][j]);
                        directPerc++;
                    } else {
                        break;
                    }
                }
            }
        }

        // StdOut.println("Direct Percolation is: " + directPerc);
        if (directPerc == N) {
            return true;
        } else {
            return false;
        }
    }

    // draw the N-by-N boolean matrix to standard draw
    public static void show(boolean[][] a, boolean which) {
        int N = a.length;
        StdDraw.setXscale(-1, N);;
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (a[i][j] == which) {
                    StdDraw.square(j, N - i - 1, .5);
                } else {
                    StdDraw.filledSquare(j, N - i - 1, .5);
                }
            }
        }
    }

    // draw the N-by-N boolean matrix to standard draw, including the points A (x1, y1) and B (x2,y2) to be marked by a circle
    public static void show(boolean[][] a, boolean which, int x1, int y1, int x2, int y2) {
        int N = a.length;
        StdDraw.setXscale(-1, N);;
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (a[i][j] == which) {
                    if ((i == x1 && j == y1) || (i == x2 && j == y2)) {
                        StdDraw.circle(j, N - 1 - i, .5);
                    } else {
                        StdDraw.square(j, N - i - 1, .5);
                    }
                } else {
                    StdDraw.filledSquare(j, N - i - 1, .5);
                }
            }
        }
    }

    // return a random N-by-N boolean matrix, where each entry is
    // true with probability p
    public static boolean[][] random(int N, double p) {
        boolean[][] a = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                a[i][j] = StdRandom.bernoulli(p);
            }
        }
        return a;
    }

    // test client
    public static void main(String[] args) {
        // boolean[][] open = StdArrayIO.readBoolean2D();

        // The following will generate a 10x10 squared grid with relatively few obstacles in it
        // The lower the second parameter, the more obstacles (black cells) are generated
        boolean[][] randomlyGenMatrix = random(10, 0.4);

        StdArrayIO.print(randomlyGenMatrix);
        show(randomlyGenMatrix, true);

        System.out.println();
        System.out.println("The system percolates: " + percolates(randomlyGenMatrix));

        System.out.println();
        System.out.println("The system percolates directly: " + percolatesDirect(randomlyGenMatrix));
        System.out.println();

        // Reading the coordinates for points A and B on the input squared grid.
        // THIS IS AN EXAMPLE ONLY ON HOW TO USE THE JAVA INTERNAL WATCH
        // Start the clock ticking in order to capture the time being spent on inputting the coordinates
        // You should position this command accordingly in order to perform the algorithmic analysis
        Stopwatch timerFlow = new Stopwatch();

        Scanner in = new Scanner(System.in);
        System.out.println("Enter i for A > ");
        int Ai = in.nextInt();

        System.out.println("Enter j for A > ");
        int Aj = in.nextInt();

        System.out.println("Enter i for B > ");
        int Bi = in.nextInt();

        System.out.println("Enter j for B > ");
        int Bj = in.nextInt();

        //Checking whether the input coordinates are possible
        if (randomlyGenMatrix[Ai][Aj] == false || randomlyGenMatrix[Bi][Bj] == false) {
            System.out.println("Error in inputting coordinates");
            return;
        }

        // THIS IS AN EXAMPLE ONLY ON HOW TO USE THE JAVA INTERNAL WATCH
        // Stop the clock ticking in order to capture the time being spent on inputting the coordinates
        // You should position this command accordingly in order to perform the algorithmic analysis
        StdOut.println("Elapsed time = " + timerFlow.elapsedTime());

        // System.out.println("Coordinates for A: [" + Ai + "," + Aj + "]");
        // System.out.println("Coordinates for B: [" + Bi + "," + Bj + "]");
        /**
         * Ai-Row of first coordinate *Aj-Column of first coordinate *Bi-Row of
         * second coordinate *Bj-Column of second coordinate
         */
        show(randomlyGenMatrix, true, Ai, Aj, Bi, Bj);
        //Calculating number of blocks
        int totalBlocks = 0;
        for (int x = 0; x < randomlyGenMatrix.length; x++) {
            for (int y = 0; y < randomlyGenMatrix[x].length; y++) {
                if (randomlyGenMatrix[x][y] == false) {
                    totalBlocks += 1;
                }
            }
        }
        //Creating a array of coordinates of blocks
        int blocks[][] = new int[totalBlocks][2];
        totalBlocks = 0;
        for (int x = 0; x < randomlyGenMatrix.length; x++) {
            for (int y = 0; y < randomlyGenMatrix[x].length; y++) {
                if (randomlyGenMatrix[x][y] == false) {
                    blocks[totalBlocks][0] = x;
                    blocks[totalBlocks][1] = y;
                    totalBlocks += 1;
                }
            }
        }
        test(1, 10, 10, Ai, Aj, Bi, Bj, blocks, randomlyGenMatrix);
    }

}
