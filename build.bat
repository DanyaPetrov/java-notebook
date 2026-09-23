@echo off
chcp 65001 >nul
setlocal

REM Переходим в папку, где лежит этот скрипт (корень проекта)
cd /d "%~dp0"

echo === Сборка проекта Notebook ===
echo.

REM Проверяем, установлен ли Maven
where mvn >nul 2>nul
if errorlevel 1 (
    echo [ОШИБКА] Maven не найден в PATH.
    echo Установи Maven или добавь его в переменную PATH.
    pause
    exit /b 1
)

echo Maven найден. Запуск: mvn clean package
echo.

call mvn clean package
if errorlevel 1 (
    echo.
    echo [ОШИБКА] Сборка провалилась. Смотри сообщения выше.
    pause
    exit /b 1
)

echo.
echo === Сборка успешно завершена ===
echo Смотри JAR-файл в папке target\
pause
endlocal