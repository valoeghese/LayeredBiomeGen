package tk.valoeghese.biomegen.gen;

public class SmoothBiomeLayer extends BiomeLayers {

	public SmoothBiomeLayer(BiomeLayers parent, long worldSeed, long initSeed) {
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
		int chance = 1;
		
		int borderResult = centreBiome;
		
		if ((northBiome != centreBiome) && this.nextInt(chance++) == 0) {
			borderResult = northBiome;
		}
		if ((eastBiome != centreBiome) && this.nextInt(chance++) == 0) {
			borderResult = eastBiome;
		}
		if ((southBiome != centreBiome) && this.nextInt(chance++) == 0) {
			borderResult = southBiome;
		}
		if ((westBiome != centreBiome) && this.nextInt(chance++) == 0) {
			borderResult = westBiome;
		}
		
		return this.nextInt(4) == 0 ? borderResult : centreBiome;
	}

}
