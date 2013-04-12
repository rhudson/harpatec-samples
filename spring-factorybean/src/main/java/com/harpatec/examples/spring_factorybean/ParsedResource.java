package com.harpatec.examples.spring_factorybean;

import java.util.List;

public class ParsedResource {

	private List<String> fieldNames;
	private List<String> fieldSizes;

	public List<String> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}

	public List<String> getFieldSizes() {
		return fieldSizes;
	}

	public void setFieldSizes(List<String> fieldSizes) {
		this.fieldSizes = fieldSizes;
	}

}
