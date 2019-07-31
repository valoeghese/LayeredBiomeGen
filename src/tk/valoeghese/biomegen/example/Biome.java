package tk.valoeghese.biomegen.example;

import javafx.scene.paint.Color;

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
	
	public Biome(int id, Color colour) {
		this.id = id;
		this.colour = colour;
		lookup[id] = this;
		biomeCount++;
	}
	
	public static Biome[] lookup = new Biome[256];
	private static int biomeCount = 0;
	
	public final int getId() {
		return id;
	}
	public final Color getColour() {
		return colour;
	}
	
	public static final int getBiomeCount() {
		return biomeCount;
	}
	
	static {
		OCEAN = new Biome(0, Color.BLUE);
		DESERT = new Biome(1, Color.KHAKI);
		GRASSLAND = new Biome(2, Color.LIME);
		TAIGA = new Biome(3, Color.MEDIUMTURQUOISE);
		TUNDRA = new Biome(4, Color.FLORALWHITE);
		FOREST = new Biome(5, Color.FORESTGREEN);
		SWAMP = new Biome(6, Color.LIGHTSEAGREEN);
		MOUNTAINS = new Biome(7, Color.DARKGRAY);
		SAVANNAH = new Biome(8, Color.LAWNGREEN);
	}
}
