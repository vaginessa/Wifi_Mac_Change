set STUDIO_JDK="C:\Program Files\Android\Android Studio\jre"
set JRE_HOME=%STUDIO_JDK%
set JDK_HOME=%STUDIO_JDK%
set JAVA_HOME=%STUDIO_JDK%
set GRADLE_HOME=C:\Program Files\Android\Android Studio\gradle\gradle-3.3
set GIT_HOME=%HOMEPATH%\Downloads\asm301\Git
set PATH=%STUDIO_JDK%\bin;%GRADLE_HOME%\bin;%GIT_HOME%\bin

cd %HOMEPATH%\Downloads\asm301\AndroidStudioProjects\Wifi_Mac_Change

del /Q *.apk
rmdir /S /Q build
rmdir /S /Q RootShell\build
rmdir /S /Q RootTools\build
rmdir /S /Q wifimacchange\build

gradle clean

rmdir /S /Q .gradle
rmdir /S /Q gradle

git add -A -f
git commit -m "# clean"
git push origin master