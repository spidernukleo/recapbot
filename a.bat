@echo off
setlocal enabledelayedexpansion

set KEY=AIzaSyCHIvLdZjPJXtBKvMZAoFTsQoV_-xyfc5E
set OUTDIR=gemini-tests

if not exist "%OUTDIR%" mkdir "%OUTDIR%"

echo model,http_code > "%OUTDIR%\summary.csv"

for %%M in (
  gemini-2.0-flash
  gemini-2.0-flash-001
  gemini-2.0-flash-lite
  gemini-2.0-flash-lite-001
  gemini-2.5-flash
  gemini-2.5-flash-lite
  gemini-2.5-pro
  gemini-flash-latest
  gemini-flash-lite-latest
  gemini-pro-latest
  gemini-3-flash-preview
  gemini-3-pro-preview
  gemini-3.1-flash-lite
  gemini-3.1-flash-lite-preview
  gemini-3.1-pro-preview
  gemma-4-26b-a4b-it
  gemma-4-31b-it
) do (
  echo Testing %%M ...

  curl -s -o "%OUTDIR%\%%M.json" -w "%%{http_code}" ^
    "https://generativelanguage.googleapis.com/v1beta/models/%%M:generateContent?key=%KEY%" ^
    -H "Content-Type: application/json" ^
    -X POST ^
    -d "{\"contents\":[{\"parts\":[{\"text\":\"Say hello in one short sentence.\"}]}]}" > "%OUTDIR%\%%M.status"

  set /p CODE=<"%OUTDIR%\%%M.status"
  echo %%M,!CODE!>> "%OUTDIR%\summary.csv"
)

del "%OUTDIR%\*.status"

echo.
echo Done.
echo Results saved in folder: %OUTDIR%
echo Summary file: %OUTDIR%\summary.csv