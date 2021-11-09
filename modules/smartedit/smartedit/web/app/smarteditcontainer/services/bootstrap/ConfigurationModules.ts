/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TypedMap } from 'smarteditcommons';

/** @internal */
export interface Module {
    /*
     * absolute URL location of a "plugin" application to be added to smarteditcontainer or smartedit
     */
    location: string;

    /*
     * top most angular module name to be found at the given location
     */
    name: string;

    /*
     * name of another application that this is extending
     */
    extends?: string;
}
/** @internal */
export interface ConfigurationModules {
    authenticationMap: TypedMap<string>;

    applications: Module[];
}
