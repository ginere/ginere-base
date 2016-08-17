package eu.ginere.base.util.properties.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import eu.ginere.base.util.dao.DaoManagerException;
import eu.ginere.base.util.properties.PropertiesInterface;
import eu.ginere.base.util.properties.PropertyDescriptor;
import eu.ginere.base.util.test.TestResult;

public class FilePropertiesImpl implements PropertiesInterface{
	
	private static  Logger log = Logger.getLogger(FilePropertiesImpl.class);
	
	final File fildes;
	private long lastLoaded=0;
	private final Hashtable<String, PropertyDescriptor> cache=new Hashtable<String, PropertyDescriptor>();
	

	public FilePropertiesImpl(File file) throws DaoManagerException{
		if (file==null){
			throw new DaoManagerException("The file passed is null");
		} else if (!file.exists()){
			throw new DaoManagerException("The file:"+file.getAbsolutePath()+" does not exists.");
		} else if (!file.canRead()){
			throw new DaoManagerException("Can not read the file:"+file.getAbsolutePath());			
		} else {
			this.fildes=file;
			load();
		}
	}

	protected String getCharSet() {
		return "UTF-8";
	}

	synchronized private void load() throws DaoManagerException  {
		if (lastLoaded == fildes.lastModified()){
			return ;
		} else {
			try {
				InputStream in = new FileInputStream(fildes);
				try {
					lastLoaded=fildes.lastModified();
					Properties props = new Properties();
					props.load(in);
	
					// add properties to the cache
					cache.clear();
					Enumeration e = props.propertyNames();
					while (e.hasMoreElements()) {
						String key = (String) e.nextElement();
						String value = (String)props.get(key);
						
						cache.put(key,new PropertyDescriptor(key, key, value, true));
					}
				}finally {
					IOUtils.closeQuietly(in);
				}
			}catch(IOException e){
				throw new DaoManagerException("While loading properties from file:"+fildes.getAbsolutePath(),e);
			}
		}
	}

	@Override
	public long getLastModified() {
		return fildes.lastModified();
	}

	@Override
	public TestResult test() {
		TestResult ret=new TestResult(FilePropertiesImpl.class);

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

	@Override
	public Map<String,PropertyDescriptor> getAll() throws DaoManagerException {
		load();
		return cache;
	}

	
}
