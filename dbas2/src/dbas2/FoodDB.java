package dbas2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FoodDB {
	
	private Connection c;
	private Statement s;
	
	public FoodDB() {
		c = null;
		s = null;
	}
	
	public void connect(String db, String user, String password) {
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(
					"jdbc:postgresql://nestor2.csc.kth.se:5432/"+db,
					user, password);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		try {
			s = c.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Opened database successfully");
	}
	
	public boolean getShoppingList(String[] recipes) throws SQLException {
		
		String qFirst = "select i_name, quantity - x.cost AS buy, in_kitchen.unit from in_kitchen," +
				"(select i_name, sum(amount) AS cost, unit from ingredients_used where (";
		String qSecond = ") group by i_name, unit) AS x where i_name = name;";
		
		String temp = "";
		
		for (String s : recipes) {
			temp += "r_name='"+s+"' OR ";
		}
		temp = temp.substring(0, temp.length()-4);
		
		String full = qFirst+temp+qSecond;
		
		ResultSet r = s.executeQuery(full);
		while (r.next()) {
			String name = r.getString("i_name");
			float quantity = r.getFloat("buy");
			String unit = r.getString("unit");
			
			if (quantity < 0) {
				System.out.println(name + "\t" + (-quantity) +"\t"+unit);
			}
			
			
			
			
		}
		
		return true;
	}
	
	public boolean printPossibleRecipes(){
		ResultSet r = null;
		
		try {
			r = s.executeQuery("select recipe.name from ingredients_used, in_kitchen, recipe where r_name = recipe.name AND i_name = in_kitchen.name AND ((quantity - amount) >= 0 OR (quantity - amount) is null) group by recipe.name having count(*) = (select count(*) from ingredients_used where r_name = recipe.name) except select recipe.name from ingredients_used, in_kitchen, recipe where r_name = recipe.name AND i_name = in_kitchen.name AND ((quantity - amount) >= 0) group by recipe.name having count(*) = (select count(*) from ingredients_used where r_name = recipe.name);");
			while (r.next()) {
				System.out.println(r.getString("name"));
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean printDefinitelyRecipes(){
		ResultSet r = null;
		
		try {
			r = s.executeQuery("select recipe.name from ingredients_used, in_kitchen, recipe where r_name = recipe.name AND i_name = in_kitchen.name AND ((quantity - amount) >= 0) group by recipe.name having count(*) = (select count(*) from ingredients_used where r_name = recipe.name);");
			while (r.next()) {
				System.out.println(r.getString("name"));
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/*
	 * add food to the kitchen
	 */
	public boolean addToKitchen(String name, float quantity, String unit) {
		
		if (inKitchen(name)) { // if the food is in kitchen update it
			try {
				s.executeUpdate("UPDATE in_kitchen SET quantity=quantity+"+quantity+" WHERE name='"+name+"' AND unit='"+unit+"'");
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} else { // else insert new
			try {
				s.executeUpdate("INSERT INTO in_kitchen(name, quantity, unit) VALUES('"+name+"',"+quantity+",'"+unit+"')");
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return false;
	}
	
	public boolean addToKitchen(String name, float quantity) {
		
		if (inKitchen(name)) { // if the food is in kitchen update it
			try {
				s.executeUpdate("UPDATE in_kitchen SET quantity=quantity+"+quantity+" WHERE name='"+name+"'");
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} else { // else insert new
			try {
				s.executeUpdate("INSERT INTO in_kitchen(name, quantity) VALUES('"+name+"',"+quantity+")");
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return false;
	}
	
	public boolean addToKitchen(String name) {
		
		try {
			s.executeUpdate("INSERT INTO in_kitchen(name) VALUES('"+name+"')");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/*
	 * remove food from the kitchen
	 */
	public boolean removeFromKitchen(String name) {
		try {
			s.executeUpdate("DELETE FROM in_kitchen WHERE name = '"+name+"'");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * add a recipe
	 */
	public boolean addRecipe(String name, String type, String description) {
		try {
			s.executeUpdate("INSERT INTO recipe(name, type, description) VALUES('"+name+"','"+ type +"','"+ description +"')");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/*
	 * remove a recipe
	 */
	public boolean removeRecipe(String name) {
		
		try {
			s.executeUpdate("DELETE FROM ingredients_used where r_name = '"+name+"'"); // delete entries from ingredients_used
			s.executeUpdate("DELETE FROM recipe WHERE name = '"+name+"'"); // delete from recipe
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/*
	 * add an ingredient to a recipe
	 */
	public boolean addIngredientToRecipe(String rName, String iName, float quantity, String unit) {
		
		if (!isIngredient(iName)) { // if the ingredient is not in ingredient-table, insert it
			try {
				s.executeUpdate("INSERT INTO ingredient(name) VALUES('"+iName+"')");
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		
		try { // insert recipe into recipe-table
			s.executeUpdate("INSERT INTO ingredients_used(i_name, r_name, amount, unit) VALUES('"+iName+"','"+rName+"',"+quantity+",'"+unit+"')");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/*
	 * check if an ingredient exists
	 */
	private boolean isIngredient(String name) {
		ResultSet r = null;
		
		try {
			r = s.executeQuery("SELECT count(*) FROM ingredient where name = '"+name+"' limit 1");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			r.next();
			return r.getInt(1) != 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/*
	 * check if an ingredient is in the kitchen
	 */
	private boolean inKitchen(String name) {
		ResultSet r = null;
		
		try {
			r = s.executeQuery("SELECT count(*) FROM in_kitchen where name = '"+name+"' limit 1");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			r.next();
			return r.getInt(1) != 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void listIngredients() throws SQLException {
		ResultSet r = s.executeQuery("SELECT * FROM in_kitchen");
		
		System.out.println("List of ingredients:");
		while (r.next()) {
			System.out.println(r.getString("name") + " - " + r.getFloat("quantity") + " - " +r.getString("unit"));
		}
	}
	
	public void listRecipes() throws SQLException {
		ResultSet r = s.executeQuery("SELECT * FROM recipe");
		System.out.println("List of recipes:");
		while(r.next()) {
			System.out.println(r.getString("name"));
			System.out.println(r.getString("type"));
			System.out.println(r.getString("description"));
		}
	}

}
