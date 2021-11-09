/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lodash from 'lodash';
import { TypedMap } from '../dtos';

/**
 * @ngdoc service
 * @name functionsModule.service:ObjectUtils
 *
 * @description
 * provides a list of useful methods used for object manipulation
 */
class ObjectUtils {
    /**
     * @ngdoc method
     * @name functionsModule.service:ObjectUtils#copy
     * @methodOf functionsModule.service:ObjectUtils
     * @description
     * <b>copy</b> will do a deep copy of the given input object.
     * If an object being stringified has a property named toJSON whose value is a function, then the toJSON() method customizes JSON stringification behavior: instead of the object being serialized, the value returned by the toJSON() method when called will be serialized.
     *
     * @param {Object} candidate the javaScript value that needs to be deep copied.
     *
     * @returns {Object} A deep copy of the input
     */

    copy<T>(candidate: T): T {
        return JSON.parse(JSON.stringify(candidate));
    }

    /**
     * @ngdoc method
     * @name functionsModule.service:ObjectUtils#isObjectEmptyDeep
     * @methodOf functionsModule.service:ObjectUtils
     *
     * @description
     * Will check if the object is empty and will return true if each and every property of the object is empty
     *
     * @param {Object} value, the value to evaluate
     * @returns {Boolean} a boolean.
     */

    isObjectEmptyDeep = (value: any): boolean => {
        if (lodash.isObject(value) as any) {
            for (const key in value) {
                if (value.hasOwnProperty(key)) {
                    if (!lodash.isEmpty(value[key])) {
                        return false;
                    }
                }
            }
            return true;
        }
        return lodash.isString(value) ? lodash.isEmpty(value) : lodash.isNil(value);
    };

    /**
     * @ngdoc method
     * @name functionsModule.service:ObjectUtils#resetObject
     * @methodOf functionsModule.service:ObjectUtils
     *
     * @description
     * Resets a given object's properties' values
     *
     * @param {Object} targetObject, the object to reset
     * @param {Object} modelObject, an object that contains the structure that targetObject should have after a reset
     * @returns {Object} Returns the object that has been reset
     */

    resetObject = (targetObject: any, modelObject: any): any => {
        if (!targetObject) {
            targetObject = this.copy(modelObject);
        } else {
            for (const i in targetObject) {
                if (targetObject.hasOwnProperty(i)) {
                    delete targetObject[i];
                }
            }
            lodash.extend(targetObject, this.copy(modelObject));
        }

        return targetObject;
    };

    /**
     * @ngdoc method
     * @name functionsModule.service:ObjectUtils#merge
     * @methodOf functionsModule.service:ObjectUtils
     *
     * @description
     * <b>merge</b> will merge the contents of two objects together into the first object.
     *
     * **Note:** This method mutates `object`.
     *
     * @param {Object} target any JavaScript object.
     * @param {Object} source any JavaScript object.
     *
     * @returns {Object} a new object as a result of merge
     */
    merge<TTarget, TSource>(target: TTarget, source: TSource): TTarget & TSource {
        return Object.assign(target, source);
    }

    /**
     * @ngdoc method
     * @name functionsModule.service:ObjectUtils#deepIterateOverObjectWith
     * @methodOf functionsModule.service:ObjectUtils
     *
     * @description
     * Iterates over object and allows to modify a value using callback function.
     * @param {Object} obj an object to iterate.
     * @param {Function} callback callback to apply to each object value.
     * @returns {Object} the object with modified values.
     */

    deepIterateOverObjectWith = (obj: any, callback: any) => {
        return lodash.reduce(
            obj,
            (result: any, value: any, key: any) => {
                if (lodash.isPlainObject(value)) {
                    result[key] = this.deepIterateOverObjectWith(value, callback);
                } else {
                    result[key] = callback(value);
                }
                return result;
            },
            {}
        );
    };

    /**
     * @ngdoc method
     * @name functionsModule.service:ObjectUtils#deepObjectPropertyDiff
     * @methodOf functionsModule.service:ObjectUtils
     *
     * @description
     * Returns an object that contains list of fields and for each field it has a boolean value
     * which is true when the property was modified, added or removed, false otherwise.
     * @param {Object} object The first object to inspect.
     * @param {Object} source The second object to inspect.
     * @returns {Object} the diff object.
     */

    deepObjectPropertyDiff = (firstObject: any, secondObject: any) => {
        // tslint:disable-next-line:no-empty
        function CHANGED_PROPERTY() {}

        // tslint:disable-next-line:no-empty
        function NON_CHANGED_PROPERTY() {}

        const mergedObj = lodash.mergeWith(lodash.cloneDeep(firstObject), secondObject, function(
            prValue: any,
            cpValue: any
        ) {
            if (!lodash.isPlainObject(prValue)) {
                return !lodash.isEqual(prValue, cpValue) ? CHANGED_PROPERTY : NON_CHANGED_PROPERTY;
            }

            // Note: Previous versions of lodash could work with null, but the latest version of lodash requires
            // undefined to be returned.
            return undefined;
        });

        // If the field is not CHANGED_PROPERTY/NON_CHANGED_PROPERTY then it was removed or added.
        const sanitizedObj = this.deepIterateOverObjectWith(mergedObj, (value: any) => {
            if (value !== CHANGED_PROPERTY && value !== NON_CHANGED_PROPERTY) {
                return CHANGED_PROPERTY;
            } else {
                return value;
            }
        });

        // If it's CHANGED_PROPERTY return true otherwise false.
        return this.deepIterateOverObjectWith(sanitizedObj, (value: any) => {
            return value === CHANGED_PROPERTY ? true : false;
        });
    };

