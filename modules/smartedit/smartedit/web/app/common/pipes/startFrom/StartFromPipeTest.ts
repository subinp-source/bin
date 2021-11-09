/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';

import { StartFromPipe } from './StartFromPipe';

describe('StartFromPipe', () => {
    const mockInput = ['a', 'b', 'c', 'd'];

    let pipe: StartFromPipe;
    beforeEach(() => {
        pipe = new StartFromPipe();
    });

    it('should slice the array of items for index type number', () => {
        const result = pipe.transform(mockInput, 1);

        expect(result.length).toBe(3);
    });

    it('should slice the array of items for index type string', () => {
        const result = pipe.transform(mockInput, '1' as any);

        expect(result.length).toBe(3);
    });

    it('should return an empty array for undefined input', () => {
        const result = pipe.transform(undefined, 1);

        expect(result.length).toBe(0);
    });
});
