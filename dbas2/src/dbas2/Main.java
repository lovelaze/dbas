package dbas2;

import java.sql.SQLException;
public class Main {
	
	private Mathaxxare mathax;
	
	public Main() {
		mathax = new Mathaxxare();
	}
	
	

	public static void main(String[] args) throws SQLException {
		Main main = new Main();
		main.mathax.connect("frebern", "frebern", "E4VkF794");
		//main.addIngredient("mango", 2f, "pieces");
		//main.addRecipe("Apple pie", "dessert", "Jockes delicous apple pie");
		//main.listIngredients();
		main.mathax.listRecipes();
		
	}

}
