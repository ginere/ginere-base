package eu.ginere.base.util.properties.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.properties.PropertiesInterface;
import eu.ginere.base.util.properties.PropertyDescriptor;
import eu.ginere.base.util.test.TestResult;

/**
 * @author ventura
 *
 * This is not yet finish but allows to read an write into file properties
 */
public class FilePropertiesReaderWriterImpl implements PropertiesInterface{
	
	private static  Logger log = Logger.getLogger(FilePropertiesReaderWriterImpl.class);
	

	public static final Pattern COMMENT=Pattern.compile("^#\\s*(.*)$");
	public static final Pattern PROPERTY=Pattern.compile("^(\\w+)\\s*=\\s*(.*)$");

	public static final Pattern QUOTED_STRING=Pattern.compile("^\"(.+)\"$");
	public static final Pattern SIMPLE_QUOTED_STRING=Pattern.compile("^\'(.+)\'$");

	public static final String FILE_HEADER = "#!/usr/bin/bash";


	class Block{
		private static final char NEW_LINE = '\n';
		
		List <String>lines=new ArrayList<String>();
		StringBuilder description=new StringBuilder();
		boolean isInValue=false;
		String propertyName=null;
		String value=null;
		
		
		void addBlannkLine(String line) {
			lines.add(line);
		}

		public void addSpetialLine(String line) {
			lines.add(line);
		}
		
		public void addComment(String commentLine) {
			description.append(commentLine).append(NEW_LINE);			
		}

		public PropertyDescriptor getPropertyDescriptor(String propertyName,
				String propertyValue) {
			PropertyDescriptor descriptor=new PropertyDescriptor(propertyName, description.toString(),propertyValue,true);
			return descriptor;
		}

		public boolean isInvalue() {			
			return isInValue;
		}

		public void setIsInValue(boolean b) {
			this.isInValue=b;			
		}

		public void addPropertyName(String propertyName) {
			this.propertyName=propertyName;			
		}

		public void addValue(String line) {
			if (this.value==null){
				this.value=line;
			} else {
				this.value+=line;
			}
		}

		public PropertyDescriptor getPropertyDescriptor() {
			if (propertyName == null) {
				return null;
			} else {
				return new PropertyDescriptor(propertyName, description.toString(), value,true);
			}
		}
	};

	class FileBuilder {		
		Vector <Block> blockList=new Vector<Block>();

		private Block getCurrent() {
			if (blockList.isEmpty()){
				Block block=new Block();
				blockList.add(block);
				return block;
			} else {
				return blockList.lastElement();
			}
		}

		private void pushNewBlock() {
			Block block=new Block();
			blockList.add(block);
		}

		public void start() {
			blockList.clear();			
		}

		public boolean isInValue() {
			Block block=getCurrent();
			
			return block.isInvalue();
		}	
		
		public void addValue(String line) {
			Block block=getCurrent();
			
			if (!StringUtils.isBlank(line) && line.endsWith("\\")){
				String lineToAdd=line.substring(0, line.length()-1);
				block.addValue(lineToAdd);
				block.setIsInValue(true);
			} else {
				block.addValue(line);
				block.setIsInValue(false);
				PropertyDescriptor desc=block.getPropertyDescriptor();
				cache.put(desc.name, desc);
				pushNewBlock();				
			}
		}

		public void addSpetialLine(String line) {
			Block block=getCurrent();
			block.addSpetialLine(line);			
		}
		
		public void addComment(String commentLine) {
			Block block=getCurrent();
			block.addComment(commentLine);
		}

		public void addProperty(String propertyName, String propertyValue) {
			Block block=getCurrent();
			block.addPropertyName(propertyName);
			addValue(propertyValue);
		}

		public void stop() {
			Block block=getCurrent();
			PropertyDescriptor desc=block.getPropertyDescriptor();
			if (desc!=null){
				cache.put(desc.name, desc);
			}
		}
	
	};
		

