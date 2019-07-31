package tk.valoeghese.biomegen.api;

import tk.valoeghese.biomegen.api.gen.BiomeLayers;
import tk.valoeghese.biomegen.api.gen.WorldSettings;

public interface BiomeLayerFactory<T extends WorldSettings> {
	public BiomeLayers build(long worldSeed, T settings);
}
