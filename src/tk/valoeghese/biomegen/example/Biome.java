package tk.valoeghese.biomegen.example;

import javafx.scene.paint.Color;
import tk.valoeghese.biomegen.api.gen.BiomeLayers;

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
	
	public static final Biome COASTAL_DUNES;
	public static final Biome DEEP_OCEAN;
	public static final Biome MOUNTAIN_PEAKS;
	public static final Biome RIVER;
	public static final Biome FROZEN_RIVER;
	
	public static final Biome FOREST_HILLS;
	public static final Biome SAVANNAH_PLATEAU;
	public static final Biome BEACH;
	public static final Biome MONTANE_SHRUBLAND;
	public static final Biome LAKE;
	
	public Biome(int id, Color colour, Humidity humidity) {
		this(id, colour, humidity, id);
	}
	
	public Biome(int id, Color colour, Humidity humidity, int parentId) {
		this.id = id;
		this.colour = colour;
		this.humidity = humidity;
		this.parent = parentId;
		
		lookup[id] = this;
		id_to_humidity[id] = humidity;
	}
	
	private final int parent;
	
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
	public final int getParent() {
		return parent;
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
		SAVANNAH = new Biome(8, Color.GREENYELLOW, Humidity.DRY);
		RAINFOREST = new Biome(9, Color.DARKOLIVEGREEN, Humidity.WET);
		CHAPARRAL = new Biome(10, Color.LIMEGREEN, Humidity.STANDARD);
		MARSH = new Biome(11, Color.VIOLET, Humidity.WET);
		
		COASTAL_DUNES = new Biome(12, Color.GOLD, Humidity.NONE);
		DEEP_OCEAN = new Biome(13, Color.DARKBLUE, Humidity.NONE);
		MOUNTAIN_PEAKS = new Biome(14, Color.DARKSLATEGRAY, Humidity.NONE, MOUNTAINS.id);
		RIVER = new Biome(15, Color.ROYALBLUE, Humidity.NONE);
		FROZEN_RIVER = new Biome(16, Color.SKYBLUE, Humidity.NONE);
		
		FOREST_HILLS = new Biome(17, Color.DARKGREEN, Humidity.HUMID);
		SAVANNAH_PLATEAU = new Biome(18, Color.LIGHTGREEN, Humidity.HUMID);
		BEACH = new Biome(19, Color.YELLOW, Humidity.NONE);
		MONTANE_SHRUBLAND = new Biome(20, Color.ROSYBROWN, Humidity.NONE);
		LAKE = new Biome(21, Color.ROYALBLUE, Humidity.NONE);
		
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
