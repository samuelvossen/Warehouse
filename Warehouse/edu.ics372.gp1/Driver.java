import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This class is the driver class for the system
 * 
 * @author Samuel Vossen
 *
 */
public class Driver {

	private static Company company;
	private static Driver driver;
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	boolean addMore = false;
	private static final int EXIT = 0;
	private static final int ADD_COMPONENT = 1;
	private static final int ADD_SUPPLIER = 2;
	private static final int ADD_COMPONENT_SUPPLIER = 3;
	private static final int ADD_PRODUCT = 4;
	private static final int CREATE_ALLOCATION = 5;
	private static final int CREATE_ORDER = 6;
	private static final int FULFILL_ORDER = 7;
	private static final int PRINT_COMPONENT = 8;
	private static final int PRINT_SUPPLIER = 9;
	private static final int LIST_OUTSTANDING_ORDERS = 10;
	private static final int LIST_OUTSTANDING_ALLOCATIONS = 11;
	private static final int LIST_ALL_COMPONENTS = 12;
	private static final int LIST_ALL_SUPPLIERS = 13;
	private static final int SAVE = 14;
	private static final int HELP = 15;

	public static void main(String[] args) {
		instance();
		driver.startUp();
		driver.menu();
	}

	/**
	 * Prints the help menu to provide instruction on how to navigate the system
	 */
	public static void help() {
		System.out.println("Enter a number between 0 and 15 as explained below:");
		System.out.println(EXIT + " to Exit\n");
		System.out.println(ADD_COMPONENT + " to add a component");
		System.out.println(ADD_SUPPLIER + " to add a supplier");
		System.out.println(ADD_COMPONENT_SUPPLIER + " to allow a supplier to supply a chosen component");
		System.out.println(ADD_PRODUCT + " to add a product ");
		System.out.println(CREATE_ALLOCATION + " to create an allocation ");
		System.out.println(CREATE_ORDER + " to create an order");
		System.out.println(FULFILL_ORDER + " to fulfill an outstanding order");
		System.out.println(PRINT_COMPONENT + " to print  a chosen component");
		System.out.println(PRINT_SUPPLIER + " to print a chosen supplier");
		System.out.println(LIST_OUTSTANDING_ORDERS + " to print all outstanding orders");
		System.out.println(LIST_OUTSTANDING_ALLOCATIONS + " to print all outstanding allocations");
		System.out.println(LIST_ALL_COMPONENTS + " to print all components");
		System.out.println(LIST_ALL_SUPPLIERS + " to print all suppliers");
		System.out.println(SAVE + " to save data");
		System.out.println(HELP + " for help");
	}

	/**
	 * Orchestrates the whole process. Calls the appropriate method for the
	 * different functionalities.
	 * 
	 */
	public void menu() {
		int command;
		help();
		while ((command = getCommand()) != EXIT) {
			switch (command) {
			case ADD_COMPONENT:
				addComponent();
				break;
			case ADD_SUPPLIER:
				addSupplier();
				break;
			case ADD_COMPONENT_SUPPLIER:
				addComponentSupplier();
				break;
			case ADD_PRODUCT:
				addProduct();
				break;
			case CREATE_ALLOCATION:
				createAllocation();
				break;
			case CREATE_ORDER:
				createOrder();
				break;
			case FULFILL_ORDER:
				fulfillOrder();
				break;
			case PRINT_COMPONENT:
				printComponent();
				break;
			case PRINT_SUPPLIER:
				printSupplier();
				break;
			case LIST_OUTSTANDING_ORDERS:
				listOutstandingOrders();
				break;
			case LIST_OUTSTANDING_ALLOCATIONS:
				listOutstandingAllocations();
				break;
			case LIST_ALL_COMPONENTS:
				printAllComponents();
				break;
			case LIST_ALL_SUPPLIERS:
				printAllSuppliers();
				break;
			case SAVE:
				save();
				break;
			case HELP:
				help();
				break;
			}
		}
	}

	/**
	 * Supports the singleton pattern
	 * 
	 * @return the singleton object
	 */
	public static Driver instance() {
		if (driver == null) {
			return driver = new Driver();
		} else {
			return driver;
		}
	}

	/**
	 * Gets an input after prompting
	 * 
	 * @param prompt - whatever the user wants as prompt
	 * @return - the input from the keyboard
	 * 
	 */
	public String getInput(String prompt) {
		do {
			try {
				System.out.println(prompt);
				String line = reader.readLine();
				return line;
			} catch (IOException ioe) {
				System.exit(0);
			}
		} while (true);
	}

