package eu.ginere.base.util.file;

import eu.ginere.base.util.dao.DaoManagerException;

import java.io.File;

public class DummyFileConnector implements FileConnectorInterface{

	public DummyFileConnector(){
		
	}
	
	@Override
	public FileInfo get(String id) throws DaoManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(String id) throws DaoManagerException {
		// TODO Auto-generated method stub
		return false;
	}

//	@Override
//	public String getContentFileURL(String id)
//			throws DaoManagerException {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
		public String insert(File contentFile,String userId) throws DaoManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String insert(File contentFile, String mimeType,String userId)
			throws DaoManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String insert(String fileName, byte[] contentFile,
			String mimeType,String userId) throws DaoManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentFileURL(String id, String additionalInfoEncoded)
			throws DaoManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(String id, FileStreamWriter fileStreamWriter)
			throws DaoManagerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String insert(String fileName, String mimeType, String userId,
			FileStreamWriter fileStreamWriter) throws DaoManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(String id) throws DaoManagerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileSyncConnectorInterface getSyncConnector() {
		return new DummySyncFileConnector();
	}
	


}
