package eu.ginere.base.util.container.exp.impl;

import eu.ginere.base.util.container.exp.ExpContainerKeyObject;


/**
 * @author ventura
 *

 * @see AbstractExpContainer,ExpContainerObject
 */
public class AbstractExpContainerKeyObject extends AbstractExpContainerObject implements ExpContainerKeyObject{
	
	public final String key;
	
	protected AbstractExpContainerKeyObject(String key){
		this.key=key;
	}
	/**
	 * @return the id
	 */
	public String getId(){
		return key;
	}
}
