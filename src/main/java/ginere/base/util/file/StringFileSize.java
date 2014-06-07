/**
 * Copyright: Angel-Ventura Mendo Gomez
 *	      ventura@free.fr
 *
 * $Id: StringFileSize.java,v 1.2 2006/11/26 09:17:29 ventura Exp $
 */
package ginere.base.util.file;

import ginere.base.util.i18n.I18NConnector;
import ginere.base.util.i18n.Language;

import java.text.NumberFormat;

/**
 * Utilities to transform files size to String files size
 * @author Angel Mendo
 * @version $Revision: 1.2 $
 */
public class StringFileSize  {

	private static final String SECTION = StringFileSize.class.getName();

	private static final String BYTE_LBL   = "Byte";
	private static final String KBYTE_LBL   = "KB";
	private static final String MBYTE_LBL   = "MB";
	private static final String GBYTE_LBL   = "GB";
	private static final String TBYTE_LBL   = "TB";

	static {
		I18NConnector.setInitLabels(new String[][] { 
			{ "en",SECTION, BYTE_LBL, "Byte" } ,
			{ "es",SECTION, BYTE_LBL, "Byte" } ,
			{ "fr",SECTION, BYTE_LBL, "octet" } ,

			{ "en",SECTION, KBYTE_LBL, "KB" } ,
			{ "es",SECTION, KBYTE_LBL, "KB" } ,
			{ "fr",SECTION, KBYTE_LBL, "Ko" } ,
			
			{ "en",SECTION, MBYTE_LBL, "MB" } ,
			{ "es",SECTION, MBYTE_LBL, "MB" } ,
			{ "fr",SECTION, MBYTE_LBL, "Mo" } ,

			{ "en",SECTION, GBYTE_LBL, "GB" } ,
			{ "es",SECTION, GBYTE_LBL, "GB" } ,
			{ "fr",SECTION, GBYTE_LBL, "Go" } ,

			{ "en",SECTION, TBYTE_LBL, "TB" } ,
			{ "es",SECTION, TBYTE_LBL, "TB" } ,
			{ "fr",SECTION, TBYTE_LBL, "To" } ,			
		});
	}


	private static final double BYTES_LIMIT=1024;
	private static final double KB_LIMIT=BYTES_LIMIT*BYTES_LIMIT;
	private static final double MB_LIMIT=KB_LIMIT*BYTES_LIMIT;
	private static final double GB_LIMIT=MB_LIMIT*BYTES_LIMIT;
	private static final double TB_LIMIT=GB_LIMIT*BYTES_LIMIT;

	public static String getStringSize(long size){
		Language lang=I18NConnector.getThreadLocalLanguage();
		return getStringSize(lang,size);
	}
	
	public static String getStringSize(Language language,long size){

		NumberFormat format=NumberFormat.getNumberInstance(language.getLocale());

		
		if (size<BYTES_LIMIT){ // bytes
			StringBuffer buffer=new StringBuffer();
			buffer.append(format.format(size));
			buffer.append(" ");
			buffer.append(I18NConnector.getLabel(language,StringFileSize.class, BYTE_LBL));
			return buffer.toString();
		} else if (size<KB_LIMIT) {
			StringBuffer buffer=new StringBuffer();
			buffer.append(format.format(size/BYTES_LIMIT));
			buffer.append(" ");
			buffer.append(I18NConnector.getLabel(language,StringFileSize.class,KBYTE_LBL));
			return buffer.toString();
		} else if (size<MB_LIMIT) {
			StringBuffer buffer=new StringBuffer();
			buffer.append(format.format(size/KB_LIMIT));
			buffer.append(" ");
			buffer.append(I18NConnector.getLabel(language,StringFileSize.class,MBYTE_LBL));
			return buffer.toString();
		} else if (size<GB_LIMIT) {
			StringBuffer buffer=new StringBuffer();
			buffer.append(format.format(size/MB_LIMIT));
			buffer.append(" ");
			buffer.append(I18NConnector.getLabel(language,StringFileSize.class,GBYTE_LBL));
			return buffer.toString();
		} else {
			StringBuffer buffer=new StringBuffer();
			buffer.append(format.format(size/GB_LIMIT));
			buffer.append(" ");
			buffer.append(I18NConnector.getLabel(language,StringFileSize.class,GBYTE_LBL));
			return buffer.toString();
		}
	}

}
