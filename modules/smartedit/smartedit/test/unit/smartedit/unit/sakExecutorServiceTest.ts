/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SakExecutorService } from 'smartedit/services/sakExecutor/SakExecutorService';
import { DecoratorService } from 'smartedit/services';

describe('sakExecutor service', function() {
    let sakExecutor: SakExecutorService;
    const decorators = ['decorator1', 'decorator2'];

    const projectedContent = 'someProjectedContent';
    const smarteditComponentType = 'smarteditComponentType';
    const smarteditComponentId = 'smarteditComponentId';

    let decoratorService: jasmine.SpyObj<DecoratorService>;
    let element: jasmine.SpyObj<HTMLElement>;

    beforeEach(() => {
        element = jasmine.createSpyObj<HTMLElement>('element', ['getAttribute']);
        element.getAttribute.and.callFake((attributeName: string) => {
            return ({
                'data-smartedit-component-type': smarteditComponentType,
                'data-smartedit-component-id': smarteditComponentId
            } as any)[attributeName];
        });

        decoratorService = jasmine.createSpyObj('decoratorService', ['getDecoratorsForComponent']);
        decoratorService.getDecoratorsForComponent.and.callFake((type: string, id: string) => {
            if (id === smarteditComponentId && type === smarteditComponentType) {
                return Promise.resolve(decorators);
            } else {
                throw new Error(
                    'unexpected arguments passed to decoratorService.getDecoratorsForComponent'
                );
            }
        });

        sakExecutor = new SakExecutorService(decoratorService);
    });

    it('sakExecutor.wrapDecorators fetches eligible decorators for given component type and stack those decorators around the projectedContent and returns a node', (done) => {
        sakExecutor
            .wrapDecorators(document.createTextNode(projectedContent), element)
            .then((returnValue: HTMLElement) => {
                expect(returnValue.outerHTML.replace(/\s/g, '')).toBe(
                    `<decorator-2 class="decorator2 se-decorator-wrap"
					active="false"
					component-attributes="componentAttributes"
					data-smartedit-component-id="smarteditComponentId"
					data-smartedit-component-type="smarteditComponentType">
					<decorator-1 class="decorator1 se-decorator-wrap"
					active="false"
					component-attributes="componentAttributes"
					data-smartedit-component-id="smarteditComponentId"
					data-smartedit-component-type="smarteditComponentType">
					someProjectedContent
				</decorator-1>
				</decorator-2>`.replace(/\s/g, '')
                );
            });
        done();
    });
});
