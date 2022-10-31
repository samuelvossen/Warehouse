import java.io.Serializable;

/**
 * This class handles the allocation of components to products
 * 
 * @author Samuel Vossen
 *
 */
public class Allocation implements Serializable {

	private static final long serialVersionUID = 738526660816519885L;
	private int componentID;
	private int productID;
	private int quantity;
	private boolean complete;

	public Allocation(int componentID, int productID, int allocationQuantity, boolean complete) {
		this.componentID = componentID;
		this.productID = productID;
		this.quantity = allocationQuantity;
		this.complete = complete;
	}

	public boolean getComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public int getComponentID() {
		return componentID;
	}

	public void setComponentID(int componentID) {
		this.componentID = componentID;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int allocationQuantity) {
		this.quantity = allocationQuantity;
	}
}