    /**
     * @ngdoc method
     * @name functionsModule.service:ObjectUtils#convertToArray
     * @methodOf functionsModule.service:ObjectUtils
     *
     * @description
     * <b>convertToArray</b> will convert the given object to array.
     * The output array elements are an object that has a key and value,
     * where key is the original key and value is the original object.
     *
     * @param {Object} inputObject any input object.
     *
     * @returns {Array} the array created from the input object
     */
    convertToArray(object: any) {
        const configuration = [];
        for (const key in object) {
            if (!key.startsWith('$') && !key.startsWith('toJSON')) {
                configuration.push({
                    key,
                    value: object[key]
                });
            }
        }
        return configuration;
    }

    /**
     * @ngdoc method
     * @name functionsModule.service:ObjectUtils#uniqueArray
     * @methodOf functionsModule.service:ObjectUtils
     *
     * @description
     * <b>uniqueArray</b> returns the first Array argument supplemented with new entries from the second Array argument.
     *
     * **Note:** This method mutates `array1`.
     *
     * @param {Array} array1 any JavaScript array.
     * @param {Array} array2 any JavaScript array.
     */
    uniqueArray(array1: any[], array2: any[]): any[] {
        const set = new Set(array1);
        array2.forEach((instance: any) => {
            if (!set.has(instance)) {
                array1.push(instance);
            }
        });

        return array1;
    }

    /**
     * @ngdoc method
     * @name functionsModule.service:ObjectUtils#isFunction
     * @methodOf functionsModule.service:ObjectUtils
     *
     * @description Checks if `value` is a function.
     *
     * @static
     * @category Objects
     * @param {*} value The value to check.
     * @returns {boolean} Returns `true` if the `value` is a function, else `false`.
     */
    isFunction(value: any): boolean {
        return typeof value === 'function';
    }

    /**
     * @ngdoc method
     * @name functionsModule.service:ObjectUtils#isObject
     * @methodOf functionsModule.service:ObjectUtils
     *
     * @description
     * checks if the value is the ECMAScript language type of Object
     */
    isObject(value: any): boolean {
        const objectTypes = {
            boolean: false,
            function: true,
            object: true,
            number: false,
            string: false,
            undefined: false
        } as any;

        return !!(value && objectTypes[typeof value]);
    }

    isTypedMap<T = string>(value: any): value is TypedMap<T> {
        return value && this.isObject(value) && value.constructor === Object;
    }

    /**
     * @ngdoc method
     * @name functionsModule.service:ObjectUtils#readObjectStructure
     * @methodOf functionsModule.service:ObjectUtils
     *
     */
    readObjectStructure = (json: any, recursiveCount: number = 0): any => {
        if (recursiveCount > 25) {
            return this.getClassName(json);
        }

        if (json === undefined || json === null || json.then) {
            return json;
        }

        if (typeof json === 'function') {
            return 'FUNCTION';
        } else if (typeof json === 'number') {
            return 'NUMBER';
        } else if (typeof json === 'string') {
            return 'STRING';
        } else if (typeof json === 'boolean') {
            return 'BOOLEAN';
        } else if (lodash.isElement(json)) {
            return 'ELEMENT';
        } else if (json.hasOwnProperty && json.hasOwnProperty('length')) {
            // jquery or Array
            if (json.forEach) {
                const arr: any = [];
                json.forEach((arrayElement: any) => {
                    recursiveCount++;
                    arr.push(this.readObjectStructure(arrayElement, recursiveCount));
                });
                return arr;
            } else {
                return 'JQUERY';
            }
        } else if (
            json.constructor &&
            json.constructor.name &&
            json.constructor.name !== 'Object'
        ) {
            return json.constructor.name;
        } else {
            // JSON
            const clone = {} as any;
            Object.keys(json).forEach((directKey) => {
                if (!directKey.startsWith('$')) {
                    recursiveCount++;
                    clone[directKey] = this.readObjectStructure(json[directKey], recursiveCount);
                }
            });
            return clone;
        }
    };

    /**
     * @ngdoc method
     * @name functionsModule.service:ObjectUtils#orderBy
     * @methodOf functionsModule.service:ObjectUtils
     *
     * @description
     * Sorts an array of strings or objects in specified order.
     * String of numbers are treated the same way as numbers.
     * For an array of objects, `prop` argument is required.
     *
     * @param {T[]} array Array to sort
     * @param {string} [prop] Property on which comparision is based. Required for an array of objects.
     * @param {boolean} [reverse=false] specify ascending or descending order
     *
     * @returns {T[]} the new sorted array
     */
    sortBy<T>(array: T[], prop?: string, reverse: boolean = false): T[] {
        const targetArray: T[] = [...array];

        const descending = reverse ? -1 : 1;
        targetArray.sort((a, b) => {
            const aVal = this.isTypedMap(a) ? a[prop] : a;
            const bVal = this.isTypedMap(b) ? b[prop] : b;
            const result = String(aVal).localeCompare(String(bVal), undefined, {
                numeric: true,
                sensitivity: 'base'
            });
            return result * descending;
        });

        return targetArray;
    }

    /** @internal */
    private getClassName(instance: any): string | null {
        return instance &&
            instance.constructor &&
            instance.constructor.name &&
            instance.constructor.name !== 'Object'
            ? instance.constructor.name
            : null;
    }
}

const objectUtils = new ObjectUtils();

export { objectUtils, ObjectUtils };
