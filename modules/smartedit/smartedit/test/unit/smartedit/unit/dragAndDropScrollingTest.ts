/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { DragAndDropScrollingService, InViewElementObserver, WindowUtils } from 'smarteditcommons';
import { jQueryHelper, TranslateHelper } from 'testhelpers';
import { TranslateService } from '@ngx-translate/core';

describe('dragAndDropScrollingService', () => {
    // Variables
    let dragAndDropScrollingService: DragAndDropScrollingService;
    let window: jasmine.SpyObj<Window>;
    let windowUtils: jasmine.SpyObj<WindowUtils>;
    let inViewElementObserver: jasmine.SpyObj<InViewElementObserver>;
    let translate: jasmine.SpyObj<TranslateService>;
    let scrollAreas: any;
    let windowHeight: number;

    beforeEach(function() {
        // Variables
        windowHeight = 1000;
        window = jasmine.createSpyObj<Window>('window', [
            'requestAnimationFrame',
            'height',
            'cancelAnimationFrame'
        ]);
        window.requestAnimationFrame.and.returnValue(123);
        (window as any).height.and.returnValue(windowHeight);
        (window as any).innerHeight = windowHeight;
        (window as any).document = document;

        windowUtils = jasmine.createSpyObj('windowUtils', ['getWindow']);
        windowUtils.getWindow.and.returnValue(window);

        inViewElementObserver = jasmine.createSpyObj<InViewElementObserver>(
            'inViewElementObserver',
            ['addSelector']
        );

        translate = new TranslateHelper()
            .prepare({
                'se.draganddrop.uihint.top': 'some top message',
                'se.draganddrop.uihint.bottom': 'some bottom message'
            })
            .getMock();

        dragAndDropScrollingService = new DragAndDropScrollingService(
            windowUtils,
            translate,
            inViewElementObserver,
            jQueryHelper.jQuery()
        );

        scrollAreas = jasmine.createSpyObj('scrollAreas', [
            'on',
            'off',
            'appendTo',
            'height',
            'text',
            'hide',
            'show',
            'remove',
            'trigger'
        ]);

        spyOn(dragAndDropScrollingService as any, 'getScrollAreas').and.returnValue(scrollAreas);
        scrollAreas.appendTo.and.returnValue(scrollAreas);
    });

    describe('initialize', () => {
        let selector;

        beforeEach(function() {
            selector = jasmine.createSpyObj('selector', ['appendTo']);

            spyOn(dragAndDropScrollingService as any, 'addScrollAreas');
            spyOn(dragAndDropScrollingService as any, 'addEventListeners').and.callThrough();
            spyOn(dragAndDropScrollingService as any, 'getSelector').and.returnValue(selector);
        });

        it('WHEN initialize is called THEN the service is initialized properly', () => {
            expect(inViewElementObserver.addSelector.calls.count()).toBe(2);
            expect(inViewElementObserver.addSelector).toHaveBeenCalledWith('#top_scroll_page');
            expect(inViewElementObserver.addSelector).toHaveBeenCalledWith('#bottom_scroll_page');
            // Arrange

            // Act
            dragAndDropScrollingService.initialize();

            // Assert
            expect((dragAndDropScrollingService as any).addScrollAreas).toHaveBeenCalled();
            expect((dragAndDropScrollingService as any).addEventListeners).toHaveBeenCalled();
            expect((dragAndDropScrollingService as any).scrollDelta).toBe(0);
            expect((dragAndDropScrollingService as any).initialized).toBe(true);
        });

        it('GIVEN the document.documentElement is defined THEN the scrollable area is the document.documentElement', () => {
            // Arrange
            expect((dragAndDropScrollingService as any).getSelector).not.toHaveBeenCalled();

            // Act
            dragAndDropScrollingService.initialize();

            // Assert
            expect((dragAndDropScrollingService as any).getSelector).toHaveBeenCalledWith(
                document.documentElement
            );
        });
    });

    it('WHEN addEventListeners is called THEN the event listeners are set on the scroll areas', () => {
        // Arrange

        // Act
        (dragAndDropScrollingService as any).addEventListeners();

        // Assert
        expect(scrollAreas.on).toHaveBeenCalledWith('dragenter', jasmine.any(Function));
        expect(scrollAreas.on).toHaveBeenCalledWith('dragover', jasmine.any(Function));
        expect(scrollAreas.on).toHaveBeenCalledWith('dragleave', jasmine.any(Function));
    });

    it('WHEN removeEventListeners is called THEN the event listeners are set on the scroll areas', () => {
        // Arrange

        // Act
        (dragAndDropScrollingService as any).removeEventListeners();

        // Assert
        expect(scrollAreas.off).toHaveBeenCalledWith('dragenter');
        expect(scrollAreas.off).toHaveBeenCalledWith('dragover');
        expect(scrollAreas.off).toHaveBeenCalledWith('dragleave');

        expect(scrollAreas.remove).toHaveBeenCalled();
    });

    it('WHEN addScrollAreas is called THEN scroll areas are added', async () => {
        // Arrange
        const topArea = jasmine.createSpyObj('topArea', ['css', 'appendTo', 'text']);
        const bottomArea = jasmine.createSpyObj('bottomArea', ['css', 'appendTo', 'text']);
        spyOn(dragAndDropScrollingService as any, 'getSelector').and.callFake((arg: any) => {
            if (arg === '<div id="top_scroll_page" class="ySECmsScrollArea"></div>') {
                return topArea;
            } else if (arg === '<div id="bottom_scroll_page" class="ySECmsScrollArea"></div>') {
                return bottomArea;
            }
        });

        topArea.appendTo.and.returnValue(topArea);
        bottomArea.appendTo.and.returnValue(bottomArea);

        // Act
        await (dragAndDropScrollingService as any).addScrollAreas();

        // Assert
        expect((dragAndDropScrollingService as any).getSelector).toHaveBeenCalledWith(
            '<div id="top_scroll_page" class="ySECmsScrollArea"></div>'
        );
        expect((dragAndDropScrollingService as any).getSelector).toHaveBeenCalledWith(
            '<div id="bottom_scroll_page" class="ySECmsScrollArea"></div>'
        );
        expect(scrollAreas.height).toHaveBeenCalledWith(jasmine.any(Number));

        expect(topArea.css).toHaveBeenCalledWith({
            top: 0
        });
        expect(bottomArea.css).toHaveBeenCalledWith({
            bottom: 0
        });

        expect(scrollAreas.hide).toHaveBeenCalled();
        expect(topArea.text).toHaveBeenCalledWith('some top message');
        expect(bottomArea.text).toHaveBeenCalledWith('some bottom message');
    });

    it('WHEN mouse enters the UI scrolling hints THEN animation starts', () => {
        // Arrange
        let isFnCalled = false;
        const event = jasmine.createSpyObj('event', ['target']);
        const scrollArea = jasmine.createSpyObj('scrollArea', ['attr']);
        spyOn(dragAndDropScrollingService as any, 'getSelector').and.returnValue(scrollArea);
        spyOn(dragAndDropScrollingService as any, 'scrollPage').and.callFake(function() {
            isFnCalled = true;
        });
        expect((dragAndDropScrollingService as any).animationFrameId).toBe(undefined);

        // Act
        (dragAndDropScrollingService as any).onDragEnter(event);

        // Assert
        const callback = window.requestAnimationFrame.calls.argsFor(0)[0];
        callback();

        expect(window.requestAnimationFrame).toHaveBeenCalled();
        expect(isFnCalled).toBe(true);
        expect((dragAndDropScrollingService as any).animationFrameId).toBe(123);
    });

    describe('on mouse over', () => {
        let event: any;
        let scrollArea: any;

        beforeEach(function() {
            (dragAndDropScrollingService as any).scrollDelta = 0;

            scrollArea = jasmine.createSpyObj('scrollArea', ['attr']);
            spyOn(dragAndDropScrollingService as any, 'getSelector').and.callFake((arg: any) => {
                if (arg === 'target') {
                    return scrollArea;
                } else {
                    return window;
                }
            });

            event = {
                target: 'target'
            };
            event.originalEvent = event;
        });

        it('WHEN mouse is over top slow area THEN the page scrolls up slowly', () => {
            // Arrange
            event.clientY = (dragAndDropScrollingService as any).FAST_SCROLLING_AREA_HEIGHT + 1;
            scrollArea.attr.and.returnValue('top_scroll_page');

            // Act
            (dragAndDropScrollingService as any).onDragOver(event);

            // Assert
            expect((dragAndDropScrollingService as any).scrollDelta).toBe(
                -(dragAndDropScrollingService as any).SCROLLING_STEP
            );
        });

        it('WHEN mouse is over top fast area THEN the page scrolls up quickly', () => {
            // Arrange
            event.clientY = (dragAndDropScrollingService as any).FAST_SCROLLING_AREA_HEIGHT - 1;
            scrollArea.attr.and.returnValue('top_scroll_page');

            // Act
            (dragAndDropScrollingService as any).onDragOver(event);

            // Assert
            expect((dragAndDropScrollingService as any).scrollDelta).toBe(
                -(dragAndDropScrollingService as any).FAST_SCROLLING_STEP
            );
        });

        it('WHEN mouse is over bottom slow area THEN the page scrolls down slowly', () => {
            // Arrange
            event.clientY =
                windowHeight - (dragAndDropScrollingService as any).FAST_SCROLLING_AREA_HEIGHT - 1;
            scrollArea.attr.and.returnValue('bottom_scroll_page');

            // Act
            (dragAndDropScrollingService as any).onDragOver(event);

            // Assert
            expect((dragAndDropScrollingService as any).scrollDelta).toBe(
                (dragAndDropScrollingService as any).SCROLLING_STEP
            );
        });

        it('WHEN mouse is over bottom fast area THEN the page scrolls down quickly', () => {
            // Arrange
            event.clientY =
                windowHeight - (dragAndDropScrollingService as any).FAST_SCROLLING_AREA_HEIGHT + 1;
            scrollArea.attr.and.returnValue('bottom_scroll_page');

            // Act
            (dragAndDropScrollingService as any).onDragOver(event);

            // Assert
            expect((dragAndDropScrollingService as any).scrollDelta).toBe(
                (dragAndDropScrollingService as any).FAST_SCROLLING_STEP
            );
        });
    });

    it('WHEN mouse leaves scroll area THEN scrolling stops', () => {
        // Arrange
        const animationFrameId = 'some id';
        (dragAndDropScrollingService as any).scrollDelta = 123;
        (dragAndDropScrollingService as any).animationFrameId = animationFrameId;

        // Act
        (dragAndDropScrollingService as any).onDragLeave();

        // Assert
        expect((dragAndDropScrollingService as any).scrollDelta).toBe(0);
        expect(window.cancelAnimationFrame).toHaveBeenCalledWith(animationFrameId);
    });

    describe('scrollPage', () => {
        const scrollLimit = 900;

        beforeEach(function() {
            (dragAndDropScrollingService as any).scrollable = jasmine.createSpyObj('scrollable', [
                'scrollTop'
            ]);
            (dragAndDropScrollingService as any).scrollLimitY = scrollLimit;
        });

        it('GIVEN scrollDelta is 0 WHEN scrollPage is called THEN scrolling is not executed', () => {
            // Arrange
            (dragAndDropScrollingService as any).scrollDelta = 0;
            spyOn(dragAndDropScrollingService as any, 'showScrollAreas');

            // Act
            (dragAndDropScrollingService as any).scrollPage();

            // Assert
            expect(
                (dragAndDropScrollingService as any).scrollable.scrollTop
            ).not.toHaveBeenCalled();
            expect(window.requestAnimationFrame).not.toHaveBeenCalled();
            expect((dragAndDropScrollingService as any).showScrollAreas).not.toHaveBeenCalled();
        });

        it('GIVEN the page is at the top WHEN scrollPage is called to scroll up THEN scrolling is not executed', () => {
            // Arrange
            (dragAndDropScrollingService as any).scrollDelta = -10;
            (dragAndDropScrollingService as any).scrollable.scrollTop.and.returnValue(0);
            spyOn(dragAndDropScrollingService as any, 'showScrollAreas');

            // Act
            (dragAndDropScrollingService as any).scrollPage();

            // Assert
            expect(window.requestAnimationFrame).not.toHaveBeenCalled();
            expect((dragAndDropScrollingService as any).showScrollAreas).toHaveBeenCalled(); // Scrolling is not executed, yet areas are shown.
        });

        it('GIVEN the page is at the bottom WHEN scrollPage is called to scroll down THEN scrolling is not executed', () => {
            // Arrange
            (dragAndDropScrollingService as any).scrollDelta = 10;
            (dragAndDropScrollingService as any).scrollable.scrollTop.and.returnValue(scrollLimit);
            spyOn(dragAndDropScrollingService as any, 'showScrollAreas');

            // Act
            (dragAndDropScrollingService as any).scrollPage();

            // Assert
            expect(window.requestAnimationFrame).not.toHaveBeenCalled();
            expect((dragAndDropScrollingService as any).showScrollAreas).toHaveBeenCalled(); // Scrolling is not executed, yet areas are shown.
        });

        it('GIVEN the page is not at the top WHEN scrollPage is called to scroll up THEN scrolling is executed', () => {
            // Arrange
            (dragAndDropScrollingService as any).scrollDelta = -10;
            (dragAndDropScrollingService as any).scrollable.scrollTop.and.returnValue(10);
            spyOn(dragAndDropScrollingService as any, 'showScrollAreas');

            // Act
            (dragAndDropScrollingService as any).scrollPage();

            // Assert
            expect(window.requestAnimationFrame).toHaveBeenCalled();
            expect((dragAndDropScrollingService as any).showScrollAreas).toHaveBeenCalled(); // Scrolling is not executed, yet areas are shown.
        });

        it('GIVEN the page is not at the bottom WHEN scrollPage is called to scroll down THEN scrolling is executed', () => {
            // Arrange
            (dragAndDropScrollingService as any).scrollDelta = 10;
            (dragAndDropScrollingService as any).scrollable.scrollTop.and.returnValue(
                scrollLimit - 10
            );
            spyOn(dragAndDropScrollingService as any, 'showScrollAreas');

            // Act
            (dragAndDropScrollingService as any).scrollPage();

            // Assert
            expect(window.requestAnimationFrame).toHaveBeenCalled();
            expect((dragAndDropScrollingService as any).showScrollAreas).toHaveBeenCalled(); // Scrolling is not executed, yet areas are shown.
        });
    });

    it('WHEN deactivate is called THEN scrolling is completely removed.', () => {
        // Arrange
        spyOn(dragAndDropScrollingService as any, 'removeEventListeners');

        // Act
        (dragAndDropScrollingService as any).deactivate();

        // Assert
        expect((dragAndDropScrollingService as any).removeEventListeners).toHaveBeenCalled();
        expect((dragAndDropScrollingService as any).scrollDelta).toBe(0);
        expect((dragAndDropScrollingService as any).initialized).toBe(false);
    });

    describe('showScrollAreas', () => {
        const scrollLimit = 900;

        beforeEach(function() {
            (dragAndDropScrollingService as any).scrollable = jasmine.createSpyObj('scrollable', [
                'scrollTop'
            ]);
            (dragAndDropScrollingService as any).topScrollArea = jasmine.createSpyObj(
                'topScrollArea',
                ['hide', 'show']
            );
            (dragAndDropScrollingService as any).bottomScrollArea = jasmine.createSpyObj(
                'topScrollArea',
                ['hide', 'show']
            );
            (dragAndDropScrollingService as any).scrollLimitY = scrollLimit;
        });

        it('GIVEN page is at top WHEN showScrollAreas is called THEN only bottom scroll area is shown', () => {
            // Arrange
            (dragAndDropScrollingService as any).scrollable.scrollTop.and.returnValue(0);

            // Act
            (dragAndDropScrollingService as any).showScrollAreas();

            // Assert
            expect((dragAndDropScrollingService as any).topScrollArea.show).not.toHaveBeenCalled();
            expect((dragAndDropScrollingService as any).topScrollArea.hide).toHaveBeenCalled();

            expect((dragAndDropScrollingService as any).bottomScrollArea.show).toHaveBeenCalled();
            expect(
                (dragAndDropScrollingService as any).bottomScrollArea.hide
            ).not.toHaveBeenCalled();
        });

        it('GIVEN page is at the bottom WHEN showScrollAreas is called THEN only top scroll area is shown', () => {
            // Arrange
            (dragAndDropScrollingService as any).scrollable.scrollTop.and.returnValue(scrollLimit);

            // Act
            (dragAndDropScrollingService as any).showScrollAreas();

            // Assert
            expect((dragAndDropScrollingService as any).topScrollArea.show).toHaveBeenCalled();
            expect((dragAndDropScrollingService as any).topScrollArea.hide).not.toHaveBeenCalled();

            expect(
                (dragAndDropScrollingService as any).bottomScrollArea.show
            ).not.toHaveBeenCalled();
            expect((dragAndDropScrollingService as any).bottomScrollArea.hide).toHaveBeenCalled();
        });

        it('GIVEN page is not at top or bottom WHEN showScrollAreas is called THEN both scroll area are shown', () => {
            // Arrange
            (dragAndDropScrollingService as any).scrollable.scrollTop.and.returnValue(10);

            // Act
            (dragAndDropScrollingService as any).showScrollAreas();

            // Assert
            expect((dragAndDropScrollingService as any).topScrollArea.show).toHaveBeenCalled();
            expect((dragAndDropScrollingService as any).topScrollArea.hide).not.toHaveBeenCalled();

            expect((dragAndDropScrollingService as any).bottomScrollArea.show).toHaveBeenCalled();
            expect(
                (dragAndDropScrollingService as any).bottomScrollArea.hide
            ).not.toHaveBeenCalled();
        });
    });

    it('WHEN enable is called THEN the scroll areas are shown', () => {
        // Arrange
        (dragAndDropScrollingService as any).initialized = true;
        (dragAndDropScrollingService as any).scrollable = {
            get() {
                return {
                    scrollHeight: 0
                };
            }
        };

        spyOn(dragAndDropScrollingService as any, 'getSelector').and.returnValue(window);
        spyOn(dragAndDropScrollingService as any, 'showScrollAreas');

        // Act
        (dragAndDropScrollingService as any).enable();

        // Assert
        expect((dragAndDropScrollingService as any).showScrollAreas).toHaveBeenCalled();
    });

    it('WHEN disable is called THEN the scroll areas are hidden the drag leave called (protection against IE bug where we lose track fo the mouse and drop must take care of firing leave)', () => {
        // Arrange
        (dragAndDropScrollingService as any).initialized = true;

        // Act
        dragAndDropScrollingService.disable();

        // Assert
        expect(scrollAreas.trigger).toHaveBeenCalledWith('dragleave');
        expect(scrollAreas.hide).toHaveBeenCalled();
    });
});
