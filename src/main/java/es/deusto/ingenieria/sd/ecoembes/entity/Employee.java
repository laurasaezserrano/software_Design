/**
 * This code is based on solutions provided by ChatGPT 4o. 
 * It has been thoroughly reviewed and validated to ensure 
 * correctness and that it is free of errors.
 */
package es.deusto.sd.restaurant.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dish {

	private Long id;
	private String name;
	private String ingredients;
	private double price;
	private List<Allergens> allergens;

	// Default constructor is needed for Jackson to deserialize JSON
	public Dish() {
		this.allergens = new ArrayList<>();
	}
	
	// Constructor without id for creating new dishes using POST
	public Dish(String name, String ingredients, double price) {
		super();
		this.name = name;
		this.ingredients = ingredients;
		this.price = price;
		this.allergens = new ArrayList<>();
	}

	// Constructor with all attributes
	public Dish(Long id, String name, String ingredients, double price) {
		super();
		this.id = id;
		this.name = name;
		this.ingredients = ingredients;
		this.price = price;
		this.allergens = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public List<Allergens> getAllergens() {
		return allergens;
	}

	public void setAllergens(List<Allergens> allergens) {
		this.allergens = allergens;
	}
	
	// Equals and HashCode methods
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Dish other = (Dish) obj;
		return Objects.equals(id, other.id);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}	
}