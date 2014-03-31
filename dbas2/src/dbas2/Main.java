package dbas2;

import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {
	
	private FoodDB db;
	
	private boolean exit = false;
	public Main() {
		db = new FoodDB();
	}
	
	public void prompt() throws SQLException{
		Scanner scanner = new Scanner(new InputStreamReader(System.in));
		while (!exit){
			System.out.print(">");
			String input = scanner.nextLine();
			
			if(input.equals("exit")){
				exit = true;
			}
			
			if(input.equals("list") || input.equals("list food")){
				db.listIngredients();
			}
			
			if(input.equals("add") || input.equals("add food")){
				System.out.print("Food name: ");
				String name = scanner.nextLine();
				System.out.print("Quantity: ");
				float quantity = scanner.nextFloat();
				System.out.print("Unit: ");
				String unit = scanner.nextLine();
				if(quantity == 0f){
					db.addToKitchen(name);
				}
				else if(unit.equals("")){
					db.addToKitchen(name, quantity);
				}
				else {
					db.addToKitchen(name, quantity, unit);
				}
			}
			
			if(input.equals("remove") || input.equals("remove food") || input.equals("delete")){
				System.out.print("Food name: ");
				String name = scanner.nextLine();
				System.out.print("Quantity: ");
				float quantity = scanner.nextFloat();
				db.addToKitchen(name, -quantity);
			}
			
			if(input.equals("possible") || input.equals("possible recipes")){
				db.printPossibleRecipes();
			}
			
			if(input.equals("definitely") || input.equals("definitely recipes")){
				db.printDefinitelyRecipes();
			}
			
			if (input.equals(("shopping"))) {
				ArrayList<String> lst = new ArrayList<>();
				System.out.println("enter recipes, exit with 'stop'");
				boolean next = true;
				while (next) {
					System.out.print(">");
					String food = scanner.nextLine();
					if (food.equals("stop")) {
						next = false;
					} else {
						lst.add(food);
					}
					
				}

				db.getShoppingList(lst.toArray(new String[lst.size()]));
			}
			
			
		}
		scanner.close();
	}

	public static void main(String[] args) throws SQLException {
		
		
		Main main = new Main();
		main.db.connect("frebern", "frebern", "E4VkF794");
		main.prompt();
		
	}

}
