package eu.ginere.base.util.dao.impl;

import eu.ginere.base.util.dao.KeyAutoincrementDTO;
import eu.ginere.base.util.descriptor.annotation.Description;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @author ventura
 *
 * Clase madra para todos los objetos producidos por un DAO
 */
public abstract class AbstractKeyAutoincrementDTO implements KeyAutoincrementDTO{
	
	@Description
	protected String id;

	protected AbstractKeyAutoincrementDTO(String id){
		this.id=id;
	}


	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id=id;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
