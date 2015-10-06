@echo off
cd Z:\AndroidStudioProjects\Wifi_Mac_Change
rmdir /S build
rmdir /S RootShell\build
rmdir /S RootTools\build
rmdir /S wifimacchange\build
del *.apk
gradle clean