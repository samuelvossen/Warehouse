import java.io.Serializable;

/**
 * This class represents the stock of a component in inventory consisting of the
 * component and its quantity
 * 
 * @author Nick Samuel Vossen
 *
 */
public class Stock implements Serializable {

	private static final long serialVersionUID = 6243478334689242461L;
	private Component component;
	private int quantity = 0;

	public Stock(Component component, int quantity) {
		this.component = component;
		this.quantity = quantity;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
