package tk.valoeghese.biomegen.example.layers;

import tk.valoeghese.biomegen.example.Biome;
import tk.valoeghese.biomegen.gen.BiomeLayers;

public final class AddClimateLayers {
	private static final int OCEAN = Biome.OCEAN.getId();
	private static final int FOREST = Biome.FOREST.getId();
	private static final int COLD_CLIMATE = Biome.TUNDRA.getId();
	private static final int HOT_CLIMATE = Biome.DESERT.getId();
	private static final int WARM_CLIMATE = Biome.GRASSLAND.getId();
	private static final int COOL_CLIMATE = Biome.TAIGA.getId();
	private static final int MOUNTAINS = Biome.MOUNTAINS.getId();

	public static class AddColdClimateBiomeLayer extends BiomeLayers {

		public AddColdClimateBiomeLayer(BiomeLayers parent, long worldSeed, long initSeed) {
			super(worldSeed, initSeed);
			this.mainParent = parent;
		}

		@Override
		public int[] sample(int startX, int startZ, int xSize, int zSize) {
			int[] sampledBiomes = this.mainParent.sample(startX, startZ, xSize, zSize);

			int[] result = create(xSize, zSize);

			forEachCoordinate(result, startX, startZ, xSize, zSize, 
					(localX, localZ, totalX) -> {
						int sampledBiome = sampledBiomes[localX * zSize + localZ];
						if (sampledBiome == FOREST) {
							return FOREST;
						} else if (sampledBiome != OCEAN) {
							this.localiseSeed(totalX, localZ + startZ);
							return this.nextInt(4) == 0 ? COLD_CLIMATE : sampledBiome;
						} else {
							return OCEAN;
						}
					}
					);
			return result;
		}

	}

	abstract static class EaseClimateBiomeLayer extends BiomeLayers {

		protected EaseClimateBiomeLayer(BiomeLayers parent, long worldSeed, long initSeed) {
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

		abstract protected int sample(int centreBiome, int northBiome, int eastBiome, int southBiome, int westBiome);
	}

	public static class EaseHotColdBiomeLayer extends EaseClimateBiomeLayer {

		public EaseHotColdBiomeLayer(BiomeLayers parent, long worldSeed, long initSeed) {
			super(parent, worldSeed, initSeed);
		}

		@Override
		protected int sample(int centreBiome, int northBiome, int eastBiome, int southBiome, int westBiome) {
			if (centreBiome == HOT_CLIMATE) {
				if (northBiome == COLD_CLIMATE || eastBiome == COLD_CLIMATE || southBiome == COLD_CLIMATE || westBiome == COLD_CLIMATE) {
					if (this.nextInt(5) == 0) {
						return MOUNTAINS;
					} else {
						return WARM_CLIMATE;
					}
				} else {
					return centreBiome;
				}

			} else if (centreBiome == COLD_CLIMATE) {
				return this.nextInt(5) == 0 ? COOL_CLIMATE : COLD_CLIMATE;
			} else {
				return centreBiome;
			}
		}
	}

	public static class EaseWarmCoolBiomeLayer extends EaseClimateBiomeLayer {

		public EaseWarmCoolBiomeLayer (BiomeLayers parent, long worldSeed, long initSeed) {
			super(parent, worldSeed, initSeed);
		}

		@Override
		protected int sample(int centreBiome, int northBiome, int eastBiome, int southBiome, int westBiome) {
			if (centreBiome == WARM_CLIMATE) {
				if (northBiome == COLD_CLIMATE || eastBiome == COLD_CLIMATE || southBiome == COLD_CLIMATE || westBiome == COLD_CLIMATE) {
					return COOL_CLIMATE;
				}
			} else if (centreBiome == COOL_CLIMATE) {
				if (northBiome == HOT_CLIMATE || eastBiome == HOT_CLIMATE || southBiome == HOT_CLIMATE || westBiome == HOT_CLIMATE) {
					return WARM_CLIMATE;
				}
			}
			return centreBiome;
		}
	}

	public static class AddMountainRangeBiomeLayer extends EaseClimateBiomeLayer {

		public AddMountainRangeBiomeLayer(BiomeLayers parent, long worldSeed, long initSeed) {
			super(parent, worldSeed, initSeed);
		}

		@Override
		protected int sample(int centreBiome, int northBiome, int eastBiome, int southBiome, int westBiome) {
			if (centreBiome == MOUNTAINS) {
				if (northBiome == HOT_CLIMATE || eastBiome == HOT_CLIMATE || southBiome == HOT_CLIMATE || westBiome == HOT_CLIMATE) {
					return MOUNTAINS;
				} else {
					return COOL_CLIMATE;
				}
			} else {
				return centreBiome;
			}
		}
	}
}
