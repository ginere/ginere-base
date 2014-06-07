/**
 * Copyright: Angel-Ventura Mendo Gomez
 *	      ventura@free.fr
 *
 * $Id: ImageSize.java,v 1.4 2006/11/25 07:19:55 ventura Exp $
 */
package ginere.base.util.image;

import ginere.base.util.dao.DaoManagerException;
import ginere.base.util.file.FileId;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.Nestable;
import org.apache.commons.lang.exception.NestableException;
import org.apache.log4j.Logger;

/**
 * For testing image Size
 *
 * @author Angel-Ventura Mendo Gomez
 * @version $Revision: 1.4 $
 */
public class ImageSize {
	private static Logger log = Logger.getLogger(ImageSize.class);
	
    public static class Dimension {
		public final int width;
		public final int height;

		public Dimension(int width,int height){
			this.width=width;
			this.height=height;
		}
    }

    @SuppressWarnings("serial")
	public static class ImageSizeException extends NestableException implements Nestable{
		public ImageSizeException(Throwable t,String message){
			super(message,t);
		}

		public ImageSizeException(String message){
			super(message);
		}
    }

    @SuppressWarnings("serial")
	public static class FormatNotSuported extends  NestableException implements Nestable{
		public FormatNotSuported(Throwable t,String message){
			super(message,t);
		}

		public FormatNotSuported(String message){
			super(message);
		}
    }

//	/**
//	 * This returns the width of this FileStorageManager ID as one image.
//	 * This returns the width to resize the image setting the height as 
//	 * the maxHeight .
//	 */
//    public static int getResizedWidth(FileId fileId,int maxHeight) throws FileNotFoundException, DaoManagerException {
//		try{
//			Dimension d = getDimensions(fileId);
//
//			double prop=(double)d.width/(double)d.height;
//			int width=(int)(maxHeight * prop);
//			return width;
//		} catch (ImageSizeException e) {
//			return maxHeight;
//		} catch (FormatNotSuported e) {
//			return maxHeight;
//		}
//	}

//	/**
//	 * This returns the height of this FileStorageManager ID as one image.
//	 * This returns the height to resize the image setting the width as 
//	 * the maxWidth .
//	 */
//    public static int getResizedHeight(FileId fileId,int maxWidth) throws FileNotFoundException, DaoManagerException {
//		try{
//			Dimension d = getDimensions(fileId);
//			
//			double prop=(double)d.width/(double)d.height;
//			int height=(int)(maxWidth / prop);
//			
//			return height;
//		} catch (ImageSizeException e) {
//			return maxWidth;
//		} catch (FormatNotSuported e) {
//			return maxWidth;
//		}
//	}

//	/**
//	 * This returns the width of this FileStorageManager ID as one image.
//	 * The image will be resized proportionaly to expand itself until
//	 * the max size delimited by maxWidth and maxHeight
//	 * @throws DaoManagerException 
//	 * @throws FileNotFoundException 
//	 * @throws FormatNotSuported 
//	 * @throws ImageSizeException 
//	 */
//    public static int getResizedWidth(FileId fileId,int maxWidth, int maxHeight) throws ImageSizeException, FormatNotSuported, FileNotFoundException, DaoManagerException  {
//    	Dimension d = getDimensions(fileId);
//    	return getResizedWidth(d, maxWidth, maxHeight);
//	}
    
    public static int getResizedWidth(String fileName,String mimeType,InputStream input,int maxWidth, int maxHeight) throws ImageSizeException, FormatNotSuported, FileNotFoundException, DaoManagerException  {
    	Dimension d = getDimensions(fileName,mimeType,input);
    	return getResizedWidth(d, maxWidth, maxHeight);
	}
    
