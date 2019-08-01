package tk.valoeghese.biomegen.example.layers;

import tk.valoeghese.biomegen.api.gen.BiomeLayers;
import tk.valoeghese.biomegen.example.Biome;

public class AddRiverBiomeLayer extends BiomeLayers {

	private static final int RIVER = Biome.RIVER.getId();
	private static final int FROZEN_RIVER = Biome.FROZEN_RIVER.getId();
	private static final int OCEAN = Biome.OCEAN.getId();
	private static final int DEEP_OCEAN = Biome.DEEP_OCEAN.getId();
	private static final int TUNDRA = Biome.TUNDRA.getId();
	private static final int MOUNTAINS = Biome.MOUNTAINS.getId();
	private static final int MOUNTAIN_PEAKS = Biome.MOUNTAIN_PEAKS.getId();
	
	private final BiomeLayers riverParent;
	
	public AddRiverBiomeLayer(BiomeLayers parent, BiomeLayers river, long worldSeed) {
		super(worldSeed, 2L);
		this.mainParent = parent;
		this.riverParent = river;
	}
	
	@Override
	public int[] sample(int startX, int startZ, int xSize, int zSize) {
		int[] sampledBiomes = this.mainParent.sample(startX, startZ, xSize, zSize);
		int[] sampledRivers = this.riverParent.sample(startX, startZ, xSize, zSize);
		
		int[] result = create(xSize, zSize);

		result = forEachCoordinate(result, startX, startZ, xSize, zSize, 
				(localX, localZ, totalX) -> {
					// equvalent position on sampled map from parent
					final int centre = localX * zSize + localZ;

					final int biomeSample = sampledBiomes[centre];
					final int riverSample = sampledRivers[centre];
					
					return sample(biomeSample, riverSample);
				});
		return result;
	}

	private int sample(int biomeSample, int riverSample) {
		if (riverSample == RIVER && !(biomeSample == OCEAN || biomeSample == DEEP_OCEAN)) {
			if (biomeSample == TUNDRA) {
				return FROZEN_RIVER;
			} else if (biomeSample == MOUNTAIN_PEAKS) {
				return MOUNTAINS;
			} else {
				return RIVER;
			}
		} else {
			return biomeSample;
		}
	}

}
