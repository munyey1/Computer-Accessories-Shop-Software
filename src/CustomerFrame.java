import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import java.util.*;
import java.util.regex.PatternSyntaxException;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

public class CustomerFrame extends JFrame {

	private JPanel contentPane;
	private Customer customer;
	private DefaultTableModel dtmproduct;
	private DefaultTableModel dtmbasket;
	private ArrayList<Product> productList = new ArrayList<Product>();
	private JTable productTbl;
	private JButton addBttn;
	private JTable basketTbl;
	private JButton checkoutBttn;
	private JTextField searchField;
	
	/**
	 * Create the frame.
	 */
	public CustomerFrame(Customer customer, ArrayList<Product> productList) {
		setTitle("Customer");
		setBackground(Color.WHITE);
		setResizable(false);
		
		// Set the customer and productlist accordingly
		this.customer = customer;
		this.productList = productList;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1000, 510);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 128, 128));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
//PRODUCT PANE----------------------------------------------------
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 10, 966, 454);
		contentPane.add(tabbedPane);
		
		JPanel products = new JPanel();
		tabbedPane.addTab("Products", null, products, null);
		products.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 45, 941, 372);
		products.add(scrollPane);
		
		/* This class list is formed to represent the classes of each column in the product table.
		 * This is needed to help sorting the table by the price of the product. To do this we 
		 * override the getColumnClass function in the DefaultTableModel class. Where we return 
		 * class of the column index provided.
		 */
		Class[] classes = new Class[] {String.class, String.class, String.class, String.class, String.class, String.class, Double.class, String.class};
		dtmproduct = new DefaultTableModel() {
			@Override
			public Class<?> getColumnClass(int columnIndex){
				return classes[columnIndex];
			}
		};
		dtmproduct.setColumnIdentifiers(new Object[] {"ID", "Product", "Type", "Brand", "Colour", "Connectivity", "Price", "Buttons/Layout"});
		productTbl = new JTable();
		scrollPane.setViewportView(productTbl);
		productTbl.setModel(dtmproduct);
		
		/* A list selection listener, where once the user selects a row in the table, 
		 * the add button highlights and is able to be pressed.
		 */
		productTbl.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				if(productTbl.getSelectedRow() > -1) {
					addBttn.setEnabled(true);
				}
			}
		});
		
		/* A table row sorter added to the product table. The table is sorted by the price
		 * of the items in ascending order.
		 */
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(productTbl.getModel());
		productTbl.setRowSorter(sorter);
		ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
		sortKeys.add(new RowSorter.SortKey(6, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);

		searchField = new JTextField();
		searchField.setBounds(10, 10, 96, 25);
		products.add(searchField);
		searchField.setColumns(10);
		
		JButton searchBttn = new JButton("Search");
		searchBttn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		/* An action listener added to the search button, where we check whether we 
		 * the text is a number or not. If its a number then search the number of 
		 * buttons on all mice, else search the brands.
		 */
		searchBttn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(searchField.getText().length() == 0) {
					// If search is pressed and nothing is entered, show original table.
					sorter.setRowFilter(null);
					return;
				}
				try {
					// If the text is a number, filter the table on the number of buttons.
					Integer.parseInt(searchField.getText());
					sorter.setRowFilter(RowFilter.regexFilter(searchField.getText(), 7));
					return;
				}catch(NumberFormatException e1) {
					//not a number
				}
				try {
					// If the text is not a number, search the table on the brands.
					sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchField.getText(), 3));
					return;
				}catch(PatternSyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		searchBttn.setBounds(116, 10, 85, 25);
		products.add(searchBttn);
		
		addBttn = new JButton("Add");
		/* An action listener on the add to basket button. We search for the product in the table 
		 * which the user has selected, by passing the items ID to the function 
		 * searchProduct. Once found we add to the basket only if there is a sufficient
		 * amount of stock available.
		 */
		addBttn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Product prd = searchProduct(Integer.parseInt(productTbl.getValueAt(productTbl.getSelectedRow(), 0).toString()));
				if(prd.getQuantity() == 0) {
					// If the quantity is 0, then we cannot add to the basket.
					JOptionPane.showMessageDialog(null, "Cannot add this item to your basket.\nNone in stock.");
				}else {
					prd.setStock(prd.getQuantity() - 1);
					customer.addToBasket(prd);
				}
			}
		});
		addBttn.setEnabled(false);
		addBttn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addBttn.setBounds(866, 10, 85, 25);
		products.add(addBttn);

		showProducts();
		
