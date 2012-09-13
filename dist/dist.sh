PRODUCT=jbpatch
VERSION=2.3.2

rm *.bin *.zip

# This requires NiLuJe's kindletool, version >= 1.5.0
kindletool create ota2 -d k5w -d k5g -d k5gb -d k5u -C src/install update_${PRODUCT}_${VERSION}_install.bin
kindletool create ota2 -d k5w -d k5g -d k5gb -d k5u -C src/uninstall update_${PRODUCT}_${VERSION}_uninstall.bin

zip -9 -r ${PRODUCT}_${VERSION}.zip update_${PRODUCT}_${VERSION}_install.bin update_${PRODUCT}_${VERSION}_uninstall.bin *.txt src/

