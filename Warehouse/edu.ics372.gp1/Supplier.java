import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a supplier of components
 * 
 * @author Samuel Vossen
 *
 */
public class Supplier implements Serializable {

	private static final long serialVersionUID = 8531827992155819980L;
	private String supplierName;
	private String supplierAddress;
	private String supplierPhone;
	private int supplierID;
	private ArrayList<Stock> componentsSent = new ArrayList<Stock>();
	private ArrayList<Integer> componentsSupplied = new ArrayList<Integer>();

	public Supplier(String supplierName, String supplierAddress, String supplierPhone, int suppliersMade) {
		this.supplierName = supplierName;
		this.supplierAddress = supplierAddress;
		this.supplierPhone = supplierPhone;
		this.supplierID = suppliersMade + 1;
		suppliersMade++;
	}

	public List<Stock> getComponentsSent() {
		return componentsSent;
	}

	public void setComponentsSent(ArrayList<Stock> componentsSent) {
		this.componentsSent = componentsSent;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierAddress() {
		return supplierAddress;
	}

	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress;
	}

	public String getSupplierPhone() {
		return supplierPhone;
	}

	public void setSupplierPhone(String supplierPhone) {
		this.supplierPhone = supplierPhone;
	}

	public int getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(int supplierID) {
		this.supplierID = supplierID;
	}

	public ArrayList<Integer> getComponentsSupplied() {
		return componentsSupplied;
	}

	public void setComponentsSupplied(ArrayList<Integer> componentsSupplied) {
		this.componentsSupplied = componentsSupplied;
	}

	public void addComponentSupplied(int componentID) {
		componentsSupplied.add(componentID);
	}

	public void addComponentSent(Component component) {
		componentsSent.add(new Stock(component, 0));
	}

	public Stock getSupplierStock(int componentID) {
		for (Stock stock : componentsSent) {
			if (componentID == stock.getComponent().getComponentID()) {
				return stock;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		String output = "";
		output += "Supplier ID: " + supplierID;
		output += "\nName: " + supplierName;
		output += "\nAddress: " + supplierAddress;
		output += "\nPhone Number: " + supplierPhone;
		return output;
	}
}
