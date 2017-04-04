import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MazeEscape { // 1 2 3 4 - 
	int N, M, G; //G == Number of gates.
	
	ArrayList<Integer> frontier = new ArrayList<Integer>();
	ArrayList<ArrayList<Cell>> gate = new ArrayList<ArrayList<Cell>>();
	ArrayList<ArrayList<Cell>> maze = new ArrayList<ArrayList<Cell>>();

	public class Cell {
		char type;       //Gate | wall | empty cell.
		int distance;    //Distance to the nearest gate.
		int posY, posX;  //Position within the maze.
		boolean visited; //Exploration flag.
		
		public Cell(int posY, int posX) { 
			this.distance = -1;
			this.visited  = false;
			this.posY = posY;
			this.posX = posX;
		}
	}
	
	public MazeEscape(String filename) {
		Scanner sc = null;
		char[] chars;
		ArrayList<Cell> mazeI; //Caching
		
		try {
			sc = new Scanner(new File(filename));

			this.N = sc.nextInt();
			this.M = sc.nextInt();
			
			for (int i = 0; i < N; i++) {
				maze.add(new ArrayList<Cell>());
			}

			for (int i = 0; i < N; i++) {
				
				chars = sc.next().toCharArray();
				
				mazeI = maze.get(i); //Caching
				
				for (int j = 0; j < M; j++) {
					
					
					maze.add(new ArrayList<Cell>());
					mazeI.add(new Cell(i, j));
					mazeI.get(j).type = chars[j];
					
					if(chars[j] == 'G') {
						frontier.add(G, 1);
						mazeI.get(j).visited = true;
						
						gate.add(G, new ArrayList<Cell>());
						gate.get(G).add(mazeI.get(j));
						
						G++;
					}
				}
			}

		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			try {
				if (sc != null) {
					sc.close();
				}
			} catch (Exception ex) {
				//ex.printStackTrace();
			}
		}
	}
	
	//In a sequential manner, gates will explore their immediate frontier, 1 step
	//at a time; this ensures that each cell is discovered by the frontier
	//corresponding to the closest gate.
	
	public void mazeDistances() {
		boolean finished = false;
		int x, y, found, f;
		Cell explorer;
		ArrayList<Cell> gi;
		Cell mazeYX;
		
		//f, gi, mazeYX - some caching stuff to quicken the pace.
		
		//Exploration: up, right, down, left  0,0 --> posX (M)
		int[] dy = {-1, 0, 1,  0}; 			// | maze matrix[posY-dy][posX-dx]
		int[] dx = { 0, 1, 0, -1}; 			// V
											//posY (N)
		int current_distance = 0;
		
		while(!finished) {
			
			for(int i = 0; i < G; i++) {
				found = 0; //Cells found this iteration by gate[i]
				f = frontier.get(i);
				gi = gate.get(i);
				
				for(int j = 0; j < f; j++) {
					
					explorer = gi.get(0);   	  //Frontier is expanded
							  							  //sequentially so the
					explorer.distance = current_distance; //exploration speed stays 
														  //constant for all gates.
					for(int k = 0; k < 4; k++) {
						
						x = explorer.posX + dx[k];
						y = explorer.posY + dy[k];
						
						//Ensuring circularity within the maze.
						if(x == -1) {
							x = M-1;
						}else {
							x = x % M;
						}
						if(y == -1) {
							y = N-1;
						}else {
							y = y % N;
						}
						mazeYX = maze.get(y).get(x);
						
						if(!mazeYX.visited) {
							
							if(mazeYX.type == 'X') {   //Walls: marked
								mazeYX.visited = true; //visited &
							}else {								   //skipped.
								
								//We mark new Cells as visited & store them.
								found++;
								mazeYX.visited = true;
								gi.add(mazeYX);
							}
						}
					}
					//We remove previously explored cells from the list.
					gi.remove(0);
				}
				frontier.set(i, found);
			}
			
			//We are done exploring if all exploration lists are empty.
			finished = true;
			
			for(int i = 0; i < G; i++) {
				if(frontier.get(i) > 0) {
					finished = false;
					break;
				}
			}
			current_distance++;
		}
		//Writing the resulting distances to mazeEscape.out
		try{
		    PrintWriter writer = new PrintWriter("maze_escape.out", "UTF-8");
		    
		    for(int i = 0; i < N; i++) {
				for(int j = 0; j < M; j++) {
					if(j == (M-1)) {
						writer.print(maze.get(i).get(j).distance);
					}else {
						writer.print(maze.get(i).get(j).distance + " ");
					}
				}
				writer.println();
			}
		    writer.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MazeEscape maze = new MazeEscape("maze_escape.in");
		maze.mazeDistances();
	}
}
