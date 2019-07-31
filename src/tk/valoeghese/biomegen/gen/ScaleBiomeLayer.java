package tk.valoeghese.biomegen.gen;

public final class ScaleBiomeLayer extends BiomeLayers {

	public ScaleBiomeLayer(BiomeLayers parent, long worldSeed) {
		super(worldSeed, 2L); // random functions are not used in the scale layer
		this.mainParent = parent;
	}

	@Override
	public int[] sample(int startX, int startZ, int xSize, int zSize) {
		int[] result = create(xSize, zSize);
		int transformZSize = (zSize + 1) >> 1;
		
		if (xSize == 0 && zSize == 0) {
			System.out.println("oh no");
		}
		
		int[] sampledBiomes = this.mainParent.sample(startX >> 1, startZ >> 1, (xSize + 1) >> 1, transformZSize);

		try {result = forEachCoordinate(result, startX, startZ, xSize, zSize, 
				(localX, localZ, totalX) -> {
					return sampledBiomes[(localX >> 1) * transformZSize + (localZ >> 1)];
				}
		);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(xSize);
			System.out.println(zSize);
			System.out.println(transformZSize);
			System.out.println(((xSize - 1) >> 1));
			System.out.println(((zSize - 1) >> 1));
			throw e;
		}

		return result;
	}
	
	public static BiomeLayers scaleBy(BiomeLayers layer, int amount, long worldSeed) {
		if (amount < 1) return layer;
		ScaleBiomeLayer scaleLayer = new ScaleBiomeLayer(layer, worldSeed);
		for (int i = 0; i < amount - 1; ++i) {
			scaleLayer = new ScaleBiomeLayer(scaleLayer, worldSeed);
		}
		return scaleLayer;
	}

}
