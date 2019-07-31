package tk.valoeghese.biomegen.example.layers;

import tk.valoeghese.biomegen.api.gen.BiomeLayers;
import tk.valoeghese.biomegen.example.Biome;

public class ShapeLandBiomeLayer extends BiomeLayers {

	private static final int OCEAN = Biome.OCEAN.getId();
	private static final int FOREST = Biome.FOREST.getId();
	private final boolean doIsland;
	
	public ShapeLandBiomeLayer(BiomeLayers parent, long worldSeed, long initSeed) {
		this(parent, worldSeed, initSeed, true);
	}
	
	public ShapeLandBiomeLayer(BiomeLayers parent, long worldSeed, long initSeed, boolean island) {
		super(worldSeed, initSeed);
		this.mainParent = parent;
		this.doIsland = island;
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
		if (centreBiome != OCEAN) {
			if (northBiome == OCEAN || eastBiome == OCEAN || southBiome == OCEAN || westBiome == OCEAN) {
				if (this.nextInt(6) == 0) {
					return OCEAN;
				}
			}
			return centreBiome;
		} else if (doIsland && (northBiome == OCEAN && eastBiome == OCEAN && southBiome == OCEAN && westBiome == OCEAN)) {
			return this.nextInt(50) == 0 ? FOREST : OCEAN;
		} else {
			int chance = 1; // pick equally among all non-ocean biomes by incrementing this number after each successful non-ocean detection.
			//                 (one non-ocean = certain, two non-oceans = pick first one (100% chance for all)
			//                 1/2 chance to be second one (overall, 50% chance for all).
			//                 And so on.
			int landResult = 1; // temporary initialisation to something else to fix compile errors

			if (northBiome != OCEAN && this.nextInt(chance++) == 0) {
				landResult = northBiome;
			}
			if (eastBiome != OCEAN && this.nextInt(chance++) == 0) {
				landResult = eastBiome;
			}
			if (southBiome != OCEAN && this.nextInt(chance++) == 0) {
				landResult = southBiome;
			}
			if (westBiome != OCEAN && this.nextInt(chance++) == 0) {
				landResult = westBiome;
			}
			
			// chance to be replace ocean with land here is dependent on how much land borders this tile
			// 100% chance if surrounded by land
			return this.nextInt(4) < (chance - 1) ? landResult : OCEAN;
		}
	}

}
