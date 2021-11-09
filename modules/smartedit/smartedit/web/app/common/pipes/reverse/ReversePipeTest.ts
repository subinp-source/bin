/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';

import { ReversePipe } from './ReversePipe';

describe('Reverse Pipe Test', () => {
    const reversePipe = new ReversePipe();

    it('GIVEN an array THEN it should return a shallow copy of reversed array', () => {
        const array = [1, 2, 3];
        const arrayReversed = reversePipe.transform(array);

        expect(arrayReversed).toEqual([3, 2, 1]);
        const isSameReference = array === arrayReversed;
        expect(isSameReference).toBe(false);
    });

    it('GIVEN undefined THEN it should return undefined', () => {
        expect(reversePipe.transform(undefined)).toBe(undefined);
    });
});
