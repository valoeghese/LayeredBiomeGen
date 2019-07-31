package tk.valoeghese.biomegen.example.layers;

import tk.valoeghese.biomegen.api.gen.BiomeLayers;
import tk.valoeghese.biomegen.example.Biome;
import tk.valoeghese.biomegen.example.Biome.Humidity;

public class EdgeBiomeLayer extends BiomeLayers {

	private static final int RAINFOREST = Biome.RAINFOREST.getId();
	private static final int MOUNTAINS = Biome.MOUNTAINS.getId();
	private static final int CHAPARRAL = Biome.CHAPARRAL.getId();
	private static final int SWAMP = Biome.SWAMP.getId();
	private static final int MARSH = Biome.MARSH.getId();
	private static final int OCEAN = Biome.OCEAN.getId();
	private static final int DEEP_OCEAN = Biome.DEEP_OCEAN.getId();
	private static final int BEACH = Biome.BEACH.getId();
	
	public EdgeBiomeLayer(BiomeLayers parent, long worldSeed, long initSeed) {
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
		if (centreBiome == OCEAN) {
			if (northBiome == SWAMP || eastBiome == SWAMP || southBiome == SWAMP || westBiome == SWAMP ||
					northBiome == RAINFOREST || eastBiome == RAINFOREST || southBiome == RAINFOREST || westBiome == RAINFOREST) {
				return BEACH;
			}
		} else if (centreBiome == DEEP_OCEAN) {
			if (northBiome != DEEP_OCEAN || eastBiome != DEEP_OCEAN || southBiome != DEEP_OCEAN || westBiome != DEEP_OCEAN) {
				return OCEAN;
			}
		}
		
		Humidity humidity = Biome.id_to_humidity[centreBiome];
		
		if (humidity == Humidity.DESERT) {
			if (northBiome == RAINFOREST || eastBiome == RAINFOREST || southBiome == RAINFOREST || westBiome == RAINFOREST) {
				return MOUNTAINS;
			}
		} else if (humidity == Humidity.DRY) {
			if (northBiome == RAINFOREST || eastBiome == RAINFOREST || southBiome == RAINFOREST || westBiome == RAINFOREST) {
				return CHAPARRAL;
			}
		} else if (centreBiome == SWAMP) {
			if (northBiome == MOUNTAINS || eastBiome == MOUNTAINS || southBiome == MOUNTAINS || westBiome == MOUNTAINS) {
				return MARSH;
			}
		}

		return centreBiome;
	}

}
