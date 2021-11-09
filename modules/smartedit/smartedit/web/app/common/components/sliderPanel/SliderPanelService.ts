/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { defaultsDeep } from 'lodash';

import { SliderPanelConfiguration } from './interfaces';
import { SliderPanelZIndexHelper } from './SliderPanelZIndexHelper';

/**
 * The SliderPanelService handles the initialization and the rendering of the se-slider-panel Angular component.
 */

export class SliderPanelService {
    public sliderPanelConfiguration: SliderPanelConfiguration;
    public inlineStyling: {
        container: CSSStyleDeclaration;
        content: CSSStyleDeclaration;
    } = { container: {} as CSSStyleDeclaration, content: {} as CSSStyleDeclaration };

    private sliderPanelDefaultConfiguration: SliderPanelConfiguration = {
        slideFrom: 'right',
        overlayDimension: '80%'
    };
    private parent: JQuery;
    private appendChildTarget: HTMLElement;

    constructor(
        private element: JQuery,
        private window: Window,
        private configuration: SliderPanelConfiguration,
        private yjQuery: JQueryStatic
    ) {
        this.init();
    }

    /**
     * This method sets the inline styling applied to the slider panel container according to the dimension and position values
     * of the parent element.
     */

    public updateContainerInlineStyling(screenResized: boolean): void {
        const parentClientRect = this.parent[0].getBoundingClientRect();
        const borders = {
            left: this.parent.css('border-left-width')
                ? parseInt(this.parent.css('border-left-width').replace('px', ''), 10)
                : 0,
            top: this.parent.css('border-top-width')
                ? parseInt(this.parent.css('border-top-width').replace('px', ''), 10)
                : 0
        };

        this.inlineStyling.container.height = this.parent[0].clientHeight + 'px';
        this.inlineStyling.container.width = this.parent[0].clientWidth + 'px';
        this.inlineStyling.container.left =
            (this.appendChildTarget.nodeName === 'BODY'
                ? Math.round(parentClientRect.left + this.window.pageXOffset + borders.left)
                : 0) + 'px';
        this.inlineStyling.container.top =
            (this.appendChildTarget.nodeName === 'BODY'
                ? Math.round(parentClientRect.top + this.window.pageYOffset + borders.top)
                : 0) + 'px';

        // z-index value is not set during screen resize
        if (!screenResized) {
            this.inlineStyling.container.zIndex = this.sliderPanelConfiguration.zIndex
                ? this.sliderPanelConfiguration.zIndex.toString()
                : (this.returningHigherZIndex() + 1).toString();
        }
    }

    private returningHigherZIndex(): number {
        return new SliderPanelZIndexHelper().getHighestZIndex(this.yjQuery('body'));
    }

    private initializeParentRawElement(): void {
        // instantiating "parent" local variable

        const parentRawElement = this.sliderPanelConfiguration.cssSelector
            ? (document.querySelector(this.sliderPanelConfiguration.cssSelector) as HTMLElement)
            : null;

        this.parent = this.yjQuery(parentRawElement || this.element.parent()) as JQuery<
            HTMLElement
        >;
    }

    private initializePanelConfiguration(): void {
        // defining the configuration set on the processed slider panel by merging the JSON object provided as parameter
        // with the default configuration

        this.sliderPanelConfiguration = defaultsDeep(
            this.configuration,
            this.sliderPanelDefaultConfiguration
        );
    }

    private initializeInlineStyles(): void {
        // setting the inline styling applied on the slider panel content according to its configuration.

        const key: 'width' | 'height' =
            ['top', 'bottom'].indexOf(this.sliderPanelConfiguration.slideFrom) === -1
                ? 'width'
                : 'height';

        this.inlineStyling.content[key] = this.sliderPanelConfiguration.overlayDimension;
    }

    private initializeChildTarget(): void {
        // instantiating "appendChildTarget" local variable
        let testedElement = this.parent;
        let modalFound = false;
        let i = 0;

        for (
            i;
            testedElement[0].nodeName !== 'BODY';
            testedElement = this.yjQuery(testedElement.parent()), i++
        ) {
            const isFundamentalModal = (testedElement[0].getAttribute('class') || '').includes(
                'fd-modal__content'
            );
            const isLegacyModal = testedElement[0].getAttribute('id') === 'y-modal-dialog';

            if (isFundamentalModal || isLegacyModal) {
                modalFound = true;
                break;
            }
        }

        this.appendChildTarget = modalFound ? testedElement[0] : document.body;
    }

    private append(): void {
        // appending the slider panel HTML tag as last child of the HTML body tag.
        this.yjQuery(this.appendChildTarget).append(this.element[0]);
    }

    private init(): void {
        this.initializePanelConfiguration();
        this.initializeParentRawElement();
        this.initializeInlineStyles();
        this.initializeChildTarget();
        this.append();
    }
}
