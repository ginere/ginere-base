package ginere.base.util.dao;

/**
 * @author ventura
 *
 * This is for objects that can be identified by one uniq ID
 */
public interface KeyAutoincrementDTO extends KeyDTO{
	
	/**
	 * @return the id
	 */
	public void setId(String id);
}
