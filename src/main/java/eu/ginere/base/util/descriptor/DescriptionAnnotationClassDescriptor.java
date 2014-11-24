package eu.ginere.base.util.descriptor;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.ginere.base.util.descriptor.annotation.Description;

/**
 * This generates the information of the class bases on the @Description annotation.
 * 
 * @author ventura
 *
 */
public class DescriptionAnnotationClassDescriptor<T> extends AbstractClassDescriptor<T>{
	public static final Logger log = Logger.getLogger(DescriptionAnnotationClassDescriptor.class);
	
	protected final List<InnerPropertyDescriptor> list;
	protected final Map<String,InnerPropertyDescriptor> map;
	protected String propertyNames[]=null;;
	
	public DescriptionAnnotationClassDescriptor(Class<T> clazz) throws IntrospectionException{
		super(clazz);
		
		this.list=new ArrayList<InnerPropertyDescriptor>();
		this.map=new Hashtable<String,InnerPropertyDescriptor>();
		
		exploreClass(clazz);
	}
	
	
	/**
	 * Add the proerties of this class and the parent class
	 * 
	 * @param clazzToAdd
	 * @throws IntrospectionException
	 */
	private void exploreClass(Class<?>  clazzToAdd) throws IntrospectionException{
		List<InnerPropertyDescriptor> classList=getProperties(clazzToAdd);
	
		for (InnerPropertyDescriptor descriptor:classList){
			map.put(descriptor.getName(),descriptor);
		}
			
		this.list.addAll(classList);

		Class<?>  superClazz=clazzToAdd.getSuperclass();
		if (superClazz !=null){
			exploreClass(superClazz);
		}
	}
	
	protected List<InnerPropertyDescriptor> getProperties(Class<?>  clazz) throws IntrospectionException{
		 Field fs[] = clazz.getDeclaredFields();
		 List <InnerPropertyDescriptor>list=new ArrayList<InnerPropertyDescriptor>(fs.length);
		 
		 for (Field field:fs){
			 Description anotation=(Description)field.getAnnotation(Description.class);
			 String description;
			 field.setAccessible(true);
			 
			 if (anotation !=null){
				 description=anotation.description();
				 list.add(new InnerPropertyDescriptor(clazz,field.getName(),description,field.getType(),field)); 
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

	public String[] getPropertyNames() {
		if (propertyNames == null){
			propertyNames=new String[list.size()];
			int i=0;
			for ( InnerPropertyDescriptor prop:list){
				propertyNames[i++]=prop.getName();
			}
		}

		return propertyNames;
	}

	public String[] getStringValues(Object t,String propertyNames[]){
		String ret[]=new String[propertyNames.length];
		
		int i=0;
		for (String propName:propertyNames){
			
			ret[i++]=getProperyStringValue(t,propName);
		}
		
		return ret;
	}
	
	public String getProperyStringValue(Object t,String propertyName){
		InnerPropertyDescriptor propertyDescriptor=get(propertyName);

		return getStringValue(t,propertyDescriptor);
	}
	
	public String getStringValue(Object t,InnerPropertyDescriptor propertyDescriptor){
		Field field=(Field)propertyDescriptor.getAccessor();
		
		String stringValue;
		try {
			Object objectValue=field.get(t);
			if (objectValue==null){
				stringValue=null;
			} else {
				stringValue=objectValue.toString();
			}

			if (stringValue == null){
				return StringUtils.EMPTY;
			} else {
				return stringValue;
			}
		} catch (IllegalAccessException e) {
			log.error("Descriptor:"+propertyDescriptor+" propertyDescriptor:"+propertyDescriptor,e);
			return StringUtils.EMPTY;
		}
	}

	@Override
	public void iterate(T src,
			eu.ginere.base.util.descriptor.AbstractClassDescriptor.Iterator iterator) {
	
		if (src==null){
			return ;
		}
		
		for (InnerPropertyDescriptor property:list){
			String name=property.getName();
			Field field=(Field)property.getAccessor();
			try {
				Object value=field.get(src);
				iterator.visit(name,value);
			}catch (Exception e) {
				log.error("While visit fieldName:'"+name+"' field:'"+field+"' over the object:'"+src+"'",e); 
			}
		}
		
	}


	@Override
	public InnerPropertyDescriptor get(String propertyName) {
		return map.get(propertyName);
	}
}
