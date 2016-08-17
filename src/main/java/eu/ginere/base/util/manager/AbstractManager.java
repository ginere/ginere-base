package eu.ginere.base.util.manager;

import java.util.ServiceConfigurationError;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.log.Logger;
import eu.ginere.base.util.log.LoggerInterface;
import eu.ginere.base.util.properties.GlobalFileProperties;
import eu.ginere.base.util.test.TestInterface;

// TODO implements implements TestInterface
public abstract class AbstractManager implements TestInterface{
	
	static protected final Logger log = Logger.getLogger(AbstractManager.class);


	protected AbstractManager(){
		Manager.MANAGER.subscrive(this);
	}

	//
	// thread local loger stuff
	//
	public static final void setThreadLocalLogger(LoggerInterface logger){
		log.setThreadLocal(logger);
	}

	public static final void removeThreadLocalLogger(){
		log.removeThreadLocal();
	}
	
	// 
	// The order is important this is 
	/**
	 * This should be called to initialize the manager. By Example, the daos shoul be initialized
	 * testing and creating databasee tables etc...
	 * The initialization order is important, this is the reason why, auromic initialization wont be done.	 
	 */
	abstract public void init()throws DaoManagerException;

//	/**
//	 * If this thread use a cache this clears the cache if not 
//	 * do noting. The clear cahce stuff will be called automatly
//	 */
//	abstract void clearCache();

	

	//
	// Properties stuff to be deprecated
	//
//	private static final String PROPERTIES_PATH = "/etc/tda/";

	private static String propertiesPath=null;

	public static final void setPropertiespath(String path){
		propertiesPath=path;
	}
	
	
	
//	protected static final FileProperties getFileProperties(String fileName) {
//		String abstolutePath=getAbsoluteFilePropertiesPath(fileName);
//
//		if (!FileUtils.canReadFile(abstolutePath)){
//			throw new InstantiationError("No existe o no hay permisos de escritura o no es un fichero sobre el path:'"+abstolutePath+"'");
//		} else {
//			FileProperties fileProperties = new FileProperties(abstolutePath);
//			
//			return fileProperties;
//		}
//	}
//	
//	protected static final String getAbsoluteFilePropertiesPath(String fileName){
//		
//		if (propertiesPath!=null){
////		StringBuilder buffer=new StringBuilder(PROPERTIES_PATH);
////		buffer.append(fileName);
//			return GlobalFileProperties.getPropertiesFilePath(propertiesPath+File.separatorChar+fileName);
//		} else {
//			return GlobalFileProperties.getPropertiesFilePath(fileName);
//		}
//	}

	protected String getMandatoryPropertyValue(String propertyName,String description)throws ServiceConfigurationError {
		return getMandatoryPropertyValue(getClass(), propertyName, description);
	}

	protected void setPropertyValue(String propertyName,String value,String description)throws ServiceConfigurationError {
		setMandatoryPropertyValue(getClass(), propertyName, value,description);
	}
	
	protected static String getMandatoryPropertyValue(Class<?> clazz,String propertyName,String description)throws ServiceConfigurationError {

		String globalPropertyName=clazz.getName()+'.'+propertyName;
		
		String value=System.getProperty(globalPropertyName);
		
		if (StringUtils.isBlank(value)) {
			value=GlobalFileProperties.getStringValue(clazz,globalPropertyName,description,null);
		}

		if (StringUtils.isBlank(value)){
			throw new ServiceConfigurationError("The property:"+globalPropertyName+" is not defined in global properties or in the system property. Description:"+description);
		} else {
			return value;
		}				
	}

	protected static void setMandatoryPropertyValue(Class<?> clazz,String propertyName,String value,String description)throws ServiceConfigurationError {

		String globalPropertyName=clazz.getName()+'.'+propertyName;
		
		System.setProperty(globalPropertyName,value);
		
	}

}



