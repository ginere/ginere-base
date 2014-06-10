/**
 * Copyright: Angel-Ventura Mendo Gomez
 *	      ventura@free.fr
 *
 * $Id: ImageUtils.java,v 1.1 2006/11/25 07:24:14 ventura Exp $
 */
package eu.ginere.base.util.image;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.file.FileConnector;
import eu.ginere.base.util.file.FileConnectorInterface;
import eu.ginere.base.util.file.FileId;
import eu.ginere.base.util.file.FileInfo;
import eu.ginere.base.util.image.ImageSize.Dimension;
import eu.ginere.base.util.image.ImageSize.FormatNotSuported;
import eu.ginere.base.util.image.ImageSize.ImageSizeException;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


/*
 * This is for images stored into the system as files
 * 
 * @author Angel Mendo
 * @version $Revision: 1.1 $
 */
public class ImageUtils {
	private static Logger log = Logger.getLogger(ImageUtils.class);

	
	protected static final float JPEG_QUALITY=(float)0.6;

//	/**
//	 *  Uses the oldThumbnail as a file if possible
//	 */
//	public static FileId updateOrCreateThumbnail(FileId oldThumbnail,FileId image,int maxWidth,int maxHeight)throws DaoManagerException,IOException{
//		if (!ImageSize.isImage(image)){
//			return null;
//		} else {
//			try {
//				int newWidth=ImageSize.getResizedWidth(image,maxWidth,maxHeight);
//				int newHeight=ImageSize.getResizedHeight(image,maxWidth,maxHeight);
//				
//				BufferedImage buffer=createImage(image);
//				BufferedImage result=scaleImage(buffer,newWidth,newHeight);
//				return updateImage(oldThumbnail,image,result);
//			}catch (ImageSizeException e) {
//				throw new DaoManagerException("FileName:"+image, e);
//			}catch (FormatNotSuported e) {
//				throw new DaoManagerException("FileName:"+image, e);
//			}
//		}
//	}
	
//	/**
//	 * 
//	 * @param image
//	 * @param maxWidth
//	 * @param maxHeight
//	 * @return null if the thumbnail can't be created
//	 * @throws FileManagerException
//	 * @throws IOException
//	 */
//	public static FileId createThumbnail(FileId image,int maxWidth,int maxHeight)throws DaoManagerException,IOException{
//		return updateOrCreateThumbnail(null,image,maxWidth,maxWidth);
//	}
	
//	/**
//	 * Resize the image into this file, if any
//	 * @param file The file representing the image to transform
//	 * @param width maxWidth, maxHeight
//	 * @param height
//	 */
//	public static void resizeImage(FileId image,int maxWidth,int maxHeight)throws DaoManagerException,IOException{
//		if (!ImageSize.isImage(image)){
//			return ;
//		} else {
//			try {
//				int newWidth=ImageSize.getResizedWidth(image,maxWidth,maxHeight);
//				int newHeight=ImageSize.getResizedHeight(image,maxWidth,maxHeight);
//				
//				BufferedImage buffer=createImage(image);
//				BufferedImage result=scaleImage(buffer,newWidth,newHeight);
//				writeImage(image,result);
//			}catch (ImageSizeException e) {
//				throw new DaoManagerException("FileName:"+image, e);
//			}catch (FormatNotSuported e) {
//				throw new DaoManagerException("FileName:"+image, e);
//			}
//		}
//	}
	
	/**
	 * Resize the image into this file, if any
	 * @param file The file representing the image to transform
	 * @param width maxWidth, maxHeight
	 * @param height
	 */
	public static FileId createImage(String fileName,String mimeType,String userId,File file,int maxWidth,int maxHeight)throws DaoManagerException,IOException{
		if (!ImageSize.isImage(mimeType)){
			return null;
		} else  {
			try {
				Dimension d = ImageSize.getDimensions(fileName,mimeType,file);
	
				
				int newWidth=ImageSize.getResizedWidth(d,maxWidth,maxHeight);
				int newHeight=ImageSize.getResizedHeight(d,maxWidth,maxHeight);
				
				FileInputStream input=new FileInputStream(file);
				try {
					BufferedImage buffer=createImage(input);
					BufferedImage result=scaleImage(buffer,newWidth,newHeight);
					return createImage(fileName, mimeType, userId, result);
				}finally{
					input.close();
				}
			}catch (ImageSizeException e) {
				throw new DaoManagerException("FileName:"+fileName, e);
			}catch (FormatNotSuported e) {
				throw new DaoManagerException("FileName:"+fileName, e);
			}
		}
	}

