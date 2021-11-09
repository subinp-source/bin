/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';

import {
    IPermissionService,
    LogService,
    MultiNamePermissionContext,
    SystemEventService
} from '../../services';
import { EVENTS } from '../../utils/smarteditconstants';

/** @internal */
export type IsPermissionGrantedHandler = (isPermissionGranted: boolean) => void;

/** @internal */
export abstract class HasOperationPermissionBaseDirective implements OnInit, OnDestroy, OnChanges {
    private _isPermissionGrantedHandler: IsPermissionGrantedHandler;
    private unregisterHandler: () => void;
    private permission: string | MultiNamePermissionContext[];

    constructor(
        private systemEventService: SystemEventService,
        private permissionService: IPermissionService,
        private logService: LogService
    ) {}

    ngOnInit() {
        // NOTE: Refreshing permission checking should only be done after permissions have been cleaned
        // (PERMISSION_CACHE_CLEANED). If this is done as soon after user is changed (USER_CHANGED) then there's a race
        // condition between when the cache is cleaned and when this permission checking is executed.
        this.unregisterHandler = this.systemEventService.subscribe(
            EVENTS.PERMISSION_CACHE_CLEANED,
            this.refreshIsPermissionGranted.bind(this)
        );
    }

    ngOnDestroy() {
        this.unregisterHandler();
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.hasOperationPermission && changes.hasOperationPermission.currentValue) {
            this.permission = changes.hasOperationPermission.currentValue;
            this.refreshIsPermissionGranted();
        }
    }

    protected set isPermissionGrantedHandler(handler: IsPermissionGrantedHandler) {
        this._isPermissionGrantedHandler = handler;
    }

    private refreshIsPermissionGranted(): void {
        this.isPermissionGranted(this.permission).then((isPermissionGranted) => {
            this._isPermissionGrantedHandler(isPermissionGranted);
        });
    }

    private isPermissionGranted(
        permission: string | MultiNamePermissionContext[]
    ): Promise<boolean> {
        return this.permissionService
            .isPermitted(this.validateAndPreparePermissions(permission))
            .then(
                (isPermissionGranted: boolean) => isPermissionGranted,
                (error) => {
                    this.logService.error('Failed to retrieve authorization', error);
                    return false;
                }
            )
            .then((isPermissionGranted: boolean) => isPermissionGranted);
    }

    private validateAndPreparePermissions(
        permissions: string | MultiNamePermissionContext[]
    ): MultiNamePermissionContext[] {
        if (typeof permissions !== 'string' && !Array.isArray(permissions)) {
            throw new Error('Permission should be string or an array of objects');
        }

        return typeof permissions === 'string' ? this.toPermissions(permissions) : permissions;
    }

    private toPermissions(permissions: string): MultiNamePermissionContext[] {
        return [{ names: permissions.split(',') }];
    }
}
