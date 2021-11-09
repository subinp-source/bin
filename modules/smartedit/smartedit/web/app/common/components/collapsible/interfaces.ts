/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
export type IconAlignment = 'right' | 'left';

export interface CollapsibleContainerApi {
    isExpanded: () => boolean;
}

/**
 * @ngdoc interface
 * @name smarteditCommonsModule.interface:CollapsibleContainerConfig
 *
 */

export interface CollapsibleContainerConfig {
    /**
     * @ngdoc property
     * @name expandedByDefault
     * @propertyOf smarteditCommonsModule.interface:CollapsibleContainerConfig
     *
     * @description
     * Defines whether collapsible is opened by default
     */

    expandedByDefault: boolean;

    /**
     * @ngdoc property
     * @name iconAlignment
     * @propertyOf smarteditCommonsModule.interface:CollapsibleContainerConfig
     *
     * @description
     * Defines alignment of the icon(left or right).
     */

    iconAlignment: IconAlignment;

    /**
     * @ngdoc property
     * @name iconVisible
     * @propertyOf smarteditCommonsModule.interface:CollapsibleContainerConfig
     *
     * @description
     * Defines whether header icon is visible
     */

    iconVisible: boolean;
}
