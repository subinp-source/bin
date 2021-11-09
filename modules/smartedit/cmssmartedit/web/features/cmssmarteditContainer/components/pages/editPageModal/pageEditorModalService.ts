/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    CrossFrameEventService,
    SeInjectable,
    SystemEventService,
    TypedMap
} from 'smarteditcommons';
import { ICMSPage } from 'cmscommons';
import * as angular from 'angular';
import { IGenericEditorModalServiceComponent } from 'cmscommons/services/IGenericEditorModalServiceComponent';
import { ContextAwareEditableItemService } from 'cmssmarteditcontainer/services/contextAwareEditableItem/ContextAwareEditableItemServiceOuter';

/**
 * @ngdoc service
 * @name pageComponentsModule.service:pageEditorModalService
 *
 * @description
 *
 * The page editor modal service module provides a service that allows opening an editor modal for a given page. The editor modal is populated with a save and cancel button, and is loaded with the
 * editorTabset of cmssmarteditContainer as its content, providing a way to edit
 * various fields of the given page.
 *
 * Convenience service to open an editor modal window for a given page's data.
 */
@SeInjectable()
export class PageEditorModalService {
    constructor(
        private $q: angular.IQService,
        private $translate: angular.translate.ITranslateService,
        private genericEditorModalService: any,
        private crossFrameEventService: CrossFrameEventService,
        private contextAwarePageStructureService: any,
        private cmsitemsRestService: any,
        private pageService: any,
        private SYNCHRONIZATION_POLLING: TypedMap<any>,
        private systemEventService: SystemEventService,
        private EVENT_CONTENT_CATALOG_UPDATE: string,
        private contextAwareEditableItemService: ContextAwareEditableItemService
    ) {}

    /**
     * @ngdoc method
     * @name pageComponentsModule.service:pageEditorModalService#open
     * @methodOf pageComponentsModule.service:pageEditorModalService
     *
     * @description
     * Uses the {@link genericEditorModalService.open genericEditorModalService} to open an editor modal.
     *
     * The editor modal is initialized with a title in the format '<TypeName> Editor', ie: 'Paragraph Editor'. The
     * editor modal is also wired with a save and cancel button.
     *
     * @param {Object} page The data associated to a page as defined in the platform.
     *
     * @returns {Promise} A promise that resolves to the data returned by the modal when it is closed.
     */
    open(page: ICMSPage) {
        const config: IGenericEditorModalServiceComponent = {
            title: 'se.cms.pageeditormodal.editpagetab.title',
            componentUuid: page.uuid,
            componentType: page.typeCode
        };

        let isPagePrimary: boolean;

        return this.$q
            .all([
                this.cmsitemsRestService.getById(page.uuid),
                this.pageService.isPagePrimary(page.uid)
            ])
            .then(([cmsPage, isPrimary]: [ICMSPage, boolean]) => {
                isPagePrimary = isPrimary;
                config.content = cmsPage;
                config.content.template = cmsPage.masterTemplateId;

                return this.contextAwarePageStructureService.getPageStructureForPageEditing(
                    config.content.typeCode,
                    config.content.uid
                );
            })
            .then((fields: any) => {
                config.structure = fields;

                if (isPagePrimary) {
                    config.structure.attributes = config.structure.attributes.filter(
                        (field: any) => {
                            return (
                                field.qualifier !== 'restrictions' &&
                                field.qualifier !== 'onlyOneRestrictionMustApply'
                            );
                        }
                    );
                }

                return this._applyReadOnlyMode(page, config).then(() => {
                    return this.genericEditorModalService.open(config, () => {
                        this.crossFrameEventService.publish(
                            this.SYNCHRONIZATION_POLLING.FETCH_SYNC_STATUS_ONCE,
                            page.uuid
                        );
                    });
                });
            })
            .then((response: any) => {
                this.systemEventService.publishAsync(this.EVENT_CONTENT_CATALOG_UPDATE, response);

                return response;
            });
    }

    /**
     * Makes the editor read only.
     */
    private _applyReadOnlyMode(
        page: ICMSPage,
        config: IGenericEditorModalServiceComponent
    ): angular.IPromise<void> {
        if (!!page.uid) {
            return this.contextAwareEditableItemService
                .isItemEditable(page.uid)
                .then((isEditable) => {
                    config.readOnlyMode = !isEditable;
                    if (config.readOnlyMode) {
                        config.messages = [
                            {
                                type: 'info',
                                message: this.$translate.instant(
                                    'se.cms.pageeditormodal.page.readonly.message'
                                )
                            }
                        ];
                    }
                });
        }
        return this.$q.resolve();
    }
}
