
public class Mice extends Product{
	
	private String type;
	private int buttons;
	
	public Mice(int barcode, String type, String brand, String colour, String connection, int quantity, double ogcost, double rprice, int buttons) {
		// TODO Auto-generated constructor stub
		super(barcode, brand, colour, connection, quantity, ogcost, rprice);
		this.type = type;
		this.buttons = buttons;
	}
	
	public String getType() {
		return type;
	}
	
	public int getBttns() {
		return buttons;
	}
	
	// Function to return all the mouses attributes separated by ',' as a string.
	public String returnAll(){
		String str = String.join(", ", String.valueOf(this.getBarcode()), "mouse", type, this.getBrand(), this.getColour(), this.getConnection(), String.valueOf(this.getQuantity()), String.valueOf(this.getOriginal_cost()), String.valueOf(this.getRetail_price()), String.valueOf(buttons));
		return str;
	}


}
