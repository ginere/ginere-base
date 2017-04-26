package eu.ginere.base.util.properties;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.file.FileUtils;
import eu.ginere.base.util.properties.impl.FilePropertiesImpl;
import eu.ginere.base.util.properties.impl.MemoryPropertiesImpl;
import eu.ginere.base.util.properties.old.FileProperties.PropertiesChangedLister;


/**
 * Esta clase lee las propiedades de un fichero, ver @see
 * {@link GlobalFileProperties#setInitFilePath(String)}. Todo cambio en el
 * fichero se actualiza la proxima vez que se consulta el valor de la propiedad.
 * No es necesario realanzar la maquina virtual java
 * 
 * @see {@link GlobalFileProperties#setInitFilePath(String)}
 * 
 * @author AV Mendo
 */
public class GlobalFileProperties {
	static Logger log = Logger.getLogger(GlobalFileProperties.class);

	private static PropertiesInterface impl=MemoryPropertiesImpl.SINGLETON;
	private static SetPropertiesInterface setImpl=null;

	private static final Map<String, PropertyDescriptor> cache = new ConcurrentHashMap<String, PropertyDescriptor>();
	private static long lastModified=0;

	/**
	 * The read only file where global properties are stored.
	 * 
	 * @param filePath
	 */
	public static boolean setInitFilePath(String filePath) {
		if (filePath!=null){
			if (init(filePath)){
				return true;
			}
			// try the file name
			String defaultPath=System.getProperty(GlobalFileProperties.class.getName()+".DefaultPath");
			String newPath=defaultPath+File.separator+filePath;
			if (init(newPath)){
				return true;
			}
		}

		String defaultPath=System.getProperty(GlobalFileProperties.class.getName()+".DefaultPath");
		if (init(defaultPath)){
			return true;
		} else {
			log.error(GlobalFileProperties.class.getName()+" No initialized width path:"+filePath+" try to set the path in System propertyes:"+GlobalFileProperties.class.getName()+".DefaultPath");
			return false;
		}
	}
	
	

	public static void setSetImpl(SetPropertiesInterface newSetImpl) throws DaoManagerException {
		if (newSetImpl!=null){
			synchronized (cache) {
				lastModified=0;
				setImpl=newSetImpl;				
			}
			loadAll();
		}
	}

	public static void setImpl(PropertiesInterface newImple) throws DaoManagerException {
		if (newImple!=null){
			synchronized (cache) {
				lastModified=0;
				impl=newImple;			
			}
			loadAll();
		}
	}

	/**
	 * Thread exclusion zone.
	 * @throws DaoManagerException 
	 */
	private synchronized static void loadAll() throws DaoManagerException {
		if (setImpl ==null) {
			long implLastModifiedTime=impl.getLastModified();
				
			if (lastModified<implLastModifiedTime){
				Map<String,PropertyDescriptor> staticValues=impl.getAll();
				// to avoid reading while cleaning
				synchronized (cache) {
					cache.clear();
					cache.putAll(staticValues);
				}
				lastModified=System.currentTimeMillis();
				notifyPropertiesChangedListers(lastModified);	
			}
		} else {
			long implLastModifiedTime=impl.getLastModified();
			long setImplLastModifiedTime=setImpl.getLastModified();
		
			if (lastModified<implLastModifiedTime || lastModified<setImplLastModifiedTime){
				Map<String,PropertyDescriptor> staticValues=impl.getAll();
				Map<String,PropertyDescriptor> dynamicValues=setImpl.getAll();
				// to avoid reading while cleaning
				synchronized (cache) {
					cache.clear();
					cache.putAll(staticValues);
					cache.putAll(dynamicValues);
				}
				lastModified=System.currentTimeMillis();
				notifyPropertiesChangedListers(lastModified);
			}
		}
	}
	
	static public void setValue(String fullPropertyName,String desscription,String value) throws DaoManagerException {	
			PropertyDescriptor desc=getDescriptor(fullPropertyName,desscription);
			
			setImpl.setValue(desc, value);			

			// Maybe the propertie is not yet loaded. We take the risk to create fake properties to allow to define
			// Properties that are not jet loaded.
//			if (cache.containsKey(fullPropertyName)){
//				PropertyDescriptor desc=cache.get(fullPropertyName);
//				// The property must exists to be created
//				((SetPropertiesInterface)impl).setValue(desc,value);
//			}
	}


	static public void setValue(Class<?> section, String propertyName,String desscription,String value) throws DaoManagerException {	
		if (setImpl == null){
			log.warn("No Set Property Implemntation defined");
			return;
		} else {
			if (section == null || propertyName == null) {
				log.warn("Section or name is null, Section:'" + section
						 + "' name:'" + propertyName + "'");
			}
			
			// Getting the full name
			String fullPropertyName = getPropertyName(section, propertyName);
			setValue(fullPropertyName,desscription,value);
		}
	}


