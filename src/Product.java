
public abstract class Product {
	
	private int barcode;
	private String brand;
	private String colour;
	private String connection;
	private int quantity;
	private double original_cost;
	private double retail_price;
	
	public Product(int barcode, String brand, String colour, String connection, int quantity, double ogcost, double rprice) {
		// TODO Auto-generated constructor stub
		this.barcode = barcode;
		this.brand = brand;
		this.colour = colour;
		this.connection = connection;
		this.quantity = quantity;
		this.original_cost = ogcost;
		this.retail_price = rprice;
	}
	
	public void decreaseStock() {
		this.quantity = this.quantity - 1;
	}
	
	public void setStock(int n) {
		quantity = n;
	}

	public int getBarcode() {
		return barcode;
	}

	public String getBrand() {
		return brand;
	}

	public String getColour() {
		return colour;
	}

	public String getConnection() {
		return connection;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getOriginal_cost() {
		return original_cost;
	}

	public double getRetail_price() {
		return retail_price;
	}
}
