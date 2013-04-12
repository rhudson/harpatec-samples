package com.harpatec.examples.spring_factorybean;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


public class ParsedResourceComponentFactory implements InitializingBean {

	private ParsedResource parsedResource;

	public void setParsedResource(ParsedResource parsedResource) {
		this.parsedResource = parsedResource;
	}

	public String[] getNames() {
		return parsedResource.getFieldNames().toArray(new String[parsedResource.getFieldNames().size()]);
	}

	public String[] getSizes() {
		return parsedResource.getFieldSizes().toArray(new String[parsedResource.getFieldSizes().size()]);
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(parsedResource, "parsedResource must never be null.");
	}
}
