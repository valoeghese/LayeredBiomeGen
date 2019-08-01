package tk.valoeghese.biomegen.api.gen;

public class WrapperLayer extends BiomeLayers {
	
	public WrapperLayer(BiomeLayers parent, long worldSeed) {
		super(worldSeed, 2L);
		this.mainParent = parent;
	}
	
	@Override
	public int[] sample(int startX, int startZ, int xSize, int zSize) {
		int[] sampledBiomes = this.mainParent.sample(startX, startZ, xSize, zSize);
		
		int[] result = create(xSize, zSize);

		result = forEachCoordinate(result, startX, startZ, xSize, zSize, 
				(localX, localZ, totalX) -> {
					return sampledBiomes[localX * zSize + localZ];
				});
		return result;
	}

}
