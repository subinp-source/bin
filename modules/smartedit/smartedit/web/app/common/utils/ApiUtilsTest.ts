/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';

import { apiUtils } from 'smarteditcommons';

describe('Api Utils Test', () => {
    it('GIVEN the provided object contains an array WHEN getDataFromResponse is called THEN it returns the array', () => {
        // GIVEN
        const sampleResponse = {
            somePromise: {},
            otherProperty: 'some property',
            testArray: ['A', 'B', 'C'],
            otherProperty2: {}
        };

        // WHEN
        const result = apiUtils.getDataFromResponse(sampleResponse);

        // THEN
        expect(result).toEqualData(['A', 'B', 'C']);
    });

    it('GIVEN the provided object contains an array WHEN getKeyHoldingDataFromResponse is called THEN it returns the id of the property holding the array', () => {
        // GIVEN
        const sampleResponse = {
            somePromise: {},
            otherProperty: 'some property',
            testArray: ['A', 'B', 'C'],
            otherProperty2: {}
        };

        // WHEN
        const result = apiUtils.getKeyHoldingDataFromResponse(sampleResponse);

        // THEN
        expect(result).toBe('testArray');
    });
});
