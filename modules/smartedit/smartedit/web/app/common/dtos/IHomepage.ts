/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Payload } from '@smart/utils';

export interface IHomepageVersions extends Payload {
    current?: IHomepage;
    old?: IHomepage;
    fallback?: IHomepage;
}

export interface IHomepage extends Payload {
    uid: string;
    name: string;
    catalogVersionUuid: string;
}
