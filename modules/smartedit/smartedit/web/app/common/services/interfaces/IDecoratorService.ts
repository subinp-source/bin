/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IDecoratorDisplayCondition } from './IDecorator';

export abstract class IDecoratorService {
    addMappings(mappings: { [index: string]: string[] }): void {
        'proxyFunction';
    }

    enable(decoratorKey: string, displayCondition?: IDecoratorDisplayCondition): void {
        'proxyFunction';
    }

    disable(decoratorKey: string): void {
        'proxyFunction';
    }

    getDecoratorsForComponent(componentType: string, componentId?: string): Promise<string[]> {
        'proxyFunction';

        return null;
    }
}