    public static int getResizedWidth(Dimension d,int maxWidth, int maxHeight) {
		double prop=(double)d.width/(double)d.height;
		int width=(int)(maxHeight * prop);
		int height=(int)(maxWidth / prop);
		
		if (width<=maxWidth){
			return width;
		} else { // if (height<=maxHeight){
			return maxWidth;
		}
	}

//	/**
//	 * This returns the width of this FileStorageManager ID as one image.
//	 * The image will be resized proportionaly to expand itself until
//	 * the max size delimited by maxWidth and maxHeight
//	 * @throws DaoManagerException 
//	 * @throws FileNotFoundException 
//	 * @throws FormatNotSuported 
//	 * @throws ImageSizeException 
//	 */
//    public static int getResizedHeight(FileId fileId,int maxWidth, int maxHeight) throws ImageSizeException, FormatNotSuported, FileNotFoundException, DaoManagerException  {
//    	Dimension d = getDimensions(fileId);
//    	return getResizedHeight(d,maxWidth,maxHeight);
//	}

    public static int getResizedHeight(String fileName,String mimeType,InputStream input,int maxWidth, int maxHeight) throws ImageSizeException, FormatNotSuported, DaoManagerException {

    	Dimension d = getDimensions(fileName,mimeType,input);
    	return getResizedHeight(d,maxWidth,maxHeight);
	}
    
    public static int getResizedHeight(Dimension d,int maxWidth, int maxHeight) {
		double prop=(double)d.width/(double)d.height;
		int width=(int)(maxHeight * prop);
		int height=(int)(maxWidth / prop);
		
		if (height<=maxHeight){
			return height;
		} else { // if (height<=maxHeight){
			return maxHeight;
		}
	}

//	/**
//	 * Return the width of the image
//	 */
//    public static int getWidth(FileId fileId) throws FileNotFoundException, DaoManagerException {
//		try{
//			Dimension d = getDimensions(fileId);
//			return d.width;
//		} catch (ImageSizeException e) {
//			return 0;
//		} catch (FormatNotSuported e) {
//			return 0;
//		}
//	}

//	/**
//	 * Return the height of the image
//	 */
//    public static int getHeight(FileId fileId) throws FileNotFoundException, DaoManagerException {
//		try{
//			Dimension d = getDimensions(fileId);
//			return d.height;
//		} catch (ImageSizeException e) {
//			return 0;
//		} catch (FormatNotSuported e) {
//			return 0;
//		}
//	}

//	/**
//	 * This returns the width of this FileStorageManager ID as one image.
//	 * If the image size do not exceeds the maxWidth and the maxHeight the
//	 * size will no be changed.
//	 * @throws DaoManagerException 
//	 * @throws FileNotFoundException 
//	 */
//    public static int getWidth(FileId fileId,int maxWidth, int maxHeight) throws FileNotFoundException, DaoManagerException {
//		try{
//			Dimension d = getDimensions(fileId);
//
//			if (d.width <= maxWidth && d.height <= maxHeight) {
//				return d.width;
//			} else if (d.width <= maxWidth && d.height > maxHeight) {
//				double prop=(double)d.width/(double)d.height;
//				
//				return (int)(maxHeight * prop);
//			} else if (d.width > maxWidth && d.height <= maxHeight) {
//				return maxWidth;
//			} else { // if (d.width > maxWidth && d.height > maxHeight) {
//				double prop=(double)d.width/(double)d.height;
//				int width=(int)(maxHeight * prop);
//				int height=(int)(maxWidth / prop);
//
//				if (width<=maxWidth){
//					return width;
//				} else { // if (height<=maxHeight){
//					return maxWidth;
//				}
//			}
//		} catch (ImageSizeException e) {
//			return maxWidth;
//		} catch (FormatNotSuported e) {
//			return maxWidth;
//		}
//	}

//    public static int getHeight(FileId fileId,int maxWidth, int maxHeight) throws FileNotFoundException, DaoManagerException {
//		try{
//			Dimension d = getDimensions(fileId);
//			
//			if (d.width <= maxWidth && d.height <= maxHeight) {
//				return d.height;
//			} else if (d.width <= maxWidth && d.height > maxHeight) {
//				return maxHeight;
//			} else if (d.width > maxWidth && d.height <= maxHeight) {
//				double prop=(double)d.width/(double)d.height;
//				
//				return (int)(maxWidth / prop);
//			} else { // if (d.width > maxWidth && d.height > maxHeight) {
//				double prop=(double)d.width/(double)d.height;
//				int width=(int)(maxHeight * prop);
//				int height=(int)(maxWidth / prop);
//
//				if (width<=maxWidth){
//					return maxHeight;
//				} else { // if (height<=maxHeight){
//					return height;
//				}
//			}
//		} catch (ImageSizeException e) {
//			return maxHeight;
//		} catch (FormatNotSuported e) {
//			return maxHeight;
//		}
//	}

