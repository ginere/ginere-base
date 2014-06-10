package eu.ginere.base.util.descriptor;


import eu.ginere.base.util.i18n.I18NConnector;
import eu.ginere.base.util.i18n.Language;


/**
 * Contains the inner description of one property
 * 
 * @author ventura
 *
 */
public class InnerPropertyDescriptor {
//	private static final Logger log = Logger.getLogger(InnerPropertyDescriptor.class);

	private final Class<?> parentClass;
	private final String name;
	private final String description;
	private final Class<?> propertyClazz;
	
	private final Object accessor;
	
	public InnerPropertyDescriptor(InnerPropertyDescriptor obj){
		this.parentClass=obj.parentClass;
		this.name=obj.name;
		this.propertyClazz=obj.propertyClazz;
		this.description=obj.description;
		this.accessor=obj.accessor;
	}
	
	public InnerPropertyDescriptor(Class<?> parentClass,
								   String propertyName,
								   String propertyDescription,
								   Class<?> clazz,
								   Object accesor){
		this.parentClass=parentClass;
		this.name=propertyName;
		this.propertyClazz=clazz;
		this.description=propertyDescription;
		this.accessor=accesor;
	}

	/**
	 * @return the propertyName
	 */
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}

	/**
	 * @return the propertyClassName
	 */
	public String getClassName() {
		return propertyClazz.getName();
	}

	public String getDisplayName() {
		return I18NConnector.getLabel(parentClass, name);	
	}

	public String getDisplayDescription() {
		return I18NConnector.getLabel(parentClass, description);	
	}

	public String getDisplayName(Language language) {
		return I18NConnector.getLabel(language,parentClass, name);	
	}

	public String getDisplayDescription(Language language) {
		return I18NConnector.getLabel(language,parentClass, description);	
	}
	/**
	 * @return the parentClass
	 */
	public Class<?> getParentClass() {
		return parentClass;
	}

	/**
	 * @return the propertyClazz
	 */
	public Class<?> getPropertyClazz() {
		return propertyClazz;
	}

	/**
	 * @return the accessor
	 */
	public Object getAccessor() {
		return accessor;
	}
		
	
}
