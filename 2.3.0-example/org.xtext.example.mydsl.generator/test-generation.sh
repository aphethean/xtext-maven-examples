#!/bin/sh

#Script that runs the mydsl generator directly via the jar generated by the mydsl.generator project build

#Make sure JAVA_HOME has been set
echo ${JAVA_HOME:-"Please set JAVA_HOME for your environment"}

#Run the generator contained within 'org.xtext.example.mydsl.generator/target/org.xtext.example.mydsl.generator-1.0.0-SNAPSHOT.jar'

#source model - org.xtext.example.mydsl.generator/src/test/mydsl/Example.mydsl

#target director - generated-sources

$JAVA_HOME/bin/java -jar target/org.xtext.example.mydsl.generator-1.0.0-SNAPSHOT.jar -src src/test/mydsl/Example.mydsl --targetdir generated-sources 
