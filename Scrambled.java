import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;



public class Scrambled {
	int cards; 		   // Total number of cards;
	Integer[] balance; // Vector containing the balance of a given card;
	
	public Scrambled(String fileName) {
		
		try {
			BufferedReader input = new BufferedReader(new FileReader(fileName));
			String[] splitLine = input.readLine().split(" ");
			
			cards = Integer.parseInt(splitLine[0]);
			balance = new Integer[cards];
			
			splitLine = input.readLine().split(" ");
			
			for(int i = 0; i < cards; i++) {
				balance[i] = Integer.parseInt(splitLine[i]);
			}
			input.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public long countMergeInversions(Integer[] array, Integer[] temp, int leftBoundary, int pivot, int rightBoundary) {
		long inversions = 0;
		int li, tempIndex, ri; //Another set of indexes - left, temp, right;
		
		li = leftBoundary; // [li->..... PIVOT - 1 / ri ->.......]
		ri = pivot;
		tempIndex = leftBoundary;
		
		while((li <= pivot - 1) && (ri <= rightBoundary)) {
			
			if(array[li] <= array[ri]) {
				temp[tempIndex] = array[li]; 
				li++;
			} else {
				temp[tempIndex] = array[ri];			
				ri++;
				
				inversions += pivot - li; //We basically have (pivot - li) elements
										  //greater than array[ri] (since they're sorted)
			}							  //and each has to be swapped over there after ri.
			tempIndex++;
		}
		
		while(li <= pivot - 1) {

			temp[tempIndex] = array[li];
			li++;
			tempIndex++;
		}
		
		while(ri <= rightBoundary) {

			temp[tempIndex] = array[ri];
			ri++;
			tempIndex++;
		}
		
		for(int i = leftBoundary; i <= rightBoundary; i++) {
			array[i] = temp[i];
		}
		
		return inversions;
	}
	
	public long countInversions(Integer[] array, Integer[] temp, int leftIndex, int rightIndex) {
		long inversions = 0;
		int pivot = 0;
		
		if(leftIndex < rightIndex) {
			pivot = (leftIndex + rightIndex) / 2;
			
			inversions += countInversions(array, temp, leftIndex, pivot);
			inversions += countInversions(array, temp, pivot + 1, rightIndex);
			
			inversions += countMergeInversions(array, temp, leftIndex, pivot + 1, rightIndex);
		}
		
		return inversions;
	}
	
	public void compute() {
		long scrambled = 0; // Number of scrambled cards;
		Integer[] temp = new Integer[cards];
		
		scrambled = countInversions(balance, temp, 0, (cards - 1));
		
		//System.out.println(scrambled);
		
		try {
			PrintWriter writer = new PrintWriter("scrambled.out", "UTF-8");
			
			writer.print(scrambled);
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Scrambled myCards = new Scrambled("scrambled.in");
		
		myCards.compute();
	}
}
