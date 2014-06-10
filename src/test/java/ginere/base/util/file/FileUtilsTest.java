package ginere.base.util.file;

import eu.ginere.base.util.file.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;


public class FileUtilsTest extends TestCase{

	private static final String FILE_PROPERTY_PATH = "src/test/resources/test.properties";

	static final Logger log = Logger.getLogger(FileUtilsTest.class);

	@Test
	static public void testConsulta() throws Exception {
		try {
			File orign=createFile("/tmp/orig.txt",100*1024);
			
			File dest=new File("/tmp/dest.txt");
		
			FileUtils.renameTo( orign, dest);
			
			for (int i=0;i<100;i++){
				final int counter=i;
				Thread thread=new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							log.debug("Start:"+counter);
//							File orign=createFile("/tmp/orig.txt",100*1024);
							File orign=createFile("/tmp/orig"+counter+".txt",100*1024);
							File dest=new File("/tmp/dest.txt");
							

							FileUtils.renameTo( orign, dest);
						} catch (Exception e) {
							log.error(""+counter, e);
						}
						log.debug("End:"+counter);
					}
				});
				thread.setDaemon(false);
				log.debug("Is daemon:"+thread.isDaemon());
				
				thread.start();
				
			}
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		
		
		Thread.sleep(100000);
		
		log.debug("The End");
	}
	
	
	static public File createFile(String name,long size) throws IOException{
		File file=new File(name);
		
		FileOutputStream out=new FileOutputStream(file);
		
		for (long i=0;i<size;i++){
			out.write('b');
		}
		
		IOUtils.closeQuietly(out);
		
		return file;
	}
}
