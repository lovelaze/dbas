package dbas2;

import java.sql.SQLException;
public class Main {
	
	private FoodDB db;
	
	public Main() {
		db = new FoodDB();
	}
	
	

	public static void main(String[] args) throws SQLException {
		Main main = new Main();
		main.db.connect("frebern", "frebern", "E4VkF794");
		//main.addIngredient("mango", 2f, "pieces");
		//main.addRecipe("Apple pie", "dessert", "Jockes delicous apple pie");
		//main.listIngredients();
		//main.db.addToKitchen("green bell pepper", 5, "kg");
		//main.db.removeFromKitchen("green bell pepper");
		//main.db.removeFromKitchen("kiwi");
		//main.db.addIngredientToRecipe("banakaka", "water", 1, "L")
		
		
	}

}
