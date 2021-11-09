/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Pipe, PipeTransform } from '@angular/core';
import { TypedMap } from '@smart/utils';
import { Observable } from 'rxjs';
import { map, pairwise, startWith, tap } from 'rxjs/operators';

import { L10nService } from '../../services/L10nService';

export type L10nPipeFilterFn = (localizedMap: TypedMap<string> | string) => string;

/** @internal */
export function getLocalizedFilterFn(language: string): L10nPipeFilterFn {
    return (localizedMap: TypedMap<string> | string): string => {
        if (typeof localizedMap === 'string') {
            return localizedMap;
        } else if (localizedMap) {
            return localizedMap[language] || localizedMap[Object.keys(localizedMap)[0]];
        } else {
            return undefined;
        }
    };
}

/**
 * Pipe for translating localized maps for the current language.
 *
 * @example
 * ```
 * localizedMap = {
 *   en: 'dummyText in english',
 *   fr: 'dummyText in french'
 * };
 *
 * {{ localizedMap | seL10n | async }}
 *  ```
 */
@Pipe({
    name: 'seL10n'
})
export class L10nPipe implements PipeTransform {
    private filterFn: L10nPipeFilterFn;

    constructor(private l10nService: L10nService) {}

    transform(localizedMap: string | TypedMap<string>): Observable<string> {
        return this.l10nService.languageSwitch$.pipe(
            startWith(null),
            pairwise(),
            tap(([prev, curr]) => {
                if (prev !== curr) {
                    this.filterFn = getLocalizedFilterFn(curr);
                }
            }),
            map(() => this.filterFn(localizedMap))
        );
    }
}
