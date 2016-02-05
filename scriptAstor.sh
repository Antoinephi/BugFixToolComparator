#!/bin/bash

#se placer dans le dossier du projet puis faire:
#mvn clean test 

#pour le premier parametre pas de souci mais pour le failing il faut récuperer le nom des tests à chaque fois...
#pour le troisieme il faut prevoir un jar avec junit sur le serveur

#/home/once/Documents/jdk1.7.0_79/bin/java -cp astor-0.0.2-SNAPSHOT-jar-with-dependencies.jar fr.inria.main.evolution.MainjGenProg -location "/home/once/Documents/wk-spoon/IntroClassJava/dataset/median/3b2376ab97bb5d1a5dbbf2b45cf062db320757549c761936d19df05e856de894e45695014cd8063cdc22148b13fa1803b3c9e77356931d66f4fbec0efacf7829/003/" -failing "introclassJava.median_3b2376ab_003BlackboxTest:introclassJava.median_3b2376ab_003WhiteboxTest" -dependencies "/home/once/Documents/wk-spoon/astor_exec/junit-4.11.jar" -package "introclassJava"

#mvn install:install-file -Dfile=/home/once/Documents/wk-spoon/astor_exec/astor-0.0.2-SNAPSHOT-jar-with-dependencies.jar -DgroupId=fr.idl.lib -#DartifactId=astor -Dversion=1 -Dpackaging=jar -DlocalRepositoryPath=/home/once/Documents/wk-spoon/astorRunnerReporter/lib

/home/once/Documents/jdk1.7.0_79/bin/java -jar astorRunnerReporter.jar

 
