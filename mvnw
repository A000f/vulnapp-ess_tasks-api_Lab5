#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements. See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership. The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License. You may obtain a copy of the License at
#
#    https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied. See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# Maven Start Up Batch script
# ----------------------------------------------------------------------------

set -e

findMavenBasedir() {
  BASEDIR=$(dirname "$0")
  BASEDIR=$(cd "$BASEDIR" && pwd -P)
  echo "$BASEDIR"
}

MAVEN_BASEDIR=$(findMavenBasedir)
MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR}"

WRAPPER_DIR="${MAVEN_PROJECTBASEDIR}/.mvn/wrapper"
WRAPPER_PROPERTIES="${WRAPPER_DIR}/maven-wrapper.properties"

if [ -f "${WRAPPER_PROPERTIES}" ]; then
  DISTRIBUTION_URL=$(grep "distributionUrl" "${WRAPPER_PROPERTIES}" | sed 's/distributionUrl=//')
fi

MAVEN_USER_HOME="${MAVEN_USER_HOME:-${HOME}/.m2}"
MAVEN_DIST_DIR="${MAVEN_USER_HOME}/wrapper/dists"

DISTRIBUTION_ID=$(echo "${DISTRIBUTION_URL}" | sed 's|.*/||' | sed 's/.zip//')
DISTRIBUTION_DIR="${MAVEN_DIST_DIR}/${DISTRIBUTION_ID}"

if [ ! -d "${DISTRIBUTION_DIR}" ]; then
  echo "Downloading Maven from ${DISTRIBUTION_URL}"
  mkdir -p "${DISTRIBUTION_DIR}"
  TEMP_FILE=$(mktemp)
  if command -v curl > /dev/null 2>&1; then
    curl -fsSL -o "${TEMP_FILE}" "${DISTRIBUTION_URL}"
  elif command -v wget > /dev/null 2>&1; then
    wget -q -O "${TEMP_FILE}" "${DISTRIBUTION_URL}"
  else
    echo "ERROR: Neither curl nor wget found. Please install one of them."
    exit 1
  fi
  unzip -q "${TEMP_FILE}" -d "${DISTRIBUTION_DIR}"
  rm -f "${TEMP_FILE}"
fi

MAVEN_HOME=$(find "${DISTRIBUTION_DIR}" -maxdepth 1 -type d -name "apache-maven-*" | head -1)

if [ -z "${MAVEN_HOME}" ]; then
  echo "ERROR: Could not find Maven installation in ${DISTRIBUTION_DIR}"
  exit 1
fi

exec "${MAVEN_HOME}/bin/mvn" "$@"
