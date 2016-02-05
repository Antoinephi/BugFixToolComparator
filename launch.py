# !/usr/bin/python3.4
import sys
import os
import subprocess
import time

###Fixit

def fixit(path):
	print(path)
	subprocess.call('java -jar fixit-1-jar-with-dependencies.jar ' + path, shell=True, timeout=300)


###Nopol

###Astor

def astor(dirname):
	subprocess.call('mvn install -f ' + dirname + '/pom.xml', shell=True, timeout=300)


start_time = time.time()

# argv[1] = IntroClassJava dir's path
for dirname, dirnames, filenames in os.walk(sys.argv[1]):
  # print path to all subdirectories first.
  for subdirname in dirnames:
    if subdirname == 'src' :
    	if sys.argv[2] == '-fixit' or sys.argv[2] == '-all':
    		fixit(os.path.join(dirname, subdirname))
    	if sys.argv[2] == '-astor' or sys.argv[2] == '-all':
    		astor(dirname) 
      


print("--- %s seconds ---" % (time.time() - start_time))

