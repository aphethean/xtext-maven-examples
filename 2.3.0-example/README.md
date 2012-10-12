xtext-maven-examples 2.3.0-example
==================================

## Generator

This example includes a standalone jar that can be used to generate your source.  It's basic usage is:
$ cd org.xtext.example.mydsl.generator
$ java -jar ./target/mygenerator.jar -src ./src/test/mydsl/Example.mydsl -targetdir ./target

I've lost count of the number of ways I tried to create an executable jar and none of them quite worked here how
I wanted.  Onejar was super easy to setup, but it had a problem when I tried to execute the generator.  So in the
end the example has ended up with quite a lot of moving parts just to create this mygenerator.jar
			
Links to some of the issues:
http://left.subtree.org/2008/01/24/creating-executable-jars-with-maven/ 
http://stackoverflow.com/questions/1757947/maven-include-dependent-libs-in-jar-without-unpacking-dependencies
http://stackoverflow.com/questions/7633347/maven-assembly-create-jar-with-dependency-and-class-path

maven-dependency-plugin:build-classpath
http://stackoverflow.com/questions/1510071/maven-how-can-i-add-an-arbitrary-classpath-entry-to-a-jar


## Issues

A few additional findings to get Maven to build an XText 2.3.0 project


### OSGi issues

The generated project from XText 2.3.0 does not declare the XText dependencies in the MANIFEST.MF.  Seems the best option is to 
update the MANIFEST.MF to declare the versions of the XText libraries we depend on.

<pre>
Require-Bundle: org.eclipse.xtext;bundle-version="2.3.0";visibility:=reexport,
 org.eclipse.xtext.xbase;bundle-version="2.3.0";resolution:=optional;visibility:=reexport,
</pre>


### Versioning scheme

XText 2.3.0 create a project with a '.qualifier' suffix in it's versioning scheme.  This version needs to match the Bundle-Version
declared in the MANIFEST.MF.  You should determine your own versioning scheme for your DSL.

<pre>
		<!-- You should change this and the MANIFEST.MF to your versioning scheme -->
		<version>1.0.0.qualifier</version>
</pre>

