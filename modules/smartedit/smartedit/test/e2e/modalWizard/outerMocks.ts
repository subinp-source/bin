/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { NgModule } from '@angular/core';

import {
    moduleUtils,
    AngularJSBootstrapIndicatorService,
    AngularJSLazyDependenciesService,
    HttpBackendService,
    SeEntryModule
} from 'smarteditcommons';
import { OuterAuthorizationMocks } from '../utils/commonMockedModules/outerAuthorizationMock';
import { OuterWhoAmIMocks } from '../utils/commonMockedModules/outerWhoAmIMocks';
import { OuterConfigurationMocks } from '../utils/commonMockedModules/outerConfigurationMock';
import { i18nMocks } from '../utils/commonMockedModules/outerI18nMock';
import { OuterLanguagesMocks } from '../utils/commonMockedModules/outerLanguagesMock';
import { OuterOtherMocks } from '../utils/commonMockedModules/outerOtherMock';
import { OuterPreviewMocks } from '../utils/commonMockedModules/outerPreviewMock';
import { OuterSitesMocks } from '../utils/commonMockedModules/outerSitesMock';
import { OuterPermissionMocks } from '../utils/commonMockedModules/outerPermissionMocks';

@SeEntryModule('OuterMocksModule')
@NgModule({
    imports: [
        OuterAuthorizationMocks,
        OuterWhoAmIMocks,
        OuterConfigurationMocks,
        i18nMocks,
        OuterLanguagesMocks,
        OuterOtherMocks,
        OuterPreviewMocks,
        OuterSitesMocks,
        OuterPreviewMocks,
        OuterPermissionMocks
    ],

    providers: [
        moduleUtils.provideValues({
            SMARTEDIT_ROOT: 'web/webroot',
            SMARTEDIT_RESOURCE_URI_REGEXP: /^(.*)\/test\/e2e/
        }),
        moduleUtils.bootstrap(
            (
                httpBackendService: HttpBackendService,
                indicator: AngularJSBootstrapIndicatorService,
                lazy: AngularJSLazyDependenciesService
            ) => {
                httpBackendService.matchLatestDefinitionEnabled(true);

                const map = [
                    {
                        value: '"thepreviewTicketURI"',
                        key: 'previewTicketURI'
                    },
                    {
                        value:
                            '{"smartEditContainerLocation":"/test/e2e/modalWizard/generated_outerapp.js"}',
                        key: 'applications.ModalWizardApp'
                    },
                    {
                        value: '"/cmswebservices/v1/i18n/languages"',
                        key: 'i18nAPIRoot'
                    }
                ];
                httpBackendService.whenGET(/smartedit\/configuration/).respond(function() {
                    return [200, map];
                });

                indicator.onSmarteditContainerReady().subscribe(() => {
                    lazy.$templateCache().put(
                        'test/e2e/wizard/stepName.html',
                        '<div id="nameStep">\n' +
                            '    Name: <input id="nameInput" type="text" data-ng-model="personController.person.name" />\n' +
                            '</div>\n'
                    );

                    lazy.$templateCache().put(
                        'test/e2e/wizard/stepAge.html',
                        '<div id="ageStep">\n' +
                            '    Age: <input type="number" name="input" ng-model="personController.person.age" />\n' +
                            '</div>\n'
                    );

                    lazy.$templateCache().put(
                        'test/e2e/wizard/stepSex.html',
                        '<div id="genderStep">\n' +
                            '    Select a Gender:\n' +
                            '    <select ng-model="personController.person.gender">\n' +
                            '        <option value="male">Male</option>\n' +
                            '        <option value="female">Female</option>\n' +
                            '    </select>\n' +
                            '    <br />\n' +
                            '    <br />\n' +
                            '    <label>\n' +
                            '        <input\n' +
                            '            id="offendedCheck"\n' +
                            '            type="checkbox"\n' +
                            '            data-ng-model="personController.person.offended"\n' +
                            '        />\n' +
                            '        How DARE you?\n' +
                            '    </label>\n' +
                            '    <button type="button" data-ng-click="personController.toggleExtraStep()">\n' +
                            '        Manual Step Toggle\n' +
                            '    </button>\n' +
                            '</div>\n'
                    );

                    lazy.$templateCache().put(
                        'test/e2e/wizard/stepDone.html',
                        '<div id="doneStep">\n' +
                            '    Your new Person: <br />\n' +
                            '    {{ personController.person }}\n' +
                            '</div>\n'
                    );

                    lazy.$templateCache().put(
                        'test/e2e/wizard/stepExtra.html',
                        '<div id="offendedStep">Aha! The sensitive type! Sorry for that...</div>'
                    );
                });
            },
            [
                HttpBackendService,
                AngularJSBootstrapIndicatorService,
                AngularJSLazyDependenciesService
            ]
        )
    ]
})
export class OuterMocksModule {}

window.pushModules(OuterMocksModule);
