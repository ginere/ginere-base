package ginere.base.util.i18n;

import ginere.base.util.dao.DaoManagerException;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author ventura
 *
 * Gestiona el multilinguismo.
 * Se puede guardar la lengua activa en una variable thread local. Puede usuarse para los servlets.
 */
public class I18NLabel implements Serializable{
	static Logger log = Logger.getLogger(I18NLabel.class);

	public static I18NLabel get(String id) throws DaoManagerException{
		if (id==null){
			return null;
		}
		return new I18NLabel(id);
	}
	public static I18NLabel get(String section,String idinSection) throws DaoManagerException{
		if (section==null || idinSection==null){
			return null;
		}
		return new I18NLabel( section,idinSection);
	}
	
	public static void delete(String id) throws DaoManagerException{
		new I18NLabel(id).delete();
	}		

	public static I18NLabel create(Language lang,String section,String idinSection,String value) throws DaoManagerException{
		if (value==null){
			return null;
		}
		return new I18NLabel(lang,section,idinSection,value);
	}

	public static I18NLabel create(String section,String idinSection,String value) throws DaoManagerException{
		if (value==null){
			return null;
		}
		return new I18NLabel(section,idinSection,value);
	}

	public static I18NLabel create(Class<?> sectionClass,String idinSection,String value) throws DaoManagerException{
		if (value==null){
			return null;
		}
		return new I18NLabel(sectionClass,idinSection,value);
	}

	
	public static I18NLabel translate(I18NLabel label,Class<?> sectionClass,String idInSection,String value)throws DaoManagerException{
		return translate(label, sectionClass.getName(), idInSection, value);
	}
	
	public static I18NLabel translate(I18NLabel label,String section,String idInSection,String value)throws DaoManagerException{
		if (value==null){
			return label;
		}
		
		if (label!=null){
			I18NConnector.translate(label.section,label.idInSection,I18NConnector.getThreadLocalLanguage(),value);
			return label;
		} else {
			return new I18NLabel(section,idInSection,value);
		}
	}
	
//	public static boolean exists(String id) throws DaoManagerException{
//		return I18NConnector.exists(id);
//	}

	protected final String id;
	protected final String section;
	protected final String idInSection;

	
	private I18NLabel(String id)throws DaoManagerException{
		this.id=id;

		String array[]=StringUtils.split(id,'|');
		
		if (array.length <2){
			throw new DaoManagerException("Id :'"+id+"' is not a valid I18nLabel id");
		}
		this.section=array[0];
		this.idInSection=array[1];
	}
	/**
	 * Crear un 18nLael con un valor para una lengua
	 * 
	 * @param lang La lengua para la que se crea el valor inicial
	 * @param section La seccion de la etique. Ej el nmbre de la clas
	 * @param idinSection. El Id en la seccion, ej el id del objeto en la base de datos
	 * @param value. El valor
	 */
	private I18NLabel(Language lang,String section,String idinSection,String value)throws DaoManagerException{
		this.section=section;
		this.idInSection=idinSection;
		this.id=section+'|'+idinSection;
		I18NConnector.createLabel(lang,section,idInSection,value);
	}
	
	private I18NLabel(String section,String idinSection)throws DaoManagerException{
		this.section=section;
		this.idInSection=idinSection;
		this.id=section+'|'+idinSection;
	}
	/**
	 * Crear un 18nLael con un valor para una lengua
	 * 
	 * @param lang La lengua para la que se crea el valor inicial
	 * @param section La seccion de la etique. Ej el nmbre de la clas
	 * @param idinSection. El Id en la seccion, ej el id del objeto en la base de datos
	 * @param value. El valor
	 */
	private I18NLabel(String section,String idinSection,String value)throws DaoManagerException{
		this.section=section;
		this.idInSection=idinSection;
		this.id=section+'|'+idinSection;
		I18NConnector.createLabel(section,idInSection,value);
	}

	private I18NLabel(Class<?> sectionClass,String idinSection,String value)throws DaoManagerException{
		this.section=sectionClass.getName();
		this.idInSection=idinSection;
		this.id=section+'|'+idinSection;
		I18NConnector.createLabel(section,idInSection,value);
	}
	/**
	 * traduce o anyade el valor para una lengua
	 * @param lang
	 * @param value
	 */
	public void setValue(Language lang,String value)throws DaoManagerException{
		I18NConnector.translate(section,idInSection,lang,value);
	}
	
	public void translate(Language lang,String value)throws DaoManagerException{
		I18NConnector.translate(section,idInSection,lang,value);
	}
	
	public String toString(){
		return I18NConnector.getLabel(section, idInSection);
	}

	public String toString(Language language){
		return I18NConnector.getLabel(language,section, idInSection);
	}
	
	public void delete() throws DaoManagerException {
		I18NConnector.delete(section, idInSection);	
	}
	
	public static boolean equals(I18NLabel label1,I18NLabel label2){
		if (label1!=null) {
			if (label2!=null) {
				return (StringUtils.equals(label1.id, label2.id));
			} else {
				return false;
			}
		} else if (label2!=null) {
			return false ;
		} else {
			return true ;
		}
	}
	public boolean equals(Object obj){
		if (obj==null){
			return false;
		} if (obj instanceof I18NLabel){
			return equals(this,(I18NLabel)obj);
		} else {
			return false;
		}
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the section
	 */
	public String getSection() {
		return section;
	}

	/**
	 * @return the idInSection
	 */
	public String getIdInSection() {
		return idInSection;
	}
	
	
}
