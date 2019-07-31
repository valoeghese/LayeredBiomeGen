package tk.valoeghese.biomegen.example.layers;

import tk.valoeghese.biomegen.api.gen.BiomeLayers;

public class NoiseScaleBiomeLayer extends BiomeLayers {

	public NoiseScaleBiomeLayer(BiomeLayers parent, long worldSeed, long initSeed) {
		super(worldSeed, initSeed);
		
		this.mainParent = parent;
	}
	
	private int noiseSeed;

	@Override
	public int[] sample(int startX, int startZ, int xSize, int zSize) {
		int[] result = create(xSize, zSize);
		
		noiseSeed = (startX + ((startX * 8476723 - startZ) << 2));
		
		result = forEachCoordinate(result, startX, startZ, xSize, zSize, 
				(localX, localZ, totalX) -> {
					int totalZ = startZ + localZ;
					
					int[] values = this.mainParent.sample(totalX / 2, (totalZ) / 2, 2, 2);
					this.localiseSeed(totalX, totalZ);
					if (this.nextInt(12) == 0) {
						int noiseValue = noiseSeed % 4;
						if (noiseValue < 0) {
							noiseValue += 4;
						}
						noiseSeed *= noiseSeed * 8476723 + 1635297;
						noiseSeed += this.nextInt(2048);
						return values[noiseValue];
					} else {
						noiseSeed *= noiseSeed * 8476723 + 1635297;
						noiseSeed += this.nextInt(2048);
						return values[0];
					}
					
				}
		);
		
		return result;
	}

}
