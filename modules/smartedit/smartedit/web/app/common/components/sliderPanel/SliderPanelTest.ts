/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ElementRef, Renderer2 } from '@angular/core';

import { SliderPanelServiceFactory } from './SliderPanelServiceFactory';
import { SliderPanelService } from './SliderPanelService';
import { CSS_CLASSNAMES, SliderPanelComponent } from './SliderPanelComponent';
import { WindowUtils } from '../../utils';

describe('sliderPanelController', () => {
    let sliderPanelController: SliderPanelComponent;
    let renderer: jasmine.SpyObj<Renderer2>;
    let element: ElementRef;
    let sliderPanelServiceFactory: jasmine.SpyObj<SliderPanelServiceFactory>;
    let mockService: SliderPanelService;

    beforeEach(() => {
        mockService = ({
            inlineStyling: {
                container: {
                    width: 'inline styling applied on slider panel container'
                } as CSSStyleDeclaration,
                content: {
                    width: 'inline styling applied on slider panel content'
                } as CSSStyleDeclaration
            },
            sliderPanelConfiguration: {
                noGreyedOutOverlay: true,
                slideFrom: 'initialPosition',
                overlayDimension: '80%'
            },
            updateContainerInlineStyling() {
                this.inlineStyling.container =
                    'updated inline styling applied on slider panel container';
            }
        } as unknown) as SliderPanelService;

        sliderPanelServiceFactory = jasmine.createSpyObj('sliderPanelServiceFactory', [
            'getNewServiceInstance'
        ]);
        sliderPanelServiceFactory.getNewServiceInstance.and.returnValue(mockService);

        renderer = jasmine.createSpyObj('renderer', ['removeClass', 'addClass']);

        element = { nativeElement: document.createElement('div') };

        sliderPanelController = new SliderPanelComponent(
            renderer,
            element,
            new WindowUtils(),
            jQuery,
            sliderPanelServiceFactory
        );
    });

    describe('init', () => {
        it('WHEN controller is created THEN the sliderpanel is initialized properly with the correct data', () => {
            sliderPanelController.ngOnInit();

            expect(sliderPanelController.sliderPanelConfiguration).toEqualData(
                mockService.sliderPanelConfiguration
            );

            expect(sliderPanelController.slideClassName).toBe(
                CSS_CLASSNAMES.SLIDERPANEL_SLIDEPREFIX + 'initialPosition'
            );

            expect((sliderPanelController as any).inlineStyling).toEqualData(
                mockService.inlineStyling
            );

            expect(sliderPanelController.sliderPanelShowChange).toBeDefined();
            expect(sliderPanelController.sliderPanelHideChange).toBeDefined();
        });
    });

    describe('show', () => {
        it('WHEN controller is shown THEN the sliderpanel inline styling and applied hmtl class are updated accordingly', () => {
            sliderPanelController.ngOnInit();
            sliderPanelController.showSlider();

            expect((sliderPanelController as any).inlineStyling.container).toBe(
                'updated inline styling applied on slider panel container'
            );
        });
    });

    describe('hide', () => {
        it('GIVEN shown panel WHEN panel is hidden THEN the sliderpanel inline styling and applied hmtl class are updated accordingly', async (done) => {
            // GIVEN
            sliderPanelController.ngOnInit();
            await sliderPanelController.showSlider();

            expect(sliderPanelController.isShown).toBe(true);
            expect((sliderPanelController as any).inlineStyling.container).toBe(
                'updated inline styling applied on slider panel container'
            );

            // WHEN
            await sliderPanelController.hideSlider();

            expect(sliderPanelController.isShown).toBe(false);
            done();
        });
    });
});
