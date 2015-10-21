package eu.ginere.base.util.dao.jdbc;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author ventura
 *
 * Clase madra para todos los objetos producidos por un DAO
 */
public abstract class KeyDTO extends DTO{

	protected String key;

	protected KeyDTO(){
		
	}

	protected KeyDTO (String id) {
		this.key=id;
	}

	public String getKey(){
		return key;
	}
	
	public void setKey(String key){
		this.key=key;
	}

	public String toString() {
//		return ToStringBuilder.reflectionToString(this);
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
