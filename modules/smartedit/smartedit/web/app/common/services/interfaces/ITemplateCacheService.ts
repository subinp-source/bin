/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

export abstract class ITemplateCacheService implements angular.ITemplateCacheService {
    info(): any {
        return undefined;
    }

    put<T>(key: string, value?: T): T {
        return undefined;
    }

    get<T>(key: string): T | undefined {
        return undefined;
    }

    remove(key: string): void {
        return undefined;
    }

    removeAll(): void {
        return undefined;
    }

    destroy(): void {
        return undefined;
    }
}
