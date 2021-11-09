/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { InjectionToken, Type } from '@angular/core';

import { CompileHtmlNgController } from '../../directives/CompileHtml';

/**
 * @ngdoc object
 * @name smarteditCommonsModule.object:PopupOverlayConfig
 * @description
 * Configuration object passed by input binding od {@link smarteditCommonsModule.component:PopupOverlayComponent PopupOverlayComponent}
 */

export interface PopupOverlayConfig {
    /**
     * @ngdoc property
     * @name valign
     * @propertyOf smarteditCommonsModule.object:PopupOverlayConfig
     * @description
     * Defines vertical align modifier: bottom or top
     */

    valign?: 'bottom' | 'top';

    /**
     * @ngdoc property
     * @name halign
     * @propertyOf smarteditCommonsModule.object:PopupOverlayConfig
     * @description
     * Defines horizontal align modifier: right or left
     */

    halign?: 'right' | 'left';

    /**
     * @ngdoc property
     * @name template
     * @propertyOf smarteditCommonsModule.object:PopupOverlayConfig
     * @description
     * @deprecated since 2005, use component
     * A template to be rendered in overlay
     */

    template?: string;

    /**
     * @ngdoc property
     * @name templateUrl
     * @propertyOf smarteditCommonsModule.object:PopupOverlayConfig
     * @description
     * @deprecated since 2005, use component
     * A templateUrl used to rendered AngularJS template in overlay
     */

    templateUrl?: string;

    /**
     * @ngdoc property
     * @name legacyController
     * @propertyOf smarteditCommonsModule.object:PopupOverlayConfig
     * @description
     * @deprecated since 2005, use component
     * A AngularJS controller of the rendered template
     */

    legacyController?: CompileHtmlNgController;

    /**
     * @ngdoc property
     * @name component
     * @propertyOf smarteditCommonsModule.object:PopupOverlayConfig
     * @description
     * A Angular component rendered within the overlay
     */

    component?: Type<any>;
}

export const POPUP_OVERLAY_DATA = new InjectionToken('POPUP_OVERLAY_DATA');
