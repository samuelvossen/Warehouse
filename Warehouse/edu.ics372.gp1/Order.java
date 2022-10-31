import java.io.Serializable;

/**
 * This class represents an order for components
 * 
 * @author Samuel Vossen
 *
 */
public class Order implements Serializable {

	private static final long serialVersionUID = 989490680820018394L;
	private Stock orderDetails;
	private Supplier orderSupplier;
	private int orderNumber;
	private boolean outstanding;
	private boolean reorder;

	public void setOutstanding(boolean oustanding) {
		this.outstanding = oustanding;
	}

	public Order(Component componentOrdered, Supplier orderSupplier, int quantityOrdered, int ordersMade,
			boolean reorder) {
		this.orderDetails = new Stock(componentOrdered, quantityOrdered);
		this.orderSupplier = orderSupplier;
		this.orderNumber = ordersMade + 1;
		this.outstanding = true;
		this.reorder = reorder;
		ordersMade++;
		boolean firstTimeSupplier = true;
		for (int index = 0; index < orderSupplier.getComponentsSent().size(); index++) {
			if (componentOrdered.getComponentID() == orderSupplier.getComponentsSent().get(index).getComponent()
					.getComponentID()) {
				firstTimeSupplier = false;
			}
		}
		if (firstTimeSupplier == true) {
			orderSupplier.getComponentsSent().add(orderDetails);
		} else if (firstTimeSupplier == false) {
			for (int index = 0; index < orderSupplier.getComponentsSent().size(); index++) {
				if (componentOrdered.getComponentID() == orderSupplier.getComponentsSent().get(index).getComponent()
						.getComponentID()) {
					orderSupplier.getComponentsSent().get(index)
							.setQuantity(orderSupplier.getComponentsSent().get(index).getQuantity() + quantityOrdered);
				}
			}
		}
	}

	public boolean isReorder() {
		return reorder;
	}

	public void setReorder(boolean reorder) {
		this.reorder = reorder;
	}

	public Stock getOrderDetails() {
		return orderDetails;
	}

	public boolean isOutstanding() {
		return outstanding;
	}

	public void setOrderDetails(Stock orderDetails) {
		this.orderDetails = orderDetails;
	}

	public Supplier getOrderSupplier() {
		return orderSupplier;
	}

	public void setOrderSupplier(Supplier orderSupplier) {
		this.orderSupplier = orderSupplier;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public String toString() {
		String output = "";
		output += "Order Number: " + orderNumber;
		output += "\nComponent ID: " + orderDetails.getComponent().getComponentID();
		output += "\nComponent Name: " + orderDetails.getComponent().getComponentName();
		output += "\nSupplier ID: " + orderSupplier.getSupplierID();
		output += "\nSupplier Name: " + orderSupplier.getSupplierName();
		output += "\nQuantity Ordered: " + orderDetails.getQuantity() + "\n\n";
		return output;
	}
}