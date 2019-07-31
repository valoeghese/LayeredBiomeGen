package tk.valoeghese.biomegen.example;

import javafx.scene.paint.Color;
import tk.valoeghese.biomegen.gen.BiomeLayers;

public final class Biome {
	private final int id;
	private final Color colour;
	
	public static final Biome OCEAN;
	public static final Biome DESERT;
	public static final Biome GRASSLAND;
	public static final Biome TAIGA;
	public static final Biome TUNDRA;
	public static final Biome FOREST;
	public static final Biome SWAMP;
	public static final Biome MOUNTAINS;
	public static final Biome SAVANNAH;
	public static final Biome RAINFOREST;
	public static final Biome CHAPARRAL;
	public static final Biome MARSH;
	public static final Biome BEACH;
	
	public Biome(int id, Color colour, Humidity humidity) {
		this.id = id;
		this.colour = colour;
		this.humidity = humidity;
		
		lookup[id] = this;
		id_to_humidity[id] = humidity;
	}
	
	public final Humidity humidity;
	
	public static Biome[] lookup = new Biome[256];
	public static Humidity[] id_to_humidity = new Humidity[256];
	
	private static final int[] valid_islands;
	
	public final int getId() {
		return id;
	}
	public final Color getColour() {
		return colour;
	}
	
	public static final int pickValidIsland(BiomeLayers rand) {
		return valid_islands[rand.nextInt(valid_islands.length)];
	}
	
	static {
		OCEAN = new Biome(0, Color.BLUE, Humidity.NONE);
		DESERT = new Biome(1, Color.KHAKI, Humidity.DESERT);
		GRASSLAND = new Biome(2, Color.LIME, Humidity.STANDARD);
		TAIGA = new Biome(3, Color.MEDIUMTURQUOISE, Humidity.STANDARD);
		TUNDRA = new Biome(4, Color.FLORALWHITE, Humidity.DRY);
		FOREST = new Biome(5, Color.FORESTGREEN, Humidity.HUMID);
		SWAMP = new Biome(6, Color.PURPLE, Humidity.HUMID);
		MOUNTAINS = new Biome(7, Color.DARKGRAY, Humidity.NONE);
		SAVANNAH = new Biome(8, Color.LAWNGREEN, Humidity.DRY);
		RAINFOREST = new Biome(9, Color.DARKOLIVEGREEN, Humidity.WET);
		CHAPARRAL = new Biome(10, Color.LIMEGREEN, Humidity.STANDARD);
		MARSH = new Biome(11, Color.VIOLET, Humidity.WET);
		BEACH = new Biome(12, Color.BEIGE, Humidity.NONE);
		
		valid_islands = new int[]{DESERT.id, GRASSLAND.id, TAIGA.id, TUNDRA.id, FOREST.id, RAINFOREST.id, CHAPARRAL.id};
	}
	
	public static enum Humidity {
		DESERT(0),
		DRY(1),
		STANDARD(2),
		HUMID(3),
		WET(4),
		
		NONE(2);
		
		private Humidity(int intVal) {
			this.val = intVal;
		}
		private final int val;
		
		public int intValue() {
			return val;
		}
	}
}
