# !/usr/bin/python3.4
import sys
import os

#argv[1] = IntroClassJava dir's path
for dirname, dirnames, filenames in os.walk(sys.argv[1]):
  # print path to all subdirectories first.
  for subdirname in dirnames:
    if subdirname == 'src' :
      print(os.path.join(dirname, subdirname))
      os.popen('java -jar fixit-1-jar-with-dependencies.jar ' + os.path.join(dirname, subdirname))
