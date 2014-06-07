package ginere.base.util.i18n;

import ginere.base.util.dao.DaoManagerException;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author ventura
 *
 * Gestiona el multilinguismo.
 * Se puede guardar la lengua activa en una variable thread local. Puede usuarse para los servlets.
 */
public class I18NConnector {
	static Logger log = Logger.getLogger(I18NConnector.class);
	
	final private static ThreadLocal<Language> threadLocalLanguage=new ThreadLocal<Language>();

//	private static final String DEBUG_LANGUAGE_ID="xx";
//	private static final Language DEBUG_LANGUAGE=new Language(DEBUG_LANGUAGE_ID,"Debug",new Locale("xx","XX"));
//	private static final Locale DEBUG_LOCALE=Locale.US;

//	private static final Map<String,Locale> localeCache=new Hashtable<String,Locale>();
	

	private static Language languageArrayIncludeDepuracion[]=null;

	private static I18NConnectorInterface connector=new DummyI18NConnector();
	
	public static void init(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		log.info("I18NConnector tryng to initialize width:'"+className+"'");
		
		if (className==null){
			log.info("not classNammed passed. ussing default connector");
			return ;
		}
		
		Class<?> clazz=Class.forName(className);
		connector=(I18NConnectorInterface)clazz.newInstance();
		log.info("I18NConnector initialized with class:'"+className+"'");
	}


	/**
	 * Returns the value for this label (setion,idInSection) for this lang
	 * @param lang
	 * @param sectionClass
	 * @param isInSection
	 * @return
	 */
	public static String getLabel(Language lang,
								  Class<?> sectionClass,
								  String isInSection) {
		return getLabel(lang, sectionClass.getName(),  isInSection);
	}
	
	/**
	 * Returns the valur for this label (section|idInsection) for this lang
	 * @param lang
	 * @param section
	 * @param idInSection
	 * @return
	 */
	public static String getLabel(Language lang, String section, String idInSection) {
		if (idInSection==null || "".equals(idInSection)){
			log.warn("Empty label for section:"+section+" and lang:"+lang);
			return "";
		} else if (lang.isDebug()){
			return lang.getLanguageId()+"|"+idInSection+"|"+section;
		} else {
			return connector.i18nLabel(lang, section, idInSection);
		}
	}
	
	/**
	 * Returns the value for this label ussing the getThreadLocaleLanguage
	 * 
	 * @param section
	 * @param idInSection
	 * @return
	 */
	public static String getLabel(String section, String idInSection) {
		Language language=getThreadLocalLanguage(null);
		
		if (language==null){
			return idInSection;
		} else {
			return getLabel(language, section, idInSection);
		}
	}
	
	/**
	 * Returns the value for this label ussing the getThreadLocaleLanguage
	 * 
	 * @param section
	 * @param idInSection
	 * @return
	 */
	public static String getLabel(Class<?> clazz, String label) {
		Language language=getThreadLocalLanguage(null);
		
		if (language==null){
			return label;
		} else {
			return getLabel(language, clazz.getName(), label);
		}
	}


	private static Language getThreadLocalLanguage(Language defaultValue) {
		Language language=threadLocalLanguage.get();

		if (language==null){
			return defaultValue;
		} else {
			return language;
		}
	}

	public static Language getThreadLocalLanguage() {
		Language language=threadLocalLanguage.get();

		if (language==null){
			return connector.getDefaultLanguage();
		} else {
			return language;
		}
	}

	public static Locale getThreadLocalLocale() {
		Language language=threadLocalLanguage.get();

		if (language==null){
			return connector.getDefaultLanguage().getLocale();
		} else {
			return language.getLocale();
		}
	}
	public static void setThreadLocalLanguage(Language language) {
		threadLocalLanguage.set(language);
	}


//	public static String getDefaultLangId() {
//		return connector.getDefaultLangId();
//	}

	public static Language getDefaultLanguage() {
		return connector.getDefaultLanguage();
	}

	public static Language[] getAvailablesLanguageList(){
		return connector.getAvailablesLanguageList();	
	}
	
	public static Language[] getAvailablesLanguageListIncludeDebug(){
		if (languageArrayIncludeDepuracion==null){
			synchronized (I18NConnector.class) {
				if (languageArrayIncludeDepuracion==null){
					Language array[]=getAvailablesLanguageList();
					
					languageArrayIncludeDepuracion=new Language[array.length+1];
					
					for (int i=0;i<array.length;i++){
						languageArrayIncludeDepuracion[i]=array[i];
					}
					languageArrayIncludeDepuracion[array.length]=Language.DEBUG;
				}
			}	
		}
		return languageArrayIncludeDepuracion;
	}

	public static void clearCache() {
		connector.clearCache();	
	}

//	public static boolean isAvailabelLang(String langId) {
//		Language[] langArray=getAvailablesLanguageListIncludeDebug();
//		
//		for (Language language:langArray){
//			if (StringUtils.equals(langId, language.getLanguageId())){
//				return true;
//			}
//		}
//		return false;
//	}

	
	public static boolean isAvailableLanguage(Language lang) {
//		Language[] langArray=getAvailablesLanguageListIncludeDebug();
//		
//		for (Language language:langArray){
//			if (Language.equals(language,lang)){
//				return true;
//			}
//		}
//		return false;		
		
		return connector.isAvailableLanguage(lang);
	}

	public static Language getLanguageFromLangId(String langId,Language defaultValue) {
		return connector.getLanguageFromLangId(langId,defaultValue);
	}
	
	public static Language getLanguageFromLanguageId(String languageId,Language defaultValue) {
		return connector.getLanguageFromLanguageId(languageId,defaultValue);
	}

//	public static Locale getLocale(String langId) {
//		if (DEBUG_LANGUAGE_ID.equals(langId)){
//			return DEBUG_LOCALE;
//		} else {
//			if (localeCache.containsKey(langId)){
//				return localeCache.get(langId);
//			} else {
//				Language language=getLanguage(langId);
//				
//				localeCache.put(langId,language.getLocale());
//
//				return language.getLocale();
//			}
//		}
//	}

	
	public static void setInitLabels(String[][] strings) {
		for (String array[]:strings){
			try {
				String langId=array[0];
				String section=array[1];
				String labelId=array[2];
				String value=array[3];
				
				Language lang=connector.getLanguageFromLangId(langId, null);
				if (lang!=null){
					connector.translate(lang,section,labelId,value);
				}
			}catch(Exception e){
				log.error(" Inserting array:'"+StringUtils.join(array,","),e
						  );
			}
		}	
	}
	

	/**
	 * Return the closest language to this one
	 * 
	 * @param language
	 * @return
	 */
	public static Language getClosestLanguage(Language language) {
		return connector.getClosestLanguage(language);
	}


	/**
	 * returns the closed language to one of this list
	 * @param list
	 * @return
	 */
	public static Language getClosestLanguage(List<Language> list) {
		return connector.getClosestLanguage(list);
	}


	public static void translate(String section,String idInSection, Language lang, String value) throws DaoManagerException{
		connector.translate(lang,section,idInSection,value);	
	}


	public static void createLabel(Language lang, String section,
			String idInSection, String value) throws DaoManagerException {
		
		connector.translate(lang,section,idInSection,value);
	}

	public static void createLabel(String section,
			String idInSection, String value) throws DaoManagerException {
		
		connector.translate(getThreadLocalLanguage(),section,idInSection,value);
	}
	public static void delete(String section, String idInSection) throws DaoManagerException{
		connector.delete(section,  idInSection);
	}

}
