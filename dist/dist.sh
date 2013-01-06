#!/bin/sh

# This file will probably only work on a Linux system.
# It requires NiLuJe's kindletool, version >= 1.5.0

PRODUCT=jbpatch
VERSION=3.1.0

firmware=$1

fail() {
	echo "$1"
	exit 1
}

if [ "x$firmware" = "x" ]; then
	fail "Usage: $0 firmware"
	exit 1;
fi

if [ ! -d "${firmware}/install" ]; then
	fail "Argument '$firmware' is very likely wrong, no subdirectory exists."
fi

rm update_${PRODUCT}_*_${firmware}_*install.bin ${PRODUCT}_*_${firmware}.zip


# get rid of symlinks.
PID=`echo "$$"`
TMPDIR="/tmp/ixtab_${PID}"
#TMPDIR="/tmp/ixtab_tmp"

mkdir -p "$TMPDIR" || fail "Failed to create directory: $TMPDIR"

cp -rL "${firmware}/install" "$TMPDIR"
cp -rL "${firmware}/uninstall" "$TMPDIR"

DIR_INSTALL="${TMPDIR}/install"
DIR_UNINSTALL="${TMPDIR}/uninstall"

if [ ! -d "$DIR_INSTALL" ]; then
	fail "Something went wrong - directory $DIR_INSTALL does not exist."
fi

if [ ! -d "$DIR_UNINSTALL" ]; then
	fail "Something went wrong - directory $DIR_UNINSTALL does not exist."
fi

# Kindle Touch, firmwares 5.1.0 - 5.1.2
if [ "$firmware" = "fw510" ]; then
	kindletool create ota2 -d k5w -d k5g -d k5gb -d k5u -C "$DIR_INSTALL" update_${PRODUCT}_${VERSION}_${firmware}_install.bin || fail "oops, packing installer failed"
	kindletool create ota2 -d k5w -d k5g -d k5gb -d k5u -C "$DIR_UNINSTALL" update_${PRODUCT}_${VERSION}_${firmware}_uninstall.bin || fail "oops, packing uninstaller failed"
	zip -9 -r ${PRODUCT}_${VERSION}_${firmware}.zip update_${PRODUCT}_${VERSION}_${firmware}_install.bin update_${PRODUCT}_${VERSION}_${firmware}_uninstall.bin *.txt || fail "oops, zipping failed"
fi

# Kindle Paperwhite, firmware 5.3.1
if [ "$firmware" = "fw531" ]; then
	kindletool create ota2 -d pw -d pwg -d pwgb -C "$DIR_INSTALL" update_${PRODUCT}_${VERSION}_${firmware}_install.bin || fail "oops, packing installer failed"
	kindletool create ota2 -d pw -d pwg -d pwgb -C "$DIR_UNINSTALL" update_${PRODUCT}_${VERSION}_${firmware}_uninstall.bin || fail "oops, packing uninstaller failed"
	zip -9 -r ${PRODUCT}_${VERSION}_${firmware}.zip update_${PRODUCT}_${VERSION}_${firmware}_install.bin update_${PRODUCT}_${VERSION}_${firmware}_uninstall.bin *.txt || fail "oops, zipping failed"
fi

rm -rf "$TMPDIR" || echo "WARNING: failed to remove $TMPDIR"
