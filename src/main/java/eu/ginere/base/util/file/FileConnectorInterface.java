package eu.ginere.base.util.file;

import eu.ginere.base.util.dao.DaoManagerException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public interface FileConnectorInterface {
	
	public static interface FileStreamWriter {
		public void write(OutputStream out)throws IOException,DaoManagerException;
	}
	
	public FileInfo get(String id) throws DaoManagerException;
	public boolean exists(String id) throws DaoManagerException;

	public String insert(File contentFile,String userId) throws DaoManagerException;
	public String insert(File contentFile, String mimeType,String userId) throws DaoManagerException;
	public String insert(String fileName, 
						 byte[] contentFile,
						 String mimeType,
						 String userId) throws DaoManagerException;
	
//	public String getContentFileURL(String id) throws DaoManagerException;
	public String getContentFileURL(String id, String additionalInfoEncoded)throws DaoManagerException;
	public void update(String id, FileStreamWriter fileStreamWriter)throws DaoManagerException;
	public String insert(String fileName, String mimeType, String userId,
			FileStreamWriter fileStreamWriter)throws DaoManagerException;
	
	/**
	 * Removes this file
	 * 
	 * @param id
	 * @throws DaoManagerException
	 */
	public void remove(String id)throws DaoManagerException;
	public String getServerName();
	
	public FileSyncConnectorInterface getSyncConnector();
}
