/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Pipe, PipeTransform } from '@angular/core';
import { noop } from 'lodash';

/**
 * @ngdoc filter
 * @name smarteditCommonsModule.filter:FilterByFieldPipe
 * @description
 * A pipe for an array of objects, that will search all the first level fields of an object,
 * or optionally allows you to specify which fields to include in the search. Only fields that correspond to string
 * values will be considered in the filtering. The filter implements the AND strategy, thus the filter will return search results
 * regardless of the search string order. IE search string "Add Mobile" will return strings such "Mobile Address" and "Address Mobile"
 *
 * @param {Object[]} items An array of objects
 * @param {string} query The search string in which the values will be filtered by. If no search string is given
 * the original array of objects is be returned.
 * @param {string[]=} keys An array of object fields which determines which key values that the filter will parse through.
 * If no array is specified the filter will check each field value in the array of objects.
 * @param {Function=} callbackFn A function that will be executed after each iteration of the filter.
 *
 */
@Pipe({ name: 'seFilterByField' })
export class FilterByFieldPipe implements PipeTransform {
    public static transform<T>(
        items: T[],
        query: string,
        keys?: string[],
        callbackFn?: (filtered: T[]) => void
    ): T[] {
        const callback = callbackFn || noop;
        const filterResult: T[] = [];

        if (!query) {
            callback(items);

            return items;
        }

        const queryList: string[] = query.toLowerCase().split(' ');

        (items || []).forEach((item: T) => {
            keys = keys || Object.keys(item);

            const terms = keys
                .map((key: string) => (item as any)[key])
                .filter(
                    (value: string) =>
                        typeof value === 'string' || (value as object) instanceof String
                )
                .map((value: string) => value.toLowerCase());

            const matchList = queryList
                .map(
                    (queryItem: string) =>
                        !!terms.find((term: string) => term.indexOf(queryItem) >= 0)
                )
                .filter((exists: boolean) => !exists);

            if (matchList.length === 0) {
                filterResult.push(item);
            }
        });

        callback(filterResult);

        return filterResult;
    }

    transform<T>(
        items: T[],
        query: string,
        keys?: string[],
        callbackFn?: (filtered: T[]) => void
    ): T[] {
        return FilterByFieldPipe.transform(items, query, keys, callbackFn);
    }
}
