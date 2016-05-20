//package ginere.base.util.file;
//
//import java.io.File;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//import org.junit.Test;
//
//import eu.ginere.base.util.file.FileUtils;
////import eu.ginere.base.util.file.FileUtils.DirectoryWalker2;
//import eu.ginere.base.util.file.FileUtils.FileIterator;
//
//
//public class FileUtilsTest extends TestCase{
//
//	private static final String FILE_PROPERTY_PATH = "src/test/resources/test.properties";
//
//	static final Logger log = Logger.getLogger(FileUtilsTest.class);
//
//	static public int counter=0;
//
//	@Test
//	static public void testIterate() throws Exception {
//		File parentDir=new File("/Users/ventura/projects/video2/installation-root/data/repo/");
//		
//		 FileIterator ITERATOR=new FileIterator() {
//			 
//			@Override
//			public boolean iterate(File file) {
//				log.error("Iterating on file:"+file.getAbsolutePath());
//				setCounter();
//				return true;
//			}
//			
//		};
//		
//		DirectoryWalker2 ITERATOR3=new DirectoryWalker2() {			 
//			@Override
//			public boolean iterate(File file) {
//				log.error("Iterating on file:"+file.getAbsolutePath());
//				setCounter();
//				return true;
//			}
//			
//		};
//
//		long time=System.currentTimeMillis();
//		FileUtils.iterateOnChildDirs(parentDir,ITERATOR);
////		FileUtils.iterateOnChildDirs2(parentDir,ITERATOR);
////		FileUtils.iterateOnChildDirs3(parentDir,ITERATOR3);
//				
//		log.error("COUNTER:"+counter+" Time:"+(System.currentTimeMillis()-time));
//
//	}
//	
//	static public void setCounter(){
//		counter++;
//	}
//
////	@Test
////	static public void testConsulta() throws Exception {
////		try {
////			File orign=createFile("/tmp/orig.txt",100*1024);
////			
////			File dest=new File("/tmp/dest.txt");
////		
////			FileUtils.renameTo( orign, dest);
////			
////			for (int i=0;i<100;i++){
////				final int counter=i;
////				Thread thread=new Thread(new Runnable() {
////					
////					@Override
////					public void run() {
////						try {
////							log.debug("Start:"+counter);
//////							File orign=createFile("/tmp/orig.txt",100*1024);
////							File orign=createFile("/tmp/orig"+counter+".txt",100*1024);
////							File dest=new File("/tmp/dest.txt");
////							
////
////							FileUtils.renameTo( orign, dest);
////						} catch (Exception e) {
////							log.error(""+counter, e);
////						}
////						log.debug("End:"+counter);
////					}
////				});
////				thread.setDaemon(false);
////				log.debug("Is daemon:"+thread.isDaemon());
////				
////				thread.start();
////				
////			}
////		} catch (Exception e) {
////			log.error("", e);
////			throw e;
////		}
////		
////		
////		Thread.sleep(100000);
////		
////		log.debug("The End");
////	}
////	
////	
////	static public File createFile(String name,long size) throws IOException{
////		File file=new File(name);
////		
////		FileOutputStream out=new FileOutputStream(file);
////		
////		for (long i=0;i<size;i++){
////			out.write('b');
////		}
////		
////		IOUtils.closeQuietly(out);
////		
////		return file;
////	}
//}
