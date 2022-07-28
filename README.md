# jakarta-migration-java

This project aims at supporting the migration of Java applications from Java EE 8 to Jakarta EE 10;

It does so by proving a command line utility which can automate or support in performing the steps described in
[Migrating Complete Duke from Jakarta EE 8 to Jakarta EE 9](https://github.com/ivargrimstad/jakartaee-duke/tree/master/complete-duke);

Please note the first two steps described in [Migrating Complete Duke from Jakarta EE 8 to Jakarta EE 9](https://github.com/ivargrimstad/jakartaee-duke/tree/master/complete-duke) are switched in order; this is because it's easier to change imports as first step and the update the dependencies to make the project build again;

## Fix the imports

```shell
java -jar target/jakarta-migration.jar -dir <PATH_TO_ROOT_FOLDER_OF_YOUR_MAVEN_PROJECT> --java-sources
```

e.g.
```shell
java -jar target/jakarta-migration.jar -dir /home/user/projects/my_maven_project --java-sources
```

Note: after executing this command your project won't build anymore (`mvn package` will fail) because 
`javax.*` imports have been replaced with `jakarta.*` imports

## Update the Jakarta EE version in pom.xml

Note: this utility doesn't replace dependencies, it just analyzes the pom.xml files in your project 
and highlights the dependencies which need  to be replaced; 
it does so by printing out the dependency tree obtained with `mvn dependency:tree` and highlighting 
those dependencies with a different color

```shell
java -jar target/jakarta-migration.jar -dir <PATH_TO_ROOT_FOLDER_OF_YOUR_MAVEN_PROJECT> --dependencies
```

## XML Schema Namespaces

```shell
java -jar target/jakarta-migration.jar -dir <PATH_TO_ROOT_FOLDER_OF_YOUR_MAVEN_PROJECT> --xml-schema-namespaces
```

## Rename properties prefixed with javax
```shell
java -jar target/jakarta-migration.jar -dir <PATH_TO_ROOT_FOLDER_OF_YOUR_MAVEN_PROJECT> --properties
```


