package org.garage;

import java.util.List;

public class Condizione {
	
	private String campo;
	private List<String> parametri;
	
	
	public Condizione(String campo, List<String> parametri) {
		this.campo = campo;
		this.parametri = parametri;
	}
	
	public Condizione() {}


	public String getCampo() {
		return campo;
	}


	public void setCampo(String campo) {
		this.campo = campo;
	}


	public List<String> getParametri() {
		return parametri;
	}


	public void setParametri(List<String> parametri) {
		this.parametri = parametri;
	}


	@Override
	public String toString() {
		return "Condizione [campo=" + campo + ", parametri=" + parametri + "]";
	}
	

}
