package ginere.base.util.descriptor;

import java.beans.IntrospectionException;
import java.util.List;

/**
 * Mother class for all de descriptor of classes. A class descriptor contains information about the classes, this information
 * may be the methos attributed, etc ... . It can be bassed on anotation of the classes.
 * 
 * @author ventura
 *
 */
public abstract class AbstractClassDescriptor<T> {

	protected final Class<T> clazz;
	
	public AbstractClassDescriptor(Class<T> clazz) throws IntrospectionException{
		this.clazz=clazz;
	}
	
	protected abstract List<InnerPropertyDescriptor> getProperties(Class<?> clazz) throws IntrospectionException;

	/**
	 * @return the list
	 */
	public abstract List<InnerPropertyDescriptor> getList();
	
	public abstract void iterate(T src,Iterator iterator);
	
	/**
	 * Returns the property description of this filed.
	 * If no one null is returned.
	 * 
	 * @param propertyName
	 * @return
	 */
	public abstract InnerPropertyDescriptor get(String propertyName);
	
	public interface Iterator{
		void visit(String name, Object value);
	}
}
