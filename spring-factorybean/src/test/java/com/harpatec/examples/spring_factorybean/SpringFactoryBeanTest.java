package com.harpatec.examples.spring_factorybean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SpringFactoryBeanTest {

	@Resource(name = "springBean")
	private SpringBean springBean;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		assertNotNull(springBean);
		assertNotNull(springBean.getFields());
		assertEquals(3, springBean.getFields().length);
		
		assertEquals("firstField", springBean.getFields()[0]);
		assertEquals("secondField", springBean.getFields()[1]);
		assertEquals("thirdField", springBean.getFields()[2]);

		assertEquals("12", springBean.getSizes()[0]);
		assertEquals("3", springBean.getSizes()[1]);
		assertEquals("55", springBean.getSizes()[2]);
	}

}
