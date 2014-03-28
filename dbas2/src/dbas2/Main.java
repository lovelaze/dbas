package dbas2;

import java.sql.SQLException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
public class Main {
	
	private FoodDB db;
	private Options options;
	
	private final boolean enableCLI = false;
	
	public Main() {
		db = new FoodDB();
		options = createOptions();
	}
	
	@SuppressWarnings("static-access")
	private Options createOptions() {
		Options options = new Options();
		
		
		Option recipes = OptionBuilder.withArgName("recipes")
				.withDescription("List all recipes")
				.create("r");
		
		Option ingredients = OptionBuilder.withArgName("ingredients")
				.withDescription("List all ingredients")
				.create("i");
		
		options.addOption(recipes);
		options.addOption(ingredients);
		
		
		
		
		return options;
	}
	
	

	public static void main(String[] args) throws SQLException {
		
		String testargs[] = {"-i"};
		
		Main main = new Main();
		main.db.connect("frebern", "frebern", "E4VkF794");
		
		if (main.enableCLI) {
			CommandLineParser parser = new GnuParser();
			CommandLine cmd = null;
			try {
				cmd = parser.parse(main.options, testargs);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if (cmd.hasOption("r")) {
				main.db.listRecipes();
			}
			
			if (cmd.hasOption("i")) {
				main.db.listIngredients();
			}
			
			
		}

		//main.addIngredient("mango", 2f, "pieces");
		//main.addRecipe("Apple pie", "dessert", "Jockes delicous apple pie");
		//main.listIngredients();
		//main.db.addToKitchen("green bell pepper", 5, "kg");
		//main.db.removeFromKitchen("green bell pepper");
		//main.db.removeFromKitchen("kiwi");
		//main.db.addIngredientToRecipe("banakaka", "water", 1, "L")
		main.db.addToKitchen("soy sauce");
		
		
		
	}

}
