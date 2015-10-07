@echo off
cd Z:\AndroidStudioProjects\Wifi_Mac_Change
rmdir /S build /Q
rmdir /S RootShell\build /Q
rmdir /S RootTools\build /Q
rmdir /S wifimacchange\build /Q
del *.apk /Q

call gradle clean
rmdir /S .gradle /Q
rmdir /S gradle /Q

call git add -A -f
call git commit -m "# clean"
call git push origin master

:end