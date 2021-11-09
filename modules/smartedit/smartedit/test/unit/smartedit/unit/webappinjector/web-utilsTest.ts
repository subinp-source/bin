/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import WebUtils from '../../../../../web/app/webappinjector/src/web-utils';

describe('Whitelisting-Utils from webapp-injector', () => {
    describe('getWebappScriptElementFromDocument()', () => {
        it('Throw error when currentScript exists but is invalid', () => {
            const mockDocument: unknown = {
                currentScript: 'bla'
            };
            expect(() =>
                WebUtils.getWebappScriptElementFromDocument(mockDocument as Document)
            ).toThrowError();
        });

        it('Return valid currentScript element', () => {
            const validScritpElement = document.createElement<'script'>('script');
            const mockDocument: unknown = {
                currentScript: validScritpElement
            };
            expect(WebUtils.getWebappScriptElementFromDocument(mockDocument as Document)).toBe(
                validScritpElement
            );
        });

        it('Returns script element from element id', () => {
            const mock = { some: 'dummy data ' } as any;
            const mockDocument: unknown = {
                currentScript: null,
                querySelector: () => {
                    return mock;
                }
            };
            expect(WebUtils.getWebappScriptElementFromDocument(mockDocument as Document)).toBe(
                mock
            );
        });

        it('Throw error when not exactly 1 webApplicationInjector found by script src', () => {
            const mockDocument = {
                currentScript: null,
                querySelector: (): any => null,
                querySelectorAll: () => {
                    return {
                        length: 1
                    };
                }
            } as unknown;
            expect(() =>
                WebUtils.getWebappScriptElementFromDocument(mockDocument as Document)
            ).toThrowError();

            // ---->>> length = 2
            (mockDocument as any).querySelectorAll = () => {
                return {
                    length: 2
                };
            };
            expect(() =>
                WebUtils.getWebappScriptElementFromDocument((mockDocument as unknown) as Document)
            ).toThrowError();
        });

        it('Returns 1 webApplicationInjector script when found by script src', () => {
            const dummy = 'data';
            const mockDocument = {
                currentScript: null,
                querySelector: (): any => null,
                querySelectorAll: () => {
                    return {
                        length: 1,
                        item: () => dummy
                    };
                }
            } as unknown;
            expect(WebUtils.getWebappScriptElementFromDocument(mockDocument as Document)).toBe(
                dummy as any
            );
        });
    });

    describe('extractQueryParameter()', () => {
        it('first of 1 query param', () => {
            expect(WebUtils.extractQueryParameter('domain?a=b', 'a')).toBe('b');
        });

        it('first of many query param', () => {
            expect(WebUtils.extractQueryParameter('domain?a=b&c=d', 'a')).toBe('b');
        });

        it('last of many query param', () => {
            expect(WebUtils.extractQueryParameter('domain?a=b&c=d', 'c')).toBe('d');
        });

        it('missing to be empty', () => {
            expect(WebUtils.extractQueryParameter('domain?a=b&c=d', 'x')).toBe(undefined);
        });

        it('empty to be empty', () => {
            expect(WebUtils.extractQueryParameter('domain?a=&c=', 'a')).toBe(undefined);
        });

        it('empty end of list to be empty', () => {
            expect(WebUtils.extractQueryParameter('domain?a=b&c=', 'c')).toBe(undefined);
        });
    });

    describe('injectJS()', () => {
        let mockScriptJsSpyWrapper;
        let scriptJsSpy: jasmine.Spy;

        beforeEach(() => {
            mockScriptJsSpyWrapper = {
                mockScriptJsFunction: (script: string, cb?: () => void) => {
                    cb();
                }
            };
            scriptJsSpy = spyOn(mockScriptJsSpyWrapper, 'mockScriptJsFunction').and.callThrough();
            spyOn(WebUtils, 'getScriptJs').and.callFake(() => scriptJsSpy);
        });

        it('Empty script list does not fail', () => {
            expect(() => WebUtils.injectJS([])).not.toThrowError();
        });

        it('Single script is added to scriptjs', () => {
            WebUtils.injectJS(['a']);

            expect(scriptJsSpy.calls.count()).toBe(1);
            expect(scriptJsSpy.calls.argsFor(0)[0]).toBe('a');
        });

        it('Multiple scripts are added to scriptjs', () => {
            WebUtils.injectJS(['a', 'b', 'c']);

            expect(scriptJsSpy.calls.count()).toBe(3);
            expect(scriptJsSpy.calls.argsFor(0)[0]).toBe('a');
            expect(scriptJsSpy.calls.argsFor(1)[0]).toBe('b');
            expect(scriptJsSpy.calls.argsFor(2)[0]).toBe('c');
        });
    });

    describe('injectJS()', () => {
        it('Empty script list does not fail', () => {
            expect(() => WebUtils.injectCSS(null, [])).not.toThrowError();
        });

        it('Null script list does not fail', () => {
            expect(() => WebUtils.injectCSS(null, null)).not.toThrowError();
        });

        it('Single link element added to page', () => {
            const mockHead = jasmine.createSpyObj<Node>('mockHead', ['appendChild']);

            WebUtils.injectCSS(mockHead as any, ['xyx']);

            // TODO - I can't seem to find a way to test the appendChild args
            // the args ends up being the whole link element somehow, not an object or anything
            expect(mockHead.appendChild.calls.count()).toBe(1);
        });

        it('Single link element added to page', () => {
            const mockHead = jasmine.createSpyObj<Node>('mockHead', ['appendChild']);

            WebUtils.injectCSS(mockHead as any, ['a', 'b', 'c']);

            // TODO - I can't seem to find a way to test the appendChild args
            // the args ends up being the whole link element somehow, not an object or anything
            expect(mockHead.appendChild.calls.count()).toBe(3);
        });
    });
});
