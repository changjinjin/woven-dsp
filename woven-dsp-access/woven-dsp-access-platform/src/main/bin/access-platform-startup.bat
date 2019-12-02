@echo off
rem ======================================================================
rem windows startup script
rem
rem author: geekidea
rem date: 2018-12-2
rem ======================================================================

rem Open in a browser
rem start "" "http://localhost:8080/api/visul/doc.html"

rem startup jar
java -jar ../boot/@project.build.finalName@.jar --logging.config=../conf/logback-spring.xml --spring.config.location=../conf/

pause
