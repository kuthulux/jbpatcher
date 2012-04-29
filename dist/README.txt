jbpatch, version 1.0.1
======================

jbpatch is an extremely powerful tool that allows to change almost
all aspects of your Kindle's behavior (at least the parts that have been
implemented in Java).

It is an unintrusive as possible (it only requires a benign modification
of a single file on the device), and you can enable or disable as many
modifications as you want.

Technically, it works by intercepting Java's class loading process,
allowing class definitions to be modified BEFORE they are loaded. Every
patch can be distributed as a single, standalone file, and can (in
principle) change as few or as many aspects of the system as required.

Patches reside in the opt/jbpatch directory on the Kindle, and are
controlled through the CONFIG.TXT file in the same directory.

INSTALLATION
------------

Because this is still a development version, there is no fancy one-click
installer yet, but you have to manually copy around files. This is on
purpose, because I need to be at least reasonably sure that you know what
you are doing, by requiring you to have access to the full file system on
your Kindle via SSH or SCP.

If you are upgrading from a previous version: the naming conventions have
changed a few times. Just follow these instructions, at the same time
removing leftovers from previous installations. That is:
* remove /opt/amazon/ebook/lib/{kpatcher|jbpatcher}.jar, if existing
* remove /mnt/us/jbpatcher directory
* update the xinit.args below accordingly, using common sense.

That said, installation is straightforward:
1. copy jbpatch.jar to /opt/amazon/ebook/lib/
2. modify /opt/amazon/ebook/bin/init.xargs:
   - insert "-istart jbpatch.jar" as the very first "-istart" line.
3. to actually take advantage of the "pluggable" patches, create the
   directory "/mnt/us/opt/jpatch/", and copy CONFIG.TXT and all *.jbpatch
   files into it.
4. Reboot your Kindle.

VERIFYING THAT THE INSTALLATION WORKED
--------------------------------------

Go to Menu > Settings, Menu > Device Info. You should see a new entry
telling you how many patches are currently active. NOTE that "active"
is not necessarily the same number as "enabled", because a patch only
becomes active when its functionality has been accessed at least once
(for instance, the TTS patch will only show up as active after you
have tried to access TTS-related functionality. This is because
classes are only loaded lazily ("on demand") in Java.

You can also check /tmp/jbpatch.log on the device.


INTEGRATING NEW PATCHES
-----------------------

Copy the respective .jbpatch files to opt/jbpatch/ on the USB drive,
and modify CONFIG.TXT accordingly. Chances are that you will need to
restart the device, because patches can only interfere with the system
when a class is initially loaded.

DEVELOPING NEW PATCHES
----------------------

This is only a framework for patches, and its usefulness is directly
proportional to the number of available patches. I created a few patches
to demonstrate both what it can do, and to provide useful examples to
get started. If you're interested in developing your own patches, you
are more than welcome! 

1. Download the source code from https://bitbucket.org/ixtab/jbpatcher
2. Take a look at com.mobileread.ixtab.patch.TTSPatch,
   com.mobileread.ixtab.patch.LegalIllegalpatch,
   com.mobileread.ixtab.jbpatch.builtin.DeviceInfoPatch, and most importantly
   com.mobileread.ixtab.jbpatch.Patch.
   The examples show various approaches to modify byte code, and the
   "Patch" class gives some hints about the general usage.
3. If you haven't already done so, get familiar with the JVM specification,
   and a few helpful tools (like Java Bytecode Editor, and JAD). Be
   prepared for frustratingly slow progress, and frustratingly many weird
   errors as you slowly make your way through Reverse Engineering
   Wonderland. And don't despair, because after going through it, the
   reward is great.
4. Post your patches on mobileread so everyone can benefit from them!
   And feel free to post (non-trivial) questions there as well. ;-)

UNINSTALLING
------------

Simply remove the line that you added to /opt/amazon/ebook/bin/init.xargs.
You may also want to clean up the files and directories you created during
installation, but strictly speaking, this is optional.



ACKNOWLEGDEMENTS
----------------

This software would not have been possible without the truely excellent
Serp framework: http://serp.sourceforge.net/
