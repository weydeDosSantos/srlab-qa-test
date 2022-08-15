package br.com.southrocklab;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import br.com.southrocklab.resources.CardResourceTest;
import br.com.southrocklab.resources.CustomerResourceTest;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
	CustomerResourceTest.class,
	CardResourceTest.class
	
})
public class Suite {

}