	public static FileId createImage(String fileName,String mimeType,String userId,byte bytes[],int maxWidth,int maxHeight)throws DaoManagerException,IOException{
		if (!ImageSize.isImage(mimeType)){
			return null;
		} else  {
			try {
				ByteArrayInputStream inputDim=new ByteArrayInputStream(bytes);
				Dimension d = ImageSize.getDimensions(fileName,mimeType,inputDim);
//				IOUtils.closeQuietly(inputDim);
				
				int newWidth=ImageSize.getResizedWidth(d,maxWidth,maxHeight);
				int newHeight=ImageSize.getResizedHeight(d,maxWidth,maxHeight);
				
				ByteArrayInputStream input=new ByteArrayInputStream(bytes);
				try {
					BufferedImage buffer=createImage(input);
					BufferedImage result=scaleImage(buffer,newWidth,newHeight);
					return createImage(fileName, mimeType, userId, result);
				}finally{
					input.close();
				}
			}catch (ImageSizeException e) {
				throw new DaoManagerException("FileName:"+fileName, e);
			}catch (FormatNotSuported e) {
				throw new DaoManagerException("FileName:"+fileName, e);
			}
		}
	}
	
	public static byte[] createImage(String fileName,String mimeType,byte bytes[],int maxWidth,int maxHeight)throws DaoManagerException,IOException{
		if (!ImageSize.isImage(mimeType)){
			return null;
		} else  {
			try {
				ByteArrayInputStream inputDim=new ByteArrayInputStream(bytes);
				Dimension d = ImageSize.getDimensions("memoryFile",mimeType,inputDim);
				
				int newWidth=ImageSize.getResizedWidth(d,maxWidth,maxHeight);
				int newHeight=ImageSize.getResizedHeight(d,maxWidth,maxHeight);
				
				ByteArrayInputStream input=new ByteArrayInputStream(bytes);
				try {
					BufferedImage buffer=createImage(input);
					BufferedImage result=scaleImage(buffer,newWidth,newHeight);
					
					ByteArrayOutputStream out=new ByteArrayOutputStream();
					ImageUtils.write(mimeType, result,out);
					
					return out.toByteArray();
				}finally{
					IOUtils.closeQuietly(input);
					IOUtils.closeQuietly(inputDim);
				}
			}catch (ImageSizeException e) {
				throw new DaoManagerException("mimeType:"+mimeType, e);
			}catch (FormatNotSuported e) {
				throw new DaoManagerException("mimeType:"+mimeType, e);
			}
		}
	}
//	/**
//	 */
//	public static void rotate90CCw(FileId image)throws DaoManagerException,IOException{
//		if (ImageSize.isImage(image)){
//			BufferedImage buffer=createImage(image);
//			BufferedImage result=rotate90CCw(buffer);
//			writeImage(image,result);
//		}
//		System.gc();
//	}

//	/**
//	 */
//	public static void rotate90Cw(FileId image)throws DaoManagerException,IOException{
//		if (ImageSize.isImage(image)){
//			BufferedImage buffer=createImage(image);
//			BufferedImage result=rotate90Cw(buffer);
//			writeImage(image,result);
//		}
//		System.gc();
//	}


	static private BufferedImage rotate90CCw(BufferedImage bi) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		
		int destWidth = bi.getHeight();
		int destHeight = bi.getWidth();
		
		
		ColorModel cm=bi.getColorModel();
		BufferedImage destBi;
		
