/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lodash from 'lodash';
import { Injectable } from '@angular/core';
import {
    IDecoratorService,
    ID_ATTRIBUTE,
    SeDowngradeService,
    TYPE_ATTRIBUTE
} from 'smarteditcommons';

@SeDowngradeService()
/* @internal */
@Injectable()
export class SakExecutorService {
    constructor(private decoratorService: IDecoratorService) {}

    wrapDecorators(projectedContent: Node, element: HTMLElement): Promise<HTMLElement> {
        return this.decoratorService
            .getDecoratorsForComponent(
                element.getAttribute(TYPE_ATTRIBUTE),
                element.getAttribute(ID_ATTRIBUTE)
            )
            .then((decorators: string[]) => {
                const compiled = decorators.reduce((templateacc: Node, decorator: string) => {
                    const decoratorSelector = lodash.kebabCase(decorator.replace('se.', ''));
                    const decoratorClass = lodash.camelCase(decorator.replace('se.', ''));

                    const decoratorElement = document.createElement(decoratorSelector);
                    decoratorElement.className = `${decoratorClass} se-decorator-wrap`;

                    // To display slot menu need 32px, if there is not enough space it should be display at the slot bottom
                    if (
                        decoratorSelector === 'slot-contextual-menu' &&
                        element.style.position === 'absolute' &&
                        parseInt(element.style.top, 10) < 32
                    ) {
                        decoratorElement.setAttribute('show-at-bottom', 'true');
                    }

                    decoratorElement.setAttribute('active', 'false');
                    decoratorElement.setAttribute('component-attributes', 'componentAttributes');

                    decoratorElement.appendChild(templateacc);

                    this.setAttributeOn(decoratorElement, 'data-smartedit-element-uuid', element);
                    this.setAttributeOn(decoratorElement, 'data-smartedit-component-id', element);
                    this.setAttributeOn(decoratorElement, 'data-smartedit-component-uuid', element);
                    this.setAttributeOn(decoratorElement, 'data-smartedit-component-type', element);
                    this.setAttributeOn(
                        decoratorElement,
                        'data-smartedit-catalog-version-uuid',
                        element
                    );
                    this.setAttributeOn(decoratorElement, 'data-smartedit-container-id', element);
                    this.setAttributeOn(decoratorElement, 'data-smartedit-container-uuid', element);
                    this.setAttributeOn(decoratorElement, 'data-smartedit-container-type', element);

                    return decoratorElement;
                }, projectedContent) as HTMLElement;

                return compiled;
            });
    }

    private setAttributeOn(
        decorator: HTMLElement,
        attributeName: string,
        element: HTMLElement
    ): void {
        const value = element.getAttribute(attributeName);
        if (value) {
            decorator.setAttribute(attributeName, value);
        }
    }
}
