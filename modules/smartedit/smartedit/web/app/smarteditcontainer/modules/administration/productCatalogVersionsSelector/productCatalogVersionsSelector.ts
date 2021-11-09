/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import * as angular from 'angular';
import * as lodash from 'lodash';

import {
    windowUtils,
    EventHandler,
    GenericEditorField,
    IBaseCatalog,
    IBaseCatalogVersion,
    ICatalogService,
    IModalService,
    LINKED_DROPDOWN,
    ModalButtonActions,
    ModalButtonStyles,
    SeComponent,
    SystemEventService,
    TypedMap
} from 'smarteditcommons';
import { MultiProductCatalogVersionsConfigurationsController } from './multiProductCatalogVersionsConfigurations/multiProductCatalogVersionsConfigurations';

export const PRODUCT_CATALOG_SINGLE_TEMPLATE = 'productsCatalogSelectSingleTemplate.html';
export const PRODUCT_CATALOG_MULTIPLE_TEMPLATE = 'productsCatalogSelectMultipleTemplate.html';
export const MULTI_PRODUCT_CATALOGS_UPDATED = 'MULTI_PRODUCT_CATALOGS_UPDATED';

@SeComponent({
    selector: 'se-product-catalog-versions-selector',
    templateUrl: 'productCatalogVersionsSelectorTemplate.html',
    inputs: ['field', 'qualifier', 'model', 'id']
})
export class ProductCatalogVersionsSelectorComponent {
    public isTooltipOpen: boolean = false;
    public initialPreview: string;
    public productCatalogs: IBaseCatalog[];
    public isReady: boolean;
    public isSingleVersionSelector: boolean;
    public isMultiVersionSelector: boolean;
    public field: GenericEditorField;
    public fetchStrategy: any; // interface will be introduced in different ticket
    public reset: () => void;

    private qualifier: string;
    private model: TypedMap<any>;
    private id: string;
    private $unRegEventForMultiProducts: () => void;
    private $unRegSiteChangeEvent: () => void;
    private eventId: string;

    constructor(
        private $translate: angular.translate.ITranslateService,
        private l10nFilter: (data: TypedMap<any>) => string,
        private catalogService: ICatalogService,
        private modalService: IModalService,
        private systemEventService: SystemEventService
    ) {}

    $onInit() {
        this.initialPreview = lodash.cloneDeep(this.model.previewCatalog);

        if (this.initialPreview) {
            this.isTooltipOpen = false;

            this.isReady = false;
            this.isSingleVersionSelector = false;
            this.isMultiVersionSelector = false;

            this.eventId = (this.id || '') + LINKED_DROPDOWN;
            this.$unRegSiteChangeEvent = this.systemEventService.subscribe(
                this.eventId,
                (id: string, handle: EventHandler) => this._resetSelector(id, handle)
            );

            this._setContent();
        }
    }

    $onDestroy() {
        if (this.$unRegSiteChangeEvent) {
            this.$unRegSiteChangeEvent();
        }
        if (this.$unRegEventForMultiProducts) {
            this.$unRegEventForMultiProducts();
        }
    }

    public onClick(
        productCatalogs: IBaseCatalog[],
        selectedCatalogVersions: IBaseCatalogVersion[]
    ): void {
        this.isTooltipOpen = true;

        MultiProductCatalogVersionsConfigurationsController.productCatalogs = productCatalogs;
        MultiProductCatalogVersionsConfigurationsController.selectedCatalogVersions = selectedCatalogVersions;

        this.modalService.open({
            title: 'se.modal.product.catalog.configuration',
            size: 'md',
            templateUrl: 'multiProductCatalogVersionsConfigurationsTemplate.html',
            controller: MultiProductCatalogVersionsConfigurationsController,
            buttons: [
                {
                    id: 'cancel',
                    label: 'se.confirmation.modal.cancel',
                    style: ModalButtonStyles.Default,
                    action: ModalButtonActions.Dismiss
                },
                {
                    id: 'done',
                    label: 'se.confirmation.modal.done',
                    action: ModalButtonActions.None,
                    style: ModalButtonStyles.Primary,
                    disabled: true
                }
            ]
        });
    }

