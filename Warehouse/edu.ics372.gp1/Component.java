import java.io.Serializable;

/**
 * This class represents a component in the system
 * 
 * @author Samuel Vossen
 *
 */
public class Component implements Serializable {

	private static final long serialVersionUID = -3015759509199147001L;
	private String componentName;
	private String componentDescription;
	private int minimumReorder;
	private int componentID;

	public Component(String componentName, String componentDescription, int minimumReorder, int componentsMade) {
		this.componentName = componentName;
		this.componentDescription = componentDescription;
		this.minimumReorder = minimumReorder;
		this.componentID = componentsMade + 1;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getComponentDescription() {
		return componentDescription;
	}

	public void setComponentDescription(String componentDescription) {
		this.componentDescription = componentDescription;
	}

	public int getMinimumReorder() {
		return minimumReorder;
	}

	public void setMinimumReorder(int minimumReorder) {
		this.minimumReorder = minimumReorder;
	}

	public int getComponentID() {
		return componentID;
	}

	public void setComponentID(int componentID) {
		this.componentID = componentID;
	}

	@Override
	public String toString() {
		String output = "";
		output += "\nComponent ID: " + componentID;
		output += "Name: " + componentName;
		output += "\nDescription: " + componentDescription;
		output += "\nMinimum Reorder Quantity: " + minimumReorder;
		return output;
	}
}
