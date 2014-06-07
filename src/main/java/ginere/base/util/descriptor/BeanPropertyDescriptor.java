package ginere.base.util.descriptor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * This generates the information of the class based on the @see java.beans.PropertyDescriptor.
 * A PropertyDescriptor describes one property that a Java Bean exports via a pair of accessor methods.
 * 
 * @author ventura
 *
 */
public class BeanPropertyDescriptor<T> extends AbstractClassDescriptor<T>{

	protected final List<InnerPropertyDescriptor> list;
	protected final Map<String,InnerPropertyDescriptor> map;
	
	public BeanPropertyDescriptor(Class<T> clazz) throws IntrospectionException{
		super(clazz);
		this.list=getProperties(clazz);

		this.map=new Hashtable<String,InnerPropertyDescriptor>(list.size());

		for (InnerPropertyDescriptor descriptor:list){
			map.put(descriptor.getName(),descriptor);
		}
	}
	
	protected List<InnerPropertyDescriptor> getProperties(Class<?> clazz) throws IntrospectionException{
		BeanInfo info = Introspector.getBeanInfo(clazz);
		
		PropertyDescriptor array[]=info.getPropertyDescriptors();
		
		List <InnerPropertyDescriptor>list=new ArrayList<InnerPropertyDescriptor>(array.length);
		
		for (PropertyDescriptor propertyDescriptor:array){
			String propertyName=propertyDescriptor.getName();
			Class<?> propertyClazz=propertyDescriptor.getPropertyType();
			String description=null;
			
			list.add(new InnerPropertyDescriptor(clazz,propertyName,description,propertyClazz,propertyDescriptor));
		}
		
		return list;
	}

	/**
	 * @return the list
	 */
	public List<InnerPropertyDescriptor> getList() {
		return list;
	}
	
	@Override
	public void iterate(T src,
			ginere.base.util.descriptor.AbstractClassDescriptor.Iterator iterator)  {
		if (src==null){
			return ;
		}
		
		for (InnerPropertyDescriptor property:list){
			PropertyDescriptor descriptor=(PropertyDescriptor)property.getAccessor();
			String name=property.getName();
			Object value=descriptor.getValue(name);
			iterator.visit(name,value);
		}		
	}
	
	@Override
	public InnerPropertyDescriptor get(String propertyName) {
		return map.get(propertyName);
	}
}
