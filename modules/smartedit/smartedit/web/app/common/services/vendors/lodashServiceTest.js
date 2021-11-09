/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('ylodash', function() {
    var lodash;

    beforeEach(inject(function(_lodash_) {
        lodash = _lodash_;
    }));

    it('loads the right underscore library', function() {
        // Arrange
        var nestedArray = [1, [2, [3, [4]], 5]];
        var flattenedArray = [1, 2, 3, 4, 5];

        // Act
        var result = lodash.flattenDeep(nestedArray);

        // Assert
        expect(result).toEqual(flattenedArray);
    });
});
