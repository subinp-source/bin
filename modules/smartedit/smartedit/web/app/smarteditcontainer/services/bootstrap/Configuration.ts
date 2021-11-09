/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Cloneable, TypedMap } from 'smarteditcommons';

/** @internal */
export class ConfigurationItem {
    key: string;
    value: string;
    isNew?: boolean;
    toDelete?: boolean;
    secured?: boolean;
    requiresUserCheck?: boolean;
    isCheckedByUser?: boolean;
    hasErrors?: boolean;
    uuid?: string;
    errors?: TypedMap<{ message: string }[]>;
}
/** @internal */
// Configuration structure from a REST payload standpoint
export type Configuration = ConfigurationItem[];

/** @internal */
// Configuration structure after conversion to object
export type ConfigurationObject = TypedMap<Cloneable>;
