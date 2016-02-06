# !/usr/bin/python3.4
import sys
import os
import subprocess
import time

##TODO : mvn install nopol pour générer le nouveau jar nopol

###Fixit

def fixit(path):
	print(path)
	subprocess.call('java -jar fixit-1-jar-with-dependencies.jar ' + path, shell=True, timeout=300)


###Nopol

def nopol(dirname):
	subprocess.call('mvn clean test -f ' + dirname + '/pom.xml', shell=True, timeout=300)
	subprocess.call('mvn package -f ' + dirname + '/pom.xml', shell=True, timeout=300)
	subprocess.call('java -jar  ../nopol/nopol/target/nopol-0.0.3-SNAPSHOT-jar-with-dependencies.jar -p ../nopol/nopol/lib/z3/z3_for_linux -s ' + dirname + ' -c ' + dirname + '/target/classes:' + dirname +'/target/test-classes:junit-4.11.jar', shell=True, timeout=300)	


start_time = time.time()

# argv[1] = IntroClassJava dir's path
for dirname, dirnames, filenames in os.walk(sys.argv[1]):
	# print path to all subdirectories first.
	for subdirname in dirnames:
		if subdirname == 'src' :
			if sys.argv[2] == '-fixit' or sys.argv[2] == '-all':
				fixit(os.path.join(dirname, subdirname))
			# if sys.argv[2] == '-astor' or sys.argv[2] == '-all':
			# 	astor(dirname)
			if sys.argv[2] == '-nopol' or sys.argv[2] == '-all':
				nopol(dirname)

print("--- %s seconds ---" % (time.time() - start_time))
