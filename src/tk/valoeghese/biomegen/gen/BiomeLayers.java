package tk.valoeghese.biomegen.gen;

import tk.valoeghese.biomegen.api.BiomeLayerFactory;
import tk.valoeghese.biomegen.api.BiomeSampler;

public abstract class BiomeLayers implements BiomeSampler {

	private long localSeed_init;
	private long localSeed_world;
	private long localSeed;
	
	protected BiomeLayers mainParent = null;
	
	public static final BiomeLayers build(long worldSeed, BiomeLayerFactory factory) {
		return factory.build(worldSeed);
	}
	
	protected BiomeLayers(long worldSeed, long initSeed) {
		localSeed_init = initSeed;

		localSeed_init *= localSeed_init * 4561342362356903605L + 1384803789269053357L;
		localSeed_init += initSeed;
		localSeed_init *= localSeed_init * 4561342362356903605L + 1384803789269053357L;
		localSeed_init += initSeed;
		localSeed_init *= localSeed_init * 4561342362356903605L + 1384803789269053357L;
		localSeed_init += initSeed;

		initWorldSeed(worldSeed);
	}

	public BiomeLayers initWorldSeed(long worldSeed) {
		localSeed_world = worldSeed;

		localSeed_world *= localSeed_world * 4561342362356903605L + 1384803789269053357L;
		localSeed_world += localSeed_init;
		localSeed_world *= localSeed_world * 4561342362356903605L + 1384803789269053357L;
		localSeed_world += localSeed_init;
		localSeed_world *= localSeed_world * 4561342362356903605L + 1384803789269053357L;
		localSeed_world += localSeed_init;

		return this;
	}

	public BiomeLayers localiseSeed(long x, long z) {
		localSeed = localSeed_world;

		localSeed *= localSeed * 4561342362356903605L + 1384803789269053357L;
		localSeed += x;
		localSeed *= localSeed * 4561342362356903605L + 1384803789269053357L;
		localSeed += z;
		localSeed *= localSeed * 4561342362356903605L + 1384803789269053357L;
		localSeed += x;
		localSeed *= localSeed * 4561342362356903605L + 1384803789269053357L;
		localSeed += z;

		return this;
	}

	public int nextInt(int bound) {
		int result = (int)((localSeed >> 24) % (long)bound);
		
		if (result < 0) {
			result += bound;
		}

		localSeed *= localSeed * 4561342362356903605L + 1384803789269053357L;
		localSeed += localSeed_world;
		
		return result;
	}
	
	protected static final int[] create(int xSize, int zSize) {
		return new int[xSize * zSize];
	}
	
	protected static final int[] forEachCoordinate(int[] arr, int startX, int startZ, int xSize, int zSize, CoordinateSampleIntFunction function) {
		for (int localX = 0; localX < xSize; ++localX) {
			int totalX = startX + localX;
			for (int localZ = 0; localZ < zSize; ++localZ) {
				arr[localX * zSize + localZ] = function.apply(localX, localZ, totalX);
			}
		}
		return arr;
	}
	
	protected static interface CoordinateSampleIntFunction {
		public int apply(int localX, int localZ, int totalX);
	}
}
