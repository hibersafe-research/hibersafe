package com.ufrn.dtos;

public enum ExceptionsEnum {
	
	AnnotationException("AnnotationException", "org.hibernate.AnnotationException"), 
	AssertionFailure("AssertionFailure", "org.hibernate.AssertionFailure"), 
	CallbackException("CallbackException", "org.hibernate.CallbackException"), 
	DuplicateMappingException("DuplicateMappingException", "org.hibernate.DuplicateMappingException"),
	HibernateError("HibernateError", "org.hibernate.HibernateError"), 
	HibernateException("HibernateException", "org.hibernate.HibernateException"), 
	InstantiationException("InstantiationException", "org.hibernate.InstantiationException"), 
	InvalidMappingException("InvalidMappingException", "org.hibernate.InvalidMappingException"), 
	JDBCException("JDBCException", "org.hibernate.JDBCException"), 
	LazyInitializationException("LazyInitializationException", "org.hibernate.LazyInitializationException"),
	MappingException("MappingException", "org.hibernate.MappingException"), 
	MappingNotFoundException("MappingNotFoundException", "org.hibernate.MappingNotFoundException"), 
	NonUniqueObjectException("NonUniqueObjectException", "org.hibernate.NonUniqueObjectException"), 
	NonUniqueResultException("NonUniqueResultException", "org.hibernate.NonUniqueResultException"), 
	ObjectDeletedException("ObjectDeletedException", "org.hibernate.ObjectDeletedException"),
	ObjectNotFoundException("ObjectNotFoundException", "org.hibernate.ObjectNotFoundException"), 
	OptimisticLockException("OptimisticLockException", "org.hibernate.OptimisticLockException"), 
	PersistentObjectException("PersistentObjectException", "org.hibernate.PersistentObjectException"), 
	PessimisticLockException("PessimisticLockException", "org.hibernate.PessimisticLockException"),
	PropertyAccessException("PropertyAccessException", "org.hibernate.PropertyAccessException"), 
	PropertyNotFoundException("PropertyNotFoundException", "org.hibernate.PropertyNotFoundException"), 
	PropertySetterAccessException("PropertySetterAccessException", "org.hibernate.PropertySetterAccessException"), 
	PropertyValueException("PropertyValueException", "org.hibernate.PropertyValueException"), 
	QueryException("QueryException", "org.hibernate.QueryException"),
	QueryParameterException("QueryParameterException", "org.hibernate.QueryParameterException"), 
	QueryTimeoutException("QueryTimeoutException", "org.hibernate.QueryTimeoutException"), 
	ResourceClosedException("ResourceClosedException", "org.hibernate.ResourceClosedException"), 
	SessionException("SessionException", "org.hibernate.SessionException"), 
	StaleObjectStateException("StaleObjectStateException", "org.hibernate.StaleObjectStateException"),
	StaleStateException("StaleStateException", "org.hibernate.StaleStateException"), 
	TransactionException("TransactionException", "org.hibernate.TransactionException"), 
	TransientObjectException("TransientObjectException", "org.hibernate.TransientObjectException"), 
	TransientPropertyValueException("TransientPropertyValueException", "org.hibernate.TransientPropertyValueException"), 
	TypeMismatchException("TypeMismatchException", "org.hibernate.TypeMismatchException"),
	UnknownEntityTypeException("UnknownEntityTypeException", "org.hibernate.UnknownEntityTypeException"), 
	UnknownProfileException("UnknownProfileException", "org.hibernate.UnknownProfileException"), 
	UnresolvableObjectException("UnresolvableObjectException", "org.hibernate.UnresolvableObjectException"), 
	UnsupportedLockAttemptException("UnsupportedLockAttemptException", "org.hibernate.UnsupportedLockAttemptException"),
	WrongClassException("WrongClassException", "org.hibernate.WrongClassException"), 
	ConstraintViolationException("ConstraintViolationException", "org.hibernate.exception.ConstraintViolationException"), 
	DataException("DataException", "org.hibernate.exception.DataException"), 
	GenericJDBCException("GenericJDBCException", "org.hibernate.exception.GenericJDBCException"),
	JDBCConnectionException("JDBCConnectionException", "org.hibernate.exception.JDBCConnectionException"), 
	LockAcquisitionException("LockAcquisitionException", "org.hibernate.exception.LockAcquisitionException"), 
	LockTimeoutException("LockTimeoutException", "org.hibernate.exception.LockTimeoutException"), 
	SQLGrammarException("SQLGrammarException", "org.hibernate.exception.SQLGrammarException");
	
	private String name;
	
	private String exPackage;

	private ExceptionsEnum(String name, String exPackage) {
		this.name = name;
		this.exPackage = exPackage;
	}

	public String getName() {
		return name;
	}

	public String getExPackage() {
		return exPackage;
	}
	
}
