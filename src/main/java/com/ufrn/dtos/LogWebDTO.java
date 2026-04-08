package com.ufrn.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogWebDTO {

    @JsonProperty("estrategia")
    private String estrategia;

    @JsonProperty("dados")
    private String[] dados;

    @JsonProperty("id")
    private Integer idUsuario;
    
    @JsonProperty("stacktrace")
    private String stacktrace;
    
    @JsonProperty("exception")
    private String exception;
    
    public LogWebDTO() {
    	
    }

	public LogWebDTO(String estrategia, String[] dados, Integer idUsuario, String stacktrace, String exception) {
		super();
		this.estrategia = estrategia;
		this.dados = dados;
		this.idUsuario = idUsuario;
		this.stacktrace = stacktrace;
		this.exception = exception;
	}

	public String getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(String estrategia) {
		this.estrategia = estrategia;
	}

	public String[] getDados() {
		return dados;
	}

	public void setDados(String[] dados) {
		this.dados = dados;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
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
