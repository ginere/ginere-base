package eu.ginere.base.util.i18n;

import eu.ginere.base.util.descriptor.annotation.Description;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * @author Angel-Ventura Mendo
 * @version $Revision:$ $Date:$
 */
@SuppressWarnings("serial")
public class Language implements Serializable{
	
	@Description
	private final String id;
	
	@Description
	public final String name;
	
	public final String langId;
	public final Locale locale;

	public final boolean debug;
	
	public static final Language DEBUG=new Language();
	public static final Language EN=new Language(Locale.ENGLISH);

	public static final char SEPARATOR_CHAR ='_';
	
	public static Language createFromId(String id,char separator) {
		
		if (id==null){
			return null; 
		} else {
			String array[]=StringUtils.split(id,separator);
		
			if (array.length==0){
				return null;
			} else if (array.length==1){
				Locale locale=new Locale(array[0].trim());
				
				return new Language(locale);
			} else if (array.length==2){
				Locale locale=new Locale(array[0].trim(),array[1].trim());
				
				return new Language(locale);
			} else if (array.length>=3){
				Locale locale=new Locale(array[0].trim(),array[1].trim(),array[2].trim());
				
				return new Language(locale);
			} else {
				return null;
			}
		}
	}
	
	public static Language createFromId(String id) {
		
		if (id==null){
			return null; 
		} else {
			String array[]=StringUtils.split(id,SEPARATOR_CHAR);
		
			if (array.length==0){
				return null;
			} else if (array.length==1){
				Locale locale=new Locale(array[0]);
				
				return new Language(locale);
			} else if (array.length==2){
				Locale locale=new Locale(array[0],array[1]);
				
				return new Language(locale);
			} else if (array.length>=3){
				Locale locale=new Locale(array[0],array[1],array[2]);
				
				return new Language(locale);
			} else {
				return null;
			}
		}
	}

	private Language(){
		this.langId="xx";
		this.name="Debug";
		this.locale=Locale.US;
		this.debug=true;
		String variant=locale.getVariant();
		
		if (variant!=null && !"".equals(variant)){
			this.id=this.langId+SEPARATOR_CHAR+locale.getCountry()+SEPARATOR_CHAR+variant;
		} else {
			this.id=this.langId+SEPARATOR_CHAR+locale.getCountry();
		}
	}

//	Language(String langId,String name,Locale locale){
//		this.langId=langId;
//		this.name=name;
//		this.locale=locale;
//		this.debug=false;
//	}
	
	public Language(Locale locale){
		this.langId=locale.getLanguage();
		
		if ("".equals(locale.getDisplayCountry(locale))){
			this.name=locale.getDisplayLanguage(locale);
		} else {
			this.name=locale.getDisplayLanguage(locale)+"-"+locale.getDisplayCountry(locale);
		}
		this.locale=locale;
		this.debug=false;
		
		String variant=locale.getVariant();
		if (variant!=null && !"".equals(variant)){
			this.id=this.langId+SEPARATOR_CHAR+locale.getCountry()+SEPARATOR_CHAR+variant;
		} else {
			this.id=this.langId+SEPARATOR_CHAR+locale.getCountry();
		}
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Locale getLocale() {
		return locale;
	}
	
	public String getLanguageId() {
		return langId;
	}

	public String getCountryId() {
		return locale.getCountry();
	}
	
	public String toString() {
		return name;
	}
	
	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	public static boolean equals(Language l1, Language l2) {
		if (l1==l2){
			return true;
		} else if (l1==null){
			if (l2==null){
				return true;
			} else {
				return false;
			}
		} else if (l2==null){
			return false;
		} else {
			return StringUtils.equals(l1.id, l2.id);
		}
	}

	public boolean equals(Language l2) {
		return equals(this,l2);
	}

}
