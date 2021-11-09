/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObject = {
        /**
         * @param type The name of the type containing the attribute whose permissions to set.
         * @param attribute The attribute for which to set permissions
         * @param permissions The object containing the permissions the set. It needs the following
         * format:
         * {
         *      read: true,
         *      change: true
         * }
         */
        setPermissionsToAttributeInType: function(type, attribute, permissions) {
            var processedPermissions = Object.keys(permissions).map(function(key) {
                return {
                    key: key,
                    value: permissions[key]
                };
            });

            var attributePermissions = {
                id: type + '.' + attribute,
                permissions: processedPermissions
            };

            var key = 'attributePermissions_' + type + '_' + attribute;
            browser.executeScript(
                'window.sessionStorage.setItem("' + key + '", arguments[0])',
                JSON.stringify(attributePermissions)
            );
        }
    };

    return componentObject;
})();
