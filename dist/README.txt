JBPatch: Java Bytecode Patch
============================

JBPatch is a powerful tool that allows to change almost all aspects
of your Kindle's behavior (at least of the parts that have been
implemented in Java).

It is as unintrusive as possible (it only requires a benign modification
to a single file on the device), and you can enable or disable as many
modifications as you want.

Technically, it works by intercepting Java's class loading process,
allowing class definitions to be modified BEFORE they are loaded. Every
patch can be distributed as a single, standalone file, and can (in
principle) change as few or as many aspects of the system as required.

Patches, as well as their configuration, and localizations,
reside in the opt/jbpatch directory on the Kindle.

More user documentation, new patches, and additional localizations
are available at the Wiki page:
http://wiki.mobileread.com/wiki/JBPatch
BTW: You are more than welcome to contribute to that page!

The most up-to-date discussion about this software can be found at:
http://www.mobileread.com/forums/showthread.php?t=175512

PREREQUISITES
------------
Before you can install JBPatch, your device needs to have the
Jailbreak installed. If you want to use the User Interface, you will
also need the Kindlet Jailbreak.

JAILBREAK
---------
See http://wiki.mobileread.com/wiki/Kindle_Touch_Hacking#Jailbreak

INSTALLATION
------------
0. !!! If you are upgrading, make sure that you have read the notes at:
   http://www.mobileread.com/forums/showpost.php?p=2044458
1. Plug your Kindle to your computer
2. Copy update_jbpatch-*.install.bin directly onto the Kindle device
   (i.e., not into any sub-directory)
3. Restart your Kindle using Menu > Settings, Menu > Update.

KINDLET JAILBREAK
-----------------
The user interface of JBPatch requires permissions not normally available to
Kindle applications. If you try to access the UI part, but only get an error
that the Kindlet Jailbreak is missing, then please install the Kindlet
Jailbreak (kindlet-jailbreak-*.zip) from http://ge.tt/4wapSaK/ to allow access
to these restricted permissions.

VERIFYING THAT THE INSTALLATION WORKED
--------------------------------------
Go to Menu > Settings, Menu > Device Info. You should see a new entry
which indicates the JBPatch version.

You can also check /tmp/jbpatch.log on the device, or display the log in the UI.

UNINSTALLING
------------
Repeat the same procedure used for installation, but using the
update_jbpatch-*.uninstall.bin file instead. Of course, the
files in opt/jbpatch are no longer required after you uninstalled
this, so you can delete them.

ADDING NEW PATCHES
------------------
Copy the respective patch files to opt/jbpatch/ on the USB drive,
wait at least 5 seconds, and then restart the device. For a faster
alternative, you can use the "Restart Framework" button on the System
tab of the JBPatch UI to restart the framework without rebooting the device.


TROUBLESHOOTING
---------------
While every effort has been taken to make this software as reliable
as possible, I cannot preclude unexpected failures. If you experience
problems, please report them at:
http://www.mobileread.com/forums/showthread.php?t=175512


DEVELOPING NEW PATCHES
----------------------
Please take a look at the Wiki page.


SUPPORTING JBPATCH
------------------
JBPatch is free software - in both senses of "free beer" and "free speech".
However, its development takes a lot of time and effort. If you like
JBPatch and wish to support its future development, a small donation is very
much appreciated!
You can donate any amount that you see fit at http://ixtab.tk - thank you!


ACKNOWLEDGEMENTS
----------------
This software would not have been possible without the truely excellent
Serp framework: http://serp.sourceforge.net/

I would also like to thank everybody at mobileread who helped with the
localization, and who provided valuable feedback. You know who you are.
