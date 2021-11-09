/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    Component,
    ContentChild,
    ElementRef,
    EventEmitter,
    Inject,
    Input,
    OnDestroy,
    OnInit,
    Output,
    Renderer2,
    TemplateRef
} from '@angular/core';

import { SeDowngradeComponent } from '../../di';
import { SliderPanelConfiguration } from './interfaces';
import { SliderPanelServiceFactory } from './SliderPanelServiceFactory';
import { SliderPanelService } from './SliderPanelService';
import { stringUtils, WindowUtils } from '../../utils';
import { YJQUERY_TOKEN } from '../../services';

/**
 * This object defines injectable Angular constants that store the CSS class names used in the controller to define the
 * rendering and animation of the slider panel.
 */

export const CSS_CLASSNAMES = {
    /**
     * The class name applied to the slide panel container to trigger the sliding action in the CSS animation.
     */
    SLIDERPANEL_ANIMATED: 'sliderpanel--animated',

    /**
     * A common prefix for the class names that defines how the content of the slider panel is to be rendered.
     */
    SLIDERPANEL_SLIDEPREFIX: 'sliderpanel--slidefrom'
};

/**
 * The se-slider-panel Angular component allows for the dynamic display of any HTML content on a sliding panel.
 *
 * @example
 *  <se-slider-panel
 *      [(sliderPanelShow)]="onSliderPanelShow"
 *      [(sliderPanelHide)]="onSliderPanelHide"
 *      [sliderPanelConfiguration]="config"
 *   >
 *      <my-content></my-content>
 *  </se-slider-panel>
 */
@SeDowngradeComponent()
@Component({
    selector: 'se-slider-panel',
    templateUrl: './SliderPanelComponent.html'
})
export class SliderPanelComponent implements OnInit, OnDestroy {
    @Input() public sliderPanelConfiguration: SliderPanelConfiguration;
    @Input() public sliderPanelHide: () => Promise<any>;
    @Input() public sliderPanelShow: () => Promise<any>;

    @Output() public sliderPanelHideChange: EventEmitter<() => Promise<any>> = new EventEmitter();
    @Output() public sliderPanelShowChange: EventEmitter<() => Promise<any>> = new EventEmitter();

    @ContentChild(TemplateRef, { static: false }) content: TemplateRef<any>;

    public isShown: boolean;
    public sliderPanelDismissAction: () => void;
    public slideClassName: string;

    private sliderPanelService: SliderPanelService;
    private uniqueId: string;
    private inlineStyling: {
        container: CSSStyleDeclaration;
        content: CSSStyleDeclaration;
    } = { container: {} as CSSStyleDeclaration, content: {} as CSSStyleDeclaration };

    constructor(
        private renderer: Renderer2,
        private element: ElementRef,
        private windowUtils: WindowUtils,
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic,
        private sliderPanelServiceFactory: SliderPanelServiceFactory
    ) {}

    ngOnInit() {
        this.isShown = false;
        this.uniqueId = stringUtils.generateIdentifier();

        // setting new instance of slider panel service
        this.sliderPanelService = this.sliderPanelServiceFactory.getNewServiceInstance(
            this.yjQuery(this.element.nativeElement),
            this.windowUtils.getWindow(),
            this.sliderPanelConfiguration
        );

        // variables made available on the html template
        this.sliderPanelConfiguration = this.sliderPanelService.sliderPanelConfiguration;

        this.slideClassName =
            CSS_CLASSNAMES.SLIDERPANEL_SLIDEPREFIX + this.sliderPanelConfiguration.slideFrom;

        this.inlineStyling = {
            container: this.sliderPanelService.inlineStyling.container,
            content: this.sliderPanelService.inlineStyling.content
        };

        this.sliderPanelShowChange.emit(() => this.showSlider());
        this.sliderPanelHideChange.emit(() => this.hideSlider());

        this.sliderPanelDismissAction =
            this.sliderPanelConfiguration.modal &&
            this.sliderPanelConfiguration.modal.dismiss &&
            this.sliderPanelConfiguration.modal.dismiss.onClick
                ? this.sliderPanelConfiguration.modal.dismiss.onClick
                : this.hideSlider;

        // applying event handler for screen resize
        this.addScreenResizeEventHandler();

        if (this.sliderPanelConfiguration.displayedByDefault) {
            this.showSlider();
        }
    }

    ngOnDestroy() {
        this.yjQuery(this.windowUtils.getWindow()).off('resize.doResize');
    }

    public hideSlider(): Promise<void> {
        return new Promise((resolve) => {
            this.renderer.removeClass(
                this.element.nativeElement,
                CSS_CLASSNAMES.SLIDERPANEL_ANIMATED
            );

            this.isShown = false;
            resolve();
        });
    }

    public showSlider(): Promise<void> {
        // container inline styling
        return new Promise((resolve) => {
            this.sliderPanelService.updateContainerInlineStyling(false);
            this.inlineStyling.container = this.sliderPanelService.inlineStyling.container;

            // container greyed out overlay
            let isSecondarySliderPanel = false;
            this.yjQuery('se-slider-panel.sliderpanel--animated .se-slider-panel-container')
                .toArray()
                .forEach((sliderPanelContainer: HTMLElement) => {
                    const container = this.yjQuery(sliderPanelContainer);
                    if (!isSecondarySliderPanel) {
                        if (
                            container.css('height') === this.inlineStyling.container.height &&
                            container.css('width') === this.inlineStyling.container.width &&
                            container.css('left') === this.inlineStyling.container.left &&
                            container.css('top') === this.inlineStyling.container.top
                        ) {
                            isSecondarySliderPanel = true;
                        }
                    }
                });

            // if no related configuration has been set, the no greyed out overlay is set to true for all secondary slider panels.
            this.sliderPanelConfiguration.noGreyedOutOverlay =
                typeof this.sliderPanelConfiguration.noGreyedOutOverlay === 'boolean'
                    ? this.sliderPanelConfiguration.noGreyedOutOverlay
                    : isSecondarySliderPanel;

            // triggering slider panel display
            this.isShown = true;

            this.renderer.addClass(this.element.nativeElement, CSS_CLASSNAMES.SLIDERPANEL_ANIMATED);
            resolve();
        });
    }

    public isSaveDisabled(): boolean {
        if (
            this.sliderPanelConfiguration.modal &&
            this.sliderPanelConfiguration.modal.save &&
            this.sliderPanelConfiguration.modal.save.isDisabledFn
        ) {
            return this.sliderPanelConfiguration.modal.save.isDisabledFn();
        }
        return false;
    }

    private addScreenResizeEventHandler(): void {
        this.yjQuery(this.windowUtils.getWindow()).on(
            'resize.sliderPanelRedraw_' + this.uniqueId,
            () => {
                if (this.isShown) {
                    setTimeout(() => {
                        this.sliderPanelService.updateContainerInlineStyling(true);
                        this.inlineStyling.container = this.sliderPanelService.inlineStyling.container;
                    }, 0);
                }
            }
        );
    }
}
