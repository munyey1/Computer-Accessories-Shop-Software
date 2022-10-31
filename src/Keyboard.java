
public class Keyboard extends Product{
	
	private String type;
	private String layout;
	
	public Keyboard(int barcode, String type, String brand, String colour, String connection, int quantity, double ogcost, double rprice, String layout) {
		// TODO Auto-generated constructor stub
		super(barcode, brand, colour, connection, quantity, ogcost, rprice);
		this.type = type;
		this.layout = layout;
	}
	
	public String getType() {
		return type;
	}
	
	public String getLayout() {
		return layout;
	}
	
	// Function to return all the keyboards attributes separated by ',' as a string.
	public String returnAll(){
		String str = String.join(", ", String.valueOf(this.getBarcode()), "keyboard", type, this.getBrand(), this.getColour(), this.getConnection(), String.valueOf(this.getQuantity()), String.valueOf(this.getOriginal_cost()), String.valueOf(this.getRetail_price()), layout);
		return str;
	}

}
