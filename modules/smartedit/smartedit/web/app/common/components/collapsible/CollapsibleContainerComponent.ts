/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    Component,
    ElementRef,
    EventEmitter,
    Input,
    OnChanges,
    OnDestroy,
    OnInit,
    Output,
    SimpleChanges,
    ViewChild
} from '@angular/core';
import { stringUtils } from '../../utils';
import { SeDowngradeComponent } from '../../di';
import { CollapsibleContainerApi, CollapsibleContainerConfig } from './interfaces';
import { COLLAPSIBLE_DEFAULT_CONFIGURATION } from './constants';

/**
 * @ngdoc component
 * @name smarteditCommonsModule.component:CollapsibleContainerComponent
 * @description
 *
 * The CollapsibleContainerComponent is an Angular component that allows for the dynamic display of any HTML content on a collapsible container.
 *
 * <pre>
 *    <se-collapsible-container>
 *       <se-collapsible-container-header>
 *           Your title here
 *       </se-collapsible-container-header>
 *      <se-collapsible-container-content>
 *           Your content here
 *       </se-collapsible-container-content>
 *    </se-collapsible-container>
 * </pre>
 *
 * @param {<Object=} configuration JSON object containing the configuration to be applied on a collapsible container.
 * @param {Boolean} configuration.expandedByDefault Specifies if the collapsible container is expanded by default.
 * @param {String} configuration.iconAlignment Specifies if the expand-collapse icon is to be displayed to the *left* or to the _right_ of the container header.
 * @param {Boolean} configuration.iconVisible Specifies if the expand-collapse icon is to be rendered.
 * @param {& Function =} getApi Exposes the collapsible container's api object
 */

@SeDowngradeComponent()
@Component({
    selector: 'se-collapsible-container',
    templateUrl: './CollapsibleContainerComponent.html'
})
export class CollapsibleContainerComponent implements OnInit, OnChanges, OnDestroy {
    @Input() configuration: CollapsibleContainerConfig;
    @Output() getApi: EventEmitter<CollapsibleContainerApi> = new EventEmitter();

    @ViewChild('container', { static: true }) set _container(container: ElementRef) {
        this.container = container;
        this.containerHeight = container.nativeElement.scrollHeight;

        // Watch DOM change in case of asynchronous collapsible population

        if (!this.mutationObserver && this.container && this.container.nativeElement) {
            this.mutationObserver = new MutationObserver(() => {
                this.containerHeight = this.container.nativeElement.scrollHeight;
            });
            this.mutationObserver.observe(this.container.nativeElement, {
                childList: true,
                subtree: true,
                attributes: true
            });
        }
    }

    public containerHeight: number = 0;
    public headingId: string = stringUtils.generateIdentifier();
    public panelId: string = stringUtils.generateIdentifier();
    public isOpen: boolean;
    public isDisabled: boolean;

    private container: ElementRef;
    private mutationObserver: MutationObserver;
    private api: CollapsibleContainerApi = {
        isExpanded: () => {
            return this.isOpen;
        }
    };

    ngOnDestroy() {
        this.mutationObserver.disconnect();
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.configuration) {
            this.configure();
        }
    }

    ngOnInit() {
        this.configure();

        this.isOpen = this.configuration.expandedByDefault;
        this.getApi.emit(this.api);
    }

    public toggle(): void {
        this.isOpen = !this.isOpen;
    }

    public handleKeypress(event: KeyboardEvent): void {
        if (event.code === 'Enter') {
            this.toggle();
        }
    }

    public isIconRight(): boolean {
        return this.configuration.iconAlignment === 'right';
    }

    public isIconLeft(): boolean {
        return this.configuration.iconAlignment === 'left';
    }

    private configure(): void {
        this.configuration = {
            ...COLLAPSIBLE_DEFAULT_CONFIGURATION,
            ...(this.configuration || {})
        };
    }
}
