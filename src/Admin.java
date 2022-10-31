import java.util.ArrayList;

public class Admin extends User{
	
	public Admin(String id, String username, String name) {
		// TODO Auto-generated constructor stub
		super(id, username, name);
	}
	
	// Add product to the stock list.
	public void addItem(Product prd, ArrayList<Product> productList) {
		productList.add(prd);
	}
}
