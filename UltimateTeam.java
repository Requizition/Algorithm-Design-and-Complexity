import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class UltimateTeam {
	int N, M;
	long B, initB;
	ArrayList<Integer> P;
	
	public UltimateTeam(String filename) {
		Scanner sc = null;
		P = new ArrayList<Integer>();
		
		try {
			sc = new Scanner(new File(filename));
			
			this.N = sc.nextInt();
			this.B = sc.nextInt();
			this.M = sc.nextInt();
			
			this.initB = B;
			
			for (int i = 0; i < N; i++) {
				P.add(i, sc.nextInt());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (sc != null) {
					sc.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void profit() {
		int i = 0; //Counter
		
		while(i < N) {
			switch(M) { //Do we own a 'Messi' or not;
			
				case 0: //We don't, so when should we buy a 'Messi'?
					while(i < (N-1)) {
					//If no price increase, keep waiting.
						if(P.get(i) >= P.get(i+1)) {
							i++;
						}else {
							break;
						}
					}
					if(i == (N-1)) { //We don't have time to turn a profit.
						i++;
						break;
					}
					if(P.get(i) <= B) { //We can afford to buy.
						B -= P.get(i);
						M = 1;
						i++;
						break;
					}
					i++; //We couldn't afford to buy.
					break;
					
				case 1: //We do, so when should we sell a 'Messi'?
					while(i < (N-1)) {
						if(P.get(i) <= P.get(i+1)) {
							//Wait for the price to rise.
							i++;
						}else {
							break;
						}
					}
					B += P.get(i); //We always sell at the highest price before a dip
					M = 0;     	   //whether it is or isn't the final day.
					i++;
					break;
			}
		}
		try{
		    PrintWriter writer = new PrintWriter("ultimate_team.out", "UTF-8");
		    //The profit is anything we made aside from what we had.
		    writer.print(B - initB); 	
		    writer.close();
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		UltimateTeam myTeam = new UltimateTeam("ultimate_team.in");
		
		myTeam.profit();
	}
}