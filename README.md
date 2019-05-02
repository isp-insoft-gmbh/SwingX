# SwingX

This project is a fork of the original SwingX version 1.6.5.

## How to build

This project uses [maven](https://maven.apache.org/) in order to produce
usable jar files. Besides Maven you need a JDK and have your `JAVA_HOME`
environment variable set to your jdk installation folder. Currently this
project has only been tested with Java 1.8. The compatibility with Java 9+
is planned.

In order to build the project, run:

```shell
mvn package
```

The produced jar files will be in the `target` sub-folders of the
sub-projects.

Alternatively you can install the output into your local artifactory via:

```shell
mvn install
```

The build will produce the libraries for all sub-projects, the jar files
that contain the source-code and the jar files that contain the javadoc.

## Changes

Since the fork was created, the following changes have been made, but are not
part of the git history.
  * Backport of bugfix for issue [4330950](https://bugs.openjdk.java.net/browse/JDK-4330950)

All following changes will be part of the git history.