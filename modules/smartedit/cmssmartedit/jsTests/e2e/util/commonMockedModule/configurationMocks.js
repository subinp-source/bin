/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular.module('configurationMocksModule', []).constant('CONFIGURATION_MOCKS', [
    {
        value: '"/cmswebservices/v1/i18n/languages"',
        key: 'i18nAPIRoot'
    },
    {
        value: '{"smartEditLocation":"/jsTests/e2e/util/commonMockedModule/rerenderMocks.js"}',
        key: 'applications.rerenderMocks'
    },
    {
        value: '{"smartEditLocation":"/jsTests/e2e/util/commonMockedModule/miscellaneousMocks.js"}',
        key: 'applications.miscellaneousMocks'
    },
    {
        value:
            '{"smartEditContainerLocation":"/web/webroot/cmssmartedit/js/cmssmarteditContainer.js"}',
        key: 'applications.cmssmarteditContainer'
    },
    {
        value: '{"smartEditLocation":"/web/webroot/cmssmartedit/js/cmssmartedit.js"}',
        key: 'applications.cmssmartedit'
    }
]);
