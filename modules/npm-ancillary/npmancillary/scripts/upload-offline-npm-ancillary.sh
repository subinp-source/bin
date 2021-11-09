#!/bin/sh
# Upload Offline NPM Ancillary Module
# Supports: Linux or Mac
# 1. Downloads the npm-ancillary-module artifact from either the release repository or the snapshot repository.
# 2. Performs "npm ci on the npm resource folder"
# 3. Zips the module and uploads to artifactory (release or snapshot)

OS_NAME=$(uname -s)

ARTIFACTORY_REPOSITORY_ID="hybris-repository"
RELEASE_REPO_ID="milestones"
SNAPSHOT_REPO_ID="snapshots"

ARTIFACTORY_RELEASE_REPO="https://repository.hybris.com/hybris-release"
ARTIFACTORY_SNAPSHOT_REPO="https://repository.hybris.com/hybris-snapshot"
RELEASE_REPO="https://common.repositories.sap.ondemand.com/artifactory/deploy-milestones-cx-commerce-maven"
SNAPSHOT_REPO="https://common.repositories.sap.ondemand.com/artifactory/deploy-snapshots-cx-commerce-maven"

PROJECT_GROUPID="de.hybris.platform"
PROJECT_ARTIFACTID="npm-ancillary-module"

ARTIFACT_VERSION=$1

# option to download of the artifact (useful when running from CI that doesn't have permission to hybris-snapshot for example).
DOWNLOAD_ARTIFACT=$2

if [[ "${ARTIFACT_VERSION}" == "" ]] ; then
    echo "Local usage: ./upload-offline-npm-ancillary.sh ARTIFACT_VERSION true"
    echo "Example local usage: ./upload-offline-npm-ancillary.sh 6.6.0.0-RC4-SNAPSHOT true"

    echo "To disable the artifact download: ./upload-offline-npm-ancillary.sh ARTIFACT_VERSION false"
    exit -1
fi

if [ "${DOWNLOAD_ARTIFACT}" == "true" ] ; then
    WORKSPACE=$(pwd)/build
else
    WORKSPACE=$(pwd)
fi

NPM_MODULE_HOME=${WORKSPACE}/hybris/bin/ext-content/npmancillary
NPM_RESOURCE_HOME=${NPM_MODULE_HOME}/resources/npm

if [[ "${ARTIFACT_VERSION}" == *SNAPSHOT ]] ; then
    TARGET_REPOSITORY=$SNAPSHOT_REPO
    ARTIFACTORY_TARGET_REPO=$ARTIFACTORY_SNAPSHOT_REPO
    REPOSITORY_ID=$SNAPSHOT_REPO_ID
else
    TARGET_REPOSITORY=$RELEASE_REPO
    ARTIFACTORY_TARGET_REPO=$ARTIFACTORY_RELEASE_REPO
    REPOSITORY_ID=$RELEASE_REPO_ID
fi

if [ "${OS_NAME}" = "Darwin" ] ; then
    NODE_HOME=${NPM_RESOURCE_HOME}/node/node-v10.24.0-darwin-x64/bin
    OFFLINE_PROJECT_ARTIFACT_ID=offline-darwin-${PROJECT_ARTIFACTID}
elif [ "${OS_NAME}" = "Linux" ] ; then
    NODE_HOME=${NPM_RESOURCE_HOME}/node/node-v10.24.0-linux-x64/bin
    OFFLINE_PROJECT_ARTIFACT_ID=offline-linux-${PROJECT_ARTIFACTID}
fi

echo """
Running upload-offline-npm-ancillary.sh

OS_NAME: ${OS_NAME}
WORKSPACE: ${WORKSPACE}

REPOSITORY_ID: ${REPOSITORY_ID}

RELEASE_REPO: ${RELEASE_REPO}
SNAPSHOT_REPO: ${SNAPSHOT_REPO}

PROJECT_GROUPID: ${PROJECT_GROUPID}
PROJECT_ARTIFACTID: ${PROJECT_ARTIFACTID}
ARTIFACT_VERSION: ${ARTIFACT_VERSION}

NPM_RESOURCE_HOME: ${NPM_RESOURCE_HOME}

TARGET_REPOSITORY: ${TARGET_REPOSITORY}

NODE_HOME: ${NODE_HOME}
OFFLINE_PROJECT_ARTIFACT_ID: ${OFFLINE_PROJECT_ARTIFACT_ID}
"""

