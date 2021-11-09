/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { UpgradeModule } from '@angular/upgrade/static';

export interface ICustomElement extends HTMLElement {
    connectedCallback?(): void;
    disconnectedCallback?(): void;
    attributeChangedCallback?(name: string, oldValue: any, newValue: any): void;
}

export type CustomElementConstructor = new (...arg: any[]) => ICustomElement;

/*
 * Abstract Class to create Custom Web Elements aimed at triggering some legacy AngularJS compilation
 * this class takes care of the following boilerplate:
 * - preventing enless recompilation due to AngularJS compilation modifying DOM hence retriggering custom web element processing.
 * - destroying the scope upon disconnection
 * - checking necessary conditions before triggering native custom element callbacks
 */
export abstract class AbstractAngularJSBasedCustomElement extends HTMLElement
    implements ICustomElement {
    protected scope: angular.IScope;

    private PROCESSED_ATTRIBUTE_NAME = 'processed';

    constructor(private upgrade: UpgradeModule) {
        super();
    }

    /*
     * this method is responsible for:
     * - preprocessing the node before compilation
     * - Assign the instance member scope
     * - compile with this scope
     * - invoke markAsProcessed if and only if compilation could proceed successfully
     */
    abstract internalConnectedCallback(): void;

    internalAttributeChangedCallback?(name: string, oldValue: any, newValue: any): void;

    internalDisconnectedCallback?(): void;

    // we need to protect against a stack overflow: custom elements -> $compile -> custom elements
    markAsProcessed(): void {
        this.setAttribute(this.PROCESSED_ATTRIBUTE_NAME, 'true');
    }

    connectedCallback(): void {
        if (!this.isConnected || this.getAttribute(this.PROCESSED_ATTRIBUTE_NAME)) {
            return;
        }
        this.internalConnectedCallback();
    }

    disconnectedCallback(): void {
        if (!this.scope || this.isConnected) {
            return;
        }
        this.scope.$destroy();
        this.internalDisconnectedCallback && this.internalDisconnectedCallback();
    }

    attributeChangedCallback(name: string, oldValue: any, newValue: any): void {
        /*
         * attributes don't change in the case of decorators:
         * - they come from the shallow clone itself
         * - only active flag changes but because of the full rewrapping it goes through constructor
         */
        if (!this.shouldReactOnAttributeChange()) {
            return;
        }
        this.internalAttributeChangedCallback(name, oldValue, newValue);
    }

    get $rootScope(): angular.IRootScopeService {
        return this.upgrade.injector.get('$rootScope');
    }

    get $compile(): angular.ICompileService {
        return this.upgrade.injector.get('$compile');
    }

    private shouldReactOnAttributeChange(): boolean {
        return (
            this.internalAttributeChangedCallback &&
            this.scope &&
            this.isConnected &&
            !!this.getAttribute(this.PROCESSED_ATTRIBUTE_NAME)
        );
    }
}
