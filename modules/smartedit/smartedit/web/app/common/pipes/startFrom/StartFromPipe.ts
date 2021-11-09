/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Pipe, PipeTransform } from '@angular/core';

/**
 * @ngdoc filter
 * @name smarteditCommonsModule.pipe:StartFromPipe
 *
 * @description
 * Used to slice the array of items starting from index passed as an argument.
 */
@Pipe({
    name: 'seStartFrom'
})
export class StartFromPipe implements PipeTransform {
    static transform<T>(input: T[], start: number): T[] {
        return input ? input.slice(Number(start)) : [];
    }

    transform<T>(input: T[], start: number): T[] {
        return StartFromPipe.transform(input, start);
    }
}
