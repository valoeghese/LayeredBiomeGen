package tk.valoeghese.biomegen.example.layers;

import tk.valoeghese.biomegen.api.gen.BiomeLayers;
import tk.valoeghese.biomegen.example.Biome;

public class RiverBiomeLayer extends BiomeLayers {
	
	private static final int RIVER = Biome.RIVER.getId();
	
	public RiverBiomeLayer(BiomeLayers parent, long worldSeed, long initSeed) {
		super(worldSeed, initSeed);
		this.mainParent = parent;
	}
	
	@Override
	public int[] sample(int startX, int startZ, int xSize, int zSize) {
		int[] sampledBiomes = this.mainParent.sample(startX - 1, startZ - 1, xSize + 2, zSize + 2);

		int[] result = create(xSize, zSize);

		result = forEachCoordinate(result, startX, startZ, xSize, zSize, 
				(localX, localZ, totalX) -> {
					int totalZ = startZ + localZ;
					this.localiseSeed(totalX, totalZ);

					// equvalent position on sampled map from parent
					final int centre = (localX + 1) * (zSize + 2) + localZ + 1;

					final int centreNoise = sampledBiomes[centre];
					final int northNoise = sampledBiomes[centre + 1];
					final int eastNoise = sampledBiomes[centre + zSize + 2];
					final int southNoise = sampledBiomes[centre - 1];
					final int westNoise = sampledBiomes[centre - zSize - 2];
					
					return sample(centreNoise, northNoise, eastNoise, southNoise, westNoise, totalX, totalZ);
				});
		return result;
	}

	private int sample(int centre, int north, int east, int south, int west, int x, int z) {
		if (centre > 2 || north > 3 || east > 3 || south > 3 || west > 3) {
			return RIVER;
		} else {
			return 0;
		}
	}

}
