/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
export abstract class ILegacyDecoratorToCustomElementConverter {
    getScopes(): string[] {
        return null;
    }

    convert(_componentName: string): void {
        return null;
    }

    convertIfNeeded(componentNames: string[]): void {
        return null;
    }
}
