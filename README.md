xtext-maven-examples
====================

A few simple examples of building XText projects with Maven


# Overview

These projects have been created with the simple XText DSL Project from within Eclipse.  File -> New -> Other -> XText 2.2.1 & XText 2.3.0
The main point is to show how to package and build an XText DSL project with Maven and is more or less an updated version of Karsten's Blog
entry - http://kthoms.wordpress.com/2010/08/18/building-xtext-projects-with-maven-tycho/

These examples now use the xtend-maven-plugin to call the XTend generator, which as I understand are now part of the latest XText versions.

Another really good project to look at for an XText maven examples can be found here (https://github.com/ckulla/xtext-tycho-example).  Unfortunately
I couldn't actually make this example work due to some issue resolving from the maven relative path local repository.


## An explanation and building the examples

Actually this is the main point of the example and despite a few examples on the web, it still took quite some time to understand how to 
get the Maven build working.


### Launching the build

Execute Maven command as normal.

	$ cd org.xtext.example.mydsl.parent
	$ mvn package
	
Clean target will clean all the generated source (which is nice as Eclipse won't)

	$ mvn clean
	
	
### Tycho moved to Eclipse

The Tycho plugin is used to build Eclipse plugins with Maven.  It has moved to an Eclipse project and as such Karsten's example needed a few
changes to the plugin definitions.
	http://wiki.eclipse.org/Tycho/Reference_Card
	http://wiki.eclipse.org/Tycho_Release_Notes/0.12


### Issue while resolving dependencies

Resolving dependencies for mwe generator from maven exposes Java 7 issue:
	http://dev.eclipse.org/mhonarc/lists/egit-dev/msg02554.html

Either downgrade to Java 1.6 or set the Java options to maven:
	SET MAVEN_OPTS=-Djava.util.Arrays.useLegacyMergeSort=true

The error you would see:
<pre>
[ERROR] Internal error: java.lang.IllegalArgumentException: Comparison method violates its general contract! -> [Help 1]
org.apache.maven.InternalErrorException: Internal error: java.lang.IllegalArgumentException: Comparison method violates its general con
tract!
        at org.apache.maven.DefaultMaven.execute(DefaultMaven.java:168)
        at org.apache.maven.cli.MavenCli.execute(MavenCli.java:537)
        at org.apache.maven.cli.MavenCli.doMain(MavenCli.java:196)
        at org.apache.maven.cli.MavenCli.main(MavenCli.java:141)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:601)
        at org.codehaus.plexus.classworlds.launcher.Launcher.launchEnhanced(Launcher.java:290)
        at org.codehaus.plexus.classworlds.launcher.Launcher.launch(Launcher.java:230)
        at org.codehaus.plexus.classworlds.launcher.Launcher.mainWithExitCode(Launcher.java:409)
        at org.codehaus.plexus.classworlds.launcher.Launcher.main(Launcher.java:352)
Caused by: java.lang.IllegalArgumentException: Comparison method violates its general contract!
        at java.util.ComparableTimSort.mergeLo(ComparableTimSort.java:714)
        at java.util.ComparableTimSort.mergeAt(ComparableTimSort.java:451)
        at java.util.ComparableTimSort.mergeCollapse(ComparableTimSort.java:376)
        at java.util.ComparableTimSort.sort(ComparableTimSort.java:182)
        at java.util.ComparableTimSort.sort(ComparableTimSort.java:146)
        at java.util.Arrays.sort(Arrays.java:472)
        at org.eclipse.equinox.internal.p2.artifact.repository.MirrorSelector.hasValidMirror(MirrorSelector.java:317)
</pre>


### OSGi issues

The generated project from XText 2.2.1 has a number of bundle imports that need to be made available via OSGi.  By far the easiest method
is to use the Tycho 'target-platform-configuration' plugin.  It can take all your defined P2 repositories (an Eclipse update site) and make 
those OSGi bundles available to your project.

If you just build without making these depdencies available to OSGi you should get an error similar to the following:
<pre>
[ERROR] Cannot resolve project dependencies:
[ERROR]   Software being installed: org.xtext.example.mydsl 1.0.0
[ERROR]   Missing requirement: org.xtext.example.mydsl 1.0.0 requires 'bundle org.eclipse.xtext 2.1.0' but it could not be found
[ERROR]
[ERROR] Internal error: java.lang.RuntimeException: "No solution found because the problem is unsatisfiable.": ["Unable to satisfy depe
ndency from org.eclipse.jdt.core 3.7.0.v_OTDT_r200_201106070730 to org.eclipse.objectteams.otdt.core.patch.feature.group [2.0.0,3.0.0).
", "Unable to satisfy dependency from org.eclipse.jdt.core 3.7.1.v_OTDT_r201_201109101025 to org.eclipse.objectteams.otdt.core.patch.fe
ature.group [2.0.0,3.0.0).", "Unable to satisfy dependency from org.eclipse.jdt.core 3.7.3.v_OTDT_r202_201202051448 to org.eclipse.obje
ctteams.otdt.core.patch.feature.group [2.0.0,3.0.0).", "Unable to satisfy dependency from org.xtext.example.mydsl 1.0.0 to bundle org.e
clipse.xtext 2.1.0.", "Unable to satisfy dependency from org.xtext.example.mydsl 1.0.0 to bundle org.eclipse.xtext.xbase 2.1.0.", "No s
olution found because the problem is unsatisfiable."] -> [Help 1]
org.apache.maven.InternalErrorException: Internal error: java.lang.RuntimeException: "No solution found because the problem is unsatisf
iable.": ["Unable to satisfy dependency from org.eclipse.jdt.core 3.7.0.v_OTDT_r200_201106070730 to org.eclipse.objectteams.otdt.core.p
atch.feature.group [2.0.0,3.0.0).", "Unable to satisfy dependency from org.eclipse.jdt.core 3.7.1.v_OTDT_r201_201109101025 to org.eclip
se.objectteams.otdt.core.patch.feature.group [2.0.0,3.0.0).", "Unable to satisfy dependency from org.eclipse.jdt.core 3.7.3.v_OTDT_r202
_201202051448 to org.eclipse.objectteams.otdt.core.patch.feature.group [2.0.0,3.0.0).", "Unable to satisfy dependency from org.xtext.ex
ample.mydsl 1.0.0 to bundle org.eclipse.xtext 2.1.0.", "Unable to satisfy dependency from org.xtext.example.mydsl 1.0.0 to bundle org.e
clipse.xtext.xbase 2.1.0.", "No solution found because the problem is unsatisfiable."]
        at org.apache.maven.DefaultMaven.execute(DefaultMaven.java:168)
        at org.apache.maven.cli.MavenCli.execute(MavenCli.java:537)
        at org.apache.maven.cli.MavenCli.doMain(MavenCli.java:196)
        at org.apache.maven.cli.MavenCli.main(MavenCli.java:141)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:601)
        at org.codehaus.plexus.classworlds.launcher.Launcher.launchEnhanced(Launcher.java:290)
        at org.codehaus.plexus.classworlds.launcher.Launcher.launch(Launcher.java:230)
        at org.codehaus.plexus.classworlds.launcher.Launcher.mainWithExitCode(Launcher.java:409)
        at org.codehaus.plexus.classworlds.launcher.Launcher.main(Launcher.java:352)
</pre>


### Fornax plugin repository

The fornax-oaw-m2-plugin is used to run the mwe2 workflow.  Karsten's example can now use a released version (3.3.0) from the following
repository:

http://www.fornax-platform.org/m2/repository


