# !/usr/bin/python3.4
import sys
import os
import subprocess
import time

start_time = time.time()

#argv[1] = IntroClassJava dir's path
for dirname, dirnames, filenames in os.walk(sys.argv[1]):
  # print path to all subdirectories first.
  for subdirname in dirnames:
    if subdirname == 'src' :
      # print(os.path.join(dirname, subdirname))
      launch_fixit(os.path.join(dirname, subdirname))


print("--- %s seconds ---" % (time.time() - start_time))

###Fixit

def launch_fixit(path):
  print(path)
  subprocess.call('java -jar fixit-1-jar-with-dependencies.jar ' + path, shell=True, timeout=300)

###Nopol

###Astor