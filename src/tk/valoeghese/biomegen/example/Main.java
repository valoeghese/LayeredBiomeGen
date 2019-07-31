package tk.valoeghese.biomegen.example;

import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tk.valoeghese.biomegen.api.BiomeSampler;
import tk.valoeghese.biomegen.example.layers.AddClimateLayers;
import tk.valoeghese.biomegen.example.layers.AddIslandBiomeLayer;
import tk.valoeghese.biomegen.example.layers.ContinentalBiomeLayer;
import tk.valoeghese.biomegen.example.layers.EdgeBiomeLayer;
import tk.valoeghese.biomegen.example.layers.InitContinentBiomeLayer;
import tk.valoeghese.biomegen.example.layers.NoiseScaleBiomeLayer;
import tk.valoeghese.biomegen.example.layers.RemoveOceanLakesLayer;
import tk.valoeghese.biomegen.example.layers.ShapeLandBiomeLayer;
import tk.valoeghese.biomegen.gen.BiomeLayers;
import tk.valoeghese.biomegen.gen.ScaleBiomeLayer;
import tk.valoeghese.biomegen.gen.SmoothBiomeLayer;

public class Main extends Application {

	private static BiomeSampler biomeSampler;
	private static final Random rand = new Random();

	public static void main(String[] args) {
		System.out.println("Creating biomeSampler");
		biomeSampler = BiomeLayers.build(rand.nextLong(), worldSeed -> {
			// Land stuff
			InitContinentBiomeLayer continentLayer_1 = new InitContinentBiomeLayer(worldSeed, 1L);

			ShapeLandBiomeLayer shapeLandBiomeLayer_1 = new ShapeLandBiomeLayer(
					new NoiseScaleBiomeLayer(continentLayer_1, worldSeed, 2L),
					worldSeed, 2L, false);

			ScaleBiomeLayer scaleBiomeLayer_1 = new ScaleBiomeLayer(new RemoveOceanLakesLayer(new ShapeLandBiomeLayer(new ShapeLandBiomeLayer(
					new ScaleBiomeLayer(shapeLandBiomeLayer_1, worldSeed),
					worldSeed, 5L, false), worldSeed, 25L, false), worldSeed, 5L), worldSeed);

			// Init climate, etc
			RemoveOceanLakesLayer removeOceanLakesLayer_1 = new RemoveOceanLakesLayer(new AddClimateLayers.AddColdClimateBiomeLayer(scaleBiomeLayer_1, worldSeed, 100L), worldSeed, 101L);

			// Ease climate
			SmoothBiomeLayer smoothBiomeLayer_1 = new SmoothBiomeLayer(new AddIslandBiomeLayer(new ScaleBiomeLayer(new AddClimateLayers.AddMountainRangeBiomeLayer(
					new ScaleBiomeLayer(new SmoothBiomeLayer(new AddClimateLayers.EaseWarmCoolBiomeLayer(new ShapeLandBiomeLayer(new AddIslandBiomeLayer(
							new ScaleBiomeLayer(new AddClimateLayers.EaseHotColdBiomeLayer(removeOceanLakesLayer_1, worldSeed, 101L), worldSeed),
							worldSeed, 50L, true), worldSeed, 102L, false), worldSeed, 103L), worldSeed, 105L), worldSeed),
					worldSeed, 100L), worldSeed), worldSeed, 101L, false), worldSeed, 200L);

			// Add Biomes
			ContinentalBiomeLayer continentalBiomeLayer_1 = new ContinentalBiomeLayer(smoothBiomeLayer_1, worldSeed, 201L);
			
			BiomeLayers scaleBiomeLayer_2 = ScaleBiomeLayer.scaleBy(continentalBiomeLayer_1, 2, worldSeed);
			
			ShapeLandBiomeLayer shapeLandBiomeLayer_2 = new ShapeLandBiomeLayer(scaleBiomeLayer_2, worldSeed, 1000L);
			
			return new EdgeBiomeLayer(new SmoothBiomeLayer(shapeLandBiomeLayer_2, worldSeed, 200L), worldSeed, 1001L);
		});
		System.out.println("Launching");
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		// Setup
		final double width = 900;
		final double height = 900;

		Pane pane = new Pane();

		Canvas canvas = new Canvas(width, height);
		GraphicsContext graphics = canvas.getGraphicsContext2D();

		// Draw
		draw(graphics, (int)width, (int)height);

		// Connect objects and add to stage

		pane.getChildren().add(canvas);
		Scene scene = new Scene(pane, width, height, Color.WHITESMOKE);

		stage.setTitle("Example Layered-Biome World Generation");
		stage.setScene(scene);
		stage.show();
	}

	private void draw(GraphicsContext graphics, int width, int height) {
		PixelWriter pw = graphics.getPixelWriter();

		System.out.println("Starting sampling");
		int[] biomes = biomeSampler.sample(-(width / 2), -(width / 2), width, height);
		System.out.println("Finished sampling");

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				Biome biome = Biome.lookup[biomes[x * height + y]];

				pw.setColor(x, y, biome.getColour());
			}
		}
	}

}
