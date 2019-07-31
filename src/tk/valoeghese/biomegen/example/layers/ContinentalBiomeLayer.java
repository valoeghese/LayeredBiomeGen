package tk.valoeghese.biomegen.example.layers;

import tk.valoeghese.biomegen.api.gen.BiomeLayers;
import tk.valoeghese.biomegen.example.Biome;

public class ContinentalBiomeLayer extends BiomeLayers {
	
	private static final int COLD_CLIMATE = Biome.TUNDRA.getId();
	private static final int HOT_CLIMATE = Biome.DESERT.getId();
	private static final int WARM_CLIMATE = Biome.GRASSLAND.getId();
	private static final int COOL_CLIMATE = Biome.TAIGA.getId();
	
	private static final int[] COLD_BIOMES = {Biome.TUNDRA.getId(), Biome.TUNDRA.getId(), Biome.TAIGA.getId()};
	private static final int[] HOT_BIOMES = {Biome.DESERT.getId(), Biome.DESERT.getId(), Biome.SAVANNAH.getId(), Biome.RAINFOREST.getId()};
	private static final int[] WARM_BIOMES = {Biome.GRASSLAND.getId(), Biome.FOREST.getId(), Biome.MOUNTAINS.getId(), Biome.SWAMP.getId()};
	private static final int[] COOL_BIOMES = {Biome.GRASSLAND.getId(), Biome.GRASSLAND.getId(), Biome.TAIGA.getId(), Biome.TAIGA.getId(), Biome.MOUNTAINS.getId(), Biome.MOUNTAINS.getId(), Biome.MARSH.getId()};
	
	private static final int OCEAN = Biome.OCEAN.getId();
	private static final int DEEP_OCEAN = Biome.DEEP_OCEAN.getId();
	
	public ContinentalBiomeLayer(BiomeLayers parent, long worldSeed, long initSeed) {
		super(worldSeed, initSeed);
		this.mainParent = parent;
	}

	@Override
	public int[] sample(int startX, int startZ, int xSize, int zSize) {
		int[] sampledBiomes = this.mainParent.sample(startX, startZ, xSize, zSize);

		int[] result = create(xSize, zSize);

		result = forEachCoordinate(result, startX, startZ, xSize, zSize, 
				(localX, localZ, totalX) -> {
					int totalZ = startZ + localZ;
					this.localiseSeed(totalX, totalZ);

					// equvalent position on sampled map from parent
					final int centre = (localX * zSize) + localZ;
					final int centreBiome = sampledBiomes[centre];
					

					return sample(centreBiome);
				});
		return result;
	}

	private int sample(int centreBiome) {
		if (centreBiome == HOT_CLIMATE) {
			return HOT_BIOMES[this.nextInt(HOT_BIOMES.length)];
		} else if (centreBiome == WARM_CLIMATE) {
			return WARM_BIOMES[this.nextInt(WARM_BIOMES.length)];
		} else if (centreBiome == COOL_CLIMATE) {
			return COOL_BIOMES[this.nextInt(COOL_BIOMES.length)];
		} else if (centreBiome == COLD_CLIMATE) {
			return COLD_BIOMES[this.nextInt(COLD_BIOMES.length)];
		} else if (centreBiome == DEEP_OCEAN && this.nextInt(12) == 0) {
			return OCEAN;
		} else {
			return centreBiome;
		}
	}

}
