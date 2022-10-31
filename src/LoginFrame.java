import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class LoginFrame extends JFrame {

	private JPanel contentPane;
	private JComboBox userBox;
	private JButton loginButton;
	private static ArrayList<User> userList = new ArrayList<User>();
	private static ArrayList<Product> productList = new ArrayList<Product>();

	/**
	 * Launch the application.
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException{
		// Call these functions for the user list and product list.
		users();
		products();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 128, 128));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		userBox = new JComboBox(userList.toArray());
		userBox.setBackground(new Color(255, 255, 255));
		userBox.setBounds(125, 73, 178, 21);
		contentPane.add(userBox);
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){	
				if((User)userBox.getSelectedItem() instanceof Admin) {
					// If the user selects the admin, then open the admin frame, passing the admin and the product list.
					AdminFrame af = new AdminFrame((Admin)userBox.getSelectedItem(), productList);
					af.setVisible(true);
				} else {
					// Else open the customer frame, passing the customer and the product list.
					CustomerFrame cf = new CustomerFrame((Customer)userBox.getSelectedItem(), productList);
					cf.setVisible(true);
				}
			}
		});
		loginButton.setBounds(149, 138, 121, 32);
		contentPane.add(loginButton);
	}
	
	// Function to get the users from the user accounts file and store into a user list.
	public static void users() throws FileNotFoundException {
		File file = new File("UserAccounts.txt");
		Scanner s = new Scanner(file);
		
		Admin ad = null;
		Customer cs = null;
		
		while(s.hasNextLine()) {
			String[] details = s.nextLine().split(",");
			if(details[6].trim().equals("admin")) {
				ad = new Admin(details[0].trim(), details[1].trim(), details[2].trim());
				userList.add(ad);
			}else {
				cs = new Customer(details[0].trim(), details[1].trim(), details[2].trim(), Integer.parseInt(details[3].trim()), details[4].trim(), details[5].trim());
				userList.add(cs);
			}
		}
		s.close();
	}
	
	// Function to get the products from the stock file and store into a product list.
	public static void products() throws FileNotFoundException {
		File file = new File("Stock.txt");
		Scanner s = new Scanner(file);
		
		Keyboard kb = null;
		Mice mc = null;
		
		while(s.hasNextLine()) {
			String[] details = s.nextLine().split(",");
			if(details[1].trim().equals("mouse")) {
				mc = new Mice(Integer.parseInt(details[0].trim()), details[2].trim(), details[3].trim(), details[4].trim(), details[5].trim(), Integer.parseInt(details[6].trim()) ,Double.parseDouble(details[7].trim()), Double.parseDouble(details[8].trim()), Integer.parseInt(details[9].trim()));
				productList.add(mc);
			}else {
				kb = new Keyboard(Integer.parseInt(details[0].trim()), details[2].trim(), details[3].trim(), details[4].trim(), details[5].trim(), Integer.parseInt(details[6].trim()) ,Double.parseDouble(details[7].trim()), Double.parseDouble(details[8].trim()), details[9].trim());
				productList.add(kb);
			}
		}
		s.close();
	}
}
