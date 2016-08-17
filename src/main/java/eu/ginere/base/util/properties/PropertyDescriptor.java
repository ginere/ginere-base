package eu.ginere.base.util.properties;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author ventura
 *
 * Propertie descriptions
 */
public class PropertyDescriptor{

	final public String name;
	final public String description;
	public String value;
	final public boolean defaultValue;

	public PropertyDescriptor(String name, 
			  String description) {
		this.name=name;
		this.description=description;
		this.value=null;
		this.defaultValue=true;
	}
	
	public PropertyDescriptor(String name, 
			  String description,
			  String value,
			  boolean defaultValue) {
		this.name=name;
		this.description=description;
		this.value=value;
		this.defaultValue=defaultValue;
	}
	
//	public PropertyDescriptor(String name, 
//							  String description,
//							  String value) {
//
//		this.name=name;
//		this.description=description;
//		this.value=value;
//		this.defaultValue=false;
//	}
	
	public void update(String value) {
		this.value=value;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
