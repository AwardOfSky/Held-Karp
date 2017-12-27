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
	
	//needed to profile
	List<PairInteger> result = new ArrayList<>();
	int[] a;
	HashMap<PairInteger, PairInteger> dict = new HashMap<>();
	int bits;
	int prev;
	Combinations combs;
	int size;
	int parent;
	int getDict;
	
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
		Map<PairInteger, PairInteger> dict = new HashMap<>();

		// Set distances between main cities (except inicial)
		// Optimizations are not crucial here
		for(int i = 0; i < size; i++) {
			if(i != startingCity) {
				dict.put(new PairInteger(1 << i, i),
						 new PairInteger(matrix[startingCity][i], startingCity));
			}
		}

		// Initialize the set (all the cities except the starting one)
		int[] set = new int[size - 1];
		for(int i = 0; i < size - 1; i++) {
			if(i < startingCity) {
				set[i] = i;
			} else {
				set[i] = i + 1;
			}
		}

		// Iterate subsets of increasing length until we reach all cities
		for(int subsetSize = 2; subsetSize < size; subsetSize++) {
			
			// Get combinations for a specific set
			combs = new Combinations(set, subsetSize);
			long numberCombs = combs.getTotal();
			
			// Iterate over all combinations - optimizations are crucial here
			for(long i = 0; i < numberCombs; i++) {
				a = combs.getNextSet();
				
				// Allocate bits for all elements in subset
				bits = 0;
				for(int j = 0; j < subsetSize; j++) {
					bits |= 1 << a[j];
				}
				
				// Find the shortest path to get to this subset
				for(int j = 0; j < subsetSize; j++) {
					
					// Set bits of previous set for each city in subset
					int prev = bits & ~(1 << a[j]);
					
					// Find all combinations to get to each city in subset
					// This pairs have in account the cost of the previous subset
					resultTemp = new ArrayList<>();
					for(int k = 0; k < subsetSize; k++) {
						if(a[k] != startingCity && a[k] != a[j]) {
							resultTemp.add(new PairInteger(
									dict.get(new PairInteger(prev,a[k])).first + matrix[a[k]][a[j]],
									a[k]));
						}
					}
					
					// Calculate the shortest distance among the pairs found
					min = maxPair;
					for(int k = 0; k < resultTemp.size(); k++) {
						if(resultTemp.get(k).first < min.first || (resultTemp.get(k).first == min.first
								&& resultTemp.get(k).second < min.second)) {
							min = resultTemp.get(k);
						}
					}
					dict.put(new PairInteger(bits, a[j]), min);
				}
			}
		}
		//The processing intensive part is over
		
		// Fill all bits except the one corresponding to the start city
		bits = (intPow(2, size) - 1) - (intPow(2, startingCity));
		
		// Gather pairs based on subsets leading to start city 
		resultTemp = new ArrayList<>();
		for(int i = 0; i < size; i++) {
			if(i != startingCity) {
				resultTemp.add(new PairInteger(
						dict.get(new PairInteger(bits, i)).first + matrix[i][startingCity],
						i));
			}
		}
		
		// Calculate the shortest of the pairs found
		min = maxPair;
		for(int k = 0; k < resultTemp.size(); k++) {
			if(resultTemp.get(k).first < min.first || (resultTemp.get(k).first == min.first
					&& resultTemp.get(k).second < min.second)) {
				min = resultTemp.get(k);
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
		
		// Add starting city to end and start for consistency 
		path.add(startingCity);
		path.add(0, startingCity);
		
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
	
}
