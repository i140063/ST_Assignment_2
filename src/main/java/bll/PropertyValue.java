package bll;

import java.util.ArrayList;
import java.util.List;

public class PropertyValue {
	
	String propertyName;
	List<String> values;
	
	public PropertyValue(String string, String string2) {
		// TODO Auto-generated constructor stub
		values = new ArrayList<String>();
		this.propertyName = string;
		values.add(string2);
	}
	
	public void addValue (String val) {
		this.values.add(val);
	}

	public String getPropertyName() {
		return propertyName;
	}
	/*public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}*/
	public List<String> getValues() {
		return values;
	}
	/*public void setValues(List<String> values) {
		this.values = values;
	}*/
	

	/*public PropertyValue() {
		values = new ArrayList<String>();
	}*/
	

}
