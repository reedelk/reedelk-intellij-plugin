<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.esb</groupId>
        <artifactId>module-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <groupId>${groupId}</groupId>
    <version>${version}</version>
    <artifactId>${artifactId}</artifactId>
    <packaging>bundle</packaging>

    <properties>
        <maven.compiler.target>${javaVersion}</maven.compiler.target>
        <maven.compiler.source>${javaVersion}</maven.compiler.source>
    </properties>

    <dependencies>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.felix</groupId>
                <artifactId>maven-bundle-plugin</artifactId>
                <version>${r"${maven.bundle.plugin.version}"}</version>
                <extensions>true</extensions>
                <configuration>
                    <instructions>
                        <Bundle-Name>${r"${project.name}"}</Bundle-Name>
                        <Bundle-SymbolicName>${r"${project.artifactId}"}</Bundle-SymbolicName>
                        <Embed-Dependency>*;scope=compile|runtime</Embed-Dependency>
                        <Embed-Transitive>true</Embed-Transitive>
                        <ESB-Module>true</ESB-Module>
                    </instructions>
                </configuration>
            </plugin>
        </plugins>
    </build>


</project>