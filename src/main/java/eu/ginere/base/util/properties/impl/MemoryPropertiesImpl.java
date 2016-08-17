package eu.ginere.base.util.properties.impl;

import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.ginere.base.util.properties.PropertiesInterface;
import eu.ginere.base.util.properties.PropertyDescriptor;
import eu.ginere.base.util.properties.SetPropertiesInterface;
import eu.ginere.base.util.test.TestResult;

public class MemoryPropertiesImpl implements SetPropertiesInterface{

	private static  Logger log = Logger.getLogger(MemoryPropertiesImpl.class);
	
	static public MemoryPropertiesImpl SINGLETON= new MemoryPropertiesImpl();

	private Hashtable<String, PropertyDescriptor>cache=new Hashtable<String, PropertyDescriptor>();

	private long lastModified=System.currentTimeMillis();

	private Hashtable<String, PropertyDescriptor> descriptorCache=new Hashtable<String, PropertyDescriptor>();
	
	private MemoryPropertiesImpl(){
	}

	@Override
	public TestResult test() {
		TestResult ret=new TestResult(PropertiesInterface.class);
		
		log.debug("Testing memory properties impl...");
		return ret;	
	}

	@Override
	public void setValue(PropertyDescriptor desc,String value) {
		if (desc == null){
			return;
		} else if (cache.containsKey(desc.name)) {
			PropertyDescriptor oldDesc=descriptorCache.get(desc.name);
			oldDesc.update(value);
		} else {
			desc.update(value);;
			descriptorCache.put(desc.name,desc);
		}
	}

	@Override
	public long getLastModified() {
		return lastModified;
	}

	@Override
	public Map<String, PropertyDescriptor> getAll() {
		return cache;
	}
}
