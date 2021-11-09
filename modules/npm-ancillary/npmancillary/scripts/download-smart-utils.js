const { promisify } = require('util');
const exec = promisify(require('child_process').exec);

// Variables
const PACKAGE_JSON_PATH = process.argv[2];
const SMARTUTILS_REPO = process.argv[3];
const SMARTUTILS_FOLDER = process.argv[4];

// Setup
const package = require(PACKAGE_JSON_PATH);

// Retrieve smartutils tag version from package.json
const PACKAGE_VERSION = package.dependencies['@smart/utils'];
const TAG = /.+smart-utils-(.+?)\.tgz/.exec(PACKAGE_VERSION)[1];

// Download tagged version
(async () => {
  try {
    await exec(`git clone --single-branch --branch ${TAG} ${SMARTUTILS_REPO} ${SMARTUTILS_FOLDER}`);
  } catch (err) {
    console.error(err);
    process.exit(1)
  }

  console.log('@smart/utils has been downloaded successfully.');
})();
