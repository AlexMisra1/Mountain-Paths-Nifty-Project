import java.util.*;
import java.io.*;
import java.awt.*;

public class MapDataDrawer
{

	public int[][] grid;

	public MapDataDrawer(String filename, int rows, int cols){
		// initialize grid 
		grid = new int[rows][cols];
		//read the data from the file into the grid
		try {
			
			File f = new File(filename);
			Scanner scn = new Scanner(f);
			int row = 0;
			int col = 0;
			
			while (scn.hasNextInt()) {
				
				int value = scn.nextInt();
				grid[row][col] = value;
				col++;
				
				if (col == cols) {
					row++;
					col = 0;
				}
				
				if (row == rows) {
					break;
				}

			}
            scn.close();

		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	/**
	 * @return the min value in the entire grid
	 */
	public int findMinValue(){
		int min = Integer.MAX_VALUE;
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[0].length; c++) {
				if (grid[r][c] < min) {
					min = grid[r][c];
				}
			}
		} 
		return min;
	}
	/**
	 * @return the max value in the entire grid
	 */
	public int findMaxValue(){
		int max = Integer.MIN_VALUE;
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[0].length; c++) {
				if (grid[r][c] > max) {
					max = grid[r][c];
				}
			}
		}
		return max;
	}


	/**
	 * @param col the column of the grid to check
	 * @return the index of the row with the lowest value in the given col for the grid
	 */
	public  int indexOfMinInCol(int col){
		int minIndex = 0;
		for (int r = 1; r < 476; r++) {
			if (grid[r][col] < grid[minIndex][col]) {
				minIndex = r;
			}
		}
		return minIndex;
	}

	/**
	 * Draws the grid using the given Graphics object.
	 * Colors should be grayscale values 0-255, scaled based on min/max values in grid
	 */
	public void drawMap(Graphics g){
		
		int maxValue = findMaxValue();
		int minValue = findMinValue();
		double scale = 255.0 / (maxValue - minValue);
		
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[0].length; c++) {
				int color = (int) ((grid[r][c] - minValue) * scale);
				g.setColor(new Color(color, color, color));
				g.fillRect(c, r, 1, 1);
			}
		}


	}

	/**
	 * Find a path from West-to-East starting at given row.
	 * Choose a foward step out of 3 possible forward locations, using greedy method described in assignment.
	 * @return the total change in elevation traveled from West-to-East
	 */
		
	public int drawLowestElevPath(Graphics g, int row) {
	    int r = row;
	    int initElev = grid[r][0];
	    Random random = new Random();

	    for (int c = 0; c < grid[0].length - 1; c++) {
	        g.fillRect(c, r, 1, 1);

	        //first time using ternary operators. They're kind of nice. Easier to read.
	        int change1 = Math.abs(grid[r][c] - grid[r][c + 1]);
	        int change2 = (r + 1 < grid.length) ? Math.abs(grid[r][c] - grid[r + 1][c + 1]) : Integer.MAX_VALUE;
	        int change3 = (r - 1 >= 0) ? Math.abs(grid[r][c] - grid[r - 1][c + 1]) : Integer.MAX_VALUE;

	        if (change2 == change3 && change2 < change1) {
	            if (random.nextBoolean()) {
	                r++;
	            } else {
	                r--;
	            }
	        } else if (change2 < change1 && change2 < change3) {
	            r++;
	        } else if (change3 < change1 && change3 < change2) {
	            r--;
	        }
	    }

	    g.fillRect(grid[0].length - 1, r, 1, 1);
	    int finalElev = grid[r][grid[0].length - 1];

	    return Math.abs(finalElev - initElev);
	}

	/**
	 * @return the index of the starting row for the lowest-elevation-change path in the entire grid.
	 */
	public int indexOfLowestElevPath(Graphics g){
		int minIndex = 0;
		for (int r = 1; r < grid.length; r++) {
			if (drawLowestElevPath(g, r) < drawLowestElevPath(g, minIndex)) {
				minIndex = r;
			}
		}
		return minIndex;

	}


}