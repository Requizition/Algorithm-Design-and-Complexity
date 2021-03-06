import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
//import java.util.Collections;
//import java.util.Enumeration;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Stores {
	int N, M, Na, Ns;
	
	Hashtable<Integer, ArrayList<EdgeTo>> graph;
	Hashtable<Integer, Integer> storeDist;
	Hashtable<Integer, Integer> nodePred;
	PriorityQueue<EdgeTo> pQueue;
	Hashtable<Integer, Hashtable<Integer, Integer>> distances;
	     // storeIndex			aptIndex,   cost
	//ArrayList<Integer> aptIndex;
	//ArrayList<Integer> storeIndex;
	
	Integer[] aptIndex;
	Integer[] storeIndex;
	
	public class MyComparator implements Comparator<EdgeTo> {

		@Override
		public int compare(EdgeTo o1, EdgeTo o2) {
			
			return o1.cost - o2.cost;
		}
		
	}
	
	public class EdgeTo{
		int nodeto,cost;
		
		public EdgeTo(int nodeto, int cost) {
			this.nodeto = nodeto;
			this.cost = cost;
		}
		
		public int getCost() {
			return cost;
		}
	}
	
	public Stores(String fileName) {
		//Scanner sc = null;
		
		
		distances = new Hashtable<Integer, Hashtable<Integer, Integer>>();
		graph = new Hashtable<Integer, ArrayList<EdgeTo>>();
		
		//aptIndex = new ArrayList<Integer>();
		//storeIndex = new ArrayList<Integer>();
		
		try {
			
			BufferedReader input = new BufferedReader(new FileReader(fileName));
			
			//sc = new Scanner(new File(fileName));
			
			String[] splitLine = input.readLine().split(" ");
			
			N = Integer.parseInt(splitLine[0]);//sc.nextInt();
			M = Integer.parseInt(splitLine[1]);//sc.nextInt();
			
			for(int i = 1; i <=  N; i++) {
				graph.put(i, new ArrayList<EdgeTo>());
			}
			
			int nOne, nTwo, cost;
			
			for(int i = 0; i < M; i++) {
				
				splitLine = input.readLine().split(" ");
				
				nOne = Integer.parseInt(splitLine[0]);//sc.nextInt();
				nTwo = Integer.parseInt(splitLine[1]);//sc.nextInt();
				cost = Integer.parseInt(splitLine[2]);//sc.nextInt();
				
				graph.get(nOne).add(new EdgeTo(nTwo, cost));
				graph.get(nTwo).add(new EdgeTo(nOne, cost));
			}
			
			splitLine = input.readLine().split(" ");
			
			Na = Integer.parseInt(splitLine[0]);//sc.nextInt();
			Ns = Integer.parseInt(splitLine[1]);//sc.nextInt();
			
			aptIndex = new Integer[Na];
			storeIndex = new Integer[Ns];
			
			splitLine = input.readLine().split(" ");		
			
			for(int i = 0; i < Na; i++) {
				aptIndex[i] = Integer.parseInt(splitLine[i]);//sc.nextInt();
			}
			
			splitLine = input.readLine().split(" ");
			
			for(int i = 0; i < Ns; i++) {
				storeIndex[i] = Integer.parseInt(splitLine[i]); //sc.nextInt();
			}
			
			input.close();
		}  catch (Exception e) {
			//e.printStackTrace();
		} 
	}
	
	
	
	public void dijkstra(int source) {
		//int u_index = 0;
		int u = 0;
		//int min = 1000000000;
		int tmp;
		boolean[] visited = new boolean[N+2];
		
		MyComparator c = new MyComparator();
		
		pQueue = new PriorityQueue<EdgeTo>(1, c);
		
		storeDist = new Hashtable<Integer, Integer>();
		nodePred  = new Hashtable<Integer, Integer>();
		
		for (int v = 1; v <= N; v++) {
			
			storeDist.put(v, 1000000000);
			nodePred.put(v, 0); //Outside node range (1-50000);
			
		}

		storeDist.put(source,0);

		pQueue.add(new EdgeTo(source, 0));

		
		while (pQueue.size() > 0 ) {

			u = pQueue.remove().nodeto;
					
			if (visited[u]) {continue;}
			
			visited[u] = true;
			
			for (int i = 0; i < graph.get(u).size(); i++) {
				EdgeTo v = graph.get(u).get(i);
				tmp = storeDist.get(u) + v.getCost();

				if ((tmp < storeDist.get(v.nodeto)) && (!visited[v.nodeto])) {
					storeDist.put(v.nodeto, tmp);
					nodePred.put(v.nodeto, u);
					pQueue.remove(new EdgeTo(v.nodeto, storeDist.get(v.nodeto)));
					pQueue.add(new EdgeTo(v.nodeto, tmp));
				}
			}
		}
		distances.put(source, storeDist);
	}// Dijkstra END
	
	public void computeDistances() {
		//long start, end, overallStart, overallEnd;
		
		ArrayList<EdgeTo> zeroNodeList = new ArrayList<EdgeTo>();
		
		int apt, min, index, zeroNode ;
		
		zeroNode = N+1; //Outside our graph's range.
		
		//overallStart = System.nanoTime();
		
		Arrays.sort(storeIndex);
		
		for(int i = 0; i < Ns; i++) {

			zeroNodeList.add(new EdgeTo(storeIndex[i], 0));
		}
		
		
		
		graph.put(zeroNode, zeroNodeList);
		
		dijkstra(zeroNode);
		
		//overallEnd = System.nanoTime();
		
		//System.out.println("Overall Dijkstra: " + ((overallEnd-overallStart)/1000000000.0) + " for " + Ns + " stores.");
		
		try {
			PrintWriter writer = new PrintWriter("stores.out", "UTF-8");
			
			for(int i = 0; i < Na; i++) {
				apt = aptIndex[i];
				//min = 1000000000;
				//index = -1;
				
				int pred = nodePred.get(apt);
				
				
				while (pred !=0 && nodePred.get(pred)!= zeroNode) {
					pred = nodePred.get(pred);
				}
				
				writer.print(pred + " ");

			}
		writer.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//long start = System.nanoTime();
		
		Stores city = new Stores("stores.in");
		
		//long read = System.nanoTime();
		
		//double reading = ((read - start) / 1000000000.0);
		
		//System.out.println(reading + " seconds to read.");
		
		city.computeDistances();
		
		//long end = System.nanoTime();
		//double duration = ((end - read) / 1000000000.0);
		//System.out.println(duration + " seconds to solve.");
		//System.out.println((duration + reading) + " seconds total.");
	}
}
