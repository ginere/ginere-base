package eu.ginere.base.util.manager;

import eu.ginere.base.util.file.FileUtils;
import eu.ginere.base.util.properties.FileProperties;
import eu.ginere.base.util.properties.GlobalFileProperties;
import eu.ginere.base.util.test.TestInterface;

import java.io.File;

import org.apache.log4j.Logger;

// TODO implements implements TestInterface
public abstract class AbstractManager {
	static protected final Logger log = Logger.getLogger(AbstractManager.class);
	
	private static String propertiesPath=null;

//	private static final String PROPERTIES_PATH = "/etc/tda/";

	public static final void setPropertiespath(String path){
		propertiesPath=path;
	}
	
	protected static final FileProperties getFileProperties(String fileName) {
		String abstolutePath=getAbsoluteFilePropertiesPath(fileName);

		if (!FileUtils.canReadFile(abstolutePath)){
			throw new InstantiationError("No existe o no hay permisos de escritura o no es un fichero sobre el path:'"+abstolutePath+"'");
		} else {
			FileProperties fileProperties = new FileProperties(abstolutePath);
			
			return fileProperties;
		}
	}
	
	protected static final String getAbsoluteFilePropertiesPath(String fileName){
		
		if (propertiesPath!=null){
//		StringBuilder buffer=new StringBuilder(PROPERTIES_PATH);
//		buffer.append(fileName);
			return GlobalFileProperties.getPropertiesFilePath(propertiesPath+File.separatorChar+fileName);
		} else {
			return GlobalFileProperties.getPropertiesFilePath(fileName);
		}
	}

}



