package eu.ginere.base.util.properties;

import eu.ginere.base.util.properties.FileProperties.PropertiesChangedLister;

import java.io.File;
import java.util.HashSet;
import java.util.MissingResourceException;

import org.apache.log4j.Logger;


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
	static private FileProperties staticFileProperties = null;

	
	private static final String ERROR_MISSING_RESOURCE="The default file is not specified, use the setInitFilePath.";

	/**
	 * Returns the current FileProperties used by the GlobalFileProperties. If no defined returns null.
	 * @return
	 */
	public static FileProperties getFileProperties() {
		return staticFileProperties;
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

	/**
	 * Define el fichero del cual se van a leer las propiedades.
	 * 
	 * @param filePath
	 */
	public static void setInitFilePath(String filePath) {
		filePath=getPropertiesFilePath(filePath);
		
		if (staticFileProperties == null) {
			staticFileProperties=new FileProperties(filePath);
		} else {
			staticFileProperties.setInitFilePath(filePath);
		}
	}

	/**
	 * Crea el path para el fichero a partir del path por defecto
	 * @param defaultPath
	 * @param filePath
	 * @return
	 */
	private static String getFilePath(String defaultPath, String filePath) {
//		int lastIndex=filePath.lastIndexOf(File.separator);
//		String fileName;
//		
//		if (lastIndex>=0){
//			fileName=filePath.substring(lastIndex+1,filePath.length());
//			
//		} else {
//			fileName=filePath;
//		}
		return defaultPath+File.separator+filePath;
	}

	static public String getStringValue(Class<?> c, String propertyName) {
		if (staticFileProperties==null){
			throw new MissingResourceException(ERROR_MISSING_RESOURCE,c.toString(),propertyName);
		} else {
			return staticFileProperties.getStringValue(c,propertyName);
		}
	}

	static public String getStringValue(Class<?> c, String propertyName,
										String defaultValue) {
		if (staticFileProperties==null){
			log.warn(ERROR_MISSING_RESOURCE);
			return defaultValue;
		} else {
			return staticFileProperties.getStringValue(c,propertyName,defaultValue);
		}
	}

	static public String[] getPropertyList(Class<?> c, String propertyName) {
		if (staticFileProperties==null){
			throw new MissingResourceException(ERROR_MISSING_RESOURCE,c.toString(),propertyName);
		} else {
			return staticFileProperties.getPropertyList(c,propertyName);
		}
	}

	static public int getIntValue(Class<?> c, 
								  String propertyName, 
								  int defaultValue) {
		if (staticFileProperties==null){
			log.warn(ERROR_MISSING_RESOURCE);
			return defaultValue;
		} else {
			return staticFileProperties.getIntValue(c,propertyName,defaultValue);
		}
	}

	
	static public double getDoubleValue(Class<?> c, String propertyName,
			double defaultValue) {
		if (staticFileProperties == null) {
			log.warn(ERROR_MISSING_RESOURCE);
			return defaultValue;
		} else {
			return staticFileProperties.getDoubleValue(c, propertyName,
					defaultValue);
		}
	}
	
	static public long getLongValue(Class<?> c, String propertyName, long defaultValue) {
		if (staticFileProperties == null) {
			log.warn(ERROR_MISSING_RESOURCE);
			return defaultValue;
		} else {
			return staticFileProperties.getLongValue(c, propertyName,
					defaultValue);
		}
	}
	
	static public boolean getBooleanValue(Class<?> c, 
										  String propertyName,
										  boolean defaultValue) {
		if (staticFileProperties==null){
			log.warn(ERROR_MISSING_RESOURCE);
			return defaultValue;
		} else {
			return staticFileProperties.getBooleanValue(c,propertyName,defaultValue);
		}
	}

	static public HashSet<String> getPropertyMap(Class<?> c, String propertyName) {
		if (staticFileProperties==null){
			log.warn(ERROR_MISSING_RESOURCE);
			throw new MissingResourceException(ERROR_MISSING_RESOURCE,c.toString(),propertyName);
		} else {
			return staticFileProperties.getPropertyMap(c,propertyName);
		}
	}
	
	public static void subscriveToPropertiesChanged(PropertiesChangedLister listener){
		if (staticFileProperties==null){
			throw new MissingResourceException(ERROR_MISSING_RESOURCE,GlobalFileProperties.class.getName(),"subscriveToPropertiesChanged");
		} else {
			staticFileProperties.subscriveToPropertiesChanged(listener);
		}
	}
	
	public static void removePropertiesChangedListener(PropertiesChangedLister listener){
		if (staticFileProperties==null){
			throw new MissingResourceException(ERROR_MISSING_RESOURCE,GlobalFileProperties.class.getName(),"removePropertiesChangedListener");
		} else {
			staticFileProperties.removePropertiesChangedListener(listener);
		}
	}

	public static File getFildes(){
		if (staticFileProperties==null){
			throw new MissingResourceException(ERROR_MISSING_RESOURCE,GlobalFileProperties.class.getName(),"removePropertiesChangedListener");
		} else {
			return staticFileProperties.getFildes();
		}
	}

	public static long getLastModified(){
		if (staticFileProperties==null){
			throw new MissingResourceException(ERROR_MISSING_RESOURCE,GlobalFileProperties.class.getName(),"removePropertiesChangedListener");
		} else {
			return staticFileProperties.getLastModified();
		}
	}

	public String getFilePath(){
		if (staticFileProperties==null){
			throw new MissingResourceException(ERROR_MISSING_RESOURCE,GlobalFileProperties.class.getName(),"removePropertiesChangedListener");
		} else {
			return staticFileProperties.getFilePath();
		}
	}

}
