package ginere.base.util.file;

import ginere.base.util.dao.DaoManagerException;
import ginere.base.util.descriptor.annotation.Description;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


@SuppressWarnings("serial")
public class FileId implements Serializable{
	public static Logger log = Logger.getLogger(FileId.class);

	@Description
	private final String id;
	
	@Description(description="The file additional Info")
	private final String additionalInfo;
//	private FileInfo fileInfo=null;

	private static final String SERVER_NAME_SEPARATOR = "-";
	/**
	 * if FileId is not a valid file this returns null
	 */
	static public FileId getInstance(String id) throws DaoManagerException {
		if (id!=null && FileConnector.exists(id)){
			return new FileId(id,null);
		} else {
			return null;
		}
	}

	static public FileId getInstance(String id,String additionalInfo) throws DaoManagerException {
		if (id!=null && FileConnector.exists(id)){
			return new FileId(id,additionalInfo);
		} else {
			return null;
		}
	}
	
	private FileId(String id,String additionalInfo){
		this.id=id;
		String encoded=null;
		
		if (additionalInfo!=null){
			try {
				encoded=URLEncoder.encode(additionalInfo,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.warn("Encoding:'"+additionalInfo+"'",e);
			}
		}
		this.additionalInfo=encoded;
	}


	/**
	 * @return the additionalInfo
	 */
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public String getServerName(){
		if (id!=null && id.contains(SERVER_NAME_SEPARATOR)){
			return id.substring(0,id.indexOf(SERVER_NAME_SEPARATOR));
		} else {
			return null;
		}
	}

	public static String getServerName(String id){
		if (id!=null && id.contains(SERVER_NAME_SEPARATOR)){
			return id.substring(0,id.indexOf(SERVER_NAME_SEPARATOR));
		} else {
			return null;
		}
	}

	
	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

//	public FileInfo getFileInfo()throws DaoManagerException{
//		return FileConnector.get(id);
//	}

	public String getContentFileURL() throws DaoManagerException{
		return FileConnector.getContentFileURL(id,additionalInfo);
	}
	
//	public String getInstanceId(){
//		try {
//			return FileManager.MANAGER.getInstanceId(id);
//		}catch(Exception e){
//			log.warn(e);
//			return null;
//		}		
//	}

	public boolean equals(Object file){
		if (file!=null && file instanceof FileId){
			return equals(this,(FileId)file);
		} else {
			return false;
		}
	}

	public static boolean equals(FileId file1,FileId file2){
		if (file1!=null) {
			if (file2!=null) {
				return StringUtils.equals(file1.getId(), file2.getId());
			} else {
				return false;
			}
		} else if (file2!=null) {
			return false ;
		} else {
			return true ;
		}
	}
	public boolean isValide() throws DaoManagerException{
		return (FileConnector.exists(id));
	}
	public String getMimeType() throws DaoManagerException {
		return FileConnector.getMimeType(id);
	}

//	public InputStream getInputStream() throws FileNotFoundException, DaoManagerException {
//		return FileConnector.getInputStream(id);
//	}
}
