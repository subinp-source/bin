/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular.module('customMocksModule', ['backendMocksUtilsModule']).run(function() {
    var setPageAsDraft = sessionStorage.getItem('setPageAsDraft');
    if (setPageAsDraft && JSON.parse(setPageAsDraft)) {
        var pagesToMakeDraft = ['homepage', 'secondpage'];
        var componentMocks = JSON.parse(sessionStorage.getItem('componentMocks'));

        var modifiedMocks = {
            componentItems: componentMocks.componentItems.map(function(item) {
                if (pagesToMakeDraft.indexOf(item.uuid) > -1) {
                    item.approvalStatus = 'CHECK';
                    item.displayStatus = 'DRAFT';
                }

                return item;
            })
        };

        sessionStorage.setItem('componentMocks', JSON.stringify(modifiedMocks));
    }
});

try {
    angular.module('smarteditloader').requires.push('customMocksModule');
} catch (e) {}
try {
    angular.module('smarteditcontainer').requires.push('customMocksModule');
} catch (e) {}
