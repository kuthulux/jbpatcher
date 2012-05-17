PRODUCT=jbpatch
VERSION=1.3.0

rm *.bin *.zip

java -jar ~/kindle-touch/localization/kindle-touch-l10n/tool/kt-l10n.jar kindletool -f -s src/install/ -t update_${PRODUCT}_${VERSION}_install.bin
java -jar ~/kindle-touch/localization/kindle-touch-l10n/tool/kt-l10n.jar kindletool -f -s src/uninstall/ -t update_${PRODUCT}_${VERSION}_uninstall.bin

zip -9 -r ${PRODUCT}_${VERSION}.zip update_${PRODUCT}_${VERSION}_install.bin update_${PRODUCT}_${VERSION}_uninstall.bin *.txt src/ opt/

