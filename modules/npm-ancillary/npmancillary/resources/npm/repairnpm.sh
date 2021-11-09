BASEDIR=$(dirname "$0")
NODE_HOME=$BASEDIR/node/node-v10.24.0-$1-x64
# Check if NODE_HOME exist or not
if [[ ! -d "${NODE_HOME}" ]] ; then
  echo "Error: NODE HOME: ${NODE_HOME} not exist in repairnpm !"
  exit -1
fi
export PATH=$NODE_HOME/bin:$PATH
rm -rf $NODE_HOME/bin/npm
ln -sf ../lib/node_modules/npm/bin/npm-cli.js $NODE_HOME/bin/npm
chmod -R +x $NODE_HOME
chmod -R +x $BASEDIR
