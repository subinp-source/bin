/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lodash from 'lodash';

import {
    GenericEditorStructure,
    GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT,
    IAlertService,
    ICatalogVersion,
    IConfirmationModalService,
    IExperience,
    IExperienceService,
    ISharedDataService,
    IUriContext,
    SystemEventService,
    TypedMap,
    WizardConfig,
    WizardService,
    WizardStep
} from 'smarteditcommons';
import { IClonePageBuilder } from './clonePageBuilderFactory';
import { PageSynchronizationService } from '../../dao/PageSynchronizationService';
import { CMSRestriction, ICMSPage } from '../../../cmscommons/dtos';

export class ClonePageWizardController {
    public uriContext: IUriContext = this.wizardManager.properties.uriContext;
    public callbacks: TypedMap<() => any> = {};
    public basePageUUID = this.wizardManager.properties.basePageUUID;
    public restrictionStepProperties = {
        id: 'restrictionsStepId',
        name: 'se.cms.restrictions.editor.tab',
        title: 'se.cms.clonepagewizard.pageclone.title',
        templateUrl: 'clonePageRestrictionsStepTemplate.html'
    };
    public restrictionsEditorFunctionBindingsClosure: TypedMap<() => void> = {}; // bound in the view for restrictions step
    public restrictionsEditorFunctionBindings = this.restrictionsEditorFunctionBindingsClosure;
    public typeChanged = true;
    public infoChanged = true;
    public model = {
        page: {},
        sharedPage: {}
    };

    private cloneInprogress: boolean = false;

    private restrictionsStepHandler = this.restrictionsStepHandlerFactory.createRestrictionsStepHandler(
        this.wizardManager,
        this.restrictionsEditorFunctionBindingsClosure,
        this.restrictionStepProperties
    );

    private pageBuilder: IClonePageBuilder = new this.ClonePageBuilderFactory(
        this.restrictionsStepHandler,
        this.basePageUUID,
        this.uriContext
    );
    private query = {
        value: ''
    };
    // Constants
    private readonly CLONE_PAGE_WIZARD_STEPS = {
        PAGE_CLONE_OPTIONS: 'cloneOptions',
        PAGE_INFO: 'pageInfo',
        PAGE_RESTRICTIONS: this.restrictionStepProperties.id
    };

    /* @ngInject */
    constructor(
        private wizardManager: WizardService,
        private ClonePageBuilderFactory: new (...args: any[]) => IClonePageBuilder,
        private restrictionsStepHandlerFactory: any,
        private experienceService: IExperienceService,
        private confirmationModalService: IConfirmationModalService,
        private systemEventService: SystemEventService,
        private pageRestrictionsFacade: any,
        private restrictionsService: any,
        private sharedDataService: ISharedDataService,
        private clonePageAlertService: any,
        private alertService: IAlertService,
        private pageFacade: any,
        private pageSynchronizationService: PageSynchronizationService
    ) {}

    // Wizard Configuration
    public getWizardConfig = (): WizardConfig => {
        return {
            isFormValid: (stepId: string) => this.isFormValid(stepId),
            onNext: () => this.onNext(),
            onDone: () => this.onDone(),
            onCancel: () => this.onCancel(),
            steps: [
                {
                    id: this.CLONE_PAGE_WIZARD_STEPS.PAGE_CLONE_OPTIONS,
                    name: 'se.cms.clonepagewizard.pagecloneoptions.tabname',
                    title: 'se.cms.clonepagewizard.pageclone.title',
                    templateUrl: 'clonePageOptionsStepTemplate.html'
                } as WizardStep,
                {
                    id: this.CLONE_PAGE_WIZARD_STEPS.PAGE_INFO,
                    name: 'se.cms.clonepagewizard.pageinfo.tabname',
                    title: 'se.cms.clonepagewizard.pageclone.title',
                    templateUrl: 'clonePageInfoStepTemplate.html'
                } as WizardStep
            ]
        };
    };

    onCancel = (): Promise<any> => {
        return this.confirmationModalService.confirm({
            description: 'se.editor.cancel.confirm'
        }) as Promise<any>;
    };

    // Wizard Navigation
    isFormValid = (stepId: string): boolean => {
        switch (stepId) {
            case this.CLONE_PAGE_WIZARD_STEPS.PAGE_CLONE_OPTIONS:
                // This step has no required inputs. However we set the valid status as soon as the data is fetched to avoid
                // modal NEXT button enabled status to be true before content is actually loaded.
                return this.isBasePageInfoAvailable();

            case this.CLONE_PAGE_WIZARD_STEPS.PAGE_INFO:
                return (
                    !this.cloneInprogress &&
                    this.callbacks.isValidPageInfo &&
                    this.callbacks.isValidPageInfo()
                );

            case this.CLONE_PAGE_WIZARD_STEPS.PAGE_RESTRICTIONS:
                return !this.cloneInprogress && this.pageBuilder.getPageRestrictions().length > 0;
        }
        return false;
    };

    onNext = (): boolean => {
        return true;
    };

