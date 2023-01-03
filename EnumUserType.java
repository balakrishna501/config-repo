package com.currypoint.rule.entity;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;


public class EnumUserType<E extends Enum<E>> implements UserType, ParameterizedType, Serializable  {


	private Class<E> clazz = null;


	public EnumUserType() {
	}

	

	protected EnumUserType(Class<E> c) {
		this.clazz = c;
	}


	private static final int[] SQL_TYPES = { Types.VARCHAR };

	private static final String PARAM_CLASS = "Enum";


	public int[] sqlTypes() {
		return SQL_TYPES;
	}


	public Class returnedClass() {
		return clazz;
	}


	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}


	public boolean isMutable() {
		return false;
	}


	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}


	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}


	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}


	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}





	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y)
			return true;
		if (null == x || null == y)
			return false;
		return x.equals(y);
	}


	public void setParameterValues(Properties parameters) {
		if (parameters != null) {
			if (parameters.containsKey(PARAM_CLASS)) {
				try {
					clazz = (Class<E>) Class.forName(parameters.getProperty(PARAM_CLASS));
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}


	@Override
	public Object nullSafeGet(ResultSet resultSet, String[] names,
							  SharedSessionContractImplementor sharedSessionContractImplementor, Object arg3) throws HibernateException,
			SQLException {
		String name = resultSet.getString(names[0]);
		E result = null;
		if ( !resultSet.wasNull()) {
			E[] universe = clazz.getEnumConstants();
			for (E constant : universe)
				if (name.equals(constant.toString()))
					result = constant;
		}
		return result;
	}



	@Override
	public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index,
							SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
		if (null == value) {
			preparedStatement.setNull(index, Types.VARCHAR);
		} else {
			preparedStatement.setString(index, (value).toString());
		}
	}
}

