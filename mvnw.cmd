@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements. See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership. The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License. You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied. See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0_NAME__%"=="" (SET "MAVEN_BASEDIR=%~dp0") ELSE (SET "MAVEN_BASEDIR=%__MVNW_ARG0_NAME__%")
@SET "MAVEN_BASEDIR=%MAVEN_BASEDIR:~0,-1%"

@SET "WRAPPER_PROPERTIES=%MAVEN_BASEDIR%\.mvn\wrapper\maven-wrapper.properties"

@FOR /F "tokens=1,2 delims==" %%A IN (%WRAPPER_PROPERTIES%) DO (
  @IF "%%A"=="distributionUrl" SET "DISTRIBUTION_URL=%%B"
)

@SET "MAVEN_USER_HOME=%USERPROFILE%\.m2"
@SET "MAVEN_DIST_DIR=%MAVEN_USER_HOME%\wrapper\dists"

@FOR %%F IN ("%DISTRIBUTION_URL%") DO SET "DISTRIBUTION_ID=%%~nF"
@SET "DISTRIBUTION_DIR=%MAVEN_DIST_DIR%\%DISTRIBUTION_ID%"

@IF NOT EXIST "%DISTRIBUTION_DIR%" (
  @ECHO Downloading Maven from %DISTRIBUTION_URL%
  @MKDIR "%DISTRIBUTION_DIR%"
  @powershell -Command "Invoke-WebRequest -Uri '%DISTRIBUTION_URL%' -OutFile '%DISTRIBUTION_DIR%\maven.zip'"
  @powershell -Command "Expand-Archive -Path '%DISTRIBUTION_DIR%\maven.zip' -DestinationPath '%DISTRIBUTION_DIR%' -Force"
  @DEL "%DISTRIBUTION_DIR%\maven.zip"
)

@FOR /D %%D IN ("%DISTRIBUTION_DIR%\apache-maven-*") DO SET "MAVEN_HOME=%%D"

@IF "%MAVEN_HOME%"=="" (
  @ECHO ERROR: Could not find Maven installation in %DISTRIBUTION_DIR%
  @EXIT /B 1
)

@"%MAVEN_HOME%\bin\mvn.cmd" %*
