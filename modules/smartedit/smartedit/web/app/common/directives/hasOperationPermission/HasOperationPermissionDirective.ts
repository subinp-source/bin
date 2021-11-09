/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    Directive,
    Inject,
    Input,
    OnChanges,
    OnDestroy,
    OnInit,
    SimpleChange,
    SimpleChanges,
    TemplateRef,
    ViewContainerRef
} from '@angular/core';

import {
    IPermissionService,
    LogService,
    MultiNamePermissionContext,
    SystemEventService
} from '../../services';
import {
    HasOperationPermissionBaseDirective,
    IsPermissionGrantedHandler
} from './HasOperationPermissionBaseDirective';

/**
 * @ngdoc directive
 * @name HasOperationPermissionDirective
 * @element [seHasOperationPermission]
 *
 * @description
 * An Authorization structural directive that conditionally will remove elements from the DOM if the user does not have authorization defined
 * by the input parameter permission keys.
 * This directive makes use of the {@link smarteditCommonsModule.service:PermissionServiceInterface IPermissionService}
 * service to validate if the current user has access to the given permission set.
 *
 * It takes a comma-separated list of permission names or an array of permission name objects structured as follows:
 * @example
 * 1. String
 * 'se-edit-page'
 *
 * 2. Object
 *  {
 *     names: ["permission1", "permission2"],
 *     context: {
 *         data: "with the context property, extra data can be included to check a permission when the Rule.verify function is called"
 *     }
 *  }
 *
 * @param {string | Object[]} [seHasOperationPermission] A comma-separated list of permission names or an array of permission name objects.
 */
@Directive({ selector: '[seHasOperationPermission]' })
export class HasOperationPermissionDirective extends HasOperationPermissionBaseDirective
    implements OnInit, OnDestroy, OnChanges {
    @Input() seHasOperationPermission: string | MultiNamePermissionContext[];

    private hasView: boolean = false;

    constructor(
        private templateRef: TemplateRef<any>,
        private viewContainerRef: ViewContainerRef,
        systemEventService: SystemEventService,
        @Inject(IPermissionService) permissionService: IPermissionService,
        logService: LogService
    ) {
        super(systemEventService, permissionService, logService);
        this.isPermissionGrantedHandler = this.getIsPermissionGrantedHandler();
    }

    ngOnInit() {
        super.ngOnInit();
    }

    ngOnChanges(changes: SimpleChanges) {
        super.ngOnChanges({
            hasOperationPermission: new SimpleChange(
                changes.seHasOperationPermission.previousValue,
                changes.seHasOperationPermission.currentValue,
                changes.seHasOperationPermission.firstChange
            )
        });
    }

    ngOnDestroy() {
        super.ngOnDestroy();
    }

    private getIsPermissionGrantedHandler(): IsPermissionGrantedHandler {
        return (isPermissionGranted: boolean) => {
            this.updateView(isPermissionGranted);
        };
    }

    private updateView(isPermissionGranted: boolean): void {
        if (isPermissionGranted && !this.hasView) {
            this.viewContainerRef.createEmbeddedView(this.templateRef);
            this.hasView = true;
        } else if (!isPermissionGranted && this.hasView) {
            this.viewContainerRef.clear();
        }
    }
}