    public parseSingleCatalogVersion(catalog: IBaseCatalog): { id: string; label: string }[] {
        return catalog.versions.map((version: IBaseCatalogVersion) => ({
            id: version.uuid,
            label: version.version
        }));
    }

    public buildMultiProductCatalogVersionsTemplate(): string {
        const sHeader = this.$translate.instant('se.product.catalogs.selector.headline.tooltip');

        return (
            "<div class='se-product-catalogs-tooltip'><div class='se-product-catalogs-tooltip__h' >" +
            sHeader +
            '</div>' +
            this.productCatalogs.reduce((accumulator: string, productCatalog: IBaseCatalog) => {
                accumulator +=
                    "<div class='se-product-catalog-info'>" +
                    this._buildCatalogNameCatalogVersionString(productCatalog);
                return accumulator + '</div>';
            }, '') +
            '</div>'
        );
    }

    public getMultiProductCatalogVersionsSelectedOptions(): string {
        if (this.productCatalogs) {
            return this.productCatalogs
                .map((productCatalog) => this._buildCatalogNameCatalogVersionString(productCatalog))
                .join(', ');
        }
        return '';
    }

    private async _setContent(): Promise<any> {
        this.productCatalogs = await this.catalogService.getProductCatalogsForSite(
            this.initialPreview.split('|')[0]
        );

        if (this.productCatalogs.length === 0) {
            return;
        }

        if (this.productCatalogs.length === 1) {
            this.fetchStrategy = {
                fetchAll: () => {
                    const parsedVersions = this.parseSingleCatalogVersion(this.productCatalogs[0]);
                    return Promise.resolve(parsedVersions);
                }
            };
            this.isSingleVersionSelector = true;
            this.isMultiVersionSelector = false;
            this.isReady = true;

            return;
        }

        this.$unRegEventForMultiProducts = this.systemEventService.subscribe(
            MULTI_PRODUCT_CATALOGS_UPDATED,
            (eventId: string, eventData: any) =>
                this._updateProductCatalogsModel(eventId, eventData)
        );

        this.isSingleVersionSelector = false;
        this.isMultiVersionSelector = true;
        this.isReady = true;

        return;
    }

    private _updateProductCatalogsModel(eventId: string, data: any): void {
        this.model[this.qualifier] = data;
    }

    private async _resetSelector(eventId: string, handle: any): Promise<any> {
        if (
            handle.qualifier === 'previewCatalog' &&
            handle.optionObject &&
            this.initialPreview !== handle.optionObject.id
        ) {
            this.initialPreview = handle.optionObject.id;

            const siteId = handle.optionObject.id.split('|')[0];

            this.productCatalogs = await this.catalogService.getProductCatalogsForSite(siteId);

            const activeProductCatalogVersions: string[] = await (this
                .catalogService as any).returnActiveCatalogVersionUIDs(this.productCatalogs);

            windowUtils.runTimeoutOutsideAngular(() => {
                this.model[this.qualifier] = activeProductCatalogVersions;
            });

            if (this.reset) {
                this.reset();
            }

            this._setContent();
        }
    }

    /** @internal */
    private _buildCatalogNameCatalogVersionString(productCatalog: IBaseCatalog): string {
        const productCatalogVersion: IBaseCatalogVersion = productCatalog.versions.find(
            (version: IBaseCatalogVersion) =>
                this.model[this.qualifier] && this.model[this.qualifier].indexOf(version.uuid) > -1
        );

        if (this.model[this.qualifier] && productCatalogVersion) {
            return (
                this.l10nFilter(productCatalog.name) + ' (' + productCatalogVersion.version + ')'
            );
        }

        return '';
    }
}
