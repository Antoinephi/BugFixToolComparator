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

def nopol(result, mode):
	stdout = result.communicate()[0]
	file = open('result_nopol.txt', 'a')
	patch_found = False
	if '----PATCH FOUND----' in str(stdout):
		for line in str(stdout).split('\\n'):
			if patch_found:
				file.write('----PATCH FOUND----\n-' + mode + '\n' + dirname +'\n' +line +'\n')
				patch_found = False
			if '----PATCH FOUND----' in line:
				patch_found = True 
	file.close()

def nopol_default(dirname):
	subprocess.call('mvn clean test -f ' + dirname + '/pom.xml', shell=True, timeout=300)
	subprocess.call('mvn package -f ' + dirname + '/pom.xml', shell=True, timeout=300)

	result = subprocess.Popen(('java -Xmx8096M -Xms8096M -jar ../nopol/nopol/target/nopol-0.0.3-SNAPSHOT-jar-with-dependencies.jar -p ../nopol/nopol/lib/z3/z3_for_linux -s ' + dirname + ' -c ' + dirname + '/target/classes:' + dirname +'/target/test-classes:junit-4.11.jar').split(' ') , stdout=subprocess.PIPE)
	nopol(result, 'conditionnal/smt')
	# subprocess.call('java -Xmx8096M -Xms8096M -jar  ../nopol/nopol/target/nopol-0.0.3-SNAPSHOT-jar-with-dependencies.jar -p ../nopol/nopol/lib/z3/z3_for_linux -s ' + dirname + ' -c ' + dirname + '/target/classes:' + dirname +'/target/test-classes:junit-4.11.jar', shell=True, timeout=300)	


def nopol_precond_smt(dirname):
	subprocess.call('mvn clean test -f ' + dirname + '/pom.xml', shell=True, timeout=300)
	subprocess.call('mvn package -f ' + dirname + '/pom.xml', shell=True, timeout=300)
	result = subprocess.Popen(('java -Xmx8096M -Xms8096M -jar ../nopol/nopol/target/nopol-0.0.3-SNAPSHOT-jar-with-dependencies.jar -e precondition -p ../nopol/nopol/lib/z3/z3_for_linux -s ' + dirname + ' -c ' + dirname + '/target/classes:' + dirname +'/target/test-classes:junit-4.11.jar').split(' ') , stdout=subprocess.PIPE)
	nopol(result, 'preconditionnal/smt')
	# subprocess.call('java -Xmx8096M -Xms8096M -jar  ../nopol/nopol/target/nopol-0.0.3-SNAPSHOT-jar-with-dependencies.jar  -e precondition  -p ../nopol/nopol/lib/z3/z3_for_linux -s ' + dirname + ' -c ' + dirname + '/target/classes:' + dirname +'/target/test-classes:junit-4.11.jar', shell=True, timeout=300)	


def nopol_cond_dynamoth(dirname):
	subprocess.call('mvn clean test -f ' + dirname + '/pom.xml', shell=True, timeout=300)
	subprocess.call('mvn package -f ' + dirname + '/pom.xml', shell=True, timeout=300)
	subprocess.call('java -Xmx8096M -Xms8096M -jar  ../nopol/nopol/target/nopol-0.0.3-SNAPSHOT-jar-with-dependencies.jar -y dynamoth -p ../nopol/nopol/lib/z3/z3_for_linux -s ' + dirname + ' -c ' + dirname + '/target/classes:' + dirname +'/target/test-classes:junit-4.11.jar', shell=True, timeout=300)

def nopol_precond_dynamoth(dirname):
	subprocess.call('mvn clean test -f ' + dirname + '/pom.xml', shell=True, timeout=300)
	subprocess.call('mvn package -f ' + dirname + '/pom.xml', shell=True, timeout=300)
	subprocess.call('java -Xmx8096M -Xms8096M -jar  ../nopol/nopol/target/nopol-0.0.3-SNAPSHOT-jar-with-dependencies.jar -e precondition -y dynamoth -p ../nopol/nopol/lib/z3/z3_for_linux -s ' + dirname + ' -c ' + dirname + '/target/classes:' + dirname +'/target/test-classes:junit-4.11.jar', shell=True, timeout=300)	

start_time = time.time()

# Compilation de Nopol

# subprocess.call('mvn install  -DskipTests=true -f  ../nopol/nopol/pom.xml', shell=True, timeout=300)
file = open('result_nopol.txt', 'w')
file.write('')
file.close()


# argv[1] = IntroClassJava dir's path
for dirname, dirnames, filenames in os.walk(sys.argv[1]):
	# print path to all subdirectories first.
	for subdirname in dirnames:
		if subdirname == 'src' :
			if sys.argv[2] == '-fixit' or sys.argv[2] == '-all':
				fixit(os.path.join(dirname, subdirname))
			if sys.argv[2] == '-astor' or sys.argv[2] == '-all':
				astor(dirname)
			if sys.argv[2] == '-nopol' or sys.argv[2] == '-all':
				nopol_default(dirname)
				nopol_precond_smt(dirname)
				# nopol_precond_dynamoth(dirname)
				# nopol_cond_dynamoth(dirname)


print("--- %s seconds ---" % (time.time() - start_time))
