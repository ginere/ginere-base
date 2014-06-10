package eu.ginere.base.util.file;

import eu.ginere.base.util.dao.DaoManagerException;

public interface FileSyncConnectorInterface {

	FileInfo get(String id)throws DaoManagerException;

	boolean exists(String id)throws DaoManagerException;

	String getContentFileURL(String id, String additionalInfoEncoded)throws DaoManagerException;

}
