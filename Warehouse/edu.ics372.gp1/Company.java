import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a company which operates the very business processes
 * 
 * @Samuel Vossen
 */
public class Company implements Serializable {

	private static final long serialVersionUID = 1173725917548293104L;
	private int componentCount = 0;
	private int supplierCount = 0;
	private int productCount = 0;
	private int orderCount = 0;
	private ArrayList<Stock> inventory = new ArrayList<Stock>();
	private ArrayList<Component> componentList = new ArrayList<Component>();
	private ArrayList<Supplier> supplierList = new ArrayList<Supplier>();
	private ArrayList<Product> productList = new ArrayList<Product>();
	private ArrayList<Order> orderList = new ArrayList<Order>();
	private ArrayList<Allocation> allocationList = new ArrayList<Allocation>();
	private static Company company;

	public Company() {
	}

	/**
	 * Serializes the Company object
	 * 
	 * @return true if the data could be saved, otherwise false
	 */
	public boolean save() {
		try {
			FileOutputStream file = new FileOutputStream("CompanyData");
			ObjectOutputStream output = new ObjectOutputStream(file);
			output.writeObject(company);
			file.close();
			return true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
	}

	/**
	 * Retrieves a deserialized version of the company from disk
	 * 
	 * @return a Company object
	 */
	public Company retrieve() {
		try {
			FileInputStream file = new FileInputStream("CompanyData");
			ObjectInputStream input = new ObjectInputStream(file);
			company = (Company) input.readObject();
			input.close();
			return company;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			return null;
		}
	}

	/**
	 * Supports the singleton pattern
	 * 
	 * @return the singleton object
	 */
	public static Company instance() {
		if (company == null) {
			return company = new Company();
		} else {
			return company;
		}
	}

	/**
	 * Organizes the operations for adding a component
	 * 
	 * @param componentName        the component's name
	 * @param componentDescription the component's description
	 * @param minimumReorder       the minimum reorder quantity for the component
	 */
	public void addComponent(String componentName, String componentDescription, int minimumReorder) {
		getComponentList().add(new Component(componentName, componentDescription, minimumReorder, componentCount));
		Component component = findComponent(componentCount + 1);
		inventory.add(new Stock(component, 0));
		System.out.println(component);
		componentCount++;
	}

	/**
	 * Organizes the operations for adding a supplier
	 * 
	 * @param supplierName    the supplier's name
	 * @param supplierAddress the supplier's address
	 * @param supplierPhone   the supplier's phone number
	 */
	public void addSupplier(String supplierName, String supplierAddress, String supplierPhone) {
		getSupplierList().add(new Supplier(supplierName, supplierAddress, supplierPhone, getSupplierCount()));
		Supplier supplier = findSupplier(getSupplierCount() + 1);
		System.out.println(supplier);
		setSupplierCount(getSupplierCount() + 1);
	}

	/**
	 * Organizes the operations for adding a component to the list of components
	 * supplied by a supplier
	 * 
	 * @param componentID the ID of the component to be added
	 * @param supplierID  the ID of the supplier to be added to
	 */
	public void addComponentSupplier(int componentID, int supplierID) {
		Supplier supplier = findSupplier(supplierID);
		Component component = findComponent(componentID);
		supplier.addComponentSupplied(componentID);
		supplier.addComponentSent(component);
		System.out.println(
				"Supplier " + supplier.getSupplierName() + " supplies component " + component.getComponentName());
	}

	/**
	 * Organizes the operations for adding a product
	 * 
	 * @param productName the product's name
	 */
	public void addProduct(String productName) {
		getProductList().add(new Product(productName, productCount));
		Product product = findProduct(productCount + 1);
		System.out.println(product);
		productCount++;
	}

	/**
	 * Organizes the operations for creating an allocation
	 * 
	 * @param componentID   the ID of the component to allocate
	 * @param productID     the ID of the product to receive the allocation
	 * @param inputQuantity the quantity of components to allocate
	 */
	public void createAllocation(int componentID, int productID, int inputQuantity) {
		Stock stock = findStock(componentID);
		int allocationQuantity = inputQuantity;
		if (allocationQuantity > stock.getQuantity()) {
			allocationQuantity = allocationQuantity - stock.getQuantity();
			stock.setQuantity(0);
			allocationList.add(new Allocation(componentID, productID, allocationQuantity, false));
		} else {
			stock.setQuantity(stock.getQuantity() - allocationQuantity);
			allocationList.add(new Allocation(componentID, productID, 0, true));
		}
	}

	/**
	 * Organizes the operations to create allocations automatically for the test bed
	 * 
	 * @param componentID   the ID of the component to allocate
	 * @param productID     the ID of the product to receive the allocation
	 * @param inputQuantity the quantity of components to allocate
	 * @param supplierID    the ID of the supplier for any automatic reorders that
	 *                      occur
	 */
	public void autoCreateAllocation(int componentID, int productID, int inputQuantity, int supplierID) {
		Stock stock = findStock(componentID);
		int allocationQuantity = inputQuantity;
		if (allocationQuantity > stock.getQuantity()) {
			allocationQuantity = allocationQuantity - stock.getQuantity();
			stock.setQuantity(0);
			allocationList.add(new Allocation(componentID, productID, allocationQuantity, false));
		} else {
			stock.setQuantity(stock.getQuantity() - allocationQuantity);
			allocationList.add(new Allocation(componentID, productID, 0, true));
		}
		if (requireReorder(stock)) {
			createReorder(componentID, supplierID);
		}
	}

	/**
	 * Checks whether a reorder will be required from a component's stock going
	 * below its minimum reorder value
	 * 
	 * @param stock the stock containing the component to be checked
	 * @return true if a reorder is required, otherwise false
	 */
	public boolean requireReorder(Stock stock) {
		if (stock.getQuantity() < stock.getComponent().getMinimumReorder()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Organizes the operations for creating an order
	 * 
	 * @param componentID the ID of the component to order
	 * @param supplierID  the ID of the supplier for the order
	 * @param quantity    the quantity of components in the order
	 * @param isReorder   true if the order is a reorder, otherwise false
	 */
	public void createOrder(int componentID, int supplierID, int quantity, boolean isReorder) {
		Component component = findComponent(componentID);
		Supplier supplier = findSupplier(supplierID);
		orderList.add(new Order(component, supplier, quantity, orderCount, isReorder));
		System.out.println(orderList.get(orderCount));
		orderCount++;
	}

	/**
	 * Organizes the operations for creating a reorder
	 * 
	 * @param componentID the ID of the component to reorder
	 * @param supplierID  the ID of the supplier for the reorder
	 */
	public void createReorder(int componentID, int supplierID) {
		Stock stock = findStock(componentID);
		int reorderQuantity = Math.max((stock.getComponent().getMinimumReorder() * 2),
				allocationList.get(allocationList.size() - 1).getQuantity());
		createOrder(componentID, supplierID, reorderQuantity, true);
	}

	/**
	 * Organizes the operations for fulfilling an order
	 * 
	 * @param orderNumber the order number of the order to fulfill
	 */
	public void fulfillOrder(int orderNumber) {
		Order order = findOrder(orderNumber);
		int componentID = order.getOrderDetails().getComponent().getComponentID();
		Stock stock = findStock(componentID);
		Supplier supplier = order.getOrderSupplier();
		if (order.isOutstanding() == true) {
			stock.setQuantity(stock.getQuantity() + order.getOrderDetails().getQuantity());
			Stock supplierStock = supplier.getSupplierStock(componentID);
			supplierStock.setQuantity(supplierStock.getQuantity() + order.getOrderDetails().getQuantity());
		} else {
			System.out.println("Order number " + order.getOrderNumber() + " has already been fulfilled.");
			return;
		}
		System.out.println("Order for component: " + componentID + " " + stock.getComponent().getComponentName());
		System.out.println("From supplier: " + supplier.getSupplierID() + " " + supplier.getSupplierName());
		System.out.println("Has been fulfilled.\n");
		order.setOutstanding(false);
		// If the order is a reorder, fulfills any possible allocations in first come,
		// first serve order
		if (order.isReorder()) {
			for (Allocation allocation : allocationList) {
				if (allocation.getComponentID() == componentID && stock.getQuantity() > 0) {
					if (allocation.getComplete() == false) {
						// If the allocation can be completely filled
						if (allocation.getQuantity() <= stock.getQuantity()) {
							stock.setQuantity(stock.getQuantity() - allocation.getQuantity());
							System.out.println("Allocation for product " + allocation.getProductID() + " "
									+ findProduct(allocation.getProductID()).getProductName());
							System.out.println("Completed with " + allocation.getQuantity() + " components from order "
									+ orderNumber + "\n");
							allocation.setQuantity(0);
							allocation.setComplete(true);
							if (stock.getQuantity() == 0) {
								break;
							}
							// If the allocation cannot be completely filled
						} else {
							int availableQuantity = stock.getQuantity();
							stock.setQuantity(0);
							allocation.setQuantity(allocation.getQuantity() - availableQuantity);
							System.out.println("Allocation for product " + allocation.getProductID() + " "
									+ findProduct(allocation.getProductID()).getProductName());
							System.out.println("Partially filled with " + availableQuantity + " components from order "
									+ orderNumber);
							System.out.println("This allocation still requires " + allocation.getQuantity()
									+ " more components to be complete.");
							break;
						}
					}
				}
			}
			System.out.println("The update stock in inventory for component " + componentID + " "
					+ stock.getComponent().getComponentName() + " is " + stock.getQuantity());
		}
	}

	/**
	 * Organizes the operations for printing a component
	 * 
	 * @param componentID the ID of the component to print
	 */
	public void printComponent(int componentID) {
		Component component = findComponent(componentID);
		Stock stock = findStock(componentID);
		System.out.println(component);
		System.out.println("Quantity in stock: " + stock.getQuantity());
		System.out.println("Supplied by: ");
		ArrayList<Supplier> componentSuppliers = suppliedBy(componentID);
		for (Supplier supplier : componentSuppliers) {
			System.out.println(supplier.getSupplierName());
		}
	}

	/**
	 * Organizes the operations for printing a supplier
	 * 
	 * @param supplierID the ID of the supplier to print
	 */
	public void printSupplier(int supplierID) {
		Supplier supplier = findSupplier(supplierID);
		System.out.println(supplier);
		System.out.println("Supplies: ");
		for (Stock stock : supplier.getComponentsSent()) {
			System.out.println(stock.getComponent().getComponentName());
		}
	}

	/**
	 * Prints the list of all outstanding orders
	 */
	public void listOutstandingOrders() {
		int outstandingOrderCount = 0;
		System.out.println("\nOutstanding orders: \n");
		for (Order order : orderList) {
			if (order.isOutstanding()) {
				System.out.println(order);
				outstandingOrderCount++;
			}
		}
		if (outstandingOrderCount < 1) {
			System.out.println("There are no outstanding orders.");
		}
	}

	/**
	 * Prints the list of all outstanding allocations
	 */
	public void listOutstandingAllocations() {
		int outstandingAllocationCount = 0;
		System.out.println("\nOutstanding Allocations: \n");
		for (Allocation allocation : allocationList) {
			if (!allocation.getComplete()) {
				System.out.println("Product: " + findProduct(allocation.getProductID()).getProductName());
				System.out.println("Component: " + findComponent(allocation.getComponentID()).getComponentName());
				System.out.println("Quantity Required: " + allocation.getQuantity() + "\n");
				outstandingAllocationCount++;
			}
		}
		if (outstandingAllocationCount < 1) {
			System.out.println("There are no outstanding allocations.");
		}
	}

	/**
	 * Finds a component in the componentList from its component ID
	 * 
	 * @param componentID the ID of the component
	 * @return the corresponding component object
	 */
	public Component findComponent(int componentID) {
		for (Component component : getComponentList()) {
			if (componentID == component.getComponentID()) {
				return component;
			}
		}
		return null;
	}

	/**
	 * Finds a stock in the inventory from its component ID
	 * 
	 * @param componentID the ID of the component
	 * @return the corresponding stock object
	 */
	public Stock findStock(int componentID) {
		for (Stock stock : inventory) {
			if (componentID == stock.getComponent().getComponentID()) {
				return stock;
			}
		}
		return null;
	}

	/**
	 * Finds a supplier in the supplierList from its supplier ID
	 * 
	 * @param supplierID the ID of the supplier
	 * @return the corresponding supplier object
	 */
	public Supplier findSupplier(int supplierID) {
		for (Supplier supplier : getSupplierList()) {
			if (supplierID == supplier.getSupplierID()) {
				return supplier;
			}
		}
		return null;
	}

	/**
	 * Finds a product in the productList from its product ID
	 * 
	 * @param productID the ID of the supplier
	 * @return the corresponding product object
	 */
	public Product findProduct(int productID) {
		for (Product product : getProductList()) {
			if (productID == product.getProductID()) {
				return product;
			}
		}
		return null;
	}

	/**
	 * Finds an order in the orderList from its order number
	 * 
	 * @param orderNumber the order number of the order
	 * @return the corresponding order object
	 */
	public Order findOrder(int orderNumber) {
		for (Order order : orderList) {
			if (orderNumber == order.getOrderNumber()) {
				return order;
			}
		}
		return null;
	}

	/**
	 * Finds the list of suppliers that supplies a component
	 * 
	 * @param inputComponent the component to find the list of suppliers for
	 * @return an ArrayList of supplier objects that all supply the input component
	 */
	public ArrayList<Supplier> suppliedBy(int inputComponent) {
		ArrayList<Supplier> componentSuppliers = new ArrayList<Supplier>();
		for (Supplier supplier : getSupplierList()) {
			for (int suppliedComponent : supplier.getComponentsSupplied()) {
				if (inputComponent == suppliedComponent) {
					componentSuppliers.add(supplier);
				}
			}
		}
		return componentSuppliers;
	}

	public int getComponentCount() {
		return componentCount;
	}

	public ArrayList<Component> getComponentList() {
		return componentList;
	}

	public void setComponentList(ArrayList<Component> componentList) {
		this.componentList = componentList;
	}

	public ArrayList<Supplier> getSupplierList() {
		return supplierList;
	}

	public void setSupplierList(ArrayList<Supplier> supplierList) {
		this.supplierList = supplierList;
	}

	public ArrayList<Product> getProductList() {
		return productList;
	}

	public void setProductList(ArrayList<Product> productList) {
		this.productList = productList;
	}

	public int getSupplierCount() {
		return supplierCount;
	}

	public void setSupplierCount(int supplierCount) {
		this.supplierCount = supplierCount;
	}

}