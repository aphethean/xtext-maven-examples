package org.xtext.example.mydsl;

import static org.junit.Assert.*;

import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xtext.example.mydsl.myDsl.Model;

import com.google.inject.Inject;

@InjectWith(MyDslInjectorProvider.class)
@RunWith(XtextRunner.class)
public class ParserTest {

	@Inject
	ParseHelper<Model> parser;

	@Test 
	public void parseModel() throws Exception {
	    Model model = parser.parse("Hello world!");
	    assertEquals ("world", model.getGreetings().get(0).getName());
	}
}
