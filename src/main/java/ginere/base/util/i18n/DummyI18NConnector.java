package ginere.base.util.i18n;

import ginere.base.util.dao.DaoManagerException;

import java.util.List;

public class DummyI18NConnector implements I18NConnectorInterface {


//	private static final Locale DEFAULT_LOCALE=new Locale("es","ES");
//	private static final Locale DEFAULT_LOCALE=Locale.US;
	

	private static final Language DEFAULT_LANGUAGE=Language.DEBUG;

	private static final Language AVAILABLES_LENGUAGE_ARRAY[]=new Language[]{ Language.DEBUG};
	
	DummyI18NConnector(){	
	}
	
	@Override
	public Language getDefaultLanguage(){
		return DEFAULT_LANGUAGE;
	}

	public void clearCache(){
		return ;
	}

	@Override
	public Language[] getAvailablesLanguageList() {
		return AVAILABLES_LENGUAGE_ARRAY;
	}

	@Override
	public Language getLanguageFromLangId(String langId,Language defaultValue) {
		return defaultValue;
	}

	@Override
	public String i18nLabel(Language lang, String section, String label) {
		return label;
	}

	@Override
	public boolean isAvailableLanguage(Language lang) {
		return DEFAULT_LANGUAGE.equals(lang);
	}

	@Override
	public Language getLanguageFromLanguageId(String languageId,
			Language defaultValue) {
		return defaultValue;
	}

	@Override
	public Language getClosestLanguage(Language language) {
		return getDefaultLanguage();
	}

	@Override
	public Language getClosestLanguage(List<Language> list) {
		return getDefaultLanguage();
	}

	@Override
	public void translate(Language lang,String section,String idInSection,  String value)
			throws DaoManagerException {	
	}

	@Override
	public void delete(String section, String idInSection) {
	}
}
