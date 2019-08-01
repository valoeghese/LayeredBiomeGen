package tk.valoeghese.biomegen.example.layers;

import tk.valoeghese.biomegen.api.gen.BiomeLayers;
import tk.valoeghese.biomegen.example.Biome;

public class InitContinentBiomeLayer extends BiomeLayers {
	
	private static final int OCEAN = Biome.OCEAN.getId();
	private static final int LAND = Biome.DESERT.getId();
	
	public InitContinentBiomeLayer(long worldSeed, long initSeed) {
		super(worldSeed, initSeed);
	}

	@Override
	public int[] sample(int startX, int startZ, int xSize, int zSize) {
		int[] result = create(xSize, zSize);
		
		result = forEachCoordinate(result, startX, startZ, xSize, zSize, 
				(localX, localZ, totalX) -> {
					int totalZ = startZ + localZ;
					if (totalX == 0 && totalZ == 0) {
						return LAND;
					}
					this.localiseSeed(totalX, totalZ);
					return (this.nextInt(7) == 0) ? LAND : OCEAN;
				});
		return result;
	}
}
