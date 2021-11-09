/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeDowngradeService } from '../../di';
import { NavigationNodeItem, TreeNodeItem } from './TreeNodeItem';
import { ITreeNodeItem, NavigationNodeItemDTO, TreeNodeItemDTO } from './types';

enum TreeNodeItemType {
    CMSNavigationNode = 'CMSNavigationNode'
}

/**
 * @ngdoc service
 * @name TreeModule.service:TreeNodeItemFactory
 * @description
 * A service used to generate instance of node item to be consumed by {@link TreeModule.component:TreeComponent TreeComponent}
 */

@SeDowngradeService()
export class TreeNodeItemFactory {
    /**
     * @ngdoc method
     * @name TreeModule.service:TreeNodeItemFactory#get
     * @methodOf TreeModule.service:TreeNodeItemFactory
     * @param {Object} dto DTO based on which the class is returned
     * @description
     *
     * Returns a class depending on itemtype
     */

    get<T, D extends TreeNodeItemDTO>(dto: D): ITreeNodeItem<T> {
        switch (dto.itemType) {
            case TreeNodeItemType.CMSNavigationNode:
                return new NavigationNodeItem((dto as unknown) as NavigationNodeItemDTO);
            default:
                return new TreeNodeItem(dto);
        }
    }
}
