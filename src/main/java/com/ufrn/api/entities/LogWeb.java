package com.ufrn.api.entities;

import java.util.Calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "log_web")
public class LogWeb {
	
	@Id
	@GeneratedValue
	public Integer id;
	
	@Column
	public Integer idUsuario;
	
	@Column
	public Calendar date;
	
	@Column
	public Integer position;
	
	@Column
	public String estrategia;
	
	@Column
	public String url;
	
	@Column(columnDefinition = "TEXT")
	public String stacktrace;
	
	@Column
	public String exception;
	
	public LogWeb() {
		
	}

	public LogWeb(Integer id, Integer idUsuario, Calendar date, Integer position, String estrategia, String url, String stacktrace, String exception) {
		super();
		this.id = id;
		this.idUsuario = idUsuario;
		this.date = date;
		this.position = position;
		this.estrategia = estrategia;
		this.url = url;
		this.stacktrace = stacktrace;
		this.exception = exception;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(String estrategia) {
		this.estrategia = estrategia;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStacktrace() {
		return stacktrace;
	}

	public void setStacktrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

}