		if (cm instanceof IndexColorModel){
			destBi = new BufferedImage(destWidth, destHeight, bi.getType(),(IndexColorModel)cm);
			Raster source=bi.getData();
			WritableRaster dest=destBi.getRaster();
				
			// double array[]=new double[16];
				
			for(int i=0; i<width; i++){
				for(int j=0; j<height; j++){
					// biFlip.setRGB(height-1-j, width-1-i, bi.getRGB(i, j));
					// biFlip.setRGB(height-1-j, i, bi.getRGB(i, j));
						
					// dest.setRGB(j, width-i-1, bi.getRGB(i, j));
						
					// source.getPixel(i,j,array);
					// dest.setPixels(j, width-i-1, 1, 1, array);
					Object data=source.getDataElements(i,j, null);
					dest.setDataElements(j, width-i-1,data);
				}
			}
		} else {
			destBi = new BufferedImage(destWidth, destHeight, bi.getType());
			for(int i=0; i<width; i++){
				for(int j=0; j<height; j++){
					destBi.setRGB(j, width-i-1, bi.getRGB(i, j));
				}
			}
		}
			
		return destBi;
	}
	
	static private BufferedImage rotate90Cw(BufferedImage bi) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		
		int destWidth = bi.getHeight();
		int destHeight = bi.getWidth();
		
		ColorModel cm=bi.getColorModel();
		BufferedImage destBi;
		
		if (cm instanceof IndexColorModel){
			destBi = new BufferedImage(destWidth, destHeight, bi.getType(),(IndexColorModel)cm);
			Raster source=bi.getData();
			WritableRaster dest=destBi.getRaster();
				
			// double array[]=new double[16];
				
			for(int i=0; i<width; i++){
				for(int j=0; j<height; j++){
					// biFlip.setRGB(height-1-j, width-1-i, bi.getRGB(i, j));
					// biFlip.setRGB(height-1-j, i, bi.getRGB(i, j));
						
					// dest.setRGB(j, width-i-1, bi.getRGB(i, j));
						
					// source.getPixel(i,j,array);
					// dest.setPixels(j, width-i-1, 1, 1, array);
					Object data=source.getDataElements(i,j, null);
					dest.setDataElements(height-1-j, i,data);
				}
			}				
		} else {
			destBi = new BufferedImage(destWidth, destHeight, bi.getType());
			for(int i=0; i<width; i++){
				for(int j=0; j<height; j++){
					destBi.setRGB(height-1-j, i, bi.getRGB(i, j));
				}
			}
		}
			
		return destBi;
	}

	static private BufferedImage rotate90CW_(BufferedImage bi) {
		int width=bi.getWidth();
		int height=bi.getHeight();
		int destWidth=bi.getHeight();
		int destHeight=bi.getWidth();
		BufferedImage dest=new BufferedImage(destWidth,destHeight,bi.getType());
		
		
		//AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(90));
		//[ x']   [  m00  m01  m02  ] [   cos(theta)    -sin(theta)    0   ]
		//[ y'] = [  m10  m11  m12  ]=[   sin(theta)     cos(theta)    0   ] 
		//[ 1 ]   [   0    0    1   ] [       0              0         1   ]
		
		// [   cos(theta)    -sin(theta)    x-x*cos+y*sin  ]
		// [   sin(theta)     cos(theta)    y-x*sin-y*cos  ]
		// [       0              0               1        ]

		AffineTransform tx = new AffineTransform(0,1, -1, 0,width/2+height/2,height/2-width/2); 		
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		op.filter(bi, dest);
	    
		return dest;
	}

	static private BufferedImage scaleImage(BufferedImage image,int width,int height) {
		java.awt.Image convertedImage= image.getScaledInstance(width, height,java.awt.Image.SCALE_SMOOTH);

		BufferedImage bi = new BufferedImage(convertedImage.getWidth(null),
                							convertedImage.getHeight(null), 
											BufferedImage.TYPE_INT_RGB);
        Graphics2D big = bi.createGraphics();
        big.drawImage(convertedImage, 0, 0, null);

        return bi;
		
	}

	private static void writeImage(final FileId file,final BufferedImage image)throws DaoManagerException,IOException{
		FileConnector.update(file.getId(), new FileConnectorInterface.FileStreamWriter(){
			  public void write(OutputStream out)throws IOException, DaoManagerException{
				  ImageUtils.write(file.getMimeType(), image,out);
			  }
		  });
	}
	

	private static FileId updateImage(final FileId oldFile,final FileId file,final BufferedImage image)throws DaoManagerException,IOException{
		if (oldFile==null || !oldFile.isValide()){
			return createImage(file,image);
		} else {
			FileConnector.update(oldFile.getId(),
						new FileConnectorInterface.FileStreamWriter(){
					public void write(OutputStream out)throws IOException, DaoManagerException{
						ImageUtils.write(file.getMimeType(), image,out);
					}
				});
				
			return oldFile;	
//				
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			ImageUtils.write(file, image, out);
//			FileInfo info = FileConnector.get(file.getId());
//
//			byte[] contentFile = out.toByteArray();
//			String id = FileConnector.update(oldFile.getId(), contentFile);
//			return oldFile;
		}
	}

	private static FileId createImage(String fileName,final String mimeType,String userId,final BufferedImage image)throws DaoManagerException,IOException{
		
		String id= FileConnector.insert(fileName,mimeType,userId,
											  new FileConnectorInterface.FileStreamWriter(){
													  public void write(OutputStream out)throws IOException, DaoManagerException{
														  ImageUtils.write(mimeType, image,out);
													  }
												  });
		return FileId.getInstance(id, userId);
	}
	private static FileId createImage(final FileId file,final BufferedImage image)throws DaoManagerException,IOException{
		FileInfo info = FileConnector.get(file.getId());
		
		String id=  FileConnector.insert(info.getName(),info.getMimeType(),info.getUserId(),
											  new FileConnectorInterface.FileStreamWriter(){
													  public void write(OutputStream out)throws IOException, DaoManagerException{
														  ImageUtils.write(file.getMimeType(), image,out);
													  }
												  });
		
		return FileId.getInstance(id);
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		ImageUtils.write(file, image, out);
//		FileInfo info = FileConnector.get(file.getId());
//
//		byte[] contentFile = out.toByteArray();
//		String id = FileConnector.insert(info.getName(), contentFile,
//				info.getMimeType(), info.getUserId());
//
//		return FileId.getInstance(id);
	}

