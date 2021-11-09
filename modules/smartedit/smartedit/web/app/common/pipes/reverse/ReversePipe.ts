/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Pipe, PipeTransform } from '@angular/core';

/**
 * @ngdoc filter
 * @name smarteditCommonsModule.filter:ReversePipe
 *
 * @description
 * Returns an array containing the items from the specified collection in reverse order
 */
@Pipe({ name: 'seReverse' })
export class ReversePipe implements PipeTransform {
    transform<T>(value: T[]): T[] | undefined {
        if (!value) {
            return undefined;
        }
        return value.slice().reverse();
    }
}