	/**
	 * Converts the string to a number within the range of menu entries
	 * 
	 * @param prompt the string for prompting
	 * @return the integer corresponding to the string
	 * 
	 */
	public int getCommand() {
		do {
			try {
				int value = Integer.parseInt(getInput("Enter command:" + HELP + " for help"));
				if (value >= EXIT && value <= HELP) {
					return value;
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Enter a number");
			}
		} while (true);
	}

	/**
	 * Converts the string to a number greater than 0
	 * 
	 * @param prompt the string for prompting
	 * @return the integer corresponding to the string
	 * 
	 */
	public int getNumber(String prompt) {
		do {
			try {
				int value = Integer.parseInt(getInput(prompt));
				if (value > 0) {
					return value;
				} else {
					System.out.println("Enter a number greater than 0.");
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Enter a number greater than 0.");
			}
		} while (true);
	}

	/**
	 * Queries for a yes or no and returns true for yes and false for no
	 * 
	 * @param prompt The string to be prepended to the yes/no prompt
	 * @return true for yes and false for no
	 * 
	 */
	private boolean yesOrNo(String prompt) {
		String more = getInput(prompt + " Enter 1 for yes or anything else for no.");
		if (more.charAt(0) != '1') {
			return false;
		}
		return true;
	}

	/**
	 * Method to be called for saving the Company object. Uses the appropriate
	 * Company method for saving.
	 * 
	 */
	private void save() {
		try {
			FileOutputStream fileOut = new FileOutputStream("data.ser");
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(company);
			objectOut.close();
			System.out.println("Data Saved.");
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * Method to be called for retrieving saved data. Uses the appropriate Company
	 * method for retrieval.
	 * 
	 */
	private void retrieve() {
		try {
			FileInputStream fileIn = new FileInputStream("data.ser");
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			company = (Company) objectIn.readObject();
			objectIn.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
		}
	}

	/**
	 * Method to be called for adding a component. Prompts the user for the
	 * appropriate values and uses the appropriate Component method for adding the
	 * component.
	 * 
	 */
	private void addComponent() {
		addMore = true;
		while (addMore == true) {
			String componentName = getInput("Please enter a name for the new component: ");
			String componentDescription = getInput("Please enter a description for the new component: ");
			int minimumReorder = getNumber("Please enter a minimum reorder quantity for the new component: ");
			company.addComponent(componentName, componentDescription, minimumReorder);
			addMore = yesOrNo("Would you like to add another component?");
		}
	}

	/**
	 * Method to be called for adding a supplier. Prompts the user for the
	 * appropriate values and uses the appropriate Company method for adding the
	 * supplier.
	 * 
	 */
	private void addSupplier() {
		addMore = true;
		while (addMore == true) {
			String supplierName = getInput("Please enter a name for the new supplier: ");
			String supplierAddress = getInput("Please enter an address for the new supplier: ");
			String supplierPhone = getInput("Please enter a phone number for the new supplier: ");
			company.addSupplier(supplierName, supplierAddress, supplierPhone);
			addMore = yesOrNo("Would you like to add another supplier?");
		}
	}

	/**
	 * Method to be called for adding a component to the list of components a
	 * supplier supplies. Prompts the user for the appropriate values and uses the
	 * appropriate Company method for adding the component to the list of components
	 * supplied.
	 * 
	 */
	private void addComponentSupplier() {
		int supplierID = getSupplierID("Please enter the ID of the supplier you wish to supply the component: ");
		int componentID = getComponentID("Please enter the ID of the component the supplier should supply: ");
		company.addComponentSupplier(componentID, supplierID);
	}

	/**
	 * Method to be called for adding a product. Prompts the user for the
	 * appropriate values and uses the appropriate Company method for adding the
	 * product.
	 * 
	 */
	private void addProduct() {
		String productName = getInput("Please enter a name for the new product: ");
		company.addProduct(productName);
	}

	/**
	 * Method to be called for creating an allocation. Prompts the user for the
	 * appropriate values and uses the appropriate Company method for creating the
	 * allocation.
	 * 
	 */
	private void createAllocation() {
		int componentID = getComponentID("Please enter the ID of the component you wish to allocate: ");
		int productID = getProductID("Please enter the ID of the product you wish to receieve the allocation: ");
		int quantity = getNumber("Please enter the quantity you wish to allocate: ");
		company.createAllocation(componentID, productID, quantity);
		Stock stock = company.findStock(componentID);
		if (company.requireReorder(stock)) {
			int supplierID = getReorderSupplier(
					"A reorder is being created. Please enter the ID of the supplier that should supply the reorder: ",
					componentID);
			if (supplierID == 0) {
				return;
			}
			company.createReorder(componentID, supplierID);
		}
	}

	/**
	 * Method to be called for creating an order. Prompts the user for the
	 * appropriate values and uses the appropriate Company method for creating the
	 * order.
	 * 
	 */
	private void createOrder() {
		int componentID = getComponentID("Please enter the ID of the component you wish to order: ");
		int supplierID = getSupplierID("Please enter the ID of the supplier you wish to order from: ");
		int quantity = getNumber("Please enter the quantity you wish to order: ");
		company.createOrder(componentID, supplierID, quantity, false);
	}

	/**
	 * Method to be called for fulfilling an order. Prompts the user for the
	 * appropriate values and uses the appropriate Company method for fulfilling the
	 * order.
	 * 
	 */
	private void fulfillOrder() {
		int orderNumber = getNumber("Please enter an outstanding order number: ");
		company.fulfillOrder(orderNumber);
	}

	/**
	 * Offers to load previous data into the system. If the user doesn't wish to
	 * load data, offers to generate a test bed for testing functionality instead.
	 * 
	 */
	private void startUp() {
		if (yesOrNo("Would you like to load previous data?")) {
			retrieve();
		} else {
			company = new Company();
			if (yesOrNo("Would you like to generate a test bed and test functionality using asserts?")) {
				automaticTest();
			}

		}
	}

	/**
	 * Method to be called for printing a component. Prompts the user for the
	 * appropriate values and uses the appropriate Company method for printing the
	 * component.
	 * 
	 */
	private void printComponent() {
		int componentID = getComponentID("Please enter the ID of the component you wish to print: ");
		if (componentID == 0) {
			return;
		}
		company.printComponent(componentID);
	}

	/**
	 * Method to be called for printing a supplier. Prompts the user for the
	 * appropriate values and uses the appropriate Company method for printing the
	 * supplier.
	 * 
	 */
	private void printSupplier() {
		int supplierID = getSupplierID("Please enter the ID of the supplier you wish to print: ");
		if (supplierID == 0) {
			return;
		}
		company.printSupplier(supplierID);
	}

	/**
	 * Method to be called for printing the list of outstanding orders. Prompts the
	 * user for the appropriate values and uses the appropriate Company method for
	 * printing the list.
	 * 
	 */
	private void listOutstandingOrders() {
		company.listOutstandingOrders();
	}

	/**
	 * Method to be called for printing the list of outstanding allocations. Prompts
	 * the user for the appropriate values and uses the appropriate Company method
	 * for printing the list .
	 * 
	 */
	private void listOutstandingAllocations() {
		company.listOutstandingAllocations();
	}

	/**
	 * Method to be called for printing all components. Prompts the user for the
	 * appropriate values and uses the appropriate Company method for printing the
	 * components.
	 * 
	 */
	private void printAllComponents() {
		if (company.getComponentCount() == 0) {
			System.out.println("No components exist.");
			return;
		}
		for (Component component : company.getComponentList()) {
			company.printComponent(component.getComponentID());
		}
	}

	/**
	 * Method to be called for printing all suppliers. Prompts the user for the
	 * appropriate values and uses the appropriate Company method for printing all
	 * suppliers.
	 * 
	 */
	private void printAllSuppliers() {
		if (company.getSupplierCount() == 0) {
			System.out.println("No suppliers exist.");
			return;
		}
		for (Supplier supplier : company.getSupplierList()) {
			company.printSupplier(supplier.getSupplierID());
		}
	}

	/**
	 * Gets an input while ensuring it corresponds to an existing component ID
	 * 
	 * @param prompt - whatever the user wants as prompt
	 * @return - the input from the keyboard
	 * 
	 */
	private int getComponentID(String prompt) {
		do {
			try {
				int componentID = getNumber(prompt);
				if (componentID == 0) {
					return 0;
				}
				company.findComponent(componentID).getComponentID();
				return componentID;
			} catch (NullPointerException e) {
				System.out.println("Please enter a valid component ID");
			}
		} while (true);
	}

	/**
	 * Gets an input while ensuring it corresponds to an existing component ID
	 * 
	 * @param prompt - whatever the user wants as prompt
	 * @return - the input from the keyboard
	 * 
	 */
	private int getSupplierID(String prompt) {
		do {
			try {
				int supplierID = getNumber(prompt);
				if (supplierID == 0) {
					return 0;
				}
				company.findSupplier(supplierID).getSupplierID();
				return supplierID;
			} catch (NullPointerException e) {
				System.out.println("Please enter a valid supplier ID");
			}
		} while (true);
	}

	/**
	 * Gets an input while ensuring it corresponds to an existing product ID
	 * 
	 * @param prompt - whatever the user wants as prompt
	 * @return - the input from the keyboard
	 * 
	 */
	private int getProductID(String prompt) {
		do {
			try {
				int productID = getNumber(prompt);
				if (productID == 0) {
					return 0;
				}
				company.findProduct(productID).getProductID();
				return productID;
			} catch (NullPointerException e) {
				System.out.println("Please enter a valid product ID");
			}
		} while (true);
	}

	/**
	 * Gets an input while ensuring it corresponds to a supplier ID of a supplier
	 * that supplies component corresponding to the parameter component ID
	 * 
	 * @param prompt - whatever the user wants as prompt
	 * @return - the input from the keyboard
	 * 
	 */
	private int getReorderSupplier(String prompt, int componentID) {
		boolean validSupplier = false;
		while (validSupplier == false) {
			int supplierID = getNumber(prompt);
			if (supplierID == 0) {
				return 0;
			}
			Supplier supplier = company.findSupplier(supplierID);
			for (int suppliedComponent : supplier.getComponentsSupplied()) {
				if (suppliedComponent == componentID) {
					validSupplier = true;
					return supplierID;
				}
			}
			System.out.println(
					"Invalid supplier ID. Please enter the ID for a supplier that offers component " + componentID);
			System.out.println("or enter 0 to return to main menu");
		}
		return 0;
	}

	/**
	 * Creates a test bed for automatic testing of functionality
	 * 
	 */
	private void automaticTest() {
		String[] componentNames = { "comp1", "comp2", "comp3", "comp4", "comp5", "comp6", "comp7", "comp8", "comp9",
				"comp10", "comp11", "comp12", "comp13", "comp14", "comp15", "comp16", "comp17", "comp18", "comp19",
				"comp20" };
		String[] componentDescriptions = { "desc1", "desc2", "desc3", "desc4", "desc5", "desc6", "desc7", "desc8",
				"desc9", "desc10", "desc11", "desc12", "desc13", "desc14", "desc15", "desc16", "desc17", "desc18",
				"desc19", "desc20" };
		int[] minimumReorders = { 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 };
		String[] productNames = { "prod1", "prod2", "prod3", "prod4", "prod5" };
		String[] supplierNames = { "supp1", "supp2", "supp3", "supp4", "supp5" };
		String[] supplierAddresses = { "add1", "add2", "add3", "add4", "add5" };
		String[] supplierPhones = { "phone1", "phone2", "phone3", "phone4", "phone5" };

		for (int index = 0; index < 20; index++) {
			company.addComponent(componentNames[index], componentDescriptions[index], minimumReorders[index]);
		}
		for (int index = 0; index < 5; index++) {
			company.addSupplier(supplierNames[index], supplierAddresses[index], supplierPhones[index]);
		}
		for (int index = 0; index < 5; index++) {
			company.addProduct(productNames[index]);
		}
		for (Supplier supplier : company.getSupplierList()) {
			for (Component component : company.getComponentList()) {
				company.addComponentSupplier(component.getComponentID(), supplier.getSupplierID());
			}
		}
		company.autoCreateAllocation(company.getComponentList().get(4).getComponentID(),
				company.getProductList().get(4).getProductID(), 22, company.getSupplierList().get(0).getSupplierID());
		company.autoCreateAllocation(company.getComponentList().get(8).getComponentID(),
				company.getProductList().get(3).getProductID(), 44, company.getSupplierList().get(1).getSupplierID());
		company.autoCreateAllocation(company.getComponentList().get(11).getComponentID(),
				company.getProductList().get(2).getProductID(), 56, company.getSupplierList().get(2).getSupplierID());
		company.autoCreateAllocation(company.getComponentList().get(17).getComponentID(),
				company.getProductList().get(1).getProductID(), 82, company.getSupplierList().get(3).getSupplierID());
		company.autoCreateAllocation(company.getComponentList().get(19).getComponentID(),
				company.getProductList().get(0).getProductID(), 11, company.getSupplierList().get(4).getSupplierID());

		company.createOrder(company.getComponentList().get(2).getComponentID(),
				company.getSupplierList().get(4).getSupplierID(), 14, false);
		company.createOrder(company.getComponentList().get(13).getComponentID(),
				company.getSupplierList().get(2).getSupplierID(), 18, false);
		company.createOrder(company.getComponentList().get(15).getComponentID(),
				company.getSupplierList().get(3).getSupplierID(), 26, false);
		company.createOrder(company.getComponentList().get(7).getComponentID(),
				company.getSupplierList().get(1).getSupplierID(), 42, false);
		company.createOrder(company.getComponentList().get(6).getComponentID(),
				company.getSupplierList().get(0).getSupplierID(), 4, false);

		company.fulfillOrder(1);
		company.fulfillOrder(8);
		company.fulfillOrder(3);
		company.fulfillOrder(9);
		company.fulfillOrder(5);

	}
}
