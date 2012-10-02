xtext-maven-examples 2.3.0-example
==================================

A few additional finding to get Maven to build an XText 2.3.0 project


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