	static private PropertyDescriptor getDescriptor(String propertyName,String description) throws DaoManagerException {
		// verifiy and load all the properties if neede
		loadAll();
		
		synchronized (cache) { 
			if (cache.containsKey(propertyName)){
				PropertyDescriptor desc=cache.get(propertyName);
				return desc;
			} else {
				// The code can create properties
				PropertyDescriptor desc=new PropertyDescriptor(propertyName, description);
				cache.put(propertyName, desc);
				
				return desc;
			}
		}
	}
	
	static public PropertyDescriptor getDescriptor(String propertyName,PropertyDescriptor defaultValue) throws DaoManagerException {
		// verifiy and load all the properties if neede
		loadAll();
		
		synchronized (cache) { 
			if (cache.containsKey(propertyName)){
				PropertyDescriptor desc=cache.get(propertyName);
				return desc;
			} else {
				return defaultValue;
			}
		}
	}
	
	static private String getValue(String propertyName,String description) throws DaoManagerException {
		PropertyDescriptor desc=getDescriptor(propertyName,description);

		return desc.value;			
	}


	private static String getPropertyName(String section, String propertieName) {
		return section + "." + propertieName;
	}

	private static String getPropertyName(Class<?> c, String propertieName) {
		return getPropertyName(c.getName(), propertieName);
	}
	
	
	static private String getValueInner(Class<?> section, String propertyName,String description,String defaultValue) {
		if (section == null || propertyName == null) {
			log.warn("Section or name is null, Section:'" + section
					 + "' name:'" + propertyName + "'");
			return defaultValue;
		}
		try {
			// Getting the full name
			String fullPropertyName = getPropertyName(section, propertyName);
			
			String ret=getValue(fullPropertyName,description);
			if (ret!=null){
				return ret;
			} else {
				if (log.isDebugEnabled()){
					log.debug("Property long name not found:"+fullPropertyName+" ussing the sort name "+propertyName);
				}
				// try the sort name
				ret=getValue(propertyName,description);
				if (ret!=null){
					return ret;
				} else {
					if (log.isDebugEnabled()){
						log.debug("Property not found:"+fullPropertyName+" returning the default value.");
					}
					return defaultValue;
				}
			}
		}catch(DaoManagerException e){
			log.error("Section:'" + section			 + 
					  "' name:'" + propertyName + 
					  "' description:'" + description + 
					  "' defaultValue:'" + defaultValue + 
					  "'",e);
			return defaultValue;
		}
	};

	static public String getStringValue(Class<?> c, String propertyName,String description,String defaultValue) {	
		return getValueInner(c,propertyName,description,defaultValue);
	}

	static public String getStringValueNoDesc(Class<?> c, String propertyName,String defaultValue) {
		return getStringValue(c,propertyName,propertyName,defaultValue);
	}
	
	static public String getStringValueDesc(Class<?> c, String propertyName,String description) {
		return getStringValue(c,propertyName,description,null);
	}
	
	static public String getStringValue(Class<?> c, String propertyName) {
		return getStringValue(c,propertyName,propertyName,null);
	}

