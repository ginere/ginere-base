package eu.ginere.base.util.notification;

import java.util.List;

import eu.ginere.base.util.enumeration.SQLEnum;

public class Level extends SQLEnum{
	
	private static final long serialVersionUID = 1L;

	public static final int FATAL_LEVEL = 0;
	public static final int ERROR_LEVEL = FATAL_LEVEL+1;
	public static final int WARN_LEVEL = ERROR_LEVEL+1;
	public static final int INFO_LEVEL = WARN_LEVEL+1;
	public static final int DEBUG_LEVEL = INFO_LEVEL+1;
	
	public static final Level FATAL = new Level("FATAL",FATAL_LEVEL);
	public static final Level ERROR = new Level("ERROR",ERROR_LEVEL);
	public static final Level WARN = new Level("WARN",WARN_LEVEL);
	public static final Level INFO = new Level("INFO",INFO_LEVEL);
	public static final Level DEBUG = new Level("DEBUG",DEBUG_LEVEL);

	public final int level;

	private Level(String id,int level){
		super(id,id,id);
		this.level=level;
	}

	public static Level value(String value) {
		return  (Level)SQLEnum.value(Level.class, value);			
	}

	public static List<SQLEnum> values() {
		return SQLEnum.values(Level.class);
	}


}
