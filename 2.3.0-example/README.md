xtext-maven-examples 2.3.0-example
==================================

## Generator

This example includes a standalone jar that can be used to generate your source.  It's basic usage is:
<pre>
$ cd org.xtext.example.mydsl.generator
$ java -jar ./target/mygenerator.jar -src ./src/test/mydsl/Example.mydsl -targetdir ./target
$ cat ./target/Example.txt
</pre>

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

A few additional findings to get Maven to build an XText 2.3.0 project and have a standalone jar generator.


### mygenerator.jar errors

When running the standalone jar 'mygenerator.jar' with version 2.2.1 of the org.eclipse.xtend2.standalone dependency
we saw these errors below.  They did not seem to affect the xtend generation, but they can be resolved by removing 
the org.eclipse.xtend2.standalone dependency and adding the org.eclipse.xtext dependencies (basically all the xtext jars 
you see in your Eclipse dependencies) to your pom.xml.  

A big thanks to Torsten at springide.org for a number of changes, but paticuliarly for pointing me at the 2.3.1 version
of the XTend standalone jar.  The example has now been updated to use the 2.3.1 version of the org.eclipse.xtend.standalone
jar which resolved the exception below.

The error:
<pre>
18   [main] ERROR clipse.xtext.service.CompoundModule  - java.lang.reflect.InvocationTargetException
java.lang.RuntimeException: java.lang.reflect.InvocationTargetException
        at org.eclipse.xtext.service.MethodBasedModule.invokeMethod(MethodBasedModule.java:136)
        at org.eclipse.xtext.service.MethodBasedModule.configure(MethodBasedModule.java:49)
        at org.eclipse.xtext.service.CompoundModule.configure(CompoundModule.java:34)
        at org.eclipse.xtext.service.AbstractGenericModule.configure(AbstractGenericModule.java:32)
        at org.eclipse.xtext.service.DefaultRuntimeModule.configure(DefaultRuntimeModule.java:74)
        at org.xtext.example.mydsl.AbstractMyDslRuntimeModule.configure(AbstractMyDslRuntimeModule.java:25)
        at com.google.inject.spi.Elements$RecordingBinder.install(Elements.java:223)
        at com.google.inject.spi.Elements.getElements(Elements.java:101)
        at com.google.inject.internal.InjectorShell$Builder.build(InjectorShell.java:133)
        at com.google.inject.internal.InternalInjectorCreator.build(InternalInjectorCreator.java:103)
        at com.google.inject.Guice.createInjector(Guice.java:95)
        at com.google.inject.Guice.createInjector(Guice.java:72)
        at com.google.inject.Guice.createInjector(Guice.java:62)
        at org.xtext.example.mydsl.MyDslStandaloneSetupGenerated.createInjector(MyDslStandaloneSetupGenerated.java:26)
        at org.xtext.example.mydsl.MyDslStandaloneSetupGenerated.createInjectorAndDoEMFRegistration(MyDslStandaloneSetupGenerated.java:20)
        at org.xtext.example.mydsl.generator.launcher.Main.main(Main.java:41)
        at org.xtext.example.mydsl.generator.launcher.Launcher.launch(Launcher.java:56)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
        at java.lang.reflect.Method.invoke(Unknown Source)
        at org.xtext.example.mydsl.generator.launcher.Launcher.main(Launcher.java:46)
Caused by: java.lang.reflect.InvocationTargetException
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
        at java.lang.reflect.Method.invoke(Unknown Source)
        at org.eclipse.xtext.service.MethodBasedModule.invokeMethod(MethodBasedModule.java:134)
        ... 21 more
Caused by: java.lang.NoClassDefFoundError: org/eclipse/xtext/serializer/sequencer/AbstractDelegatingSemanticSequencer
        at java.lang.ClassLoader.defineClass1(Native Method)
        at java.lang.ClassLoader.defineClass(Unknown Source)
        at java.security.SecureClassLoader.defineClass(Unknown Source)
        at java.net.URLClassLoader.defineClass(Unknown Source)
        at java.net.URLClassLoader.access$100(Unknown Source)
        at java.net.URLClassLoader$1.run(Unknown Source)
        at java.net.URLClassLoader$1.run(Unknown Source)
        at java.security.AccessController.doPrivileged(Native Method)
        at java.net.URLClassLoader.findClass(Unknown Source)
        at java.lang.ClassLoader.loadClass(Unknown Source)
        at sun.misc.Launcher$AppClassLoader.loadClass(Unknown Source)
        at java.lang.ClassLoader.loadClass(Unknown Source)
        at org.xtext.example.mydsl.AbstractMyDslRuntimeModule.bindISemanticSequencer(AbstractMyDslRuntimeModule.java:44)
        ... 26 more
Caused by: java.lang.ClassNotFoundException: org.eclipse.xtext.serializer.sequencer.AbstractDelegatingSemanticSequencer
        at java.net.URLClassLoader$1.run(Unknown Source)
        at java.net.URLClassLoader$1.run(Unknown Source)
        at java.security.AccessController.doPrivileged(Native Method)
        at java.net.URLClassLoader.findClass(Unknown Source)
        at java.lang.ClassLoader.loadClass(Unknown Source)
        at sun.misc.Launcher$AppClassLoader.loadClass(Unknown Source)
        at java.lang.ClassLoader.loadClass(Unknown Source)
        ... 39 more
Code generation finished.
</pre>

Add the org.eclipse.xtext dependency to your pom.xml:
<pre>
        <dependency>
            <groupId>org.eclipse.xtext</groupId>
            <artifactId>org.eclipse.xtext</artifactId>
            <version>2.3.0.v201206120633</version>
        </dependency>
</pre>

Change to your Eclipse plugins directory and install the dependency into your local repository:
<pre>
mvn install:install-file -DgroupId=org.eclipse.xtext -DartifactId=org.eclipse.xtext -Dpackaging=jar -Dversion=2.3.0.v201206120633 -Dfile=org.eclipse.xtext_2.3.0.v201206120633.jar -DgeneratePom=true
</pre>



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