    onDone = () => {
        this.cloneInprogress = true;
        return this.callbacks.savePageInfo().then(
            (page: ICMSPage) => {
                const payload = this._preparePagePayload(page);

                if (this.pageBuilder.getTargetCatalogVersion()) {
                    payload.siteId = this.pageBuilder.getTargetCatalogVersion().siteId;
                    payload.catalogId = this.pageBuilder.getTargetCatalogVersion().catalogId;
                    payload.version = this.pageBuilder.getTargetCatalogVersion().version;
                }
                return this.sharedDataService.get('experience').then((experience: IExperience) => {
                    return this.pageFacade.createPageForSite(payload, payload.siteId).then(
                        (response: ICMSPage) => {
                            const forceGetSynchronization = true;
                            this.pageSynchronizationService.getSyncStatus(
                                payload.pageUuid,
                                this.uriContext,
                                forceGetSynchronization
                            );

                            if (
                                experience.catalogDescriptor.catalogVersionUuid ===
                                response.catalogVersion
                            ) {
                                this.experienceService.loadExperience({
                                    siteId: payload.siteId,
                                    catalogId: payload.catalogId,
                                    catalogVersion: payload.version,
                                    pageId: response.uid
                                });
                            } else {
                                this.clonePageAlertService.displayClonePageAlert(response);
                            }
                            return this.alertService.showSuccess({
                                message: 'se.cms.clonepage.alert.success'
                            });
                        },
                        (failure: any) => {
                            this.cloneInprogress = false; // re-enable the button
                            this.systemEventService.publishAsync(
                                GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT,
                                {
                                    messages: failure.error.errors
                                }
                            );

                            if (
                                !failure.error.errors.find((error: any) => {
                                    return error.subject.indexOf('restrictions') === 0;
                                })
                            ) {
                                this.wizardManager.goToStepWithId(
                                    this.CLONE_PAGE_WIZARD_STEPS.PAGE_INFO
                                );
                            }

                            return Promise.reject();
                        }
                    );
                });
            },
            () => {
                this.cloneInprogress = false; // re-enable the button
            }
        );
    };

    public _preparePagePayload(page: ICMSPage): ICMSPage {
        const newClonePage = lodash.cloneDeep(page);

        lodash.merge(newClonePage, this.pageBuilder.getPageProperties()); // set page info properties

        newClonePage.cloneComponents = this.pageBuilder.getComponentCloneOption() === 'clone'; // set clone option
        newClonePage.itemtype = page.typeCode;

        if (this.isRestrictionsActive()) {
            // set restrictions
            newClonePage.restrictions = this.pageBuilder.getPageRestrictions();
        }

        return newClonePage;
    }

    getPageTypeCode = (): string => {
        return this.pageBuilder.getPageTypeCode();
    };

    getPageLabel = (): string => {
        return this.pageBuilder.getPageLabel();
    };

    getPageTemplate = (): string => {
        return this.pageBuilder.getPageTemplate();
    };

    getPageInfo = (): ICMSPage => {
        const page = this.pageBuilder.getPageInfo();
        page.uriContext = this.uriContext;

        return page;
    };

    getBasePageInfo = (): ICMSPage => {
        const page = this.pageBuilder.getBasePageInfo();
        page.uriContext = this.uriContext;
        return page;
    };

    getPageRestrictions = (): CMSRestriction[] => {
        return this.pageBuilder.getPageRestrictions();
    };

    variationResult = (displayConditionResult: ICMSPage): void => {
        this.infoChanged = true;
        this.pageBuilder.displayConditionSelected(displayConditionResult);
    };

    onTargetCatalogVersionSelected = ($catalogVersion: ICatalogVersion): void => {
        this.pageBuilder.onTargetCatalogVersionSelected($catalogVersion);
    };

    triggerUpdateCloneOptionResult = (cloneOptionResult: string): void => {
        this.pageBuilder.componentCloneOptionSelected(cloneOptionResult);
    };

    getPageInfoStructure = (): GenericEditorStructure => {
        return this.pageBuilder.getPageInfoStructure();
    };

    restrictionsResult = (onlyOneRestrictionMustApply: boolean, restrictions: CMSRestriction[]) => {
        this.pageBuilder.restrictionsSelected(onlyOneRestrictionMustApply, restrictions);
    };

    isRestrictionsActive = (): boolean => {
        if (
            !this.typeChanged ||
            this.wizardManager.getCurrentStepId() === this.CLONE_PAGE_WIZARD_STEPS.PAGE_RESTRICTIONS
        ) {
            this.typeChanged = false;
            return true;
        }
        return false;
    };

    isPageInfoActive = (): boolean => {
        if (
            !this.infoChanged ||
            this.wizardManager.getCurrentStepId() === this.CLONE_PAGE_WIZARD_STEPS.PAGE_INFO
        ) {
            this.infoChanged = false;
            return true;
        }
        return false;
    };

    resetQueryFilter = (): void => {
        this.query.value = '';
    };

    getRestrictionTypes = (): CMSRestriction[] => {
        return this.pageRestrictionsFacade.getRestrictionTypesByPageType(this.getPageTypeCode());
    };

    getSupportedRestrictionTypes = (): string[] => {
        return this.restrictionsService.getSupportedRestrictionTypeCodes();
    };

    getTargetCatalogVersion = (): ICatalogVersion => {
        return this.pageBuilder.getTargetCatalogVersion();
    };

    isBasePageInfoAvailable = (): boolean => {
        return this.pageBuilder.isBasePageInfoAvailable();
    };
}