//	/**
//	 * @throws DaoManagerException 
//	* 
//	*/
//	private static BufferedImage createImage(FileId file)throws IOException, DaoManagerException{
//		InputStream input=file.getInputStream();
//		return ImageIO.read(input);  
//	}

	private static BufferedImage createImage(InputStream input) throws IOException{
		return ImageIO.read(input);  
	}
	static void write(String mimeType, BufferedImage image,OutputStream out)throws IOException, DaoManagerException{
		// String array[]=ImageIO.getWriterFormatNames();
		
		if (ImageSize.isJPEG(mimeType)){
			JPEGImageEncoder encoder= JPEGCodec.createJPEGEncoder(out); 
			JPEGEncodeParam param =encoder.getDefaultJPEGEncodeParam(image);
			param.setQuality(JPEG_QUALITY,true);
			encoder.encode(image,param); 		
			// ImageIO.write(image,"jpeg",out);	
		} else if (ImageSize.isGIF(mimeType)){	
			// there is no encoder for GIF into the j2ee ussing the local one
			try {
				GifEncoder encoder=new GifEncoder(image);
				encoder.Write(out);
			}catch (AWTException e){
				log.error("While encding gif file",e);
				throw new IOException("While encding gif file");
			}
			// ImageIO.write(image,"gif",out);
		} else if (ImageSize.isPNG(mimeType)){
			ImageIO.write(image,"png",out);
		}
	}


	static public void encodeJPG(BufferedImage image,OutputStream out,float jpegQuality)throws IOException{
		JPEGImageEncoder encoder= JPEGCodec.createJPEGEncoder(out); 
		JPEGEncodeParam param =encoder.getDefaultJPEGEncodeParam(image);
		param.setQuality(jpegQuality,true);
		encoder.encode(image,param); 				
	}	
}
