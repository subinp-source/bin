/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { JQueryUtilsService } from './JQueryUtilsService';
// import { domHelper } from 'testhelpers';
import { windowUtils } from '../utils';

function elementFromString(strElem: string) {
    const elem = document.createElement('div');

    elem.innerHTML = strElem;

    return elem;
}
describe('JQuery Utils Service', () => {
    let service: JQueryUtilsService;
    const EXTENDED_VIEW_PORT_MARGIN = 10;

    beforeEach(() => {
        service = new JQueryUtilsService(jQuery, document, EXTENDED_VIEW_PORT_MARGIN, windowUtils);
    });

    describe('JQueryUtilsService.extractFromElement', () => {
        it('Returns correct element when the searched element resides within parent', () => {
            const span = `<span id="mySpan"></span>`;
            const parent = `
                <div>
                    ${span}
                </div>
            `;
            const selector = '#mySpan';

            const result = service.extractFromElement(parent, selector);

            expect(result).toBeDefined();
            expect(result.attr('id')).toBe('mySpan');
        });

        it('Returns no element when the searched element does not reside within parent', () => {
            const span = `<span id="mySpan"></span>`;
            const parent = `
                <div>
                    ${span}
                </div>
            `;
            const selector = '#myOtherSpan';

            const result = service.extractFromElement(parent, selector);

            expect(result.length).toBe(0);
        });
    });

    describe('JQueryUtilsService.isInDOM', () => {
        const element = elementFromString('<span id="mySpan"></span>');
        const element2 = elementFromString('<span id="mySpan2"></span>');

        beforeEach(() => {
            document.body.appendChild(element);
        });

        it('Returns true if element is within the DOM', () => {
            expect(service.isInDOM(element)).toBe(true);
        });

        it('Returns true if element is not within the DOM', () => {
            expect(service.isInDOM(element2)).toBe(false);
        });
    });

    describe('JQueryUtilsService.isInExtendedViewPort', () => {
        it('Returns true if element is intersecting', () => {
            const element = elementFromString('<span id="mySpan"></span>');

            document.body.appendChild(element);

            expect(service.isInExtendedViewPort(element)).toBe(true);
        });

        it('Returns true if element is not intersecting', () => {
            const element = elementFromString('<span id="mySpan"></span>');

            element.style.left = '-1000px';
            element.style.position = 'absolute';

            document.body.appendChild(element);

            expect(service.isInExtendedViewPort(element)).toBe(false);
        });
    });
});
