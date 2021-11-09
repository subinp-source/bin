/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObject = {
        /**
         * @param key key that is used to store value in sessionStorage
         * The format for params object:
         * {
         *   read: true,
         *   change: false,
         *   create: true,
         *   remove: false
         * }
         * @param {*} params
         */
        setTypePermission: function(key, params) {
            var typePermissions = [
                {
                    key: 'read',
                    value: params.read.toString()
                },
                {
                    key: 'change',
                    value: params.change.toString()
                },
                {
                    key: 'create',
                    value: params.create.toString()
                },
                {
                    key: 'remove',
                    value: params.remove.toString()
                }
            ];
            browser.executeScript(
                'window.sessionStorage.setItem("' + key + '", arguments[0])',
                JSON.stringify(typePermissions)
            );
        }
    };

    return componentObject;
})();
