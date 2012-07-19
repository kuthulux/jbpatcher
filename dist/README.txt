JBPatch, version 2.0.0
======================

JBPatch is an extremely powerful tool that allows to change almost
all aspects of your Kindle's behavior (at least the parts that have been
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
is available at the Wiki page:
http://wiki.mobileread.com/wiki/JBPatch
BTW: You are more than welcome to contribute to that page!

The most up-to-date discussion about this software can be found at:
http://www.mobileread.com/forums/showthread.php?t=175512

PREREQUISITES
------------
In order to be able to install this, your device has to have the
jailbreak installed. If you want to use the User Interface, you will
also need the Kindlet jailbreak.

JAILBREAK
---------
See http://wiki.mobileread.com/wiki/Kindle_Touch_Hacking#Jailbreak

INSTALLATION
------------
1. Plug your Kindle to your computer
2. Copy update_jbpatch-*.install.bin directly onto the Kindle device
   (i.e., not into any sub-directory)
3. Restart your Kindle using Menu > Settings, Menu > Update.
4. If you do not see all the expected results directly after the
   installation, reboot your Kindle once more.


KINDLET JAILBREAK
-----------------
The user interface of JBPatch requires permissions not normally available to
Kindle applications. If you try to access the UI part, but only get an error
that the Kindlet Jailbreak is missing, then please install the Kindlet
Jailbreak from http://ge.tt/4wapSaK/v/02 to allow access to these restricted
permissions.

VERIFYING THAT THE INSTALLATION WORKED
--------------------------------------
Go to Menu > Settings, Menu > Device Info. You should see a new entry
telling you how many patches are currently active, and how many are
available, along with the version of jbpatch that you have installed.

Note:
- it is normal that the "available" count is larger than the number of
  patch files that you specified in the configuration file. A single patch
  can potentially alter behavior in many classes.
- it is also normal that the "active" count is smaller than the "available"
  count. Patches only become active when they are actually needed.

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


ACKNOWLEDGEMENTS
----------------
This software would not have been possible without the truely excellent
Serp framework: http://serp.sourceforge.net/

I would also like to thank everybody at mobileread who helped with the
localization, and who provided valuable feedback. You know who you are.
