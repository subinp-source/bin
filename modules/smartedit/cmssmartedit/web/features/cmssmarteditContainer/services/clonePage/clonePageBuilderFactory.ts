/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lodash from 'lodash';
import * as angular from 'angular';

import {
    stringUtils,
    GenericEditorStructure,
    ICatalogService,
    ICatalogVersion,
    IPageInfoService,
    IUriContext
} from 'smarteditcommons';
import { IRestrictionsStepHandler } from '../../interfaces';
import { CMSRestriction, ICMSPage } from 'cmscommons';

// tslint:disable:max-classes-per-file

export interface IClonePageBuilder {
    componentCloneOptionSelected(cloneOptionResult: string): void;
    displayConditionSelected(displayConditionResult: ICMSPage): Promise<void>;
    getBasePageInfo(): ICMSPage;
    getComponentCloneOption(): string;
    getPageInfo(): ICMSPage;
    getPageInfoStructure(): GenericEditorStructure;
    getPageLabel(): string;
    getPageProperties(): Partial<ICMSPage>;
    getPageRestrictions(): CMSRestriction[];
    getPageTemplate(): string;
    getPageTypeCode(): string;
    getTargetCatalogVersion(): ICatalogVersion;
    isBasePageInfoAvailable(): boolean;
    onTargetCatalogVersionSelected(targetCatalogVersion: ICatalogVersion): void;
    restrictionsSelected(
        onlyOneRestrictionMustApply: boolean,
        restrictions: CMSRestriction[]
    ): void;
}

/* @ngInject */
export function ClonePageBuilderFactory(
    $q: angular.IQService,
    contextAwarePageStructureService: any,
    pageInfoService: IPageInfoService,
    typeStructureRestService: any,
    cmsitemsRestService: any,
    catalogService: ICatalogService
): new (...args: any[]) => IClonePageBuilder {
    function getPageUUID(pageUUID: string): Promise<string> {
        return !stringUtils.isBlank(pageUUID)
            ? Promise.resolve(pageUUID)
            : pageInfoService.getPageUUID();
    }

    return class ClonePageBuilder {
        private pageInfoStructure: GenericEditorStructure = {} as GenericEditorStructure;
        private basePage: ICMSPage = {} as ICMSPage; // the page being cloned
        private pageData: ICMSPage = {} as ICMSPage; // holds current clone page tabs data
        private componentCloneOption: string = '';
        private basePageInfoAvailable: boolean = false;
        private targetCatalogVersion: ICatalogVersion;

        constructor(
            private restrictionsStepHandler: IRestrictionsStepHandler,
            private basePageUUID: string,
            private uriContext: IUriContext
        ) {
            this.init();
        }

        public getPageTypeCode(): string {
            return this.pageData.typeCode;
        }

        public getPageTemplate(): string {
            return this.pageData.template;
        }

        public getPageLabel(): string {
            return this.pageData.label;
        }

        public getPageInfo(): ICMSPage {
            return this.pageData;
        }

        public getBasePageInfo(): ICMSPage {
            return this.basePage;
        }

        public getPageProperties(): Partial<ICMSPage> {
            const pageProperties = {} as Partial<ICMSPage>;

            pageProperties.type = this.pageData.type;
            pageProperties.typeCode = this.pageData.typeCode;
            pageProperties.template = this.pageData.template;
            pageProperties.onlyOneRestrictionMustApply = this.pageData.onlyOneRestrictionMustApply;
            pageProperties.catalogVersion = this.pageData.catalogVersion;

            return pageProperties;
        }

        public getPageInfoStructure(): GenericEditorStructure {
            return this.pageInfoStructure;
        }

        public getPageRestrictions(): CMSRestriction[] {
            return this.pageData.restrictions || [];
        }

        public getComponentCloneOption(): string {
            return this.componentCloneOption;
        }

        public async displayConditionSelected(displayConditionResult: ICMSPage): Promise<void> {
            const isPrimaryPage = displayConditionResult.isPrimary;

            this.pageData.defaultPage = isPrimaryPage;
            this.pageData.homepage = displayConditionResult.homepage;
            if (isPrimaryPage) {
                this.pageData.label = this.basePage.label;

                if (this.pageData.restrictions) {
                    delete this.pageData.restrictions;
                }
                this.restrictionsStepHandler.hideStep();
            } else {
                this.pageData.label = displayConditionResult.primaryPage
                    ? (displayConditionResult.primaryPage as ICMSPage).label
                    : '';
                this.restrictionsStepHandler.showStep();
            }
            this.pageData.uid = '';
            await this._updatePageInfoFields();
        }

        public onTargetCatalogVersionSelected(targetCatalogVersion: ICatalogVersion): void {
            this.targetCatalogVersion = targetCatalogVersion;
            this.pageData.catalogVersion = targetCatalogVersion.uuid;
        }

        public componentCloneOptionSelected(cloneOptionResult: string): void {
            this.componentCloneOption = cloneOptionResult;
        }

        public restrictionsSelected(
            onlyOneRestrictionMustApply: boolean,
            restrictions: CMSRestriction[]
        ): void {
            this.pageData.onlyOneRestrictionMustApply = onlyOneRestrictionMustApply;
            this.pageData.restrictions = restrictions;
        }

        public getTargetCatalogVersion(): ICatalogVersion {
            return this.targetCatalogVersion;
        }

        public isBasePageInfoAvailable(): boolean {
            return this.basePageInfoAvailable;
        }

        private async init(): Promise<void> {
            const pageUUID = await getPageUUID(this.basePageUUID);
            const uuid = await catalogService.getCatalogVersionUUid(this.uriContext);

            // Use full async/await approach when CmsitemsRestService methods will be migrated.

            return new Promise((resolve) => {
                cmsitemsRestService.getById(pageUUID).then(async (page: ICMSPage) => {
                    this.basePage = page;
                    this.pageData = lodash.cloneDeep(this.basePage);
                    this.pageData.catalogVersion = uuid;
                    this.pageData.pageUuid = this.basePage.uuid;
                    delete this.pageData.uuid;

                    this.basePageInfoAvailable = true;

                    this.pageData.template = this.basePage.masterTemplateId;

                    const structure = await typeStructureRestService.getStructureByTypeAndMode(
                        this.pageData.typeCode,
                        'DEFAULT',
                        {
                            getWholeStructure: true
                        }
                    );

                    this.pageData.type = structure.type;
                    resolve();
                });
            });
        }

        private async _updatePageInfoFields(): Promise<void> {
            if (typeof this.pageData.defaultPage !== 'undefined') {
                if (this.pageData.typeCode) {
                    const pageInfoFields = await contextAwarePageStructureService.getPageStructureForNewPage(
                        this.pageData.typeCode,
                        this.pageData.defaultPage
                    );
                    this.pageInfoStructure = pageInfoFields;
                } else {
                    this.pageInfoStructure = {} as GenericEditorStructure;
                }
            }
        }
    };
}
