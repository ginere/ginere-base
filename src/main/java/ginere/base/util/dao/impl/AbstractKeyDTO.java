package ginere.base.util.dao.impl;

import ginere.base.util.dao.KeyDTO;
import ginere.base.util.descriptor.annotation.Description;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @author ventura
 *
 * Clase madra para todos los objetos producidos por un DAO
 */
@SuppressWarnings("serial")
public abstract class AbstractKeyDTO implements KeyDTO,Serializable{
	
	@Description
	protected final String id;
	
	protected AbstractKeyDTO(String id){
		this.id=id;
	}


	public String getId(){
		return id;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
