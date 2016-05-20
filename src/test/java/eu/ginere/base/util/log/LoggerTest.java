package eu.ginere.base.util.log;

import junit.framework.TestCase;

import org.junit.Test;

public class LoggerTest extends TestCase {

	@Test
	static public void testPerf() throws Exception {
		int NUMBER=1000000;
		String MESSAGE="This is a test message";
		long time;
		
		org.apache.log4j.Logger log1 = org.apache.log4j.Logger.getLogger(LoggerTest.class);
		time=System.currentTimeMillis();

		for (int i=0;i<NUMBER;i++){
			log1.error(MESSAGE);
		}

		long finalTime1=System.currentTimeMillis()-time;
		
		
		LoggerInterface log2 = Logger4j.getLogger(LoggerTest.class);
		time=System.currentTimeMillis();

		for (int i=0;i<NUMBER;i++){
			log2.error(MESSAGE);
		}

		long finalTime2=System.currentTimeMillis()-time;

		LoggerInterface log4 = Logger.getLogger(LoggerTest.class);
		time=System.currentTimeMillis();

		for (int i=0;i<NUMBER;i++){
			log4.error(MESSAGE);
		}

		long finalTime4=System.currentTimeMillis()-time;
		
		// 5
		Logger log5 = Logger.getLogger(LoggerTest.class);
		log5.setThreadLocal(Logger.getLogger(Object.class));
		time=System.currentTimeMillis();

		for (int i=0;i<NUMBER;i++){
			log5.error(MESSAGE);
		}

		long finalTime5=System.currentTimeMillis()-time;
		
		// 6
		org.apache.log4j.Logger log6 = org.apache.log4j.Logger.getLogger(LoggerTest.class);
		time=System.currentTimeMillis();

		for (int i=0;i<NUMBER;i++){
			log6.error(MESSAGE);
		}

		long finalTime6=System.currentTimeMillis()-time;
		
		System.err.println("Time:"+finalTime1);
		System.err.println("Time:"+finalTime2);
		System.err.println("Time:"+finalTime4);				
		System.err.println("Time:"+finalTime5);		

		System.err.println("Time:"+finalTime6);		

	}


}
