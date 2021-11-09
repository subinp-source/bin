/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* forbiddenNameSpaces angular.module:false */
import * as angular from 'angular';
import { IWaitDialogService } from 'smarteditcommons';
import { IframeManagerService } from './services';

/**
 * Backwards compatibility for partners and downstream teams
 * The deprecated modules below were moved to smarteditServicesModule
 *
 * IMPORTANT: THE DEPRECATED MODULES WILL NOT BE AVAILABLE IN FUTURE RELEASES
 * @deprecated since 1811
 */
/* @internal */
export function deprecatedSince1811() {
    angular.module('permissionServiceModule', ['legacySmarteditServicesModule']);
    angular
        .module('iFrameManagerModule', ['legacySmarteditServicesModule'])
        .service(
            'iFrameManager',
            (iframeManagerService: IframeManagerService, waitDialogService: IWaitDialogService) => {
                (iframeManagerService as any).showWaitModal = (key?: string) => {
                    waitDialogService.showWaitModal(key);
                };
                (iframeManagerService as any).hideWaitModal = () => {
                    waitDialogService.hideWaitModal();
                };
                return iframeManagerService;
            }
        );
    angular.module('catalogVersionPermissionRestServiceModule', ['legacySmarteditServicesModule']);
    angular.module('catalogVersionDetailsModule', ['catalogDetailsModule']);
    angular.module('catalogVersionsThumbnailCarouselModule', ['catalogDetailsModule']);
    angular.module('homePageLinkModule', ['catalogDetailsModule']);
}

export function deprecatedSince1905() {
    angular.module('heartBeatServiceModule', ['legacySmarteditServicesModule']);

    angular.module('alertCollectionModule', ['legacySmarteditCommonsModule']);
    angular.module('alertCollectionFacadesModule', ['legacySmarteditCommonsModule']);
    angular.module('alertFactoryModule', ['legacySmarteditCommonsModule']);
    angular.module('renderServiceModule', ['legacySmarteditServicesModule']);
    angular.module('alertServiceModule', ['legacySmarteditCommonsModule']);
}

export function deprecatedSince1911() {
    angular.module('perspectiveSelectorModule', ['smarteditServicesModule']);
}

export function deprecatedSince2005() {
    angular.module('confirmationModalServiceModule', ['legacySmarteditServicesModule']);
    angular.module('smarteditServicesModule', ['legacySmarteditServicesModule']);
    angular.module('pageSensitiveDirectiveModule', ['legacySmarteditcontainer']);
    angular.module('yCollapsibleContainerModule', ['legacySmarteditcontainer']);
    angular.module('inflectionPointSelectorModule', ['legacySmarteditcontainer']);
    angular.module('toolbarModule', ['legacySmarteditServicesModule']);
    angular.module('filterByFieldFilterModule', ['legacySmarteditServicesModule']);
    angular.module('resizeComponentServiceModule', ['legacySmartedit']);
    angular.module('systemAlertsModule', ['legacySmarteditcontainer']);
    angular.module('alertsBoxModule', []);
    angular.module('catalogVersionPermissionModule', ['legacySmarteditServicesModule']);
    angular.module('previewDataDropdownPopulatorModule', ['legacySmarteditServicesModule']);
    angular.module('catalogDetailsModule', ['legacyCatalogDetailsModule']);
}

export const deprecate = () => {
    deprecatedSince1811();
    deprecatedSince1905();
    deprecatedSince1911();
    deprecatedSince2005();
};
