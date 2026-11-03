/**
 * This code is based on solutions provided by ChatGPT 4o. 
 * It has been thoroughly reviewed and validated to ensure 
 * correctness and that it is free of errors.
 */
package es.deusto.sd.restaurant.entity;

import java.util.Objects;

/**
 * Represents a dish item within an order with quantity and pricing information.
 */
public class DishOrderItem {
	
	private String name;
	private Integer quantity;
	private Double price;
	
	// Default constructor for Jackson deserialization
	public DishOrderItem() { }
	
	// Constructor with all attributes
	public DishOrderItem(String name, Integer quantity, Double price) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	/**
	 * Calculates the total price for this dish item (price * quantity).
	 * @return the total price for this dish item
	 */
	public Double getItemTotalPrice() {
		if (quantity == null || price == null) {
			return 0.0;
		}
		return quantity * price;
	}
	
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
		DishOrderItem other = (DishOrderItem) obj;
		return Objects.equals(name, other.name);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
