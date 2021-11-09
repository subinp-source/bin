/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Pipe, PipeTransform } from '@angular/core';
import { TypedMap } from '@smart/utils';

/**
 * @ngdoc directive
 * @name smarteditCommonsModule.pipe:PropertyPipe
 * @element seProperty
 *
 * @description
 * Angular pipe used to filter array of objects by object passed as an argument. The pipe will return array of objects that contains the exact keys and values of passed object.
 *
 * @example
 *
 * <div *ngFor='let item of items | seProperty:{ isEnabled: true }'></div>
 */

@Pipe({ name: 'seProperty' })
export class PropertyPipe<T extends TypedMap<any>> implements PipeTransform {
    transform(array: T[], propObject: TypedMap<string>): T[] {
        if (!array) {
            return undefined;
        }

        if (!propObject) {
            return [...array];
        }

        return array.filter((item) =>
            Object.keys(propObject).every((key: string) => item[key] === propObject[key])
        );
    }
}
