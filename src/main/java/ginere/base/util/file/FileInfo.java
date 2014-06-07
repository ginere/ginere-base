package ginere.base.util.file;

import ginere.base.util.dao.DaoManagerException;
import ginere.base.util.dao.cache.KeyCacheObject;
import ginere.base.util.i18n.Language;

import java.io.File;
import java.util.Date;

/**
 * @author Angel-Ventura Mendo
 *
 */
public interface FileInfo extends KeyCacheObject {

	/**
	 * El nombre del fichero sin ruta y con extension
	 */
	public String getName();

	public long getSize() ;

	public String getMimeType();

	public File getFile();

	/**
	 * la fecha de insercion del fichero en el gestion documental
	 */
	public Date getCreated();

	/**
	 * Path relativo a la raiz donde se encuentran los ficheros.
	 * Incluye el nombre del fichero
	 */
	public String getRelativePath();

	/**
	 * Path absoluto donde se encuentran los ficheros.
	 * La raiz se obtiene de las propiedades del gestor documental.
	 * /etc/estrada/DocumentManager.properties
	 * @return
	 */
	public String getAbsoluteFilePath();
	
	public String getStringFileSize(Language language);

	public String getUserId();
	
	public String getContentFileURL() throws DaoManagerException;
	
	public String getContentFileURL(String additionalInfo) throws DaoManagerException;
	
}
