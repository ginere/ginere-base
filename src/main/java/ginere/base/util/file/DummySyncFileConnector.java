package ginere.base.util.file;

import ginere.base.util.dao.DaoManagerException;

public class DummySyncFileConnector implements FileSyncConnectorInterface{

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

	@Override
	public String getContentFileURL(String id, String additionalInfoEncoded)
			throws DaoManagerException {
		// TODO Auto-generated method stub
		return null;
	}

}
