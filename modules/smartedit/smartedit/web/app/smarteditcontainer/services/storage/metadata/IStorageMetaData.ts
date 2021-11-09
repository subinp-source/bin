/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IStorageOptions } from 'smarteditcommons';

/** @internal */
export interface IStorageMetaData extends IStorageOptions {
    // millis
    lastAccess: number;
}
