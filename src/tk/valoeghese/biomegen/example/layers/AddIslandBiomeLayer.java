package tk.valoeghese.biomegen.example.layers;

import tk.valoeghese.biomegen.example.Biome;
import tk.valoeghese.biomegen.gen.BiomeLayers;

public class AddIslandBiomeLayer extends BiomeLayers {

	private static final int OCEAN = Biome.OCEAN.getId();
	private static final int LAND = Biome.DESERT.getId();
	
	private final boolean basicLand;
	
	public AddIslandBiomeLayer(BiomeLayers parent, long worldSeed, long initSeed, boolean justBasicLand) {
		super(worldSeed, initSeed);
		this.mainParent = parent;
		this.basicLand = justBasicLand;
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
		if (centreBiome == OCEAN) {
			if (northBiome == OCEAN && eastBiome == OCEAN && southBiome == OCEAN && westBiome == OCEAN) {
				if (basicLand) {
					return this.nextInt(150) == 0 ? LAND : OCEAN;
				} else {
					return this.nextInt(150) == 0 ? Biome.lookup[this.nextInt(Biome.getBiomeCount())].getId() : OCEAN;
				}
			}
		}
		return centreBiome;
	}

}
