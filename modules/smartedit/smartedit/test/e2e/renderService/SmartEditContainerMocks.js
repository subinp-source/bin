/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('SmartEditContainerMocksModule', [])
    .constant('SMARTEDIT_ROOT', 'web/webroot')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/test\/e2e/)
    .value('CONFIGURATION_MOCK', [
        {
            key: 'i18nAPIRoot',
            value: '"somepath"'
        },
        {
            key: 'applications.InnerRenderDecorators',
            value:
                '{"smartEditLocation":"/test/e2e/utils/decorators/generated_innerRenderDecorators.js"}'
        },
        {
            key: 'applications.OthersMockModule',
            value: '{"smartEditLocation": "/test/e2e/utils/commonMockedModules/OthersMock.js"}'
        }
    ]);

angular.module('smarteditloader').requires.push('SmartEditContainerMocksModule');
angular.module('smarteditcontainer').requires.push('SmartEditContainerMocksModule');
