package tk.valoeghese.biomegen.example;

import tk.valoeghese.biomegen.api.gen.WorldSettings;

public class OverworldWorldSettings implements WorldSettings {
	
	public static final OverworldWorldSettings DEFAULT = new OverworldWorldSettings(4);
	
	private final int biomeSize;
	
	public OverworldWorldSettings(int biomeSize) {
		this.biomeSize = biomeSize;
	}
	
	public int biomeSize() {
		return biomeSize;
	}
	
}
