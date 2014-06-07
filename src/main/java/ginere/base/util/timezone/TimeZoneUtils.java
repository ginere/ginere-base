package ginere.base.util.timezone;

import ginere.base.util.properties.GlobalFileProperties;

import java.util.TimeZone;

import org.apache.log4j.Logger;

public class TimeZoneUtils {
	static final Logger log = Logger.getLogger(TimeZoneUtils.class);
	
	private static TimeZone defaultTimeZone=null;

	public static TimeZone getTimeZone(String id){
		TimeZone tz=TimeZone.getTimeZone(id);
		
		if (tz==null){
			return getDefaultTimeZone();
		} else {
			return tz;
		}
	}

	private static TimeZone getDefaultTimeZone() {
		if (defaultTimeZone==null){
			String id=GlobalFileProperties.getStringValue(TimeZoneUtils.class, "DefaultTimeZone");
			defaultTimeZone=TimeZone.getTimeZone(id);
			
			if (defaultTimeZone==null){
				log.error("DEfault time zone id:"+id+" is unknown");
				defaultTimeZone=TimeZone.getTimeZone("GMT");
			}
			
		}
		
		return defaultTimeZone;
	}
}
