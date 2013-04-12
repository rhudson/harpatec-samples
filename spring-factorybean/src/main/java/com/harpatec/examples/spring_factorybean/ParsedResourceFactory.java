package com.harpatec.examples.spring_factorybean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

public class ParsedResourceFactory implements FactoryBean<ParsedResource> {

	private Resource fieldNamesResource;
	
	public ParsedResource getObject() throws Exception {
		
		List<String> names = new ArrayList<String>();
		List<String> sizes = new ArrayList<String>();
		LineIterator iterator = null;
		
		try {
			iterator = IOUtils.lineIterator(fieldNamesResource.getInputStream(), "UTF-8");
			while (iterator.hasNext()) {
				String[] items = iterator.nextLine().split(",");
				names.add(items[0]);
				sizes.add(items[1]);
			}
		} finally {
			LineIterator.closeQuietly(iterator);
		}

		ParsedResource result = new ParsedResource();
		result.setFieldNames(names);
		result.setFieldSizes(sizes);

		return result;

	}

	public Class<?> getObjectType() {
		return ParsedResource.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setFieldNamesResource(Resource fieldNamesResource) {
		this.fieldNamesResource = fieldNamesResource;
	}

}
