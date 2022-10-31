
import java.util.*;

public class Customer extends User{
	
	List<Product> basket = new ArrayList<Product>();
	
	private int houseNo;
	private String postCode;
	private String city;
	
	public Customer(String id, String username, String name, int houseNo, String postCode, String city) {
		// TODO Auto-generated constructor stub
		super(id, username, name);
		this.houseNo = houseNo;
		this.postCode = postCode;
		this.city = city;
	}
	
	// Return full address.
	public String getAddress() {
		String addr = String.join(" ", String.valueOf(houseNo), postCode, city);
		return addr;
	}
	
	// Add given item to the customers basket.
	public void addToBasket(Product item) {
		basket.add(item);
	}
	
	// Empty customers basket.
	public void emptyBasket() {
		basket.removeAll(basket);
	}
}
