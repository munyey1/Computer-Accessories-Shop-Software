import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.JTabbedPane;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class AdminFrame extends JFrame {

	private JPanel contentPane;
	private Admin admin;
	private ArrayList<Product> productList = new ArrayList<Product>();
	private JTextField id;
	private JTextField type;
	private JTextField brand;
	private JTextField colour;
	private JTextField quantity;
	private JTextField ogCost;
	private JTextField retailPrice;
	private JTextField special;
	private JTable productTbl;
	private DefaultTableModel dtmproduct;
	private JTabbedPane tabbedPane;
	private JLabel productLbl;
	private JComboBox productType;
	private JComboBox connectivity;
	private JLabel idLbl;
	private JLabel typeLbl;
	private JLabel specialLbl;
	private JLabel brandLbl;
	
	// Lists used for JComboBox 
	private String[] typeList = {"Mice", "Keyboard"};
	private String[] conn = {"Wired", "Wireless"};
	// List to store all the fields for adding a new product.
	private ArrayList<JTextField> inputs = new ArrayList<JTextField>();

	public AdminFrame(Admin admin, ArrayList<Product> productList){
		setTitle("Admin");
		setResizable(false);
		this.admin = admin;
		this.productList = productList;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1000, 510);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 128, 128));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(new Color(255, 255, 255));
		tabbedPane.setBounds(10, 10, 966, 453);
		contentPane.add(tabbedPane);
		
//PRODUCTS PANE--------------------------------------------		
		JPanel products = new JPanel();
		tabbedPane.addTab("View", null, products, null);
		products.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 941, 406);
		products.add(scrollPane);
		
		/* This class list is formed to represent the classes of each column in the product table.
		 * This is needed to help sorting the table by the price of the product. To do this we 
		 * override the getColumnClass function in the DefaultTableModel class. Where we return 
		 * class of the column index provided.
		 */
		Class[] classes = new Class[] {String.class, String.class, String.class, String.class, String.class, String.class, Integer.class, Double.class, Double.class, String.class};
		dtmproduct = new DefaultTableModel() {
			@Override
			public Class<?> getColumnClass(int columnIndex){
				return classes[columnIndex];
			}
		};
		dtmproduct.setColumnIdentifiers(new Object[] {"ID", "Product", "Type", "Brand", "Colour", "Connectivity", "Quantity", "Original Cost", "Retail Price", "Buttons/Layout"});
		showProducts();
		productTbl = new JTable();
		scrollPane.setViewportView(productTbl);
		productTbl.setModel(dtmproduct);

		/* A table row sorter added to the product table. The table is sorted by the price
		 * of the items in ascending order.
		 */
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(productTbl.getModel());
		productTbl.setRowSorter(sorter);
		ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
		sortKeys.add(new RowSorter.SortKey(8, SortOrder.DESCENDING));
		sorter.setSortKeys(sortKeys);
		
