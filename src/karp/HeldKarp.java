package karp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeldKarp {
	
	private int[][] matrix;
	private int startingCity;
	private static final int MAX_INT = Integer.MAX_VALUE;
	private static final PairInteger maxPair = new PairInteger(MAX_INT, MAX_INT);
	private int opt;
	private int dictionaryEntries;
	
	public HeldKarp(int[][] matrix, int startingCity) {
		setMatrix(matrix);
		setStartingCity(startingCity);
	}
	
	// This implementation of the Held Karp algorithm is based on the python
	// implementation over at https://github.com/CarlEkerot/held-karp
	public List<Integer> calculateHeldKarp() {
		List<Integer> resultSequence = new ArrayList<>(); // return list
		List<PairInteger> resultTemp; // temporary result
		PairInteger min; // minimun distance of temporary result
		int[] a; // current subset in the group
		int bits; // bit manipulation
		Combinations combs; // generate combinations of different sizes
		int size = matrix.length; //number of cities
		// Distance between two cities, first value is distance, second value is the last pair
		// Calculate size of dictionary and prevent it from resizing to save performance
		Map<PairInteger, PairInteger> dict = new HashMap<>(getEntriesNum(size - 1) + 1, (float)1.0);
		
		// Set distances between main cities (except inicial)
		// Optimizations are not crucial here
		for(int i = 1; i < size; i++) {
			dict.put(new PairInteger(1 << i, i),
					 new PairInteger(matrix[0][i], 0));
		}

		// Iterate subsets of increasing length until we reach all cities
		for(int subsetSize = 2; subsetSize < size; subsetSize++) {
			
			// Get combinations for a specific set
			combs = new Combinations(size - 1, subsetSize);
			
			// Iterate over all combinations - optimizations are crucial here
			while(combs.numLeft > 0) {
				a = combs.getNext();
				
				// Allocate bits for all elements in subset
				bits = 0;
				
				// Enhanced loops start gaining performance for large
				// arrays when we refer the element more than 1 time
				for(int j = 0; j < subsetSize; j++) {
					bits |= 1 << a[j];
				}
				
				// Find the shortest path to get to this subset
				for(int elementj : a) {
				
					// Set bits of previous set for each city in subset
					int prev = bits & ~(1 << elementj);
					
					// Find all combinations to get to each city in subset
					// This pairs have in account the cost of the previous subset
					resultTemp = new ArrayList<>();
					for(int elementk : a) {
						if(elementk != 0 && elementk != elementj) {
							resultTemp.add(new PairInteger(
									dict.get(new PairInteger(prev,elementk)).first + matrix[elementk][elementj],
									elementk));
						}
					}
					
					// Calculate the shortest distance among the pairs found
					min = maxPair;
					for(PairInteger element : resultTemp) {
						if(element.first < min.first || (element.first == min.first
								&& element.second < min.second)) {
							min = element;
						}
					}
					dict.put(new PairInteger(bits, elementj), min);
				}
			}
		}
		//The processing intensive part is over
		
		// Fill all bits except the one corresponding to the start city
		bits = intPow(2, size) - 2;
		
		// Gather pairs based on subsets leading to start city 
		resultTemp = new ArrayList<>();
		for(int i = 1; i < size; i++) {
			resultTemp.add(new PairInteger(
					dict.get(new PairInteger(bits, i)).first + matrix[i][0],
					i));
		}
		
		// Calculate the shortest of the pairs found
		min = maxPair;
		for(PairInteger element : resultTemp) {
			if(element.first < min.first || (element.first == min.first
					&& element.second < min.second)) {
				min = element;
			}
		}
		
		// Optimal path and cost to get there
		this.opt = min.first;
		int parent = min.second;
		
		// Backtrack to find the full path
		List<Integer> path = new ArrayList<>();
		for(int i = 0; i < size - 1; i++) {
			path.add(parent);
			int newBits = bits & ~(1 << parent);
			parent = dict.get(new PairInteger(bits, parent)).second;
			bits = newBits;
		}

		// Start at starting city
		path.add(0);
		while(path.get(0) != startingCity) {
			path.add(path.get(0));
			path.remove(0);
		}
		path.add(startingCity);
		
		// Reverse list because of backtrack and return the result
		for(int i = path.size() - 1; i >= 0; i--) {
			resultSequence.add(path.get(i));
		}
		this.dictionaryEntries = dict.size();
		
		return resultSequence;
	}
	
	//setters
	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}
	
	public void setStartingCity(int startingCity) {
		if(startingCity < 0 || startingCity >= this.matrix.length) {
			this.startingCity = 0;
		} else {
			this.startingCity = startingCity;
		}
	}
	
	public void setOpt(int opt) {
		this.opt = opt;
	}
	
	public void dictionaryEntries(int dictionaryEntries) {
		this.dictionaryEntries = dictionaryEntries;
	}
	
	//getters
	public int[][] getMatrix() {
		return this.matrix;
	}
	
	public int getStartingCity() {
		return this.startingCity;
	}
	
	public int getOpt() {
		return this.opt;
	}
	
	public int getDictionaryEntries() {
		return this.dictionaryEntries;
	}
	
	public static int intPow(int a, int b) {
		int res = 1;
        while (b > 0) {
            if ((b & 1) == 1) {
                res *= a;        
            }
            b >>= 1;
            a *= a; 
        }
        return res;
	}
	
	public static int getEntriesNum(int cities) {
		return cities * intPow(2, (cities - 1));
	}
	
}
