/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TreeNodeItem, TreeNodeItemDTO } from '../treeModule';

export interface EditableListNodeItemThumbnail {
    url: string;
}
export interface EditableListNodeItemDTO extends TreeNodeItemDTO {
    thumbnail?: EditableListNodeItemThumbnail;
    id: string;
    code: string;
    catalogVersion: string;
    catalogId: string;
}
export class EditableListNodeItem extends TreeNodeItem {
    public thumbnail: EditableListNodeItemThumbnail;
    public catalogVersion: string;
    public catalogId: string;
    public id: string;
    public code: string;
    public nodes: EditableListNodeItem[];
    public parent: EditableListNodeItem;

    constructor(config: EditableListNodeItemDTO) {
        super(config);

        this.thumbnail = config.thumbnail || ({} as EditableListNodeItemThumbnail);
        this.id = config.id;
        this.code = config.code;
        this.catalogId = config.catalogId;
        this.catalogVersion = config.catalogVersion;
    }
}
