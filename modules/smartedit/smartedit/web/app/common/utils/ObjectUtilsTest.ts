/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';

import { objectUtils } from 'smarteditcommons';

describe('Object Utils Test', () => {
    it('merge will merge two objects into one', () => {
        const source = {
            apple: 0,
            banana: {
                weight: 52,
                price: 100
            },
            cherry: 97
        };

        const dest = {
            banana: {
                price: 200
            },
            durian: 100
        };

        const result = objectUtils.merge(source, dest);

        expect(result).toEqualData({
            apple: 0,
            banana: {
                price: 200
            },
            cherry: 97,
            durian: 100
        });
        expect(result).toBe(source as typeof result);
    });

    it('convertToArray will convert a given object to an array', () => {
        const sampleObj = {
            key1: 'value1',
            key2: 'value2',
            key3: 'value3',
            key4: 'value4'
        };

        const sampleObjWithExcludedKeys = {
            key1: 'value1',
            $: 'value$',
            key3: 'value3',
            toJSON: 'valuetoJSON'
        };

        expect(objectUtils.convertToArray(sampleObj)).toEqual([
            {
                key: 'key1',
                value: 'value1'
            },
            {
                key: 'key2',
                value: 'value2'
            },
            {
                key: 'key3',
                value: 'value3'
            },
            {
                key: 'key4',
                value: 'value4'
            }
        ]);

        expect(objectUtils.convertToArray(sampleObjWithExcludedKeys)).toEqual([
            {
                key: 'key1',
                value: 'value1'
            },
            {
                key: 'key3',
                value: 'value3'
            }
        ]);
    });

    it('uniqueArray will return an array of unique items from a given pair of input arrays', () => {
        const array1 = ['item1', 'item2', 'item3'];
        const array2 = ['item4', 'item3', 'item6', 'item2'];

        const uniqueArr = objectUtils.uniqueArray(array1, array2);

        expect(uniqueArr).toEqualData(['item1', 'item2', 'item3', 'item4', 'item6']);
    });

    it('isFunction will return true if value is a function otherwise false', () => {
        expect(
            objectUtils.isFunction(function() {
                return;
            })
        ).toBe(true);
        expect(objectUtils.isFunction({})).toBe(false);
        expect(objectUtils.isFunction(123)).toBe(false);
    });

    it('WHEN deepIterateOverObjectWith is called THEN it iterates through all the properties of the object and applies the callback for every property value', () => {
        // GIVEN
        const inputObj: any = {
            visible: true,
            restrictions: [],
            content: {
                de: 'aaaa',
                fr: 'bbbb'
            },
            position: 2,
            slotId: 'id',
            removedField: 1
        };

        const callback = (value: any) => typeof value === 'string';

        // WHEN
        const result = objectUtils.deepIterateOverObjectWith(inputObj, callback);

        // THEN
        const outputObj = {
            visible: false,
            restrictions: false,
            content: {
                de: true,
                fr: true
            },
            position: false,
            slotId: true,
            removedField: false
        };

        expect(result).toEqual(outputObj);
    });

    it('WHEN deepObjectPropertyDiff is called THEN it returns object that is populated with all the fields that are modified, removed or added by the user', () => {
        // GIVEN
        const firstObject: any = {
            visible: true,
            restrictions: [],
            content: {
                de: 'aaaa',
                fr: 'bbbb'
            },
            position: 2,
            slotId: 'id',
            removedField: 1
        };

        const secondObject: any = {
            visible: false,
            restrictions: ['dddd'],
            content: {
                de: 'aaaa',
                en: 'cccc'
            },
            position: 54,
            slotId: 'id',
            addedField: 2
        };

        // WHEN
        const result = objectUtils.deepObjectPropertyDiff(firstObject, secondObject);

        // THEN
        const outputObj = {
            content: {
                de: false,
                en: true,
                fr: true
            },
            position: true,
            restrictions: true,
            slotId: false,
            visible: true,
            removedField: true,
            addedField: true
        };
        expect(result).toEqual(outputObj);
    });

    it('copy will do a deep copy of a given object into another object', () => {
        const JSVar = {
            key1: 'value1',
            key2: 'value2'
        };

        const newVar = objectUtils.copy(JSVar);

        expect(newVar).not.toBe(JSVar);
        expect(newVar).toEqualData({
            key1: 'value1',
            key2: 'value2'
        });
    });

    describe('readObjectStructure', () => {
        it('will return given value when it is one of: undefined, null, Promise', () => {
            const promise = Promise.resolve();
            expect(objectUtils.readObjectStructure(undefined)).toBe(undefined);
            expect(objectUtils.readObjectStructure(null)).toBe(null);
            expect(objectUtils.readObjectStructure(promise)).toBe(promise);
        });

        it('will return structure name', () => {
            const mockFunction = function() {
                return;
            };
            const mockElement = document.createElement('span');
            const mockyjQuerySelector = { length: 0 };

            class MockClass {}
            const mockInstance = new MockClass();

            const mockJson = {
                propA$: 2,
                propB: '3',
                propC: {
                    nested1: '1',
                    nested2: '2',
                    nested3: '3'
                }
            };

            expect(objectUtils.readObjectStructure(mockFunction)).toBe('FUNCTION');
            expect(objectUtils.readObjectStructure(1)).toBe('NUMBER');
            expect(objectUtils.readObjectStructure('1')).toBe('STRING');
            expect(objectUtils.readObjectStructure(true)).toBe('BOOLEAN');
            expect(objectUtils.readObjectStructure(mockElement)).toBe('ELEMENT');
            expect(objectUtils.readObjectStructure([1, '1'])).toEqual(['NUMBER', 'STRING']);
            expect(objectUtils.readObjectStructure(mockyjQuerySelector)).toBe('JQUERY');
            expect(objectUtils.readObjectStructure(mockInstance)).toBe('MockClass');

            const jsonStructure = {
                propA$: 'NUMBER',
                propB: 'STRING',
                propC: { nested1: 'STRING', nested2: 'STRING', nested3: 'STRING' }
            };
            expect(objectUtils.readObjectStructure(mockJson)).toEqual(jsonStructure);
        });

        it('will return Class Name for object of class which has more than 25 properties', () => {
            // tslint:disable-next-line:max-classes-per-file
            class MockClass {}
            const mockInstance = new MockClass();
            // add 26 props to mockInstance dynamically
            Array.from(Array(26).keys()).forEach((key) => ((mockInstance as any)[key] = key));

            const objectStructure = objectUtils.readObjectStructure(mockInstance);

            expect(objectStructure).toBe('MockClass');
        });
    });

    it('uniqueArray will return first Array argument supplemented with new entries from the second Array argument', () => {
        const arr1 = [1, 2, 3, 4, 5];
        const arr2 = ['1', '2', '3', '4', 5];

        const uniqueArray = objectUtils.uniqueArray(arr1, arr2);

        expect(uniqueArray).toEqual([1, 2, 3, 4, 5, '1', '2', '3', '4']);
        expect(uniqueArray).toBe(arr1); // mutates
    });

    describe('orderBy', () => {
        it('should return a new reference for a given array', () => {
            const arr1 = [1];
            const sortedArr1 = objectUtils.sortBy(arr1);
            expect(sortedArr1).not.toBe(arr1);

            const arr2 = [{ a: 'a' }, { a: 'b' }];
            const sortedArr2 = objectUtils.sortBy(arr2, 'a');
            expect(sortedArr2).not.toBe(arr2);
        });

        describe('array of strings', () => {
            it('should return a sorted array of strings in ascending order', () => {
                const arr = ['c', 'a', 'b'];
                // default
                const sortedArr1 = objectUtils.sortBy(arr);
                expect(sortedArr1).toEqual(['a', 'b', 'c']);

                const sortedArr3 = objectUtils.sortBy(arr, undefined, false);
                expect(sortedArr3).toEqual(['a', 'b', 'c']);
            });

            it('should return a sorted array of strings in descending order', () => {
                const arr = ['c', 'a', 'b'];
                const sortedArr = objectUtils.sortBy(arr, undefined, true);
                expect(sortedArr).toEqual(['c', 'b', 'a']);
            });

            it('should return a sorted array of string numbers in ascending order', () => {
                const arr = ['3', '1', '2'];
                // default
                const sortedArr1 = objectUtils.sortBy(arr);
                expect(sortedArr1).toEqual(['1', '2', '3']);

                const sortedArr3 = objectUtils.sortBy(arr, undefined, false);
                expect(sortedArr3).toEqual(['1', '2', '3']);
            });

            it('should return a sorted array of string numbers in descending order', () => {
                const arr = ['3', '1', '2'];
                const sortedArr = objectUtils.sortBy(arr, undefined, true);
                expect(sortedArr).toEqual(['3', '2', '1']);
            });

            describe('array of numbers', () => {
                it('should return a sorted array of numbers in ascending order', () => {
                    // default
                    const arr1 = [3, 1, 2];
                    const sortedArr1 = objectUtils.sortBy(arr1);
                    expect(sortedArr1).toEqual([1, 2, 3]);

                    const arr2 = [1, 3, 2];
                    const sortedArr3 = objectUtils.sortBy(arr2, undefined, false);
                    expect(sortedArr3).toEqual([1, 2, 3]);
                });

                it('should return a sorted array of numbers in descending order', () => {
                    const arr = [1, 3, 2];
                    const sortedArr = objectUtils.sortBy(arr, undefined, true);
                    expect(sortedArr).toEqual([3, 2, 1]);
                });
            });
        });

        describe('objects', () => {
            it('should return a sorted array of objects in ascending order', () => {
                // default
                const arr1 = [{ a: '1', b: 'b' }, { a: '2', b: 'b' }];
                const sortedArr1 = objectUtils.sortBy(arr1, 'a');
                expect(sortedArr1).toEqual([{ a: '1', b: 'b' }, { a: '2', b: 'b' }]);

                const arr3 = [{ a: '1', b: 'b' }, { a: '2', b: 'b' }];
                const sortedArr3 = objectUtils.sortBy(arr3, 'a', false);
                expect(sortedArr3).toEqual([{ a: '1', b: 'b' }, { a: '2', b: 'b' }]);
            });

            it('should return a sorted array of objects in descending order', () => {
                const arr = [{ a: '1', b: 'b' }, { a: '2', b: 'b' }];
                const sortedArr = objectUtils.sortBy(arr, 'a', true);
                expect(sortedArr).toEqual([{ a: '2', b: 'b' }, { a: '1', b: 'b' }]);
            });
        });
    });
});
