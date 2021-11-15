package org.garage;

public class Auto {
	
	private String colore;
	private String modello;
	private String marca;
	private int id;
	
	public Auto(String colore, String modello, String marca) {
		this.colore = colore;
		this.marca = marca;
		this.modello = modello;
		
	}
	
	public Auto() {}
	
	public void setColore(String colore) {
		this.colore = colore;
	}
	
	public String getColore() {
		return colore;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Auto [colore=" + colore + ", modello=" + modello + ", marca=" + marca + ", id=" + id + "]";
	}

}