    public static boolean isImage(FileId file) throws DaoManagerException{
		return isImage(file.getMimeType());
	}

    public static boolean isImage(String mimeType){
		if (isGIF(mimeType)){
			return true ;
		} else if (isJPEG(mimeType) ){
			return true ;			
		} else if (isPNG(mimeType) ){
			return true ;
		} else {
			return false;
		}			
	}

    public static boolean isGIF(String mimeType){
		if ("image/gif".equals(mimeType.toLowerCase())){
			return true;			
		} else {
			return false;
		}
	}
	
    public static boolean isJPEG(String mimeType){
    	if ("image/jpeg".equals(mimeType) || "image/pjpeg".equals(mimeType) ){
			return true;			
		} else {
			return false;
		}
	}
    public static boolean isPNG(String mimeType){
    	 if ("image/png".equals(mimeType) || "image/x-png".equals(mimeType) ){
			return true;			
		} else {
			return false;
		}
	}

    public static Dimension getDimensions(String fileName,String mimeType,File file)
	throws ImageSizeException,FormatNotSuported, DaoManagerException{
    	try {
	    	FileInputStream input=new FileInputStream(file);
	    	
	    	try {
	    		return getDimensions(fileName, mimeType, input);
	    	}finally{
	    		IOUtils.closeQuietly(input);
	    	}
    	}catch (FileNotFoundException e){
    		throw new DaoManagerException("fileName:"+fileName,e);
    	}
    }
    
    /**
     * ATENCION SE LEEN CARACTERES DEL INPUT STREM POR LO QUE LA POSICION DE LECTURA DENTRO DEL INPUT
     * QUEDA INUTILIZADA
     * @param fileName
     * @param mimeType
     * @param input
     * @return
     * @throws ImageSizeException
     * @throws FormatNotSuported
     * @throws DaoManagerException
     */
    public static Dimension getDimensions(String fileName,String mimeType,InputStream input)
		throws ImageSizeException,FormatNotSuported, DaoManagerException{


		if (isGIF(mimeType)){
			try {
				return getGifDimensions(input);
			}catch(IOException e){
				throw new ImageSizeException(e,"while getting dimension for file:"+fileName+" mimeType:"+mimeType);
			}
	
		} else if (isJPEG(mimeType) ){
			try {
				Dimension dim=getJpgDimensions(input);
				if (dim!=null){
					return dim;
				} else {
					throw new ImageSizeException("Illegal size for file:"+fileName+" mimeType:"+mimeType);
				}
			}catch(IOException e){
				throw new ImageSizeException(e,"while getting dimension for file:"+fileName+" mimeType:"+mimeType);
			}
	
		} else if (isPNG(mimeType) ){
			try {
				Dimension dim=getPngDimensions(input);
				if (dim!=null){
					return dim;
				} else {
					throw new ImageSizeException("Illegal size for file:"+fileName+" mimeType:"+mimeType);
				}
			}catch(IOException e){
				throw new ImageSizeException(e,"while getting dimension for file:"+fileName+" mimeType:"+mimeType);
			}
	
		} else {
			throw new FormatNotSuported("Mime type:"+mimeType+" not supported.");
		}

	}
    
//    public static Dimension getDimensions(FileId fileId)
//		throws ImageSizeException,FormatNotSuported, DaoManagerException, FileNotFoundException{
//
//		String mimeType=fileId.getMimeType();
//		InputStream input=fileId.getInputStream();
//
//		return getDimensions(fileId.getId(),mimeType,input);
//    }

