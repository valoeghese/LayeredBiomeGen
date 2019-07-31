package tk.valoeghese.biomegen.api.gen;

public final class EmptyWorldSettings implements WorldSettings {
	private EmptyWorldSettings() {
	}
	
	public static final EmptyWorldSettings INSTANCE = new EmptyWorldSettings();
}