if [ "${DOWNLOAD_ARTIFACT}" == "true" ] ; then
    # Create workspace and download artifact
    rm -rf $WORKSPACE
    mkdir -p $WORKSPACE
    cd $WORKSPACE
    echo "Downloading artifact with dest=${WORKSPACE}/${PROJECT_ARTIFACTID}-${ARTIFACT_VERSION}.zip"
    mvn org.apache.maven.plugins:maven-dependency-plugin:2.4:get \
       -Dartifact=${PROJECT_GROUPID}:${PROJECT_ARTIFACTID}:${ARTIFACT_VERSION}:zip \
       -Ddest=${WORKSPACE}/${PROJECT_ARTIFACTID}-${ARTIFACT_VERSION}.zip
    echo "Unziping artifact"
    unzip -qq ${PROJECT_ARTIFACTID}-${ARTIFACT_VERSION}.zip
fi

echo "Updating PATH for node binary to $NODE_HOME"
export PATH=$NODE_HOME:$PATH

cd ${NPM_RESOURCE_HOME}

echo "Download smart-utils tar"
SMART_UTILS_VERSION_TAG=$(cat ${NPM_RESOURCE_HOME}/package.json | grep @smart/utils | sed 's/.*smart-utils-\(.*\)\.tgz",/\1/g')
SMART_UTILS_REPO=$(cat ${NPM_MODULE_HOME}/resources/ant/linux.properties | grep smartutils.repo | sed 's/smartutils.repo=\(.*\)/\1/g')

git clone --single-branch --branch ${SMART_UTILS_VERSION_TAG} ${SMART_UTILS_REPO} embedded

echo "Running npm ci"
npm ci
rm -rf .cache

cd $WORKSPACE
# zip file might now always exist.
rm -f ${PROJECT_ARTIFACTID}-${ARTIFACT_VERSION}.zip

echo "Zipping offline ancillary"
# need to store symbolic links as such in the zip
zip -ryq ${OFFLINE_PROJECT_ARTIFACT_ID}-${ARTIFACT_VERSION}.zip .

echo "Deploying artifact ${OFFLINE_PROJECT_ARTIFACT_ID}-${ARTIFACT_VERSION}.zip to SAP artifactory"
mvn deploy:deploy-file -DrepositoryId=${REPOSITORY_ID} -Durl=${TARGET_REPOSITORY} -Dfile=${OFFLINE_PROJECT_ARTIFACT_ID}-${ARTIFACT_VERSION}.zip -DgroupId=${PROJECT_GROUPID} -Dversion=${ARTIFACT_VERSION} -DartifactId=${OFFLINE_PROJECT_ARTIFACT_ID} -DgeneratePom=true

echo "Deploying artifact ${OFFLINE_PROJECT_ARTIFACT_ID}-${ARTIFACT_VERSION}.zip to hybris artifactory"
mvn deploy:deploy-file -DrepositoryId=${ARTIFACTORY_REPOSITORY_ID} -Durl=${ARTIFACTORY_TARGET_REPO} -Dfile=${OFFLINE_PROJECT_ARTIFACT_ID}-${ARTIFACT_VERSION}.zip -DgroupId=${PROJECT_GROUPID} -Dversion=${ARTIFACT_VERSION} -DartifactId=${OFFLINE_PROJECT_ARTIFACT_ID} -DgeneratePom=true


echo "Cleaning workspace"
if [ "${DOWNLOAD_ARTIFACT}" == "true" ] ; then
    rm -rf $WORKSPACE
else
    rm ${OFFLINE_PROJECT_ARTIFACT_ID}-${ARTIFACT_VERSION}.zip
fi
