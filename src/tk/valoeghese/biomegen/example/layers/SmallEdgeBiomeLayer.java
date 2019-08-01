package tk.valoeghese.biomegen.example.layers;

import tk.valoeghese.biomegen.api.gen.BiomeLayers;
import tk.valoeghese.biomegen.example.Biome;

public class SmallEdgeBiomeLayer extends BiomeLayers {

	private static final int RAINFOREST = Biome.RAINFOREST.getId();
	private static final int CHAPARRAL = Biome.CHAPARRAL.getId();
	private static final int SWAMP = Biome.SWAMP.getId();
	private static final int MARSH = Biome.MARSH.getId();
	private static final int OCEAN = Biome.OCEAN.getId();
	private static final int DEEP_OCEAN = Biome.DEEP_OCEAN.getId();
	private static final int BEACH = Biome.BEACH.getId();
	private static final int DESERT = Biome.DESERT.getId();
	private static final int TUNDRA = Biome.TUNDRA.getId();
	private static final int MOUNTAINS = Biome.MOUNTAINS.getId();
	private static final int MOUNTAIN_PEAKS = Biome.MOUNTAIN_PEAKS.getId();
	
	public SmallEdgeBiomeLayer(BiomeLayers parent, long worldSeed, long initSeed) {
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
		if (centreBiome != OCEAN && centreBiome != DEEP_OCEAN) {
			if (northBiome == OCEAN || eastBiome == OCEAN || southBiome == OCEAN || westBiome == OCEAN ||
					northBiome == DEEP_OCEAN || eastBiome == DEEP_OCEAN || southBiome == DEEP_OCEAN || westBiome == DEEP_OCEAN) {
				return (centreBiome == DESERT || centreBiome == TUNDRA) ? centreBiome : BEACH;
			}
		} else if (centreBiome == DEEP_OCEAN) {
			if (northBiome != DEEP_OCEAN || eastBiome != DEEP_OCEAN || southBiome != DEEP_OCEAN || westBiome != DEEP_OCEAN) {
				return OCEAN;
			}
		}
		
		if (centreBiome == RAINFOREST) {
			if (northBiome == MARSH || eastBiome == MARSH || southBiome == MARSH || westBiome == MARSH) {
				return CHAPARRAL;
			}
		} else if (centreBiome == DESERT) {
			if (northBiome == SWAMP || eastBiome == SWAMP || southBiome == SWAMP || westBiome == SWAMP) {
				return CHAPARRAL;
			}
		} else if (centreBiome == MOUNTAIN_PEAKS) {
			if (!((northBiome == MOUNTAIN_PEAKS || northBiome == MOUNTAINS) &&
					(eastBiome == MOUNTAIN_PEAKS || eastBiome == MOUNTAINS) &&
					(southBiome == MOUNTAIN_PEAKS || southBiome == MOUNTAINS) &&
					(westBiome == MOUNTAIN_PEAKS || westBiome == MOUNTAINS))) {
				return MOUNTAINS;
			}
		}

		return centreBiome;
	}

}
