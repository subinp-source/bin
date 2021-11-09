/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { SimpleChange } from '@angular/core';
import { LogService } from '@smart/utils';

import { SeDirective } from '../../di';
import { IPermissionService, SystemEventService } from '../../services';
import {
    HasOperationPermissionBaseDirective,
    IsPermissionGrantedHandler
} from '../../directives/hasOperationPermission';

/**
 * @ngdoc directive
 * @name hasOperationPermission
 * @scope
 * @restrict A
 * @element ANY
 *
 * @deprecated since 2005, use {@link HasOperationPermissionDirective HasOperationPermissionDirective}
 * @description
 * Use this directive for AngularJS templates.
 *
 * For Angular components use {@link HasOperationPermissionDirective HasOperationPermissionDirective}
 */
@SeDirective({
    transclude: true,
    templateUrl: 'legacyHasOperationPermissionTemplate.html',
    controllerAs: 'ctrl',
    selector: '[has-operation-permission]',
    inputs: ['hasOperationPermission']
})
export class LegacyHasOperationPermissionDirective extends HasOperationPermissionBaseDirective
    implements angular.IOnInit, angular.IOnChanges, angular.IOnDestroy {
    public isPermissionGrantedFlag: boolean;

    constructor(
        systemEventService: SystemEventService,
        permissionService: IPermissionService,
        logService: LogService
    ) {
        super(systemEventService, permissionService, logService);
        this.isPermissionGrantedHandler = this.getIsPermissionGrantedHandler();
    }

    $onInit() {
        super.ngOnInit();
    }

    $onChanges(changes: angular.IOnChangesObject) {
        if (changes.hasOperationPermission && changes.hasOperationPermission.currentValue) {
            this.isPermissionGrantedFlag = false;
            super.ngOnChanges({
                hasOperationPermission: new SimpleChange(
                    changes.hasOperationPermission.previousValue,
                    changes.hasOperationPermission.currentValue,
                    changes.hasOperationPermission.isFirstChange()
                )
            });
        }
    }

    $onDestroy() {
        super.ngOnDestroy();
    }

    private getIsPermissionGrantedHandler(): IsPermissionGrantedHandler {
        return (isPermissionGranted: boolean) => {
            this.isPermissionGrantedFlag = isPermissionGranted;
        };
    }
}
