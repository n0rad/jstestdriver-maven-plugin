**I'm not working on this project anymore as jstestdriver is limited
I have stared another project which support more things** : https://github.com/awired/jstest-maven-plugin



Based on Burke Webster plugin :  http://code.google.com/p/jstd-maven-plugin/

with new features :
 - available in repo1
 - using default jstestdriver conf file location to be compatible with eclipse plugin 
 - jstestdriver provided by maven repository without dependencies needed
 - support coverage

known bugs on windows without advanced configuration :
 - cannot auto start windows browser without knowing his name (jstestdriver want to start it by executable and there is no command like open or xdg-open)
 - cannot auto support coverage because jstestdriver start looking jar from current directory and maven repo cannot be accessed

known constraints : 
 - a browser is needed
