# !/usr/bin/python3.4
import sys
import os
import subprocess
import time

def astor(dirname):
	subprocess.call('mvn -f' + dirname + '/pom.xml clean install', shell=True, timeout=300)


for dirname, dirnames, filenames in os.walk(sys.argv[1]):
	# print path to all subdirectories first.
	for subdirname in dirnames:
		if subdirname == 'src' :
			astor(dirname)
