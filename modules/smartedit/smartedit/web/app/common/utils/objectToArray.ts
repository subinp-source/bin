/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TypedMap } from '@smart/utils';

export function objectToArray<T = string>(obj: TypedMap<T>): { key: string; value: T }[] {
    return Object.keys(obj).reduce((acc, key) => [...(acc || []), { key, value: obj[key] }], []);
}
