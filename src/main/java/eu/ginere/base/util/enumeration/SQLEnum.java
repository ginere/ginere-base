package eu.ginere.base.util.enumeration;

import eu.ginere.base.util.descriptor.annotation.Description;
import eu.ginere.base.util.i18n.I18NConnector;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


@SuppressWarnings("serial")
public abstract class SQLEnum implements Serializable {
	static protected final Logger log = Logger.getLogger(SQLEnum.class);
	
	private static long lastModified=System.currentTimeMillis();
	
	@Description(description="Uniq id per class")
	private final String id;
	@Description
	private final String name;

	private final String description;
	
	private final Class<? extends SQLEnum> clazz;

	static private final Hashtable<Class<? extends SQLEnum>,List<SQLEnum>> valuesList=new Hashtable<Class<? extends SQLEnum>,List<SQLEnum>>();

	static private final Hashtable<Class<? extends SQLEnum>,Map<String,SQLEnum>> valuesCache=new Hashtable<Class<? extends SQLEnum>,Map<String,SQLEnum>>();

	protected SQLEnum(String id,String name,String description,Class<? extends SQLEnum> clazz){
		this.id=id;
		this.name=name;
		this.description=description;
		this.clazz=clazz;

		init();
	}


	protected SQLEnum(String id,String name,String description){
		this.id=id;
		this.name=name;
		this.description=description;
		this.clazz=this.getClass();
		
		init();
	}
	
	private void init(){		
		synchronized(valuesList){
			if (!valuesList.containsKey(clazz)){
				List <SQLEnum>list=new ArrayList<SQLEnum>();
				valuesList.put(clazz,list);
				
				Map<String,SQLEnum> map=new Hashtable<String,SQLEnum>();
				valuesCache.put(clazz,map);
			}
		}
		
		Map<String,SQLEnum> map=valuesCache.get(clazz);
		if (!map.containsKey(id)){
			map.put(id,this);	
			
			List <SQLEnum> list=valuesList.get(clazz);
			list.add(this);
		} 
		
		lastModified=System.currentTimeMillis();
	}
	
//	public static Enumeration<Class<? extends SQLEnum>> getChildClasses(){	
//		return valuesList.keys();
//	}
	
	public static Set<Class<? extends SQLEnum>> getChildClasses(){	
		return valuesList.keySet();
	}
	protected void delete(){		
//		synchronized(valuesList){
//			if (!valuesList.containsKey(clazz)){
//				List <SQLEnum>list=new ArrayList<SQLEnum>();
//				valuesList.put(clazz,list);
//				
//				Map<String,SQLEnum> map=new Hashtable<String,SQLEnum>();
//				valuesCache.put(clazz,map);
//			}
//		}

		List <SQLEnum> list=valuesList.get(clazz);
		for (Iterator<SQLEnum> iterator=list.iterator();iterator.hasNext();){	
			SQLEnum enumVal=iterator.next();
			
			if (id.equals(enumVal.getId())){
				iterator.remove();
			}
		}
		

		
		Map<String,SQLEnum> map=valuesCache.get(clazz);
		map.remove(id);		
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return I18NConnector.getLabel(clazz,name);
	}

	public String getDescription() {
		return I18NConnector.getLabel(clazz,description);
	}
	
	public String toString() {
		return id;
	}
	
	/**
	 * Hay un problema se puede llamar a este metodo antes de cargar la clase hija por lo que habria un proiblema
	 * @param clazz
	 * @param value
	 * @return
	 */
	protected static SQLEnum value(Class<? extends SQLEnum> clazz,String value) {
		if (value == null) {
			return null;
		}
		
		value=value.trim();
		
		if (valuesCache.containsKey(clazz)){
			Map<String,SQLEnum> map2=valuesCache.get(clazz);
			
			if (map2.containsKey(value)){
				return map2.get(value);
			}
		}

		return null;
	}

	/**
	 * Hay un problema se puede llamar a este metodo antes de cargar la clase hija por lo que habria un proiblema
	 * @param clazz
	 * @param value
	 * @return
	 */
	public static SQLEnum valueUseWithCare(Class<? extends SQLEnum> clazz,String value) {
		if (value == null) {
			return null;
		}
		
		value=value.trim();
		
		if (valuesCache.containsKey(clazz)){
			Map<String,SQLEnum> map2=valuesCache.get(clazz);
			
			if (map2.containsKey(value)){
				return map2.get(value);
			}
		}

		return null;
	}
	public static SQLEnum value(Class<? extends SQLEnum> clazz,ResultSet rset,String colName) throws SQLException {
		String value=rset.getString(colName);

		return value(clazz,value);
	}

	public static List<SQLEnum> values(Class<? extends SQLEnum> clazz) {
		if (valuesList.containsKey(clazz)){
			return valuesList.get(clazz);
		} else {
			return null;
		}
	}

	public static boolean equals(SQLEnum a,SQLEnum b){
		if (a==null){
			if (b==null){
				return true;
			} else {
				return false;
			}
		} if (b == null){
			return false;
		} else {
			if (!StringUtils.equals(a.id, b.id)){
				return false;
			} else {
				if (a.clazz == null){
					if (b.clazz==null){
						return true;
					} else {
						return false;
					}
				} else if (b.clazz == null){
					return false;
				} else {
					return a.clazz == b.clazz;
				}
			}
		}
	}


	public static long getLastModified() {
		return lastModified;
	}
	

	public boolean equals(Object obj){
		if (obj==null){
			return false;
		} if (obj instanceof SQLEnum){
			return this.equals(this,(SQLEnum)obj);
		} else {
			return false;
		}
	}
	
	public int hashCode(){
		return getId().hashCode();
	}
	
	protected Object readResolve() throws ObjectStreamException {
		SQLEnum ret=valueUseWithCare(clazz,id);
		
		if (ret!=null){
			return ret;
		} else {
			log.warn(" Possible ObjectStreamException for Clazz:'"+clazz+"' id:'"+id+"'") ;
			return this;
		}
	}

}
