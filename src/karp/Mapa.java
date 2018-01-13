package karp;

import java.util.Random;

public class Mapa {
	
	private boolean sameDistance;
	private int cities;
	private int maxDistance;
	
	
	public Mapa(int cities, int maxDistance, boolean sameDistance) {
		setCities(cities);
		setMaxDistance(maxDistance);
		setSameDistance(sameDistance);
	}
	
	public Mapa(int cities, int maxDistance) {
		this(cities, maxDistance, true);
	}
	
	public int[][] setMatrix() {
		int[][] matrix = new int[cities][cities];
		Random generator = new Random();
		for(int i = 0; i < cities; i++)  {
			for(int j = 0; j < cities; j++)  {
				if(i > j && sameDistance) {
					matrix[i][j] = matrix[j][i];
				} else {
					matrix[i][j] = generator.nextInt(maxDistance + 1);
				}
			}
		}
		return matrix;
	}
	
	//setters
	public void setCities(int cities) {
		if(cities >= 24) {
			System.out.println("[WARNING]:\tMake sure you have enough memory "
					+ "in your system.\n\t\tExample: 24 cities takes 10G of RAM");
		}
		if(cities > Main.MAX_CITIES) {
			System.out.println("The maximun number of cities is " + Main.MAX_CITIES);
		}
		this.cities = Main.constrain(1, Main.MAX_CITIES, cities);
		
	}
	
	public void setMaxDistance(int maxDistance) {
		this.maxDistance = maxDistance;
	}
	
	public void setSameDistance(boolean sameDistance) {
		this.sameDistance = sameDistance;
	}
	
	//getters
	public int getCities() {
		return this.cities;
	}
	
	public int getMaxDistance() {
		return this.maxDistance;
	}
	
	public boolean getSameDistance() {
		return this.sameDistance;
	}
	
}