	static public String[] getPropertyList(Class<?> c, String propertyName) {
		return getPropertyList(c, propertyName, propertyName);
	}
	static public String[] getPropertyList(Class<?> c, String propertyName,String description) {
		try {
			String value = getStringValue(c, propertyName,description,null);
			// split(null) return null
			String ret[] = StringUtils.split(value, ",");
			
			if (ret == null) {
				return ArrayUtils.EMPTY_STRING_ARRAY;
			} else {				
				return ret;
			}
		} catch (Exception e) {
			log.warn("getPropertyList c:" + c 
					 + "' propertyName:'"+ propertyName + "'", e);
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
	}

	static public int getIntValue(Class<?> c, String propertyName,int defaultValue) {
		return getIntValue(c, propertyName, propertyName, defaultValue);
	}
	
	static public int getIntValue(Class<?> c, String propertyName,String description, int defaultValue) {
		try {
			String ret = getStringValue(c, propertyName,description,null);
			
			if (ret == null) {
				return defaultValue;
			} else {
				try {
					return Integer.parseInt(ret);
				} catch (NumberFormatException e) {
					return defaultValue;
				}
			}
		} catch (Exception e) {
			log.warn("getIntValue c:" + c + "' propertyName:'" + propertyName
					+ "' defaultValue:'" + defaultValue + "'", e);
			return defaultValue;
		}
	}

	
	static public double getDoubleValue(Class<?> c, String propertyName,String description,double defaultValue) {
		try {
			String ret = getStringValue(c, propertyName,description,null);
			
			if (ret == null) {
				return defaultValue;
			} else {
				try {
					return Double.parseDouble(ret);
				} catch (NumberFormatException e) {
					return defaultValue;
				}
			}
		} catch (Exception e) {
			log.warn("getIntValue c:" + c + 
					 "' propertyName:'" + propertyName
					 + "' defaultValue:'" + defaultValue + "'", e);
			return defaultValue;
		}
	}
	
	static public long getLongValue(Class<?> c, String propertyName,long defaultValue) {
		return getLongValue(c, propertyName, propertyName, defaultValue);
	}
	
	static public long getLongValue(Class<?> c, String propertyName,String description, long defaultValue) {
		try {
			String ret = getStringValue(c, propertyName,description,null);

			if (ret == null) {
				return defaultValue;
			} else {
				try {
					return Long.parseLong(ret);
				} catch (NumberFormatException e) {
					return defaultValue;
				}
			}
		} catch (Exception e) {
			log.warn("getIntValue c:" + c + 
					 "' propertyName:'" + propertyName +
					 "' defaultValue:'" + defaultValue + "'", e);
			return defaultValue;
		}
	}
	
	static public boolean getBooleanValue(Class<?> section, 
										  String propertyName,
										  String description,
										  boolean defaultValue) {
		try {
			String ret = getStringValue(section, propertyName,description,null);
			return toBoolean(ret, defaultValue);
		} catch (Exception e) {
			log.warn("getIntValue c:" + section  +
					 "' propertyName:'" + propertyName + 
					 "' defaultValue:'" + defaultValue + "'", e);
			return defaultValue;
		}
	}

	static public HashSet<String> getPropertyMap(Class<?> c, String propertyName) {
		return getPropertyMap(c, propertyName, propertyName);
	}
	
	static public HashSet<String> getPropertyMap(Class<?> c, String propertyName,String description) {
		String array[]=getPropertyList(c, propertyName,description);
		
		HashSet<String> ret=new HashSet<String>(array.length);

		ret.addAll(Arrays.asList(array));
		
		return ret;
	}

	/**
	 * true: "true".</br> false: "false".</br>
	 */
	public static boolean toBoolean(String str, boolean defaultValue) {

		if (StringUtils.isEmpty(str)) {
			return defaultValue;
		} else if (StringUtils.equalsIgnoreCase("true", str)) {
			return true;
		} else if (StringUtils.equalsIgnoreCase("false", str)) {
			return false;
		} else if (StringUtils.equalsIgnoreCase("1", str)) {
			return true;
		} else if (StringUtils.equalsIgnoreCase("0", str)) {
			return false;
		} else {
			return defaultValue;
		}

	}

	public static long getLastModified(){
		if (setImpl==null){
			return impl.getLastModified();
		} else {
			return Math.max(setImpl.getLastModified(),impl.getLastModified());
		}
	}

	private static boolean init(String filePath){
		if (FileUtils.canReadFile(filePath)){
			File file=new File(filePath);
		
			try {
				impl=new FilePropertiesImpl(file);
				return true;
			}catch (Throwable e){
				log.warn("Ehile trying to init width path:"+filePath, e);
			}
		}		
		
		return false;
	}
	
	/**
	 * Properties changed listenrers
	 */
	private static final Vector<PropertiesChangedLister> listenerList=new Vector<PropertiesChangedLister>();
	
	private static void notifyPropertiesChangedListers(long lastModified){
		for (PropertiesChangedLister listener:listenerList){
			listener.propertiesChanged(lastModified);
		}
	}
	
	public static void subscriveToPropertiesChanged(PropertiesChangedLister listener){
		listenerList.add(listener);
	}
	
	public static void removePropertiesChangedListener(PropertiesChangedLister listener){
		listenerList.remove(listener);
	}


	public static Collection<PropertyDescriptor> getDescriptors() throws DaoManagerException {
		Hashtable<String,PropertyDescriptor> ret=new Hashtable<String, PropertyDescriptor>();
		
		ret.putAll(cache);
		if (impl!=null){
			ret.putAll(impl.getAll());
		}

		if (setImpl!=null){
			ret.putAll(setImpl.getAll());
		}
		
		return ret.values();
	}
	
	/**
	 * Crea el path para el fichero a partir del path por defecto
	 * @param defaultPath
	 * @param filePath
	 * @return
	 */
	private static String getFilePath(String defaultPath, String filePath) {
		return defaultPath+File.separator+filePath;
	}
	
	/**
	 * Utiliza el valor de la JVM GlobalFileProperties.class.getName()+".DefaultPath"
	 * para obtener el path de la propiedad
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getPropertiesFilePath(String filePath) {
		// Verificamos is esta definida una propiedad Global para los ficheros de properties
		String defaultPath=System.getProperty(GlobalFileProperties.class.getName()+".DefaultPath");

		if (defaultPath!=null){
			return getFilePath(defaultPath,filePath);
		} else {
			return filePath;
		}
	}
}