//ADD PRODUCT PANE-------------------------------------------------------------------------------
		JPanel newProduct = new JPanel();
		tabbedPane.addTab("Add Product", null, newProduct, null);
		newProduct.setLayout(null);
		
		productLbl = new JLabel("Product");
		productLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		productLbl.setBounds(10, 20, 75, 20);
		newProduct.add(productLbl);
		
		productType = new JComboBox(typeList);
		/* An action listener to changed the text of a label
		 * to show 'Layout' when a keyboard is selected, or 
		 * 'No of Buttons' when a mouse is selected.
		 */
		productType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(productType.getSelectedItem().toString() == "Keyboard") {
					specialLbl.setText("Layout");
				}else {
					specialLbl.setText("No of Buttons");
				}
			}
		});
		productType.setBounds(135, 20, 130, 20);
		newProduct.add(productType);
		
		idLbl = new JLabel("ID");
		idLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		idLbl.setBounds(10, 70, 30, 20);
		newProduct.add(idLbl);
		
		id = new JTextField();
		id.setBounds(135, 70, 130, 20);
		newProduct.add(id);
		id.setColumns(10);
		inputs.add(id);
		
		typeLbl = new JLabel("Type");
		typeLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		typeLbl.setBounds(10, 120, 50, 20);
		newProduct.add(typeLbl);
		
		type = new JTextField();
		type.setBounds(135, 120, 130, 20);
		newProduct.add(type);
		type.setColumns(10);
		inputs.add(type);
		
		brandLbl = new JLabel("Brand");
		brandLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		brandLbl.setBounds(10, 170, 50, 20);
		newProduct.add(brandLbl);
		
		brand = new JTextField();
		brand.setBounds(135, 170, 130, 20);
		newProduct.add(brand);
		brand.setColumns(10);
		inputs.add(brand);
		
		JLabel colourLbl = new JLabel("Colour");
		colourLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		colourLbl.setBounds(10, 220, 50, 20);
		newProduct.add(colourLbl);
		
		colour = new JTextField();
		colour.setBounds(135, 220, 130, 20);
		newProduct.add(colour);
		colour.setColumns(10);
		inputs.add(colour);
		
		connectivity = new JComboBox(conn);
		connectivity.setBounds(135, 270, 130, 20);
		newProduct.add(connectivity);
		
		JLabel connLbl = new JLabel("Connectivity");
		connLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		connLbl.setBounds(10, 270, 90, 20);
		newProduct.add(connLbl);
		
		JLabel instockLbl = new JLabel("Quantity In Stock");
		instockLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		instockLbl.setBounds(10, 320, 120, 20);
		newProduct.add(instockLbl);
		
		quantity = new JTextField();
		quantity.setBounds(135, 320, 130, 20);
		newProduct.add(quantity);
		quantity.setColumns(10);
		inputs.add(quantity);
		
		JLabel originalLbl = new JLabel("Original Cost");
		originalLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		originalLbl.setBounds(330, 20, 85, 20);
		newProduct.add(originalLbl);
		
		ogCost = new JTextField();
		ogCost.setBounds(435, 21, 130, 20);
		newProduct.add(ogCost);
		ogCost.setColumns(10);
		inputs.add(ogCost);
		
		JLabel retailLbl = new JLabel("Retail Price");
		retailLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		retailLbl.setBounds(330, 70, 75, 20);
		newProduct.add(retailLbl);
		
		retailPrice = new JTextField();
		retailPrice.setBounds(435, 70, 130, 20);
		newProduct.add(retailPrice);
		retailPrice.setColumns(10);
		inputs.add(retailPrice);
		
		specialLbl = new JLabel("No of Buttons");
		specialLbl.setHorizontalAlignment(SwingConstants.LEFT);
		specialLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		specialLbl.setBounds(330, 120, 98, 20);
		newProduct.add(specialLbl);
		
		special = new JTextField();
		special.setBounds(435, 120, 130, 20);
		newProduct.add(special);
		special.setColumns(10);
		inputs.add(special);
		
		JButton submitBttn = new JButton("Add");
		/* Action listener added to the submit button in the add product
		 * tab. Input checking for all the fields, then adding the item
		 * to the stock list.
		 */
		submitBttn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Keyboard kb = null;
				Mice mc = null;
				
				for(JTextField field : inputs) {
					if(field.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Do not leave any fields blank.");
						return;
					}
				}
				
				if(!isValidId(id.getText())) {
					JOptionPane.showMessageDialog(null, "Invalid ID.");
					return;
				}
				
				if(!isDouble(ogCost.getText()) || !isDouble(retailPrice.getText())) {
					JOptionPane.showMessageDialog(null, "Enter the correct price form.");
					return;
				}
				
				if(productType.getSelectedItem().toString() == "Mice") {
					if(!isInt(special.getText())) {
						JOptionPane.showMessageDialog(null, "Enter the correct number of buttons form.");
						return;
					}
				}
				
				 if(productType.getSelectedItem().toString() == "Keyboard") {
					 kb = new Keyboard(Integer.parseInt(id.getText()), type.getText(), brand.getText(), colour.getText(), connectivity.getSelectedItem().toString().toLowerCase(), Integer.parseInt(quantity.getText()), Double.parseDouble(ogCost.getText()), Double.parseDouble(retailPrice.getText()), special.getText());
					 admin.addItem(kb, productList);
				 }else {
					 mc = new Mice(Integer.parseInt(id.getText()), type.getText(), brand.getText(), colour.getText(), connectivity.getSelectedItem().toString().toLowerCase(), Integer.parseInt(quantity.getText()), Double.parseDouble(ogCost.getText()), Double.parseDouble(retailPrice.getText()), Integer.parseInt(special.getText()));
					 admin.addItem(mc, productList);
				 }
				 
				 try {
					updateStock();
				 }catch (IOException e1) {
					e1.printStackTrace();
				 }
			}
		});
		submitBttn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		submitBttn.setBounds(866, 386, 85, 30);
		newProduct.add(submitBttn);
		
		JButton clearBttn = new JButton("Clear");
		// Clear all input fields.
		clearBttn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(JTextField field : inputs) {
					field.setText(null);
				}
			}
		});
		clearBttn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		clearBttn.setBounds(771, 386, 85, 30);
		newProduct.add(clearBttn);

		ChangeListener changelistener = new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				if(sourceTabbedPane.getTitleAt(index) == "View") {
					showProducts();
				}
			}
		};
		tabbedPane.addChangeListener(changelistener);
	}
	
	// Function to show products.
	public void showProducts() {
		dtmproduct.setRowCount(0);
		for(Product prdt : productList) {
			if(prdt instanceof Keyboard) {
				Keyboard prd = (Keyboard)prdt;
				Object[] rowdata = new Object[] {prd.getBarcode(), "Keyboard", prd.getType(), prd.getBrand(), prd.getColour(), prd.getConnection(), prd.getQuantity(), prd.getOriginal_cost(), prd.getRetail_price(), prd.getLayout()};
				dtmproduct.addRow(rowdata);
			}else {
				Mice prd = (Mice)prdt;
				Object[] rowdata = new Object[] {prd.getBarcode(), "Mice", prd.getType(), prd.getBrand(), prd.getColour(), prd.getConnection(),  prd.getQuantity(), prd.getOriginal_cost(), prd.getRetail_price(), prd.getBttns()};
				dtmproduct.addRow(rowdata);
			}
		}
	}
	
	// Function to validate given ID.
	public boolean isValidId(String s) {
		if(s.isEmpty())
			return false;
		try {
			Integer.parseInt(s);
			if(s.length() != 6) {
				return false;
			}
			for(Product prdt : productList) {
				if(Integer.parseInt(s) == prdt.getBarcode()) {
					return false;
				}
			}
			return true;
		} catch(Exception e){
			return false;
		}
	}
	
	// Function to validate if value is type double.
	public boolean isDouble(String s) {
		if(s.isEmpty())
			return false;
		try {
			Double.parseDouble(s);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	// Function to validate if value is type integer.
	public boolean isInt(String s) {
		if(s.isEmpty())
			return false;
		try {
			Integer.parseInt(s);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	// Function to update the stock file.
	public void updateStock() throws IOException {
		FileWriter fw = new FileWriter("Stock.txt");
		
		for(Product product : productList) {
			if(product instanceof Keyboard) {
				Keyboard prd = (Keyboard) product;
				fw.write(prd.returnAll() + System.lineSeparator());
			}else {
				Mice prd = (Mice) product;
				fw.write(prd.returnAll() + System.lineSeparator());
			}
		}
		fw.close();
	}

}
