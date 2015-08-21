package eu.ginere.base.util.file;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.file.FileConnectorInterface.FileStreamWriter;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * @author ventura
 *
 */
public class FileConnector {
	static Logger log = Logger.getLogger(FileConnector.class);


	/**
	 * Es para las inserciones locales
	 */
	private static FileConnectorInterface connector=new DummyFileConnector();
	private static FileSyncConnectorInterface syncConnector=connector.getSyncConnector();

	private static String serverName=null;

	
	public static void init(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (log.isInfoEnabled()){
			log.info("FileConnector tryng to initialize width:'"+className+"'");
		}
		
		if (className==null){
			log.warn("not classNammed passed. ussing default connector");
			return ;
		}
		
		Class<?> clazz=Class.forName(className);
		connector=(FileConnectorInterface)clazz.newInstance();

		if (log.isInfoEnabled()){
			log.info("FileConector initialized with class:'"+className+"'");
		}
		
		serverName=connector.getServerName();
		syncConnector=connector.getSyncConnector();
		
	}


	public static FileInfo get(String id) throws DaoManagerException{
		if (isCurrentServer(id)){
			return connector.get(id);
		} else {
			return syncConnector.get(id);
		}
	}

	public static boolean exists(String id) throws DaoManagerException{
		if (isCurrentServer(id)){
			return connector.exists(id);
		} else {
			return syncConnector.exists(id);
		}
	}

	public static String insert(File contentFile,String userId) throws DaoManagerException{
		return connector.insert(contentFile,userId);
	}

	public static String insert(File contentFile, String mimeType,String userId) throws DaoManagerException{
		return connector.insert(contentFile,mimeType,userId);
	}

	public static String insert(String fileName, 
								byte[] contentFile,
								String mimeType,String userId) throws DaoManagerException{
		return connector.insert(fileName,contentFile,mimeType,userId);
	}

	
	public static String getContentFileURL(String id) throws DaoManagerException{
		if (isCurrentServer(id)){
			return connector.getContentFileURL(id,null);
		} else {
			return syncConnector.getContentFileURL(id,null);
		}
	}
	
	public static String getContentFileURL(String id, String additionalInfoEncoded) throws DaoManagerException{
		if (isCurrentServer(id)){
			return connector.getContentFileURL(id,additionalInfoEncoded);
		} else {
			return syncConnector.getContentFileURL(id,additionalInfoEncoded);
		}
	}


	public static String getMimeType(String id) throws DaoManagerException {
		FileInfo info=get(id);
		
		return info.getMimeType();
	}


//	public static InputStream getInputStream(String id) throws DaoManagerException, FileNotFoundException {
//		FileInfo info=connector.get(id);
//		File file=info.getFile();
//		
//		return new FileInputStream(file);
//	}

	public static void update(String id, FileStreamWriter fileStreamWriter) throws DaoManagerException {
		if (isCurrentServer(id)){
			connector.update(id, fileStreamWriter);
		} else {
			log.warn("Updating file id:'"+id+"' not belonging to current server:'"+serverName+"'");
		}
	}
	
	public static void remove(String id) throws DaoManagerException {
		if (isCurrentServer(id)){
			connector.remove(id);
		} else {
			// Esto esta implementado pero no permitimos su uso pues tiene que ser el servidor duenyo
			// del fichero el que lo borre
			log.warn("removing file id:'"+id+"' not belonging to current server:'"+serverName+"'");
		}
	}

	public static String insert(String fileName, 
								String mimeType,
								String userId, 
								FileStreamWriter fileStreamWriter) throws DaoManagerException {
		return connector.insert(fileName, mimeType, userId, fileStreamWriter);
	}
	
	private static boolean isCurrentServer(String id) {
		if (id==null) {
			return true;
		} else {
			String fileServerName=FileId.getServerName(id);
			
			if (fileServerName == null || fileServerName.equals(serverName)){
				return true;
			} else {
				return false;
			}
		}
	}

}
