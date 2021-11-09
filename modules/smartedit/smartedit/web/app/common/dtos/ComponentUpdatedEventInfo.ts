/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Payload } from '@smart/utils';

/**
 * @ngdoc interface
 * @name ComponentUpdatedEventInfo.interface:ComponentUpdatedEventInfo
 * @description
 * Contains information about a component being updated.
 */
export interface ComponentUpdatedEventInfo extends Payload {
    /**
     * @ngdoc property
     * @name componentId
     * @propertyOf ComponentUpdatedEventInfo.interface:ComponentUpdatedEventInfo
     * @description
     * The smartedit id of the updated component.
     */
    componentId: string;

    /**
     * @ngdoc property
     * @name componentType
     * @propertyOf ComponentUpdatedEventInfo.interface:ComponentUpdatedEventInfo
     * @description
     * The smartedit type of the updated component.
     */
    componentType: string;

    /**
     * @ngdoc property
     * @name requiresReplayingDecorators
     * @propertyOf ComponentUpdatedEventInfo.interface:ComponentUpdatedEventInfo
     * @description
     * Flag that specifies if the update to the component requires decorators to be refreshed (replayed).
     */
    requiresReplayingDecorators: boolean;
}