    /**
     * @return this function do not returns null
     */
    public static Dimension getGifDimensions(InputStream s) throws ImageSizeException,IOException {
		int x;
		int buffer[] = new int[4];

		for(int i=0; i<6; i++) { // discard first six bytes
			x = s.read();
		}
		for(int i=0; i<4; i++) { // next four bytes are width and height
			buffer[i] = s.read();
		}

		int width=buffer[0]+((buffer[1])<<8);
		int height=buffer[2]+((buffer[3])<<8);

		if((width < 0)||(height < 0)){
			throw new ImageSizeException("Illegal dimentions , width="+width+" height="+height);
		} else {
			return new Dimension(width,height);
		}
    }

    /**
     * @return this function may return null
     */
    public static Dimension getJpgDimensions(InputStream s) throws IOException {
		int x;
		int frameType = 0;
		int b[] = new int[4];
		Dimension rtn = null;
		boolean foundDimensions = false;

		b[0] = s.read();
		b[1] = s.read();
		if(b[0] == 0xFF && b[1] == 0xD8) { // otherwise, not valid JPEG file
			while(!foundDimensions) { // discard bytes until we get to an appropriate frame
				x = s.read();
				if(x == -1) {
					// EOF -- malformed JPG file
					break;
				}
				if(x == 0xFF) { // start of frame
					frameType = x;
					while(frameType == 0xFF) { // skip extraneous pad bytes
						frameType = s.read();
					}
					switch(frameType) {
					case 0xC0:    // Baseline DCT
					case 0xC1:    // Sequential DCT
					case 0xC2:    // Progressive DCT
					case 0xC3:    // Spatial (sequential) lossless
					case 0xC5:    // Differential Sequential DCT
					case 0xC6:    // Differential Progressive DCT
					case 0xC7:    // Differential Spatial
					case 0xC9:    // Extended Sequential DCT
					case 0xCA:    // Progressive DCT
					case 0xCB:    // Spacial (sequential) Lossless
					case 0xCD:    // Differential Sequential DCT
					case 0xCE:    // Differential Progressive DCT
					case 0xCF:    // Differential Spatial
						for(int i=0; i<3; i++) { // discard another three bytes
							s.read();
						}
						for(int i=0; i<4; i++) { // next four bytes are w/h
							b[i] = s.read();
						}
						rtn = new Dimension(b[3]+((b[2])<<8), b[1]+((b[0])<<8));
						foundDimensions = true;
						break;
					default:      // skip over uninteresting/misleading sections
						b[0] = s.read();
						b[1] = s.read();
						int skipLength = b[1]+((b[0])<<8);
						skipLength -= 2; // length includes itself
						while(skipLength > 0 && x!=-1) {
							x = s.read();
							skipLength--;
						}
						break;
					}
				}
			}
		}
		return(rtn);
    }

    /**
     * @return this function may return null
     */
    public static Dimension getPngDimensions(InputStream s) throws IOException {
		int signature[] = {137, 80, 78, 71, 13, 10, 26, 10} ;
		int header[] = {'I', 'H', 'D', 'R'};
		int buffer[] = new int[8];
		int width[] = new int[4];
		int height[] = new int[4];
		Dimension rtn = null;

		for(int i=0; i<8; i++) { // read signature
			buffer[i] = s.read();
			if(signature[i] != buffer[i]) {
				// signature does not match -- malformed PNG file
				return rtn;
			}
		}
		for(int i=0; i<4; i++) { // dump four bytes = length of next chunk
			buffer[i] = s.read();
		}
		for(int i=0; i<4; i++) { // dump chunk type code (if it's not the header, it's an invalid PNG file)
			buffer[i] = s.read();
			if(header[i] != buffer[i]) {
				// chunk code is not header -- malformed PNG file
				return rtn;
			}
		}
		for(int i=0; i<4; i++) { // width
			width[i] = s.read();
		}
		for(int i=0; i<4; i++) { // height
			height[i] = s.read();
		}
		rtn = new Dimension(width[3]+((width[2])<<8)+((width[1])<<16)+((width[0])<<24),
							height[3]+((height[2])<<8)+((height[1])<<16)+((height[0])<<24));
		return(rtn);
    }
}
