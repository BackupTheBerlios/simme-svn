SimME Client Readme
http://simme.berlios.de


This is the SimME client release. The SimME client builds on MIDP 1.0 and
can be installed on any current mobile phone. This release comes in two flavours
which will be explained in more detail.

#################
## Binary Release

The binary release consists of two files, debug files were stripped from this 
release and will not show up in future binary releases. SimME.jar and SimME.jad
may be used to install the software on a real device instead of just running it
in the emulator.

The SimME.jad files points to the einspiel.at server, where database and user
management are provided. You may change the attribute "MIDlet-Jar-URL" to point
to the local SimME.jar file, if you do not want to download it via your mobile
phone.


#################
## Source Release

This release contains all the sources and test sources needed to build the SimME
client. You will need some libraries in the $dir.lib directory:

  * antenna.jar (Antenna - http://antenna.sourceforge.net/)

Please have a look at the developer documentation at the website. It will be
updated within the next days.


########################
## Bugs and Known Issues

Please consult the bug pages at

http://developer.berlios.de/bugs/index.php?group_id=730



Kariem