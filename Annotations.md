# JavaDude Annotations #

## NOTE ##
I'm working on a newer version of the annotations. THE API IS CHANGING SOMEWHAT. I learned several things about how the API should feel and was able to figure out some nice things to do in the processor to get rid of the options that you needed to use to extend a superclass.

The upgrade path shouldn't be too bad; I'll see if I can write a little converter.

Please let me know if you use these annotations and find them useful!
[scott@javadude.com](mailto:scott@javadude.com)

## Overview ##
The JavaDude Annotations are Java 5/6 annotations and annotation processors that generate some of the tedious code you need in your day-to-day programming. Using these annotations, you can generate:

| Properties | fields with is/get/set accessor/mutator methods, optionally firing PropertyChangeEvents when their value has been changed. |
|:-----------|:---------------------------------------------------------------------------------------------------------------------------|
| Observer Pattern | code to track and manage added observer/listener objects (defined by their listener interfaces) and fire events to those objects. |
| Prototype Pattern | the java clone() method. hashCode() / equals() / toString() - basic implementations of these methods using properties that have been defined for the bean. |
| Delegation | generation of delegation methods. All methods in named interfaces will delegate to a specified implementation class or bean property. |
| Null Object Pattern | all methods in named interfaces will perform "null" operations; void methods do nothing, while non-void methods return null, false, or 0. |
| Interface Extraction | an interface will be generated for all public methods in a class, or just those that are explicitly tagged for export.     |
| Default Method Parameters | method overloads are generated to provide default parameter values                                                         |

## License ##
### JavaDude Annotations and Annotation Processor ###
The annotations and annotation processor are only required at build time; there is no runtime component.

Copyright (c) 2008 Scott Stanchfield.
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html

### Apache Velocity ###
Velocity is required for the annotation processor to generate code, and is provided as a separate download.

Apache Velocity

Copyright (C) 2000-2007 The Apache Software Foundation

This product includes software developed at
The Apache Software Foundation (http://www.apache.org/).


## Documentation ##
(Work in progress... might be a while...)
  * [Quick Reference](AnnotationsQuickRef.md)
  * [Examples](AnnotationsExamples.md)

## Downloading and Installation ##
### Jars ###
If you're using the eclipse plugin or not, you'll still need to download the annotation jar, which contains the annotations and the annotation processors. Download

  * [javadude-annotation\_2.1.6.jar](http://javadude.googlecode.com/svn/trunk/com.javadude.annotation/javadude-annotation-2.1.6.jar)
  * [velocity-dep-1.5.jar](http://javadude.googlecode.com/svn/trunk/com.javadude.annotation/lib/velocity-dep-1.5.jar) (Only if not using the Eclipse plugin)


---


### Annotation Processing and Compilation ###
Depending upon how you edit your code, you will need different combinations of jars and plugins.

#### In Eclipse ####
Eclipse Plugin
If you're using Eclipse for development, you'll want to download the eclipse plugin.

If you know how to use the Eclipse Update Manager:

Update Site URL: http://javadude.googlecode.com/svn/trunk/com.javadude.updatesite
Select Annotations.

If you're not sure how to use the Eclipse Update Manager open the Eclipse Help->Help Contents, and read the section under

> Workbench User Guide->Tasks->Updating Features with the update Manager->Installing new features with the update manager.

#### Project Setup ####
If you're creating a plugin, you can add a dependency to the com.javadude.annotation plugin. Otherwise:

  * Download javadude-annotation\_2.1.6.jar and import it into your project. (Alternatively you can place it in another project, export it, and add the other project as a dependency)
  * Right-click on the jar and choose Build Path->Add to Build Path. (This gives you access to the annotation definitions so you can use them in your code.)
  * Open your project properties (Select the project and press Alt-Enter)
  * Go to Java Compiler->Annotation Processing
  * Check Enable project specific settings
  * Uncheck Enable processing in editor (not really necessary but can improve performance slightly)
  * The plugin will automatically process the @Bean and @ExtractInterface annotations.


#### In Ant ####
You'll need to download both [javadude-annotation\_2.1.6.jar](http://javadude.googlecode.com/svn/trunk/com.javadude.annotation/javadude-annotation-2.1.6.jar) and  [velocity-dep-1.5.jar](http://javadude.googlecode.com/svn/trunk/com.javadude.annotation/lib/velocity-dep-1.5.jar).

Sample ant build script:

NOTE: The apt task in ant 1.7.1 seems to not work well here... Please use ant 1.7.0 for now

```
<project name="foo" default="build">
    <property environment="env"/>
    <property name="apt.generated.source" value="generated" />
    <property name="classes" value="javac-out" />
    <property name="source" value="src" />
    <property name="annotation.jar" value="jar-location/javadude-annotation-2.1.6.jar" />
    <property name="velocity.jar" value="jar-location/velocity-dep-1.5.jar" />
    <target name="build">
        <mkdir dir="${apt.generated.source}"/>
        <mkdir dir="${classes}"/>
        <!-- if compiling for java 5 target, you can just do the <apt> task with compile="true
             if compiling for java 6 target, you must do compile="false" and run a separate <javac> -->
        <apt compile="false" preprocessdir="${apt.generated.source}" destdir="${classes}">
            <compilerarg value="-J-Dannotations.to.hush.during.apt=java.lang.Override,javax.annotation.Generated"/>
            <src location="${source}"/>
            <classpath path="${annotation.jar};${velocity.jar}"/>
        </apt>
        <javac destdir="javac-out" source="1.6" target="1.6">
            <src location="${source}"/>
            <src location="${apt.generated.source}"/>
            <classpath path="${annotation.jar}"/> <!-- only needed for compile, not for runtime! -->
        </javac>
    </target>
    <target name="clean">
        <delete dir="${classes}" />
        <delete dir="${apt.generated.source}" />
    </target>
</project>
```


### Runtime ###
The generated code does not have any dependencies; you can run without adding extra jars to your classpath!