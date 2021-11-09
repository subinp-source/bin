/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ISiteName } from './siteName.entity';

export interface ISite {
    contentCatalogs: string[];
    name: ISiteName;
    previewUrl: string;
    uid: string;
    redirectUrl: string;
}
