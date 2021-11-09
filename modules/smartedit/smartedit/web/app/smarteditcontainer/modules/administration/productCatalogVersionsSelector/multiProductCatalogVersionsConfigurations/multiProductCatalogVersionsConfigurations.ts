/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { MULTI_PRODUCT_CATALOGS_UPDATED } from '../productCatalogVersionsSelector';
import {
    IBaseCatalog,
    IBaseCatalogVersion,
    LogService,
    ModalManager,
    SystemEventService
} from 'smarteditcommons';

/* @ngInject */
export class MultiProductCatalogVersionsConfigurationsController {
    static productCatalogs: IBaseCatalog[];
    static selectedCatalogVersions: IBaseCatalogVersion[];

    public productCatalogs: IBaseCatalog[];
    public selectedCatalogVersions: IBaseCatalogVersion[];
    public updatedCatalogVersions: IBaseCatalogVersion[];

    public updateSelection: (updatedSelectedVersions: IBaseCatalogVersion[]) => void;
    public init: () => void;

    private onSave: () => void;
    private onCancel: () => Promise<any>;

    constructor(
        private logService: LogService,
        private modalManager: ModalManager,
        private systemEventService: SystemEventService
    ) {
        this.productCatalogs = MultiProductCatalogVersionsConfigurationsController.productCatalogs;
        this.selectedCatalogVersions =
            MultiProductCatalogVersionsConfigurationsController.selectedCatalogVersions;

        this.updateSelection = (updatedSelectedVersions: IBaseCatalogVersion[]) => {
            if (
                JSON.stringify(updatedSelectedVersions) !==
                JSON.stringify(this.selectedCatalogVersions)
            ) {
                this.updatedCatalogVersions = updatedSelectedVersions;
                this.modalManager.enableButton('done');
            } else {
                this.modalManager.disableButton('done');
            }
        };

        this.init = () => {
            this.modalManager.setDismissCallback(() => this.onCancel());

            this.modalManager.setButtonHandler((buttonId: string) => {
                switch (buttonId) {
                    case 'done':
                        return this.onSave();
                    case 'cancel':
                        return this.onCancel();
                    default:
                        this.logService.error(
                            'A button callback has not been registered for button with id',
                            buttonId
                        );
                        break;
                }
            });
        };

        this.onSave = () => {
            this.systemEventService.publishAsync(
                MULTI_PRODUCT_CATALOGS_UPDATED,
                this.updatedCatalogVersions
            );
            this.modalManager.close(null);
        };

        this.onCancel = () => {
            this.modalManager.close(null);

            return Promise.resolve({});
        };
    }
}
