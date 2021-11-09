/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

/**
 * Backwards compatibility for partners and downstream teams
 * The deprecated modules below were moved to smarteditServicesModule
 *
 * IMPORANT: THE DEPRECATED MODULES WILL NOT BE AVAILABLE IN FUTURE RELEASES
 * @deprecated since 1811
 */
/* forbiddenNameSpaces angular.module:false */
const deprecatedSince1811 = () => {
    angular.module('permissionServiceModule', ['legacySmarteditServicesModule']);
};

export function deprecatedSince1905() {
    angular.module('alertServiceModule', ['legacySmarteditServicesModule']);
    angular.module('decoratorServiceModule', ['legacySmarteditServicesModule']);
    angular.module('renderServiceModule', ['legacySmarteditServicesModule']);
    angular.module('renderServiceInterfaceModule', ['legacySmarteditServicesModule']);
}

export function deprecatedSince2005() {
    angular.module('confirmationModalServiceModule', ['legacySmarteditServicesModule']);
    angular.module('smarteditServicesModule', ['legacySmarteditServicesModule']);
    angular.module('pageSensitiveDirectiveModule', ['legacySmartedit']);
    angular.module('toolbarModule', ['legacySmarteditServicesModule']);
    angular.module('contextualMenuItemModule', ['legacySmartedit']);
    angular.module('contextualMenuDecoratorModule', ['legacySmartedit']);
    angular.module('positionRegistryModule', ['legacySmarteditServicesModule']);
    angular.module('slotContextualMenuDecoratorModule', ['legacySmartedit']);
    angular.module('resizeListenerModule', ['legacySmarteditServicesModule']);
    angular.module('sanitizeHtmlInputModule', ['legacySmartedit']);
    angular.module('catalogVersionPermissionModule', ['legacySmarteditServicesModule']);
}

export const deprecate = () => {
    deprecatedSince1811();
    deprecatedSince1905();
    deprecatedSince2005();
};
