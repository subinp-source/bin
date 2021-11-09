/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SliderPanelServiceFactory } from './SliderPanelServiceFactory';
import { SliderPanelService } from './SliderPanelService';

describe('sliderPanelService', () => {
    let sliderPanelServiceFactory: SliderPanelServiceFactory;
    let sliderPanelService: SliderPanelService;
    let mockedDiv: JQuery<HTMLElement>;
    let div: HTMLElement;

    beforeEach(() => {
        // building mock HTML structure and appending it to the body element
        div = document.createElement('div');
        document.body.appendChild(div);
        mockedDiv = angular.element(div);

        sliderPanelServiceFactory = new SliderPanelServiceFactory(jQuery);
    });

    afterEach(() => {
        mockedDiv.remove();
    });

    describe('legacySliderPanel instantiation', () => {
        it('returns the correct default configuration', () => {
            sliderPanelService = sliderPanelServiceFactory.getNewServiceInstance(
                mockedDiv,
                window,
                {}
            );

            expect(sliderPanelService.sliderPanelConfiguration).toEqual({
                slideFrom: 'right',
                overlayDimension: '80%'
            });
        });

        it('returns the updated configuration', () => {
            const expectedConfiguration = {
                cssSelector: 'value',
                slideFrom: 'bottom',
                overlayDimension: '80%'
            };

            sliderPanelService = sliderPanelServiceFactory.getNewServiceInstance(
                mockedDiv,
                window,
                {
                    cssSelector: 'value',
                    slideFrom: 'bottom'
                }
            );

            expect(sliderPanelService.sliderPanelConfiguration).toEqual(expectedConfiguration);
        });

        it('appends the slider panel as last child of the body element', () => {
            spyOn(document.body, 'appendChild');

            sliderPanelService = sliderPanelServiceFactory.getNewServiceInstance(
                mockedDiv,
                window,
                {}
            );

            expect(document.body.appendChild).toHaveBeenCalledWith(mockedDiv[0]);
        });
    });
});
