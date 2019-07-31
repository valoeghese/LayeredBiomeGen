package tk.valoeghese.biomegen.api;

import tk.valoeghese.biomegen.gen.BiomeLayers;

public interface BiomeLayerFactory {
	public BiomeLayers build(long worldSeed);
}
