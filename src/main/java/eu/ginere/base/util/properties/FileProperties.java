package eu.ginere.base.util.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Esta clase lee las propiedades de un fichero, ver @see
 * {@link GlobalFileProperties#setInitFilePath(String)}. Todo cambio en el
 * fichero se actualiza automaticamente, por lo que la proxima vez que se
 * consulte el valor de la propiedad este estara actualizado. No es necesario
 * realanzar la maquina virtual java
 * 
 * @see {@link GlobalFileProperties#setInitFilePath(String)}
 * 
 * @author AV Mendo
 * 
 */
public class FileProperties {
	static final Logger log = Logger.getLogger(FileProperties.class);

	private final Properties fileProperties = new Properties();

	private File fildes = null;
	private long lastModified = 0;
	private String filePath = null;

	public FileProperties(File file) throws IOException {
		this.fildes=file;
		filePath=file.getAbsolutePath();

		InputStream in = new FileInputStream(fildes);
		try {
			fileProperties.load(in);
			lastModified = fildes.lastModified();
			if (log.isInfoEnabled()){
				log.info("The properties has been actualized from file:'"
						 + fildes.getAbsolutePath() + "'");
			}			
		}finally {
			IOUtils.closeQuietly(in);
		}	
	}

	public FileProperties(File file,String charset) throws IOException {
		this.fildes=file;
		filePath=file.getAbsolutePath();

		InputStream in = new FileInputStream(fildes);		
		InputStreamReader reader=new InputStreamReader(in,charset);
		try {
		fileProperties.load(reader);
		
			lastModified = fildes.lastModified();
			if (log.isInfoEnabled()){
				log.info("The properties has been actualized from file:'"
						 + fildes.getAbsolutePath() + "'");
			}
		}finally{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(reader);
		}
	}

	public FileProperties(String filePath) {
		getFileProperties(filePath);
	}

	public void setInitFilePath(String filePath) {
		getFileProperties(filePath);
	}

	public String getStringValue(Class c, String propertyName) {
		return getValue(c, propertyName);
	}

	public String getStringValue(Class c, String propertyName,
			String defaultValue) {
		try {
			String ret = getStringValue(c, propertyName);
			if (ret == null) {
				return defaultValue;
			} else {
				return ret;
			}
		} catch (Exception e) {
			log
					.warn("getStringValue c:" + c + "' propertyName:'"
							+ propertyName + "' defaultValue:'" + defaultValue
							+ "'", e);
			return defaultValue;
		}
	}

	public String[] getPropertyList(Class c, String propertyName) {
		try {
			String value = getStringValue(c, propertyName);
			String ret[] = StringUtils.split(value, ",");
			if (ret == null) {
				return ArrayUtils.EMPTY_STRING_ARRAY;
			} else {
				return ret;
			}
		} catch (Exception e) {
			log.warn("getPropertyList c:" + c + "' propertyName:'"
					+ propertyName + "'", e);
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
	}

	public HashSet<String> getPropertyMap(Class c, String propertyName) {
		String array[]=getPropertyList(c, propertyName);
		
		HashSet<String> ret=new HashSet<String>(array.length);

		ret.addAll(Arrays.asList(array));
		
		return ret;
	}
	
	
	public int getIntValue(Class c, String propertyName, int defaultValue) {
		try {
			String ret = getStringValue(c, propertyName);

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

	
	
	public long getLongValue(Class c, String propertyName, long defaultValue) {
		try {
			String ret = getStringValue(c, propertyName);

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
			log.warn("getIntValue c:" + c + "' propertyName:'" + propertyName
					+ "' defaultValue:'" + defaultValue + "'", e);
			return defaultValue;
		}
	}
	
	
	
	public double getDoubleValue(Class c, String propertyName, double defaultValue) {
		try {
			String ret = getStringValue(c, propertyName);

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
			log.warn("getIntValue c:" + c + "' propertyName:'" + propertyName
					+ "' defaultValue:'" + defaultValue + "'", e);
			return defaultValue;
		}
	}
	public boolean getBooleanValue(Class section, String propertyName,
			boolean defaultValue) {
		try {
			String ret = getStringValue(section, propertyName);
			return toBoolean(ret, defaultValue);
		} catch (Exception e) {
			log
					.warn("getIntValue c:" + section + "' propertyName:'"
							+ propertyName + "' defaultValue:'" + defaultValue
							+ "'", e);
			return defaultValue;
		}
	}

	/**
	 * This looks for file modification. If file has been modified
	 * This will call the listeners
	 */
	public void checkForModification(){
		if (fildes != null && lastModified != fildes.lastModified()) {
			getFileProperties(filePath);
		}
	}

	/**
	 * This class will try to find the current value "name" into the System
	 * Properties or into the default file. If it can't be found it will use the
	 * mother class La propiededd tien eque ser de la fdorma siguiente
	 * es.nombre.Clase.nombrePropiedad
	 */
	private String getValue(Class section, String name) {

		if (section == null || name == null) {
			log.warn("Section or name is null, Section:'" + section
					+ "' name:'" + name + "'");
			return null;
		}

		if (fildes != null && lastModified != fildes.lastModified()) {
			getFileProperties(filePath);
		}

		// Getting the full name
		String propertyName = getPropertyName(section, name);

		// The tryng from file properties
		Object obj = fileProperties.get(propertyName);
		if (obj != null) {
			if (log.isDebugEnabled()){
				log.debug("propertyName:'" + propertyName + "' and value:'" + obj
						  + "' from file:'" + filePath + "'");
			}
			if (ObjectUtils.NULL.equals(obj)) {
				return null;
			} else {
				return (String) obj;
			}
		}

		// Else using the short name into the file properties

		String ret = (String) fileProperties.get(name);
		if (ret != null) {
			return (String) ret;
		}
		// Si es nulo al final se guardara en la cache

		// // try from the default path
		// Properties prop=FilePropertyUtils.getProperties(section);
		// if (prop!=null) {
		// // try the full cualified name
		// ret=prop.getProperty(name);
		// if (ret!=null) {
		// log.debug("propertyName:'"+propertyName+"' from file.prop");
		// fileProperties.put(propertyName,ret);
		// return (String)ret;
		// }
		// }

		// Getting property from system Propertie
		ret = (String) System.getProperty(propertyName);
		if (ret != null) {
			if (log.isDebugEnabled()){
				log.debug("Property name:'" + propertyName
						  + "' found in system properties.");
			}
			return ret;
		}

		// Sets the NULL value to this key
		fileProperties.put(propertyName, ObjectUtils.NULL);

		return null;
	}

	private String getProperty(Properties fileProperties, String propertyName) {
		Object obj = fileProperties.get(propertyName);
		if (obj != null) {
			if (log.isDebugEnabled()){
				log.debug("getting propertyName:'" + propertyName + "' from cache");
			}
			if (ObjectUtils.NULL.equals(obj)) {
				return null;
			} else {
				return (String) obj;
			}
		} else {
			fileProperties.put(propertyName, ObjectUtils.NULL);
			return null;
		}
	}

	/**
	 * true: "true".</br> false: "false".</br>
	 */
	public boolean toBoolean(String str, boolean defaultValue) {

		if (StringUtils.equalsIgnoreCase(null, str)) {
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

	private String getPropertyName(String section, String propertieName) {
		return section + "." + propertieName;
	}

	private String getPropertyName(Class c, String propertieName) {
		return getPropertyName(c.getName(), propertieName);
	}

	/**
	 * Reads the file and store the new readed properties.
	 */
	private void getFileProperties(String fileName) {
		fileProperties.clear();
		if (!StringUtils.equals(filePath, fileName)) {
			if (log.isInfoEnabled()){
				log.info("setting the file proerties path to :'" + fileName + "'");
			}
			filePath = fileName;
			if (filePath != null) {
				fildes = new File(filePath);
			} else {
				fildes = null;
			}
		} else if (fildes == null) {
			if (filePath != null) {
				fildes = new File(filePath);
			} else {
				log.warn("The file properties path can't be null.");
				return;
			}
		}

		if (!fildes.canRead()) {
			log.warn("The file properties path :'" + filePath
					+ "' can't be readed");
			return;
		}

		try {
			InputStream in = new FileInputStream(fildes);
			fileProperties.load(in);
			in.close();

			lastModified = fildes.lastModified();
			if (log.isInfoEnabled()){
				log.info("The properties has been actualized from file:'"
						 + fildes.getAbsolutePath() + "'");
			}

			// notifico a los listener que las propiedades han podido cambiar
			notifyPropertiesChangedListers(lastModified);

		} catch (IOException e) {
			log.warn("The file:'" + fildes.getName() + "' can't be read.", e);
		}
	}

	/**
	 * Listener para notificar que el fichero de propiedades ha podido cambiar.
	 * Esta comprovacion se realiza comprovando la fecha del fichero.
	 * Cuando se inicializa las propiedades por primera vez se llama a los listeners, 
	 * pasandoles como fecha de ultima modificacion lastUpdated el valor 0.
	 * @author mvega
	 *
	 */
	public static interface PropertiesChangedLister {
		/**
		 * @param lastModified fecha de la modificacion precedente.
		 * Si no ha habido, es la primera vez que se carga el fichero esta fecha es 0.
		 * Todos los listeners son llamados desde el mismo hilo si alguno se bloquea o 
		 * tarda demasiado la aplicacion quedara bloqueada. Por favor si tienes que hacer 
		 * muchas cosas crea un Hilo
		 */
		public void propertiesChanged(long lastModified);
	}
	
	private final Vector<PropertiesChangedLister> listenerList=new Vector<PropertiesChangedLister>();
	
	private void notifyPropertiesChangedListers(long lastModified){
		for (PropertiesChangedLister listener:listenerList){
			listener.propertiesChanged(lastModified);
		}
	}
	
	public void subscriveToPropertiesChanged(PropertiesChangedLister listener){
		listenerList.add(listener);
	}
	
	public void removePropertiesChangedListener(PropertiesChangedLister listener){
		listenerList.remove(listener);
	}

	public File getFildes(){
		return fildes;
	}

	public long getLastModified(){
		return lastModified;
	}

	public String getFilePath(){
		return filePath;
	}

}