//CUSTOMER BASKET PANE----------------------------------		
		JPanel basket = new JPanel();
		tabbedPane.addTab("Basket", null, basket, null);
		basket.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 10, 941, 372);
		basket.add(scrollPane_1);
		
		basketTbl = new JTable();
		scrollPane_1.setViewportView(basketTbl);
		
		dtmbasket = new DefaultTableModel();
		dtmbasket.setColumnIdentifiers(new Object[] {"ID", "Product","Type", "Brand", "Colour", "Connectivity", "Price", "Buttons/Layout"});
		basketTbl.setModel(dtmbasket);
		
		checkoutBttn = new JButton("Checkout");
		/* An action listener for the checkout button. If there are no items
		 * then show a message for the user.
		 * Else show the user their options of payment.
		 */
		checkoutBttn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(basketTbl.getRowCount() < 1) {
					// If basket is empty.
					JOptionPane.showMessageDialog(null, "There are no items currently in your basket.");
				} else{
					Object[] options = {"PayPal", "Credit Card"};
					Object payment = JOptionPane.showInputDialog(null, "Choose payment method", "Payment Method", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
					// Display the payment options, then enter the respective methods.
					if(payment != null) {
						if(payment.toString() == "PayPal") {
							PayPal();
						}else {
							CreditCard();
						}
					}
				}
			}
		});
		checkoutBttn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		checkoutBttn.setBounds(866, 392, 85, 25);
		basket.add(checkoutBttn);
		
		JButton clearBttn = new JButton("Clear");
		/* An action listener for the clear button in the basket.
		 * If pressed, empty the shopping basket.
		 */
		clearBttn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(Product prd : customer.basket) {
					// Reset all the products quantity.
					prd.setStock(prd.getQuantity() + 1);
				}
				// Empty basket and set to the row count to 0 to show nothing in the basket.
				customer.emptyBasket();
				dtmbasket.setRowCount(0);
			}
		});
		clearBttn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		clearBttn.setBounds(771, 392, 85, 25);
		basket.add(clearBttn);
		
		/* A change listener added to the tabs. This is so the items of each table
		 * do not keep adding up, so we reset each table and re-populate the tables.
		 */
		ChangeListener changelistener = new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				if(sourceTabbedPane.getTitleAt(index) == "Basket") {
					// Clear the tables so the tables do not keep adding to itself.
					dtmbasket.setRowCount(0);
					productTbl.clearSelection();
					addBttn.setEnabled(false);
					// Populate the tables.
					for(Product prdt : customer.basket) {
						if(prdt instanceof Keyboard) {
							Keyboard prd = (Keyboard)prdt;
							Object[] rowdata = new Object[] {prd.getBarcode(), "Keyboard",prd.getType(), prd.getBrand(), prd.getColour(), prd.getConnection(), prd.getRetail_price(), prd.getLayout()};
							dtmbasket.addRow(rowdata);
						}else {
							Mice prd = (Mice)prdt;
							Object[] rowdata = new Object[] {prd.getBarcode(), "Mouse",prd.getType(), prd.getBrand(), prd.getColour(), prd.getConnection(), prd.getRetail_price(), prd.getBttns()};
							dtmbasket.addRow(rowdata);
						}
					}
				}else {
					showProducts();
				}
			}
		};
		tabbedPane.addChangeListener(changelistener);
	}
	
	// Show the products of product table.
	public void showProducts() {
		dtmproduct.setRowCount(0);
		for(Product prdt : productList) {
			if(prdt instanceof Keyboard) {
				Keyboard prd = (Keyboard)prdt;
				Object[] rowdata = new Object[] {prd.getBarcode(), "Keyboard", prd.getType(), prd.getBrand(), prd.getColour(), prd.getConnection(), prd.getRetail_price(), prd.getLayout()};
				dtmproduct.addRow(rowdata);
			}else {
				Mice prd = (Mice)prdt;
				Object[] rowdata = new Object[] {prd.getBarcode(), "Mouse", prd.getType(), prd.getBrand(), prd.getColour(), prd.getConnection(), prd.getRetail_price(), prd.getBttns()};
				dtmproduct.addRow(rowdata);
			}
		}
	}
	
	// Return the product given its ID.
	public Product searchProduct(int id) {
		for(Product prdt : productList) {
			if(prdt.getBarcode() == id) 
				return prdt;
		}
		return null;
	}
	
	// Procedure when the user pays via PayPal.
	public void PayPal() {
		JTextField paypalEmail = new JTextField();
		Object[] paypalDetails = {"Enter your PayPal email:", paypalEmail};

		int option = JOptionPane.showConfirmDialog(null, paypalDetails, "Credit Card", JOptionPane.OK_CANCEL_OPTION);
		if(option == JOptionPane.OK_OPTION) {
			// Validate the email
			if(paypalEmail.getText().contains("@")) {
				try {
					// Checkout using PayPal
					checkout("PayPal");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}else {
				// If email is not valid.
				JOptionPane.showMessageDialog(null, "PayPal email is invalid.");
			}
		}
	}
	
	// Procedure when user pays via credit card.
	public void CreditCard() {
		JTextField creditNum = new JTextField();
		JTextField securityNum = new JPasswordField();
		Object[] ccDetails = {"Card Number", creditNum, "Security Code", securityNum};
		
		int option = JOptionPane.showConfirmDialog(null, ccDetails, "Credit Card", JOptionPane.OK_CANCEL_OPTION);
		if(option == JOptionPane.OK_OPTION) {
			// Validate the credit card and security number, then enter checkout.
			if(validCreditNum(creditNum.getText()) && validSecurityNum(securityNum.getText())) {
				try {
					checkout("Credit Card");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// Function for validating the credit card.
	public boolean validCreditNum(String s) {
		try {
			Integer.parseInt(s);
			if(s.length() != 6) {
				JOptionPane.showMessageDialog(null, "Credit Card number is invalid.");
				return false;
			}
			return true;
			
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, "Credit Card number is invalid.");
			return false;
		}
	}
	
	// Function for validating the security number.
	public boolean validSecurityNum(String s) {
		try {
			Integer.parseInt(s);
			if(s.length() != 3) {
				JOptionPane.showMessageDialog(null, "Security number is invalid.");
				return false;
			}
			return true;
			
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Security number is invalid.");
			return false;
		}
	}
	
	// Procedure for checking out.
	public void checkout(String method) throws IOException {
		String total = calcTotal();
		if(method == "PayPal") {
			JOptionPane.showMessageDialog(null, total + " paid using PayPal, and the delivery address is: " + customer.getAddress());
			dtmbasket.setRowCount(0);
			customer.emptyBasket();
			updateStock();
		}else {
			JOptionPane.showMessageDialog(null, total + " paid using Credit Card, and the delivery address is: " + customer.getAddress());
			dtmbasket.setRowCount(0);
			customer.emptyBasket();
			updateStock();
		}
	}
	
	// Function for calculating the total of the customers basket.
	public String calcTotal() {
		DecimalFormat df = new DecimalFormat("#.00");
		double total = 0;
		for(Product prd : customer.basket) {
			total += prd.getRetail_price();
		}
		String str = "£" + df.format(total);
		return str;
	}
	
	// Function for writing back to the stock file.
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
