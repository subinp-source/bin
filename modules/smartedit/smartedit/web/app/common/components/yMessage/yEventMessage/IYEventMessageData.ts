/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { EventMessageData } from '../../message';

/**
 * @ngdoc interface
 * @name smarteditCommonsModule.interface:IYEventMessageData
 * @deprecated since 2005
 *
 * @description
 * Deprecated, use EventMessageData interface.
 * IYEventMessageData represents the data that can optionaly be passed to the event service
 * when firing an event to show a {@link smarteditCommonsModule.directive:YEventMessage YEventMessage}
 */
export interface IYEventMessageData extends EventMessageData {
    /**
     * @ngdoc property
     * @name smarteditCommonsModule.interface:IYEventMessageData.property:description
     * @propertyOf smarteditCommonsModule.interface:IYEventMessageData
     *
     * @description
     * ```description?: string```
     *
     * The new description to be displayed in the yMessage
     */
    /**
     * @ngdoc property
     * @name smarteditCommonsModule.interface:IYEventMessageData.property:title
     * @propertyOf smarteditCommonsModule.interface:IYEventMessageData
     *
     * @description
     * ```title?: string```
     *
     * The new title to be displayed in the yMessage
     */
}
