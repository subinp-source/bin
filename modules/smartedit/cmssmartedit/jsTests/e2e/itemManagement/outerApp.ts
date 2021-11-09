/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
/* forbiddenNameSpaces angular.module:false */
import * as angular from 'angular';
import { PromiseUtils, SystemEventService } from 'smarteditcommons';

angular
    .module('restrictionsManagementApp', [
        'templateCacheDecoratorModule',
        'itemManagementModule',
        'restrictionsServiceModule',
        'cmsSmarteditServicesModule'
    ])
    .service('promiseUtils', PromiseUtils)
    .service('systemEventService', SystemEventService)
    .controller('testController', function(
        $timeout: angular.ITimeoutService,
        restrictionsService: any,
        cmsitemsRestService: any
    ) {
        const restrictions = {
            add: {
                uid: 'addId',
                name: 'restriction to add',
                typeCode: 'CMSTimeRestriction',
                description: 'some time restriction description'
            },
            edit: {
                uid: 'editId',
                name: 'editing restriction',
                typeCode: 'CMSTimeRestriction',
                description: 'some time restriction description'
            },
            create: {
                typeCode: 'CMSTimeRestriction'
            }
        };

        this.visible = true;
        this.uriContext = {
            siteUID: 'mySite',
            catalogId: 'myCatalog',
            catalogVersion: 'myCatalogVersion'
        };

        // gets replaced/overridden by the child component
        this.submit = function submit() {
            //
        };

        // gets replaced/overridden by the child component
        this.isDirty = function submit() {
            //
        };

        this.submit = function submit() {
            this.submitCallback().then(
                function(result: any) {
                    this.result = result;
                }.bind(this)
            );
        }.bind(this);

        this.modeChanged = function(newMode: any) {
            this.mode = newMode;
            this.restriction = (restrictions as any)[newMode];
            this.type = this.restriction.typeCode;
            this.visible = false;
            this.result = null;
            this.structureApi = restrictionsService.getStructureApiUri(this.mode);
            this.contentApi = cmsitemsRestService.get({});
            $timeout(
                function() {
                    this.visible = true;
                }.bind(this)
            );
        };

        this.submitEnabled = function submitEnabled() {
            return this.mode === 'add' || this.isDirty();
        };

        // initialize
        this.modeChanged('add');
    });

angular.module('cmssmarteditContainer').requires.push('restrictionsManagementApp');
