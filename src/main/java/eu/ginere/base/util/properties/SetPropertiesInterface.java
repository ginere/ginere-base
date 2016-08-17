package eu.ginere.base.util.properties;

import eu.ginere.base.util.dao.DaoManagerException;


public interface SetPropertiesInterface extends PropertiesInterface{

	/**
	 * This set a value in the backend. The default vslue flag will be marked false.
	 *
	 * @param propertyName
	 * @param description
	 * @param value
	 */
	public void setValue(PropertyDescriptor desc,String value)  throws DaoManagerException;
	
}
