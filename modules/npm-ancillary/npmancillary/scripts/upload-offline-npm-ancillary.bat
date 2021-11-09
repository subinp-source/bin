@echo off
REM Upload Offline NPM Ancillary Module
REM Supports: Windows
REM 1. Downloads the npm-ancillary-module artifact from either the release repository or the snapshot repository. 
REM 2. Performs "npm ci on the npm resource folder"
REM 3. Zips the module and uploads to artifactory (release or snapshot)

REM Pre requisits: Install python 2.7, 7Zip and run this script as ADMINISTRATOR

SET WORKSPACE=%cd%\build

SET ARTIFACTORY_REPOSITORY_ID=hybris-repository
SET RELEASE_REPO_ID=milestones
SET SNAPSHOT_REPO_ID=snapshots

SET ARTIFACTORY_RELEASE_REPO=https://repository.hybris.com/hybris-release
SET ARTIFACTORY_SNAPSHOT_REPO=https://repository.hybris.com/hybris-snapshot
SET RELEASE_REPO=https://common.repositories.sap.ondemand.com/artifactory/deploy-milestones-cx-commerce-maven
SET SNAPSHOT_REPO=https://common.repositories.sap.ondemand.com/artifactory/deploy-snapshots-cx-commerce-maven

SET PROJECT_GROUPID=de.hybris.platform
SET PROJECT_ARTIFACTID=npm-ancillary-module
SET ARTIFACT_VERSION=%1

SET OFFLINE_PROJECT_ARTIFACT_ID=offline-win-%PROJECT_ARTIFACTID%

SET NPM_RESOURCE_HOME=%WORKSPACE%\hybris\bin\ext-content\npmancillary\resources\npm
SET NODE_HOME=%WORKSPACE%\%NPM_RESOURCE_HOME%\node\node-v10.24.0-win-x64

if "%ARTIFACT_VERSION%" == "" (
    ECHO "Usage upload-offline-npm-ancillary.bat ARTIFACT_VERSION"
    ECHO "Example: upload-offline-npm-ancillary.bat 6.6.0.0-RC4-SNAPSHOT"  
    exit /b -1
)

if "%ARTIFACT_VERSION:~-8%" == "SNAPSHOT" (
    SET TARGET_REPOSITORY=%SNAPSHOT_REPO%
    SET ARTIFACTORY_TARGET_REPO=%ARTIFACTORY_SNAPSHOT_REPO%
    SET REPOSITORY_ID=%SNAPSHOT_REPO_ID%
) else (
    SET TARGET_REPOSITORY=%RELEASE_REPO%
    SET ARTIFACTORY_TARGET_REPO=%ARTIFACTORY_RELEASE_REPO%
    SET REPOSITORY_ID=%RELEASE_REPO_ID%
)

ECHO Running upload-offline-npm-ancillary.sh
ECHO OS_NAME: %OS_NAME%
ECHO WORKSPACE: %WORKSPACE%
ECHO REPOSITORY_ID: %REPOSITORY_ID%
ECHO RELEASE_REPO: %RELEASE_REPO%
ECHO SNAPSHOT_REPO: %SNAPSHOT_REPO%
ECHO PROJECT_GROUPID: %PROJECT_GROUPID%
ECHO PROJECT_ARTIFACTID: %PROJECT_ARTIFACTID%
ECHO ARTIFACT_VERSION: %ARTIFACT_VERSION%
ECHO TARGET_REPOSITORY: %TARGET_REPOSITORY%
ECHO NODE_HOME: %NODE_HOME%
ECHO OFFLINE_PROJECT_ARTIFACT_ID: %OFFLINE_PROJECT_ARTIFACT_ID%

REM ----- main script
REM Create workspace and download artifact
rmdir /S /Q %WORKSPACE%
mkdir %WORKSPACE%
cd %WORKSPACE%

CALL mvn org.apache.maven.plugins:maven-dependency-plugin:2.4:get -Dartifact=%PROJECT_GROUPID%:%PROJECT_ARTIFACTID%:%ARTIFACT_VERSION%:zip -Ddest=%WORKSPACE%\%PROJECT_ARTIFACTID%-%ARTIFACT_VERSION%.zip

REM Install 7z open source zip tool before running the script
7z x %PROJECT_ARTIFACTID%-%ARTIFACT_VERSION%.zip

REM Run npm install from npm resource home folder

SET PATH=%PATH%;%NODE_HOME%
cd %NPM_RESOURCE_HOME%
CALL npm ci
rmdir /S /Q .cache

REM Go back to workspace folder, zip contents of the original artifact
cd %WORKSPACE%
del %PROJECT_ARTIFACTID%-%ARTIFACT_VERSION%.zip
7z a %OFFLINE_PROJECT_ARTIFACT_ID%-%ARTIFACT_VERSION%.zip .

REM "Deploying artifact to SAP artifactory"
CALL mvn deploy:deploy-file -DrepositoryId=%REPOSITORY_ID% -Durl=%TARGET_REPOSITORY% -Dfile=%WORKSPACE%\%OFFLINE_PROJECT_ARTIFACT_ID%-%ARTIFACT_VERSION%.zip -DgroupId=%PROJECT_GROUPID% -Dversion=%ARTIFACT_VERSION% -DartifactId=%OFFLINE_PROJECT_ARTIFACT_ID% -DgeneratePom=true

REM "Deploying artifact to hybris artifactory"
CALL mvn deploy:deploy-file -DrepositoryId=%ARTIFACTORY_REPOSITORY_ID% -Durl=%ARTIFACTORY_TARGET_REPO% -Dfile=%WORKSPACE%\%OFFLINE_PROJECT_ARTIFACT_ID%-%ARTIFACT_VERSION%.zip -DgroupId=%PROJECT_GROUPID% -Dversion=%ARTIFACT_VERSION% -DartifactId=%OFFLINE_PROJECT_ARTIFACT_ID% -DgeneratePom=true

REM clean workspace
cd %WORKSPACE%\..
rmdir /S /Q %WORKSPACE%
