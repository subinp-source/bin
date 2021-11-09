/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TypedMap } from 'smarteditcommons';
import { CMSItem } from 'cmscommons';

export { NavigationNode, NavigationNodeEntry } from 'smarteditcommons';

export interface NavigationNodeCMSItem extends CMSItem {
    catalogVersion: string;
    visible: boolean;
    title: TypedMap<string>;
    uuid: string;
    uid: string;
    entries: string[];
    pages: number[];
    itemType: string;
    modifiedtime: Date;
    children: string[];
    name: string;
    links: string[];
    creationtime: Date;
}
