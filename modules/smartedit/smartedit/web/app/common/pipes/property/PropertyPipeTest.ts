/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { PropertyPipe } from './PropertyPipe';

describe('Property Pipe - ', () => {
    let propertyPipe: PropertyPipe<any>;

    beforeEach(() => {
        propertyPipe = new PropertyPipe();
    });

    it('returns array with properties matching passed object', () => {
        // Arrange
        const input = [
            { id: '1', value: 'val' },
            { id: '2', value: 'val' },
            { id: '3', value: 'val1' }
        ];
        const expected = [{ id: '1', value: 'val' }, { id: '2', value: 'val' }];
        const object = { value: 'val' };

        // Act
        const output = propertyPipe.transform(input, object);

        // Assert
        expect(output).toEqual(expected);
    });

    it('returns array with properties matching passed object for multiple properties', () => {
        // Arrange
        const input = [
            { id: '1', value: 'val' },
            { id: '1', value: 'val' },
            { id: '2', value: 'val' }
        ];
        const expected = [{ id: '1', value: 'val' }, { id: '1', value: 'val' }];
        const object = { value: 'val', id: '1' };

        // Act
        const output = propertyPipe.transform(input, object);

        // Assert
        expect(output).toEqual(expected);
    });
});
