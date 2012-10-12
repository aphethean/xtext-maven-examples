package org.xtext.example.mydsl.generator.launcher;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.xtext.example.mydsl.MyDslStandaloneSetup;

import com.google.inject.Injector;

public class Main {

	public static void main(String[] args) {
		// handle command line options
		final Options options = new Options();
		Option optSrc = OptionBuilder.withArgName("src")
				.withDescription("Model source").hasArg()
				.isRequired().withValueSeparator(' ').create("src");
		 
		Option optTargetDir = OptionBuilder.withArgName("targetdir")
				.withDescription("Generator target directory").hasArg()
				.isRequired().withValueSeparator(' ').create("targetdir");

		options.addOption(optSrc); options.addOption(optTargetDir);
		
		// create the command line parser
		final CommandLineParser parser = new GnuParser(); 
		CommandLine line = null;
		try {
			line = parser.parse(options, args);
		} catch (final ParseException exp) {
			System.err.println("Parsing arguments failed.  Reason: " + exp.getMessage()); 
			wrongCall(options); return;
		}
		
		// execute the generator
		Injector injector = new MyDslStandaloneSetup().createInjectorAndDoEMFRegistration();
		Generator generator = injector.getInstance(Generator.class);
		generator.runGenerator(line.getOptionValue(optSrc.getArgName()), line.getOptionValue(optTargetDir.getArgName()));

		System.out.println("Code generation finished.");
	}
	
	/**
	 * Print usage information and terminate the program.
	 * 
	 * @param options
	 */
	private static void wrongCall(final Options options) {
		final HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java " + Launcher.class.getName() + " [OPTIONS]",
				options);
		System.exit(-1);
	}

}
