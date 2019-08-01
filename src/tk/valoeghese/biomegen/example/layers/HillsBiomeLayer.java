package tk.valoeghese.biomegen.example.layers;

import tk.valoeghese.biomegen.api.gen.BiomeLayers;
import tk.valoeghese.biomegen.example.Biome;

public class HillsBiomeLayer extends BiomeLayers {
	
	private static final int OCEAN = Biome.OCEAN.getId();
	private static final int DEEP_OCEAN = Biome.DEEP_OCEAN.getId();
	
	private static final int SAVANNAH = Biome.SAVANNAH.getId();
	private static final int SAVANNAH_PLATEAU = Biome.SAVANNAH_PLATEAU.getId();
	private static final int FOREST = Biome.FOREST.getId();
	private static final int FOREST_HILLS = Biome.FOREST_HILLS.getId();
	private static final int GRASSLAND = Biome.GRASSLAND.getId();
	private static final int MOUNTAINS = Biome.MOUNTAINS.getId();
	private static final int MONTANE_SHRUBLAND = Biome.MONTANE_SHRUBLAND.getId();

	private final BiomeLayers noiseParent;

	public HillsBiomeLayer(BiomeLayers parent, BiomeLayers noise, long worldSeed, long initSeed) {
		super(worldSeed, initSeed);
		this.mainParent = parent;
		this.noiseParent = noise;
	}

	@Override
	public int[] sample(int startX, int startZ, int xSize, int zSize) {
		int[] sampledBiomes = this.mainParent.sample(startX - 1, startZ - 1, xSize + 2, zSize + 2);
		int[] sampledNoise = this.mainParent.sample(startX, startZ, xSize, zSize);

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

					return sample(centreBiome, Biome.lookup[northBiome].getParent(), Biome.lookup[eastBiome].getParent(), Biome.lookup[southBiome].getParent(), Biome.lookup[westBiome].getParent(), sampledNoise[localX * zSize + localZ]);
				});
		return result;
	}

	private int sample(int centreBiome, int northBiome, int eastBiome, int southBiome, int westBiome, int noise) {

		if (centreBiome != OCEAN && centreBiome != DEEP_OCEAN && noise < 5 && this.nextInt(3) == 0) {

			int parent = Biome.lookup[centreBiome].getParent();
			int hills = parent;
			
			if (hills == GRASSLAND) {
				hills = this.nextInt(3) == 0 ? FOREST_HILLS : FOREST;
			} else if (hills == FOREST) {
				hills = FOREST_HILLS;
			} else if (hills == MOUNTAINS) {
				hills = this.nextInt(2) == 0 ? MONTANE_SHRUBLAND : centreBiome;
			} else if (hills == SAVANNAH) {
				hills = SAVANNAH_PLATEAU;
			}
			
			if (hills != centreBiome) {
				int similarity = 0;
				if (parent == northBiome) {
					++similarity;
				}
				if (parent == eastBiome) {
					++similarity;
				}
				if (parent == westBiome) {
					++similarity;
				}
				if (parent == southBiome) {
					++similarity;
				}
				
				return this.nextInt(4) < similarity ? hills : centreBiome;
			}
		}
		return centreBiome;
	}

}
