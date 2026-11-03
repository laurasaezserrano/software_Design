package es.deusto.ingenieria.sd.ecoembes.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a customer order with dishes, quantities, pricing, and status.
 */
public class Order {
	
	private Long orderId;
	private String customerName;
	private List<DishOrderItem> dishes;
	private Double totalPrice;
	private String status; // "pending", "completed", "canceled"
	
	// Default constructor for Jackson deserialization
	public Order() {
		this.dishes = new ArrayList<>();
		this.status = "pending";
		this.totalPrice = 0.0;
	}
	
	// Constructor without orderId for creating new orders using POST
	public Order(String customerName, List<DishOrderItem> dishes) {
		super();
		this.customerName = customerName;
		this.dishes = dishes != null ? dishes : new ArrayList<>();
		this.status = "pending";
		this.calculateTotalPrice();
	}
	
	// Constructor with all attributes
	public Order(Long orderId, String customerName, List<DishOrderItem> dishes, Double totalPrice, String status) {
		super();
		this.orderId = orderId;
		this.customerName = customerName;
		this.dishes = dishes != null ? dishes : new ArrayList<>();
		this.totalPrice = totalPrice;
		this.status = status != null ? status : "pending";
	}
	
	public Long getOrderId() {
		return orderId;
	}
	
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public List<DishOrderItem> getDishes() {
		return dishes;
	}
	
	public void setDishes(List<DishOrderItem> dishes) {
		this.dishes = dishes != null ? dishes : new ArrayList<>();
	}
	
	public Double getTotalPrice() {
		return totalPrice;
	}
	
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Calculates the total price by summing up all dish items' total prices.
	 */
	public void calculateTotalPrice() {
		if (dishes == null || dishes.isEmpty()) {
			this.totalPrice = 0.0;
		} else {
			this.totalPrice = dishes.stream()
				.mapToDouble(DishOrderItem::getItemTotalPrice)
				.sum();
		}
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
		Order other = (Order) obj;
		return Objects.equals(orderId, other.orderId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(orderId);
	}
}