	final File fildes;
	private long lastLoaded=0;
	final FileBuilder builder=new FileBuilder();
	private final Hashtable<String, PropertyDescriptor> cache=new Hashtable<String, PropertyDescriptor>();
	

	public FilePropertiesReaderWriterImpl(File file) throws DaoManagerException{
		if (file==null){
			throw new DaoManagerException("The file passed is null");
		} else if (!file.exists()){
			throw new DaoManagerException("The file:"+file.getAbsolutePath()+" does not exists.");
		} else if (!file.canRead()){
			throw new DaoManagerException("Can not read the file:"+file.getAbsolutePath());			
		} else {
			this.fildes=file;
			try {
				load();
			}catch(IOException e){
				throw new DaoManagerException("While reading file:"+file.getAbsolutePath(),e);							
			}
		}
	}

	protected String getCharSet() {
		return "UTF-8";
	}

	synchronized private void load() throws IOException {
		// test if We have to reload
		long fildesLastModified=fildes.lastModified();
		
		if (lastLoaded==fildesLastModified) {
			log.debug("Already loaded file:"+fildes.getAbsolutePath());
			return;
		}

		builder.start();
		
		InputStream in = new FileInputStream(fildes);		
		InputStreamReader reader=new InputStreamReader(in,getCharSet());
		BufferedReader bufferedReader=new BufferedReader(reader);
		
		// set last loaded
		lastLoaded=fildesLastModified;

		try {
			String line;
			while ( (line=bufferedReader.readLine()) != null){				
				
				if (builder.isInValue()){
					builder.addValue(line);
				} else {
					Matcher matcher=PROPERTY.matcher(line);
					
					if (matcher.matches()){
						String propertyName=matcher.group(1);
						String propertyValue=matcher.group(2);
						
						builder.addProperty(propertyName,propertyValue);
					} else {
						matcher=COMMENT.matcher(line);
						if (matcher.matches()){
							String commentLine=matcher.group(1);
							builder.addComment(commentLine);
						} else {
							builder.addSpetialLine(line);
						}
					}										
				}

// 				if (StringUtils.isBlank(line)){
// 					builder.addBlannkLine(line);
// 				} else {
// 					Matcher matcher=COMMENT.matcher(line);
					
// 					if (matcher.matches()){
// 						String commentLine=matcher.group(1);
// 						builder.addComment(commentLine);
// 					} else {				
// 						matcher=PROPERTY.matcher(line);
						
// 						if (matcher.matches()){
// 							String propertyName=matcher.group(1);
// 							String propertyValue=matcher.group(2);
// 							// propertyValue=unEscapeFromFile(propertyValue);

// 							builder.addProperty(propertyName,propertyValue);
// 						} else {
// 							builder.addSpetialLine(line);
// 						}						
// 					}
// 				}
			}
			
			builder.stop();
		}finally{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(bufferedReader);
		}		

	}

	@Override
	public long getLastModified() {
		return fildes.lastModified();
	}

	@Override
	public TestResult test() {
		TestResult ret=new TestResult(FilePropertiesReaderWriterImpl.class);

		if (fildes==null){
			ret.addError("The fildes is null");
			return ret;
		} else {
			if (!fildes.exists()){
				ret.addError("The file :"+fildes.getAbsolutePath()+" does not exist.");
				return ret;				
			} else if (!fildes.canRead()){
				ret.addError("Ca not read de tile :"+fildes.getAbsolutePath()+".");
				return ret;								
			} else {
				return ret;
			}
		}	
	}

//	@Override
//	public void setValue(String propertyName, String description, String value) {
//		if (cache.containsKey(propertyName)){
//			PropertyDescriptor descriptor=cache.get(propertyName);
//			descriptor.update(description, value);
//		} else {
//			PropertyDescriptor descriptor=new PropertyDescriptor(propertyName,description,value);
//			cache.put(propertyName,descriptor);			
//		}
//		
//		// try to load if the file has been changed
//		load();
//		save();
//		
//	}


	@Override
	public Map<String,PropertyDescriptor> getAll() {
		return cache;
	}

	
}
