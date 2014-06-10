package eu.ginere.base.util.lang;

import java.io.IOException;

import org.apache.log4j.Logger;

public class Exec {
	static Logger log = Logger.getLogger(Exec.class);
	
	
	static public boolean execCommand(String command) throws IOException{
		Runtime rt = Runtime.getRuntime();
//		rt.traceInstructions(true);
//		rt.traceMethodCalls(true);
		
		Process pr = rt.exec(command);

		try {
			pr.waitFor();
		} catch (InterruptedException e) {
//			log.info("Exec DONE:'"+command+"'",e);			
		}
//
//		IOUtils.copy(pr.getErrorStream(), System.out);
//		IOUtils.copy(pr.getInputStream(), System.err);
		

//		if (log.isInfoEnabled()){
//			String ret=(pr.exitValue()==1)?"FAILS[1]":"OK["+pr.exitValue()+"]";
//			log.info("Exec DONE:'"+command+"' status:"+ret);
//		}

		if (pr.exitValue() == 0){
			return true;
		} else {
			return false;
		}
	}
}
