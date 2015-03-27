package eu.ginere.base.util.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class FileUtils {
	static Logger log = Logger.getLogger(FileUtils.class);

	private static final File EMPTY_FILE_LIST[]=new File[0];
	
	private static final DirFileFilterInner DIR_FILTER=new DirFileFilterInner();
	
	private static final FileFilterInner FILE_FILTER=new FileFilterInner();
	
	/**
	 * Use this iterate to iterate on files.
	 */
	public static interface FileIterator{
		/**
		 * @param file current file to iterate on.
		 * 
		 * @return If false the iteration is stopped and the called iteration function will returns also false
		 */
		public boolean iterate(File file);
	}
	
	private static class DirFileFilterInner implements FileFilter {
		 public boolean accept(File pathname) {
			 if (pathname==null){
				 return false;
			 } else if (pathname.isDirectory()){
				 return true;
			 } else {
				 return false;
			 }
		 }
		
	}
	
	private static class FileFilterInner implements FileFilter {
		 public boolean accept(File pathname) {
			 if (pathname==null){
				 return false;
			 } else if (pathname.isFile()){
				 return true;
			 } else {
				 return false;
			 }
		 }
		
	}
	
	/**
	 * Clase que implementa un filtro por extenxion del nombre del fichero
	 * @author ventura
	 *
	 */
	public static class ExtensionFilenameFilter implements FilenameFilter{
		private final String extension;
		private final boolean ignoreCase;

		public ExtensionFilenameFilter(String extension, boolean ignoreCase) {
			this.ignoreCase = ignoreCase;
			if (ignoreCase){
				this.extension = extension.toLowerCase();
			} else {
				this.extension = extension;
			}
		}

		public boolean accept(File dir, String name) {
			if (dir == null || name==null) {
				return false;
			} 
			
			if (ignoreCase) {
				name=name.toLowerCase();
			}
			
			if (name.endsWith(extension)){
				return true;
			} else {
				return false;
			}
		}
	}
	
	public static class FileNameComararator implements Comparator<File>{
		public static final FileNameComararator COMPARATOR=new FileNameComararator();
		
		private FileNameComararator(){			
		}

		@Override
		public int compare(File f1, File f2) {
			if (f1==null){
				if (f2 == null){
					return 0;
				} else {
					return -1;
				}
			} else if (f2 == null){
				return 1;
			} else {
				return f1.getName().compareTo(f2.getName());
			}
		}
	}
	
	public static void sortByName(File array[]){
		if (array == null || array.length==0){
			return ;
		} else {
			Arrays.sort(array, FileNameComararator.COMPARATOR);
			
			return;
		}
	}
	
	/**
	 * Crea los los directorios relativos al la raiz pasada en parametro. Ej
	 * root="/export/share-img" relativePathToCreate="/2009/03/01" esto creara
	 * el directorio "/export/share-img/2009/03/01"
	 * 
	 * @param root
	 * @param relativePathToCreate
	 * @return true si se pudo crear todo el path false si no se pudo crear
	 *         algunos de los directorios necesarios
	 */
	public static boolean createPath(File root, String relativePathToCreate) {
		File target = new File(root, relativePathToCreate);

		if (!target.exists()) {
			return target.mkdirs();
		} else {
			return true;
		}
	}

	/**
	 * Crea el directorio y devuelve el objeto File asociado, devuelve null si no se ha podido crear
	 * @param root
	 * @param relativePathToCreate
	 * @return
	 */
	public static File createPathAndGetFile(File root, String relativePathToCreate) {
		File target = new File(root, relativePathToCreate);

		if (!target.exists()) {
			if (target.mkdirs()){
				return target;
			} else {
				log.warn("No se ha podido crear el directorio:'"+target.getAbsolutePath()+"'");
				return null;
			}
		} else {
			return target;
		}
	}
	
	/**
	 * Si root="/export/share-img y file="/export/share-img/2009/03/01/file.txt"
	 * esto devuelve la cadena de caracteres "/2009/03/01/file.txt".
	 * 
	 * If both files are the same that return "/"
	 * 
	 * Si root es nulo o si root no es un directorio padre de file esta funcion devuelve file.getAbsolutePath()
	 * @param root
	 * @param relativePathToCreate
	 * @return true si se pudo crear todo el path false si no se pudo crear
	 *         algunos de los directorios necesarios
	 *         @deprecated use getRelativePath(File file, File root,String defaultValue);
	 */
	public static String getRelativePath(File file, File root) {
		if (root==null){
			return file.getAbsolutePath();
		}
		
		if (file==null){
			return null;
		}
		
		String rootAbsolutePath=root.getAbsolutePath();
		String fileAbsolutePath=file.getAbsolutePath();
		
		if (fileAbsolutePath.startsWith(rootAbsolutePath)){
			String ret=fileAbsolutePath.substring(rootAbsolutePath.length());
			if ("".equals(ret)){
				return "/";
			} else {
				return ret;
			}
		} else {
			return fileAbsolutePath;
		}
	}

	/**
	 * Si root="/export/share-img y file="/export/share-img/2009/03/01/file.txt"
	 * esto devuelve la cadena de caracteres "/2009/03/01/file.txt".
	 * 
	 * If both files are the same that return "/"
	 * 
	 * Si root es nulo o si root no es un directorio padre de file esta funcion devuelve file.getAbsolutePath()
	 * @param root
	 * @param relativePathToCreate
	 * @return true si se pudo crear todo el path false si no se pudo crear
	 *         algunos de los directorios necesarios
	 */
	public static String getRelativePath(File file, File root,String defaultValue) {
		if (root==null){
			return file.getAbsolutePath();
		}
		
		if (file==null){
			return defaultValue;
		}
		
		String rootAbsolutePath=root.getAbsolutePath();
		String fileAbsolutePath=file.getAbsolutePath();
		
		if (fileAbsolutePath.startsWith(rootAbsolutePath)){
			String ret=fileAbsolutePath.substring(rootAbsolutePath.length());
			if ("".equals(ret)){
				return "/";
			} else {
				return ret;
			}
		} else {
			return defaultValue;
		}
	}


	/**
	 * Verifica que el directorio existe, que es un directorio y que tenemos
	 * permisos de lectura y escritura. En caso de error devuelve false y escribe un log
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean verifyDir(File dir,Logger logger){
		if (dir==null){
			logger.error("El directorio es nulo.");
			return false;
		}
		String fileName=dir.getAbsolutePath();
		if (fileName==null){
			return false;
		}
		
		if (!dir.exists()){
			logger.error("El path '"+fileName+"' no existe.");
			return false;
		}

		if (!dir.isDirectory()){
			logger.error("El path '"+fileName+"' no es un directorio.");
			return false;
		}
		
		if (!dir.canRead()){
			logger.error("No tenemos permisos de lectura en el path '"+fileName+"'.");
			return false;
		}
		
		if (!dir.canWrite()){
			logger.error("No tenemos permisos de escritura en el path '"+fileName+"'.");
			return false;
		}

		return true;				
	}

	/**
	 * Verifica que el directorio existe, que es un directorio y que tenemos
	 * permisos de lectura y escritura. En caso de error devuelve false y escribe un log
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean verifyDir(File dir){
		return verifyDir(dir,log);
	}	

	public static String verifyReadDir(File dir){
		if (dir == null){
			return "The path is null.";
		}

		String absolutePath=dir.getAbsolutePath();
		if (!dir.exists()){
			return "The path '"+absolutePath+"' do not exits.";
		}

		if (!dir.isDirectory()){
			return "The path '"+absolutePath+"' is not a directory.";
		}
		
		if (!dir.canRead()){
			return "We have not permision to read the path:'"+absolutePath+"'.";
		}

		return null;				
	}
	
	public static boolean verifyReadDir(File dir,Logger logger){
		String error=verifyReadDir(dir);

		if (error == null ) {
			return true;
		} else {
			logger.error("verifyReadDir, file:"+dir.getAbsolutePath()+", error:"+error);
			return false;
		}
	}	
		
	public static String verifyReadDir(String path){
		if (path==null){
			return "The path is null";
		} 

		File dir=new File(path);

		return verifyReadDir(dir);		
	}

	public static boolean verifyReadDir(String path,Logger log){
		String error=verifyReadDir(path);
		
		if (error == null ) {
			return true;
		} else {
			log.error("verifyReadDir, file:'"+path+"', error:"+error);
			return false;
		}
	}	
		

	/**
	 * Verificamos que file es un fichero y que tenemos permisos de lectura
	 * @param dir
	 * @return
	 */
	public static String verifyReadFile(File file){
		if (file == null){
			return "The file is null.";
		}

		String absolutePath=file.getAbsolutePath();
		if (!file.exists()){
			return "The path '"+absolutePath+"' do not exits.";
		}

		if (!file.isFile()){
			return "The path '"+absolutePath+"' is not a file.";
		}
		
		if (!file.canRead()){
			return "We have not permision to read the path:'"+absolutePath+"'.";
		}

		return null;				
	}
	
	/**
	 * Verificamos que file es un fichero y que tenemos permisos de lectura
	 * @param dir
	 * @return
	 */
	public static String verifyReadFile(String filePath){
		if (filePath==null){
			return "The path is null";
		} 

		File dir=new File(filePath);

		return verifyReadFile(dir);			
	}
	
	public static String verifyWrite(File dir){
		String error=verifyReadDir(dir);		

		if (error==null){
			if (!dir.canWrite()){
				return "The dont have permision to write into path:'"+dir.getAbsolutePath()+"'.";
			} else{
				return null;
			}
		} else {
			return error;
		}
	}

	public static String verifyWrite(String path){
		if (path==null){
			return "The path is null";
		} 

		File dir=new File(path);

		return verifyWrite(dir);
	}

	/**
	 * Copia el contenido de un fichero a otro en caso de error lanza una excepcion.
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFromFileToFile(File source, File dest) throws IOException{
		try {
			FileInputStream in = new FileInputStream(source);
			FileOutputStream out = new FileOutputStream(dest);
			try {
				FileChannel canalFuente = in.getChannel();
				FileChannel canalDestino = out.getChannel();
				canalFuente.transferTo(0, canalFuente.size(), canalDestino);
			} catch (IOException e) {
				throw new IOException("copiando ficheros orig:'" + source.getAbsolutePath()
						+ "' destino:'" + dest.getAbsolutePath() + "'", e);
			} finally {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
//				try { 
//					in.close();
//				} catch (IOException e) {
//				}
//				try {
//					out.close();
//				} catch (IOException e) {
//				}
			}
		} catch (FileNotFoundException e) {
			throw new IOException("copiando ficheros orig:'" + source.getAbsolutePath()
					+ "' destino:'" + dest.getAbsolutePath() + "'", e);
		}
	}

	/**
	 * Copia el contenido de un fichero a otro en caso de error lanza una excepcion.
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	public static void append(File source, File dest) throws IOException{
		try {
			FileInputStream in = new FileInputStream(source);
			RandomAccessFile out = new RandomAccessFile(dest,"rwd");
			try {
				FileChannel canalFuente = in.getChannel();
				FileChannel canalDestino = out.getChannel();
				long count=canalDestino.transferFrom(canalFuente,canalDestino.size(), canalFuente.size());
				canalDestino.force(true);
				
			} catch (IOException e) {
				throw new IOException("copiando ficheros orig:'" + source.getAbsolutePath()
									  + "' destino:'" + dest.getAbsolutePath() + "'", e);
			} finally {
				IOUtils.closeQuietly(in);
				out.close();
			}
		} catch (FileNotFoundException e) {
			throw new IOException("copiando ficheros orig:'" + source.getAbsolutePath()
								  + "' destino:'" + dest.getAbsolutePath() + "'", e);
		}
	}


	/**
	 * Copia un archivo a un directorio
	 * 
	 * @param source
	 * @param dest
	 * @return true si va bien, false si falla
	 */
	public static boolean copyFile(File source, File dest) {
		// if(!source.renameTo(new File(dest,source.getName()))) {
		// return false;
		// } else {
		// return true;
		// }
		try {
			FileInputStream in = new FileInputStream(source);
			FileOutputStream out = new FileOutputStream(dest);
			try {
				FileChannel canalFuente = in.getChannel();
				FileChannel canalDestino = out.getChannel();
//				canalFuente.transferTo(0, canalFuente.size(), canalDestino);
				
				long count = 0;
				long size = canalFuente.size();
				while ((count += canalDestino.transferFrom(canalFuente, count,
						size - count)) < size)
					;
			        
			} catch (IOException e) {
				log.error("copiando ficheros orig:'" + source.getAbsolutePath()
						+ "' destino:'" + dest.getAbsolutePath() + "'", e);
				return false;
			} finally {
//				try {
//					in.close();
//				} catch (IOException e) {
//				}
//				try {
//					out.close();
//				} catch (IOException e) {
//				}
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			}
		} catch (FileNotFoundException e) {
			log.error("copiando ficheros orig:'" + source.getAbsolutePath()
					+ "' destino:'" + dest.getAbsolutePath() + "'", e);
			return false;
		}
		return true;
	}

	/**
	 * Mueve los directorios y ficheros de la carpeta origen hasta la carpeta
	 * destino. En el path de destino crea los directorios que sean necesarios.
	 * Despues borra todos los directorios padre de la carpeta origen hasta la
	 * carpeta basDir siempre que estos esten vacios.
	 * 
	 * @param origDir
	 *            directorio origen
	 * @param destDir
	 *            directorio final
	 * @return true si va bien, false si falla
	 */
	public static boolean moveFilesFromDirectoryToDirectory(File origDir,
															File destDir, 
															File baseDir) {

		if (!origDir.exists()) {
			return false;
		}
		if (!baseDir.exists()) {
			return false;
		}
		
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		
		File array[]=origDir.listFiles();
		if ( array!= null) {
			for (File file : array) {
				File destFile = new File(destDir, file.getName());
				if (file.isDirectory()) {
					moveFilesFromDirectoryToDirectory(file, destFile,
							baseDir);
				} else {
					if (!copyFile(file, destFile)) {
						return false;
					}
				}
			}
		} else {
			return false;
		}
		if (deleteFilesOfDir(origDir)) {
			cleanParentDirectories(origDir, baseDir);
			return true;
		} else {
			return false;
		}
	}


	/**
	 * Copya los directorios y ficheros de la carpeta origen hasta la carpeta
	 * destino. En el path de destino crea los directorios que sean necesarios.
	 * 
	 * @param origDir
	 *            directorio origen
	 * @param destDir
	 *            directorio final
	 * @return true si va bien, false si falla
	 */
	public static boolean copyFilesFromDirectoryToDirectory(File origDir,
															File destDir) {

		if (!origDir.exists()) {
			return false;
		}
		
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		
		File array[]=origDir.listFiles();
		if ( array!= null) {
			for (File file : array) {
				File destFile = new File(destDir, file.getName());
				if (file.isDirectory()) {
					copyFilesFromDirectoryToDirectory(file, destFile);
				} else {
					if (!copyFile(file, destFile)) {
						return false;
					}
				}
			}
		} else {
			return false;
		}

		return true;
	}
	
	/**
	 * Copia los ficheros que cumplen el filtro de un directoruo a otro.
	 *  El directorio origen tiene que existir el destino si no existe se crea
	 *
	 * @param origDir El directorio origen, tiene que existir y ser leible
	 * @param destDir, el directorio de destino si no existe se crea
	 * @param filter El filtro que los ficheros tienen que cumplir para ser copiados
	 * @param recursive if true recursive
	 * @return
	 */
	public static boolean copyFilesFromDirectoryToDirectory(File origDir,
															File destDir,
															ExtensionFilenameFilter filter,
															boolean recursive) {
		
		if (!origDir.exists()) {
			return false;
		}
		
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		
		File array[]=origDir.listFiles(filter);
		
		if ( array != null) {
			for (File file : array) {
				File destFile = new File(destDir, file.getName());
				if (file.isDirectory() && recursive) {
					copyFilesFromDirectoryToDirectory(file, destFile);
				} else {
					if (!copyFile(file, destFile)) {						
						return false;
					}
				}
			}
		} else {
			return false;
		}
		
		return true;
	}

	/**
	 * Borra todos los ficheros contenidos en el directorio. 
	 * Parra borrar los directorios estos tienen que estar vacios
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean deleteFilesOfDir(File dir) {
		if (!dir.exists() || !dir.isDirectory()) {
			// No hay ficheros que borrar a si que OK
			log.warn("El directorio:'" + dir.getAbsolutePath()
					+ "' no existe o no es un directorio");
			return false;
		}
		// el fichero es un directorio
		boolean succed = true;
		File listFile[] = dir.listFiles();
		for (File file:listFile) {
			if (!file.delete()) {
				log.warn("No se ha podido borrar el fichero:'"
						+ file.getAbsolutePath() + "'");
				succed = false;
			}
		}

		return succed;
	}

	/**
	 * Borra este directorio y todo lo que haya dentro.
	 * Si esto no es un directorio sale.
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean deleteDirRecursively(File dir) {
		if (!dir.exists() || !dir.isDirectory()) {
			// No hay ficheros que borrar a si que OK
			log.warn("El directorio:'" + dir.getAbsolutePath()
					+ "' no existe o no es un directorio");
			return false;
		}
		// el fichero es un directorio
		boolean succed = true;
		File listFile[] = dir.listFiles();
		for (File file:listFile) {
			if (file.isDirectory()) {
				deleteDirRecursively(file);
			} else {
				if (!file.delete()) {
					log.warn("No se ha podido borrar el fichero:'"
							+ file.getAbsolutePath() + "'");
					succed = false;
				}
			}	
		}

		if (!dir.delete()) {
			log.warn("No se ha podido borrar el fichero:'"
					+ dir.getAbsolutePath() + "'");
			succed = false;
		}
		return succed;
	}

	
	/**
	 * This delete the child dirs of the current dir only if the child dirs are empty or contains other dirs only
	 * @param dir
	 */
	public static void deleteChilsDirsIfEmpty(File dir){
		File childs[]=getChildDirs(dir);
		
		for (File file:childs){
			deleteChilsDirsIfEmpty(file);
			if (!file.delete()) {
				log.info("No se ha podido borrar el directorio " + file.getAbsolutePath());
			}
		}
		
	}
	
	/**
	 * Metodo encargado de limpiar la estructura de directorios temporales. Esta
	 * metodo va borrando del currentPathDir hasta el basePathFile siempre que
	 * los directorios esten vacios.
	 * 
	 * Ej : currentPathFile = /export/share-images/2009/03/12/LOCAL Ej :
	 * basePathFile = /export/share-images Iria borrando los directorios LOCAL,
	 * 12, 03, 2009 solo si estos entan vacios
	 * 
	 * @param currentPathFile
	 *            Representa el directorio del inicio de la limpieza.
	 */
	public static void cleanParentDirectories(File currentPathFile,
			File basePathFile) {

		if (currentPathFile==null){
			log.warn("El path inicial es nulo");
			return ;
		}
		
		if (basePathFile==null){
			log.warn("El path final es nulo");
			return ;
		}
		
		try {
			if (!basePathFile.equals(currentPathFile)) {
				if (currentPathFile.isDirectory() && currentPathFile.list().length == 0) {
					
					if (log.isDebugEnabled()) {
						log.debug("Borrando el directorio vacio:'" + currentPathFile.getAbsolutePath() + "'");
					}
					
					if (currentPathFile.delete()) {
						cleanParentDirectories(currentPathFile.getParentFile(),basePathFile);
					}
				}
			}
		}catch (Exception e) { 
			log.error("Borando los directorios desde :"+currentPathFile.getAbsolutePath()+" hasta:"+basePathFile.getAbsolutePath()+".");
			return ;
		}
	}

//	public static String hashFile(File file) {
//		Long inicio = new Date().getTime();
//		InputStream is = null;
//		try {
//			is = new FileInputStream(file);
//			byte[] data = new byte[(int)file.length()];
//			is.read(data);
//			return hashFile(data);
//		} catch (FileNotFoundException e) {
//			log.error("El fichero '" + file.getAbsolutePath() + "' no existe", e);
//		} catch (IOException e) {
//			log.error("Error al leer el fichero '" + file.getAbsolutePath() + "'.", e);
//		} finally {
//			Long fin = new Date().getTime();
//			log.info("Tiempo tardado en generar el HASH del documento " + file.getName() + ": " + (fin - inicio) + " ms.");
//			try {is.close();} catch (Exception e){}
//		}
//		return null;
//	}
	
//	public static String hashFile (byte[] fileContent) {
//		MessageDigest digest;
//		try {
//			digest = java.security.MessageDigest.getInstance("MD5");
//		} catch (NoSuchAlgorithmException e) {
//			log.error("No hay soporte para algoritmo MD5", e);
//			return null;
//		}
//		digest.update(fileContent);
//	    byte[] hash = digest.digest();
//	    
//	    return new BASE64Encoder().encode(hash);
//	}
	
	/**
	 * Nos dice si un fichero es un PDF
	 * @param file
	 * @return
	 * @deprecated use isPDF
	 */
	public static boolean esPDF(File file) {
		boolean ret = false;
		FileReader reader = null;
		try {
			reader= new FileReader(file);
			char[] buffer  = new char[4];
			reader.read(buffer, 0, 4);
			ret = ("%PDF".equals(String.valueOf(buffer)));
		} catch (FileNotFoundException e) {
			log.error("Error: no se encuentra el fichero " + file.getAbsolutePath());
		} catch (IOException e) {
			log.error("Error leyendo el fichero " + file.getAbsolutePath());
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				log.error("Error cerrando el fichero " + file.getAbsolutePath());
			}
		}
		return ret;
	}
	
	/**
	 * Nos dice si un fichero es un PDF
	 * @param file
	 * @return
	 */
	public static boolean isPDF(File file) {
		boolean ret = false;
		FileReader reader = null;
		try {
			reader= new FileReader(file);
			char[] buffer  = new char[4];
			reader.read(buffer, 0, 4);
			ret = ("%PDF".equals(String.valueOf(buffer)));
		} catch (FileNotFoundException e) {
			log.error("Error: no se encuentra el fichero " + file.getAbsolutePath());
		} catch (IOException e) {
			log.error("Error leyendo el fichero " + file.getAbsolutePath());
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				log.error("Error cerrando el fichero " + file.getAbsolutePath());
			}
		}
		return ret;
	}
	
	
	/**
	 * Returns the child dirs if any, otherwise return new File[0]
	 * @param file
	 * @return
	 * @deprecated use listDirs
	 */
	public static File [] getChildDirs(File file) {
//		if (file==null){
//			return EMPTY_FILE_LIST;
//		} 
//		
//		File ret[]=file.listFiles(DIR_FILTER);
//		
//		if (ret==null){
//			return EMPTY_FILE_LIST;
//		} else {
//			return ret;
//		}
		return listDirs(file);
	}

	/**
	 * Returns the child dirs if any, otherwise return new File[0]
	 * @param file
	 * @return
	 */
	public static File [] listDirs(File file) {
		if (file==null){
			return EMPTY_FILE_LIST;
		} 
		
		File ret[]=file.listFiles(DIR_FILTER);
		
		if (ret==null){
			return EMPTY_FILE_LIST;
		} else {
			return ret;
		}		
	}

	/**
	 * Iterate on all the files/dir of a folder no mather the type and the read state.
	 * 
	 * @param parentDir the parent folder where the chils to iterate on are.
	 * @return
	 */
	public static boolean iterateFiles(File parentDir,FileIterator iterator) {		
		if (parentDir==null){
			log.warn("Parent dir is null");
			return true;
		} 

		// verify that we can read dir.
		String message=verifyReadDir(parentDir);
		if (message !=null){
			log.warn(message);
			return true;
		}


		File childs[]=parentDir.listFiles();
		
		if (childs==null){
			log.info("Parent dir:"+parentDir.getAbsolutePath()+" do not have child dirs");
			return true;
		} else {
			for (File child:childs){
				
				boolean iteratorRet=iterator.iterate(child);
				
				if (iteratorRet==false){
					if (log.isDebugEnabled()){
						log.debug("Iterator returns false. Iteration on:"+parentDir.getAbsolutePath()+" stopped.");
					}
					return false;
				}
			}
			
			return true;
		}		
		
	}



	/**
	 * Iterate on all the files/dir of a folder no mather the type and the read state.
	 * 
	 * @param parentDir the parent folder where the chils to iterate on are.
	 * @return
	 */
	public static boolean iterate(File parentDir, 
								  FilenameFilter fileFilter,
								  FileIterator iterator) {		
		if (parentDir==null){
			log.warn("Parent dir is null");
			return true;
		} 

		// verify that we can read dir.
		String message=verifyReadDir(parentDir);
		if (message !=null){
			log.warn(message);
			return true;
		}


		File childs[]=parentDir.listFiles(fileFilter);
		
		if (childs==null){
			log.info("Parent dir:"+parentDir.getAbsolutePath()+" do not have child dirs");
			return true;
		} else {
			for (File child:childs){
				
				boolean iteratorRet=iterator.iterate(child);
				
				if (iteratorRet==false){
					if (log.isDebugEnabled()){
						log.debug("Iterator returns false. Iteration on:"+parentDir.getAbsolutePath()+" stopped.");
					}
					return false;
				}
			}
			
			return true;
		}		
		
	}

	/**
	 * Iterate only on the childs of the parent folder
	 * Iterates on the folder pased on parameter only if the content can be readed.
	 * Return false if the iterator returns false on a file.
	 * 
	 * @param parentDir the parent folder where the chils to iterate on are.
	 * @return
	 */
	public static boolean iterateOnChildDirs(File parentDir,FileIterator iterator) {		
		if (parentDir==null){
			log.warn("Parent dir is null");
			return true;
		} 
		
		// verify that we can read dir.
		String message=verifyReadDir(parentDir);
		if (message !=null){
			log.warn(message);
			return true;
		}

		File childs[]=parentDir.listFiles(DIR_FILTER);
		
		if (childs==null){
			log.info("Parent dir:"+parentDir.getAbsolutePath()+" do not have child dirs");
			return true;
		} else {
			for (File child:childs){
				
				boolean iteratorRet=iterator.iterate(child);

				if (iteratorRet==false){
					if (log.isDebugEnabled()){
						log.debug("Iterator returns false. Iteration on:"+parentDir.getAbsolutePath()+" stopped.");
					}
					return false;
				}
			}

			return true;
		}		
	}

	
	
	/**
	 * Iterates on the folder pased on parameter only if the content can be readed.
	 * Return false if the iterator returns false on a file.
	 * 
	 * @param parentDir the parent folder where the chils to iterate on are.
	 * @return
	 */
	public static boolean iterateOnChildFilesOnly(File parentDir,FileIterator iterator) {		
		if (parentDir==null){
			log.warn("Parent dir is null");
			return true;
		} 
		
		// verify that we can read dir.
		String message=verifyReadDir(parentDir);
		if (message !=null){
			log.warn(message);
			return true;
		}

		File childs[]=parentDir.listFiles(FILE_FILTER);
		
		if (childs==null){
			log.info("Parent dir:"+parentDir.getAbsolutePath()+" do not have child dirs");
			return true;
		} else {
			for (File child:childs){
				
				boolean iteratorRet=iterator.iterate(child);

				if (iteratorRet==false){
					if (log.isDebugEnabled()){
						log.debug("Iterator returns false. Iteration on:"+parentDir.getAbsolutePath()+" stopped.");
					}
					return false;
				}
			}

			return true;
		}		
	}
	
	/**
	 * Devuelve true si es un fichero y se puede leer.
	 * @param abstolutePath el path absoluto de un fichero
	 * @return
	 */
	public static boolean canReadFile(String abstolutePath) {
		File file=new File(abstolutePath);
		
		if(file.canRead() && !file.isDirectory()){
			return true;
		} else {
			return false;
		}		
	}

	/**
	 * Devuelve true si es un fichero y se puede leer.
	 * @param abstolutePath el path absoluto de un fichero
	 * @return
	 */
	public static boolean canReadFile(File file) {		
		if(file!=null && file.canRead() && !file.isDirectory()){
			return true;
		} else {
			return false;
		}		
	}

	public static boolean canReadFile(File file,Logger logger) {		
		if(file==null){
			logger.warn("File is null");
			return false;
		}

		if (file.isDirectory()){
			logger.warn("Is a dir, file:"+file.getAbsolutePath()+"");
			return false;
		}

		return file.canRead();
	}

	
	/**
	 * Devuelve true si es un fichero y se puede leer.
	 * @param abstolutePath el path absoluto de un fichero
	 * @return
	 */
	public static boolean canWriteFile(File file) {		
		if(file!=null && file.canWrite() && !file.isDirectory()){
			return true;
		} else {
			return false;
		}		
	}

	/**
	 * If the file is like name.extension This function returns extension 
	 * 	for filename=name. this returns ""
	 *  for filemane=name  this retuns null;
	 *  for filemane=.extension  this retuns extension;
	 *  for filemane=.  this retuns "";
	 *  
	 * @param fileNameAndPath
	 * @return
	 */
	public static String getExtension(String fileName) {
		if (fileName==null){
			return null;
		} else {
			int number=fileName.lastIndexOf('.');
			
			if (number>=0){
				return fileName.substring(number+1, fileName.length());
			} else {
				return null;
			}
		}		
	}
	
	/**
	 * If the file is like name.extension This function returns name 
	 * 	for filename=name. this returns name
	 *  for filemane=name  this retuns name;
	 *  for filemane=.extension  this retuns "";
	 *  for filemane=.  this retuns "";
	 *  
	 * @param fileNameAndPath
	 * @return
	 */
	public static String getFileNameWithoutExtenxion(String fileName) {
		if (fileName==null){
			return null;
		} else {
			int number=fileName.lastIndexOf('.');
			
			if (number>=0){
				return fileName.substring(0, number);
			} else {
				return fileName;
			}
		}		
	}
	
	/**
	 * Devuelve el nombre de un path, por ejmeplo: /dir/toto.txt > toto.txt  o en windows \toto\toto.txt > toto.txt.
	 * El separador se escoje en funcion de si la el fileNameAndPath ya contien \ o /
	 * 
	 * @param fileNameAndPath contiene un path y un nombre del fichero, ej : /dir/toto.txt
	 * @return
	 */
	public static String getFileName(String fileNameAndPath) {
		
		if (fileNameAndPath == null){
			return null;
		}
		
		String fileSeparator;		
		if(fileNameAndPath.contains("/")){
			fileSeparator = "/";
		}else{
			fileSeparator = "\\";
		}		
		
		int lastIndexOf=fileNameAndPath.lastIndexOf(fileSeparator);
		
		if (lastIndexOf<0){
			return fileNameAndPath;
		} else {
			return fileNameAndPath.substring(lastIndexOf+1,fileNameAndPath.length());
		}
	}

	/**
	 * Devuelve el path de un path absoluto de un fichero, por ejmeplo: /dir/toto.txt > /dir/  o en windows \toto\toto.txt > \toto\ 
	 * El separador se escoje en funcion de si la el fileNameAndPath ya contien \ o /.
	 * El separador del final del path tambien esta incluido.
	 * 
	 * @param fileNameAndPath contiene un path y un nombre del fichero, ej : /dir/toto.txt
	 * @return el path or "" si no tiene
	 */
	public static String getFilePath(String fileNameAndPath) {
		if (fileNameAndPath == null){
			return null;
		}
		
		String fileSeparator;			
		if(fileNameAndPath.contains("/")){
			fileSeparator = "/";
		}else{
			fileSeparator = "\\";
		}
		
		int lastIndexOf=fileNameAndPath.lastIndexOf(fileSeparator);
		
		if (lastIndexOf<0){
			return "";
		} else {
			return fileNameAndPath.substring(0,lastIndexOf);
		}
	}


	public static File getReadableFile(String path,File defaultValue){
		if (path == null){
			log.warn("The path is null");
			return defaultValue;
		} else {
			File ret=new File(path);

			if (!ret.exists()){
				log.warn("The file:'"+ret.getAbsolutePath()+" do not exist.");
				return defaultValue;
			} else if (!ret.canRead()){
				log.warn("The file:'"+ret.getAbsolutePath()+" can not be readed.");
				return defaultValue;
			} else {
				return ret;
			}
		}
	}

	public static File getReadableDir(String path,File defaultValue){
		File ret=getReadableFile(path, null);
		
		if (ret==null){
			return defaultValue;
		} else if (ret.isDirectory()){
//			log.warn("The file:'"+ret.getAbsolutePath()+" is not a directory.");
			return ret;
		} else {
			return defaultValue;
		}
	}
	
	public static File getWriteableDir(String path,File defaultValue){
		if (path == null){
			log.warn("The path is null");
			return defaultValue;
		} else {
			File ret=new File(path);

			if (!ret.exists()){
				log.warn("The file:'"+ret.getAbsolutePath()+" do not exist.");
				return defaultValue;
			} else if (!ret.canWrite()){
				log.warn("The file:'"+ret.getAbsolutePath()+" can not be writed.");
				return defaultValue;
			} else if (!ret.isDirectory()){
				log.warn("The file:'"+ret.getAbsolutePath()+" is not a directory.");
				return defaultValue;
			} else {
				return ret;
			}
		}
	}

	public static boolean renameTo(File origin,File dest){
		if (origin == null){
			log.error("Origing file to move is null");

			return false;
		} else if (dest == null){
			log.error("Destiny file to move is null");

			return false;
		} else {
			if (!origin.renameTo(dest)){
				if (!origin.exists()){
					log.info("File :"+origin.getAbsolutePath()+" allready exists after renaming retry move to:'"+dest.getAbsolutePath()+"'");
					if (!origin.renameTo(dest)){
						log.warn("Retry to move file from File :"+origin.getAbsolutePath()+" to:'"+dest.getAbsolutePath()+"' FAILS !!!");
						
						return false;
					} else {
						return true;
					}					
				} else {
					log.warn("Mocing File :"+origin.getAbsolutePath()+" to:'"+dest.getAbsolutePath()+"', fails but origin does not esixts.");
					return true;
				}
			} else {
				return true;
			}
//		}else {
//			
//			DEmasiado LENTO
//			
//			StringBuilder buffer=new StringBuilder();
//			buffer.append("mv ");
//			buffer.append(origin.getAbsolutePath());
//			buffer.append(" ");
//			buffer.append(dest.getAbsolutePath());
//			
//			try {
//				return Exec.execCommand(buffer.toString());
//			} catch (IOException e) {
//				log.error("Moving files files from:"+origin.getAbsolutePath()+" to "+dest.getAbsolutePath(),e);
//				
//				return false;
//			}
		}
	}

	public static String getString(File file,String defaultText,String charset) {
		try {
			String stringToParse = new Scanner(file,charset).useDelimiter("\\Z").next();

			return stringToParse;
		}catch(FileNotFoundException e){
			log.error("While oppening file:"+file,e);
			return defaultText;
		}		
	}
	
	public static boolean exists(File file){
		if (file==null){
			return false;
		} else {
			return file.exists();
		}
	}
	
	public static String BASE64_IMAGE_HEADER="data:image/jpeg;base64,";
	
	public static boolean isBase64Image(String base64){
		// data:image/jpeg;base64,
		if (base64== null || base64.length()<=BASE64_IMAGE_HEADER.length()){
			return false;
		} else {
			if (base64.startsWith(BASE64_IMAGE_HEADER)){
				return true;
			} else {
				String header=base64.substring(0, BASE64_IMAGE_HEADER.length()).toLowerCase();
				
				if (BASE64_IMAGE_HEADER.equals(header)){
					return true;
				} else {
					return false;
				}
			}
		}
	}
	
	public static String getBase64String(String base64){
		if (base64==null){
			return null;
		} else {
			if (isBase64Image(base64)){
				String ret=base64.substring(BASE64_IMAGE_HEADER.length());
				
				return ret;
			} else {
				return base64;
			}
		}
	}
		
	public static boolean importBase64Image(File file,String base64) throws IOException{
		String b=getBase64String(base64);
		
		if (b == null) {
			return false;
		} else {
            byte bytes[]=Base64.decodeBase64(b);
            
			FileOutputStream output = new FileOutputStream(file);
			try {
				IOUtils.write(bytes, output);
			}finally{
				IOUtils.closeQuietly(output);
			}
			
			return true;
		}
	}
}
