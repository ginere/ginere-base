package eu.ginere.base.util.descriptor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This generates the information of the class based on the @see java.beans.PropertyDescriptor, but generates only
 * the documentation of the dessigned fields, the rest are ignored.
 * 
 * @author ventura
 *
 */
public class PropertyListClassDescriptor<T> extends AbstractClassDescriptor<T> {
	public static final Logger log = Logger.getLogger(PropertyListClassDescriptor.class);
	
	protected final List<InnerPropertyDescriptor> list;
	protected final Map<String,InnerPropertyDescriptor> map;
	
	private final HashMap <String,String>properties=new HashMap<String,String>();
	
	/**
	 * @param clazz
	 * @param properyList One array with the names an description of the properties to add. Ex: { {"propertyName1","propertyDescription1"},{"propertyName2"},{"propertyName3","propertyDescription3"}}
	 * @throws IntrospectionException
	 */
	public PropertyListClassDescriptor(Class<T>  clazz,String [][]properyList) throws IntrospectionException{
		super(clazz);
		
    	for (String []array:properyList){
    		String propertyName=array[0];
    		String description;
    		
    		if (array.length>1){
    			description=array[1];
    		} else {
    			description="";
    		}
    		
    		properties.put(propertyName,description);
    	}
		
		this.list=getProperties(clazz);
		
		this.map=new Hashtable<String,InnerPropertyDescriptor>(list.size());

		for (InnerPropertyDescriptor descriptor:list){
			map.put(descriptor.getName(),descriptor);
		}
    	
	}
	
	protected List<InnerPropertyDescriptor> getProperties(Class<?> clazz) throws IntrospectionException{ 
		BeanInfo info = Introspector.getBeanInfo(clazz);
		
		PropertyDescriptor array[]=info.getPropertyDescriptors();
		 
		List <InnerPropertyDescriptor>list=new ArrayList<InnerPropertyDescriptor>();
		 
		 for (PropertyDescriptor propertyDescriptor:array){
			 String propertyName=propertyDescriptor.getName();
			 
			 if (properties.containsKey(propertyName)){
				 Class <?>propertyClazz=propertyDescriptor.getPropertyType();
				 String description=properties.get(propertyName);
				 
				 list.add(new InnerPropertyDescriptor(clazz,propertyName,description,propertyClazz,propertyDescriptor.getReadMethod()));
			 }	 

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
			eu.ginere.base.util.descriptor.AbstractClassDescriptor.Iterator iterator)  {
		if (src==null){
			return ;
		}
		
//		for (InnerPropertyDescriptor property:list){
//			PropertyDescriptor descriptor=(PropertyDescriptor)property.getAccessor();
//			String name=property.getName();
//			Object value=descriptor.getValue(src);
//			iterator.visit(name,value);
//		}
		
		for (InnerPropertyDescriptor property:list){
//			PropertyDescriptor descriptor=(PropertyDescriptor)property.getAccessor();
			Method readMethod=(Method)property.getAccessor();
			String name=property.getName();
//			Object value=descriptor.getValue(name);
			try {
				Object value=readMethod.invoke(src);
				iterator.visit(name,value);
			}catch (Exception e) {
				log.error("While visit fieldName:'"+name+"' readMethod:'"+readMethod+"' over the object:'"+src+"'",e); 
			}
		}	
	}
	
	@Override
	public InnerPropertyDescriptor get(String propertyName) {
		return map.get(propertyName);
	}
}
