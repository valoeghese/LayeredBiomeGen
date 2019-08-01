package tk.valoeghese.biomegen.example.layers;

import tk.valoeghese.biomegen.api.gen.BiomeLayers;

public class SimpleNoiseBiomeLayer extends BiomeLayers {
	
	public SimpleNoiseBiomeLayer(BiomeLayers parent, long worldSeed, long initSeed) {
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

					final int centreBiome = sampledBiomes[centre];
					final int northBiome = sampledBiomes[centre + 1];
					final int eastBiome = sampledBiomes[centre + zSize + 2];
					final int southBiome = sampledBiomes[centre - 1];
					final int westBiome = sampledBiomes[centre - zSize - 2];

					return sample(centreBiome, northBiome, eastBiome, southBiome, westBiome);
				});
		return result;
	}

	private int sample(int centreBiome, int northBiome, int eastBiome, int southBiome, int westBiome) {
		int surround = 0;
		if (centreBiome != northBiome) {
			surround++;
		}
		if (centreBiome != eastBiome) {
			surround++;
		}
		if (centreBiome != southBiome) {
			surround++;
		}
		if (centreBiome != westBiome) {
			surround++;
		}

		return surround + this.nextInt(2 + (surround * 2));
		// no surround 0-1, single-surround 1-4, double surround 2-7, triple surround 3-10, full surround 4-13. 
		// because of this we know that if the number is above 1 there must be surround
		// if the number is 4 it could be any surround
		// etc
	}

}
