jbpatch, version 1.3.1
======================

jbpatch is an extremely powerful tool that allows to change almost
all aspects of your Kindle's behavior (at least the parts that have been
implemented in Java).

It is as unintrusive as possible (it only requires a benign modification
to a single file on the device), and you can enable or disable as many
modifications as you want.

Technically, it works by intercepting Java's class loading process,
allowing class definitions to be modified BEFORE they are loaded. Every
patch can be distributed as a single, standalone file, and can (in
principle) change as few or as many aspects of the system as required.

Patches reside in the opt/jbpatch directory on the Kindle, and are
controlled through the CONFIG.TXT file in the same directory.

The most up-to-date information about this software is available at:
http://www.mobileread.com/forums/showthread.php?t=175512

PREREQUISITES
------------
In order to be able to install this, your device has to have the
jailbreak installed.

INSTALLATION
------------
1. Plug your Kindle to your computer
2. Copy update_jbpatch-*.install.bin directly onto the Kindle device
   (i.e., not into any sub-directory)
3. Copy the entire directory named "opt" directly to your device.
   This directory (more specifically, the "jbpatch" subdirectory)
   contains and controls the patches that are actually active on
   your device. Consult opt/jbpatch/CONFIG.TXT for more information
   about how patches are applied.
4. Restart your Kindle using Menu > Settings, Menu > Update.
5. If you do not see all the expected results directly after the
   installation, reboot your Kindle once more.


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

You can also check /tmp/jbpatch.log on the device.

UNINSTALLING
------------
Repeat the same procedure used for installation, but using the
update_jbpatch-*.uninstall.bin file instead. Of course, the
files in opt/jbpatch are no longer required after you uninstalled
this, so you can delete them.

ADDING NEW PATCHES
------------------
Copy the respective patch files to opt/jbpatch/ on the USB drive,
and modify CONFIG.TXT accordingly. Chances are that you will need to
restart the device, because patches can only interfere with the system
when a class is initially loaded.

Note: if you updated the configuration file, please wait at least
5 seconds before restarting the device. This is needed to make sure
that your changes are actually applied.


TROUBLESHOOTING
---------------
While every effort has been taken to make this software as reliable
as possible, I cannot preclude unexpected failures. If you experience
problems, please report them at:
http://www.mobileread.com/forums/showthread.php?t=175512


DEVELOPING NEW PATCHES
----------------------
Please see the file DEVELOPERS.txt


ACKNOWLEDGEMENTS
----------------
This software would not have been possible without the truely excellent
Serp framework: http://serp.sourceforge.net/
