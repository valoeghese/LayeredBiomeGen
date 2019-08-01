package tk.valoeghese.biomegen.example.layers;

import tk.valoeghese.biomegen.api.gen.BiomeLayers;

public class FixPatchesBiomeLayer extends BiomeLayers {

	public FixPatchesBiomeLayer(BiomeLayers parent, long worldSeed, long initSeed) {
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
		if (northBiome != centreBiome && eastBiome != centreBiome && southBiome != centreBiome && westBiome != centreBiome) {
			return choose(northBiome, eastBiome, southBiome, westBiome);
		}
		return centreBiome;
	}

	private int choose(int... args) {
		return args[this.nextInt(args.length)];
	}

}
