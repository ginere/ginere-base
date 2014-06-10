package eu.ginere.base.util.i18n;

import eu.ginere.base.util.dao.DaoManagerException;

import java.util.List;

public interface I18NConnectorInterface {

	public String i18nLabel(Language lang, String section, String label);

	public Language getDefaultLanguage();

	public void clearCache();

	public Language[] getAvailablesLanguageList();

	/**
	 * inserta una nueva etiqueta en el modulo de traduccion .
	 * @param lang
	 * @param section
	 * @param labelId
	 * @param value
	 */
//	public void insert(Language lang, String section, String labelId, String value)throws DaoManagerException;

	public boolean isAvailableLanguage(Language lang);

	public Language getLanguageFromLangId(String langId, Language defaultValue);

	public Language getLanguageFromLanguageId(String languageId,
											  Language defaultValue);

	/**
	 * Returns the closed languaguaje to this one. If no one returns the default
	 * @param language
	 * @return
	 */
	public Language getClosestLanguage(Language language);

	/**
	 * returns the closed language to one of this list. If no one returns the default
	 * @param list
	 * @return
	 */
	public Language getClosestLanguage(List<Language> list);

	/**
	 * @param id The label to translate
	 * @param lang The lang to inser
	 * @param value the value
	 */
	public void translate(Language lang, String section,String idInSection,String value)throws DaoManagerException;

	public void delete(String section, String idInSection)throws DaoManagerException;
}
