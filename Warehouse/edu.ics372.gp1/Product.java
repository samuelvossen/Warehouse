import java.io.Serializable;

/**
 * This class represents a product in the system
 * 
 * @author Samuel Vossen
 *
 */
public class Product implements Serializable {

	private static final long serialVersionUID = 1224653287371338467L;
	private String productName;
	private int productID;

	public Product(String productName, int productsMade) {
		this.productName = productName;
		this.productID = productsMade + 1;
		productsMade++;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	@Override
	public String toString() {
		String output = "";
		output += "Product ID: " + productID;
		output += "\nName: " + productName;
		return output;
	}
}