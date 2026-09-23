@echo off
chcp 65001 >nul
setlocal

cd /d "%~dp0"

echo === Запуск Notebook ===
echo.

REM Если JAVA_HOME задан — используем именно его
if defined JAVA_HOME (
    set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVA_CMD=java"
)

REM Проверяем, что Java вообще доступна
"%JAVA_CMD%" -version >nul 2>nul
if errorlevel 1 (
    echo [ОШИБКА] Java не найдена.
    echo JAVA_HOME = %JAVA_HOME%
    echo Убедись, что переменная JAVA_HOME указывает на JDK, или что java есть в PATH.
    pause
    exit /b 1
)

echo Используется Java:
"%JAVA_CMD%" -version
echo.

REM Ищем shaded JAR: исключаем original-*, sources, javadoc
set "JAR="
for %%f in (target\*.jar) do (
    echo %%~nxf | findstr /b /c:"original-" >nul
    if errorlevel 1 (
        echo %%~nxf | findstr /b /c:"sources" >nul
        if errorlevel 1 (
            echo %%~nxf | findstr /b /c:"javadoc" >nul
            if errorlevel 1 set "JAR=%%f"
        )
    )
)

if not defined JAR (
    echo [ОШИБКА] JAR-файл не найден в target\.
    echo Сначала запусти build.bat, чтобы собрать проект.
    echo.
    echo Содержимое папки target\:
    dir target\ 2>nul
    pause
    exit /b 1
)

echo Найден JAR: %JAR%
echo Запуск: "%JAVA_CMD%" -jar "%JAR%"
echo.

"%JAVA_CMD%" -jar "%JAR%"

echo.
echo === Программа завершена ===
pause
endlocal