package com.harpatec.examples.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
public class Address {

	@NotNull
	@Size(min=1, max=10)
	private String streetNumber;

	@NotNull
	@Size(min=1, max=50)
	private String streetName;

	@NotNull
	@Size(min=1, max=50)
	private String city;
	
	@NotNull
	@Size(min=2, max=2)
	private String state;

	@NotNull
	@Size(min=5, max=9)
	private String zipCode;

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	@Override
	public boolean equals(Object o) {
		return Pojomatic.equals(this, o);
	}

	@Override
	public int hashCode() {
		return Pojomatic.hashCode(this);
	}

	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

}
