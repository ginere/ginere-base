package ginere.base.util.dao.impl;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @author ventura
 *
 * Clase madra para todos los objetos producidos por un DAO
 */
public abstract class AbstractDTO {

	//	public static Logger log = Logger.getLogger(DTO.class);
	
//	protected String getString(ResultSet rset,String columnName,String tableName) throws DaoManagerException{
//		try {
//			return rset.getString(columnName);
//		}catch(SQLException e){
//			throw new DaoManagerException("columnName:"+columnName+" tableName:"+tableName,e);
//		}
//	}
//
//	protected int getInt(ResultSet rset,String columnName,String tableName) throws DaoManagerException{
//		try {
//			return rset.getInt(columnName);
//		}catch(SQLException e){
//			throw new DaoManagerException("columnName:"+columnName+" tableName:"+tableName,e);
//		}
//	}
//	
//	protected boolean getBoolean(ResultSet rset,String columnName,String tableName) throws DaoManagerException{
//		try {
//			return rset.getBoolean(columnName);
//		}catch(SQLException e){
//			throw new DaoManagerException("columnName:"+columnName+" tableName:"+tableName,e);
//		}
//	}
//	
//	protected long getLong(ResultSet rset,String columnName,String tableName) throws DaoManagerException{
//		try {
//			return rset.getLong(columnName);
//		}catch(SQLException e){
//			throw new DaoManagerException("columnName:"+columnName+" tableName:"+tableName,e);
//		}
//	}
//	
//	protected Date getDate(ResultSet rset,String columnName,String tableName) throws DaoManagerException{
//		try {
//			return rset.getTimestamp(columnName);
//		}catch(SQLException e){
//			throw new DaoManagerException("columnName:"+columnName+" tableName:"+tableName,e);
//		}
//	}
//	
//	protected SQLEnum getSQLEnum(ResultSet rset,Class clazz,String columnName,String tableName) throws DaoManagerException{
//		return SQLEnum.value(clazz, getString(rset,columnName,tableName));
//	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
