/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// Bundling app as legacy script

import { doImport as forImports } from './forcedImports';
forImports();
import { deprecate } from './deprecate';
deprecate();

import {
    moduleUtils,
    CATALOG_DETAILS_COLUMNS,
    InViewElementObserver,
    IFeatureService,
    IPerspectiveService,
    IToolbarServiceFactory,
    SeModule,
    SeRouteService,
    SystemEventService,
    ToolbarDropDownPosition,
    ToolbarItemType,
    ToolbarSection
} from 'smarteditcommons';

import {
    AssetsService,
    CmsResourceLocationsModule,
    ComponentService,
    CMSModesService
} from 'cmscommons';
import {
    trashedPageListControllerModule,
    TrashedPageListController
} from 'cmssmarteditcontainer/components/pages/trashedPageList/TrashedPageListController';
import { CmsSmarteditComponentsModule } from 'cmssmarteditcontainer/components/cmsSmarteditComponentsModule';
import { CmsSmarteditServicesModule } from 'cmssmarteditcontainer/services/cmsSmarteditServicesModule';
import { NavigationManagementPageController } from 'cmssmarteditcontainer/components/navigation/NavigationManagementPageController';
import { ManagePageVersionService } from './components/versioning/services/ManagePageVersionService';
import { RollbackPageVersionService } from './components/versioning/services/RollbackPageVersionService';
import { WorkflowFacade } from './components/workflow/facades/WorkflowFacade';
import { CmsDragAndDropService, CmsDragAndDropServiceModule } from './components/dragAndDrop';
import {
    PRODUCT_CATEGORY_RESOURCE_BASE_URI,
    PRODUCT_CATEGORY_RESOURCE_URI,
    PRODUCT_CATEGORY_SEARCH_RESOURCE_URI
} from './services/ProductCategoryService';
import { CmsGenericEditorConfigurationServiceModule } from './components/genericEditor/config/CmsGenericEditorConfigurationServiceModule';
import { ClonePageWizardServiceModule } from './services/clonePage/clonePageWizardServiceModule';

@SeModule({
    imports: [
        CmsSmarteditServicesModule,
        CmsSmarteditComponentsModule,
        CmsResourceLocationsModule,
        'legacyCatalogDetailsModule',
        'resourceLocationsModule',
        'cmssmarteditContainerTemplates',
        'componentMenuModule',
        'cmscommonsTemplates',
        'genericEditorModule',
        'synchronizeCatalogModule',
        'pageListLinkModule',
        'pageListControllerModule',
        'slotRestrictionsServiceModule',
        CmsDragAndDropServiceModule,
        'seMediaFieldModule',
        'seMediaContainerFieldModule',
        'restrictionsEditorModule',
        'seNavigationNodeSelector',
        'pageSyncMenuToolbarItemModule',
        'productSelectorModule',
        'categorySelectorModule',
        ClonePageWizardServiceModule,
        'cmsLinkToSelectModule',
        'rulesAndPermissionsRegistrationModule',
        'smarteditServicesModule',
        'singleActiveCatalogAwareItemSelectorModule',
        'productCatalogDropdownPopulatorModule',
        'productDropdownPopulatorModule',
        'categoryDropdownPopulatorModule',
        'cmsItemDropdownModule',
        'catalogAwareRouteResolverModule',
        'componentRestrictionsEditorModule',
        'pageRestrictionsEditorModule',
        'pageVersionsModule',
        'displayConditionsEditorModule',
        'linkToggleModule',
        'functionsModule',
        'componentVisibilityAlertServiceModule',
        CmsGenericEditorConfigurationServiceModule,
        'deletePageToolbarItemModule',
        'workflowModule',
        trashedPageListControllerModule.name
    ],
    providers: [
        ...moduleUtils.provideValues({
            PRODUCT_CATEGORY_RESOURCE_URI,
            PRODUCT_CATEGORY_RESOURCE_BASE_URI,
            PRODUCT_CATEGORY_SEARCH_RESOURCE_URI
        })
    ],
    config: (
        PAGE_LIST_PATH: string,
        TRASHED_PAGE_LIST_PATH: string,
        NAVIGATION_MANAGEMENT_PAGE_PATH: string,
        $routeProvider: ng.route.IRouteProvider,
        catalogAwareRouteResolverFunctions: any
    ) => {
        'ngInject';
        SeRouteService.init($routeProvider);

        SeRouteService.provideLegacyRoute({
            path: PAGE_LIST_PATH,
            route: {
                templateUrl: 'pageListTemplate.html',
                controller: 'pageListController',
                controllerAs: 'pageListCtl',
                resolve: {
                    experienceFromPathResolve:
                        catalogAwareRouteResolverFunctions.experienceFromPathResolve
                }
            },
            titleI18nKey: 'se.cms.pagelist.title',
            priority: 20
        });

        SeRouteService.provideLegacyRoute({
            path: TRASHED_PAGE_LIST_PATH,
            route: {
                templateUrl: 'trashedpageListTemplate.html',
                controller: TrashedPageListController,
                controllerAs: 'trashedPageListCtl',
                resolve: {
                    experienceFromPathResolve:
                        catalogAwareRouteResolverFunctions.experienceFromPathResolve
                }
            }
        });

        SeRouteService.provideLegacyRoute({
            path: NAVIGATION_MANAGEMENT_PAGE_PATH,
            route: {
                templateUrl: 'navigationManagementPageTemplate.html',
                controller: NavigationManagementPageController,
                controllerAs: 'nav',
                resolve: {
                    experienceFromPathResolve:
                        catalogAwareRouteResolverFunctions.experienceFromPathResolve
                }
            },
            titleI18nKey: 'se.cms.toolbaritem.navigationmenu.name',
            priority: 10
        });
    },
    initialize:
        /* jshint -W098*/
        /*need to inject for gatewayProxy initialization of componentVisibilityAlertService*/
        (
            $log: angular.ILogService,
            $rootScope: angular.IRootScopeService,
            $routeParams: any,
            $route: angular.route.IRouteService,
            NAVIGATION_MANAGEMENT_PAGE_PATH: string,
            toolbarServiceFactory: IToolbarServiceFactory,
            componentService: ComponentService,
            systemEventService: SystemEventService,
            catalogDetailsService: any,
            featureService: IFeatureService,
            perspectiveService: IPerspectiveService,
            assetsService: AssetsService,
            editorFieldMappingService: any,
            genericEditorTabService: any,
            cmsDragAndDropService: CmsDragAndDropService,
            editorModalService: any,
            clonePageWizardService: any,
            componentVisibilityAlertService: any,
            cmsGenericEditorConfigurationService: any,
            managePageVersionService: ManagePageVersionService,
            rollbackPageVersionService: RollbackPageVersionService,
            workflowFacade: WorkflowFacade,
            inViewElementObserver: InViewElementObserver,
            COMPONENT_CLASS: string
        ) => {
            'ngInject';
            // Configure generic editor
            cmsGenericEditorConfigurationService.setDefaultEditorFieldMappings();
            cmsGenericEditorConfigurationService.setDefaultTabFieldMappings();
            cmsGenericEditorConfigurationService.setDefaultTabsConfiguration();

            featureService.addToolbarItem({
                toolbarId: 'smartEditPerspectiveToolbar',
                key: 'se.cms.componentMenuTemplate',
                type: 'HYBRID_ACTION',
                nameI18nKey: 'se.cms.componentmenu.btn.label.addcomponent',
                descriptionI18nKey: 'cms.toolbaritem.componentmenutemplate.description',
                priority: 100,
                section: 'left',
                dropdownPosition: 'left',
                iconClassName: 'icon-add se-toolbar-menu-ddlb--button__icon',
                callback: () => {
                    systemEventService.publish('ySEComponentMenuOpen', {});
                },
                include: 'componentMenuWrapperTemplate.html',
                permissions: ['se.add.component'],
                keepAliveOnClose: true
            });

            featureService.addToolbarItem({
                toolbarId: 'smartEditPerspectiveToolbar',
                key: 'se.cms.pageInfoMenu',
                type: 'TEMPLATE',
                nameI18nKey: 'se.cms.pageinfo.menu.btn.label',
                priority: 140,
                section: 'left',
                include: 'pageInfoMenuWrapperTemplate.html',
                permissions: ['se.read.page']
            });

            featureService.addToolbarItem({
                toolbarId: 'smartEditPerspectiveToolbar',
                key: 'se.cms.clonePageMenu',
                type: 'ACTION',
                nameI18nKey: 'se.cms.clonepage.menu.btn.label',
                iconClassName: 'icon-duplicate se-toolbar-menu-ddlb--button__icon',
                callback: () => {
                    clonePageWizardService.openClonePageWizard();
                },
                priority: 130,
                section: 'left',
                permissions: ['se.clone.page']
            });

            // sync 120
            featureService.addToolbarItem({
                toolbarId: 'smartEditPerspectiveToolbar',
                key: 'se.cms.pageSyncMenu',
                nameI18nKey: 'se.cms.toolbaritem.pagesyncmenu.name',
                type: 'TEMPLATE',
                include: 'pageSyncMenuToolbarItemWrapperTemplate.html',
                priority: 102,
                section: 'left',
                permissions: ['se.sync.page']
            });

            featureService.addToolbarItem({
                toolbarId: 'smartEditPerspectiveToolbar',
                key: 'deletePageMenu',
                nameI18nKey: 'se.cms.actionitem.page.trash',
                type: 'TEMPLATE',
                include: 'deletePageToolbarItemWrapperTemplate.html',
                priority: 150,
                section: 'left',
                permissions: ['se.delete.page.menu']
            });

            // versions 102
            featureService.addToolbarItem({
                toolbarId: 'smartEditPerspectiveToolbar',
                key: 'se.cms.pageVersionsMenu',
                type: 'HYBRID_ACTION',
                nameI18nKey: 'se.cms.actionitem.page.versions',
                priority: 104,
                section: 'left',
                iconClassName: 'icon-timesheet se-toolbar-menu-ddlb--button__icon',
                include: 'pageVersionsMenuWrapperTemplate.html',
                contextTemplateUrl: 'versionItemContextWrapperTemplate.html',
                permissions: ['se.version.page'],
                keepAliveOnClose: true
            });

            featureService.addToolbarItem({
                toolbarId: 'smartEditPerspectiveToolbar',
                key: 'se.cms.createVersionMenu',
                type: 'ACTION',
                nameI18nKey: 'se.cms.actionitem.page.versions.create',
                iconClassName: 'icon-add se-toolbar-menu-ddlb--button__icon',
                callback: () => {
                    managePageVersionService.createPageVersion();
                },
                priority: 120,
                section: 'left',
                permissions: ['se.version.page', 'se.create.version.page']
            });

            featureService.addToolbarItem({
                toolbarId: 'smartEditPerspectiveToolbar',
                key: 'se.cms.rollbackVersionMenu',
                type: 'ACTION',
                nameI18nKey: 'se.cms.actionitem.page.versions.rollback',
                iconClassName: 'hyicon hyicon-rollback se-toolbar-menu-ddlb--button__icon',
                callback: () => {
                    rollbackPageVersionService.rollbackPageVersion();
                },
                priority: 120,
                section: 'left',
                permissions: ['se.version.page', 'se.rollback.version.page']
            });

            featureService.addToolbarItem({
                toolbarId: 'smartEditPerspectiveToolbar',
                key: 'se.cms.pageWorkflowMenu',
                type: 'TEMPLATE',
                nameI18nKey: 'se.cms.workflow.toolbar.view.workflow.menu',
                include: 'pageWorkflowMenuWrapperTemplate.html',
                priority: 110,
                section: 'right'
            });

            featureService.addToolbarItem({
                toolbarId: 'smartEditPerspectiveToolbar',
                key: 'se.cms.pageDisplayStatus',
                type: 'TEMPLATE',
                nameI18nKey: 'se.cms.page.display.status',
                include: 'pageDisplayStatusWrapperTemplate.html',
                priority: 120,
                section: 'right',
                permissions: ['se.show.page.status']
            });

            featureService.addToolbarItem({
                toolbarId: 'smartEditPerspectiveToolbar',
                key: 'se.cms.pageApprovalSelector',
                type: 'TEMPLATE',
                nameI18nKey: 'se.cms.page.approval.selector',
                include: 'pageApprovalSelectorWrapperTemplate.html',
                priority: 165,
                section: 'right',
                permissions: ['se.force.page.approval']
            });

            const smartEditHeaderToolbarService = toolbarServiceFactory.getToolbarService(
                'smartEditHeaderToolbar'
            );
            smartEditHeaderToolbarService.addItems([
                {
                    key: 'se.cms.workflowInbox',
                    type: ToolbarItemType.TEMPLATE,
                    include: 'headerToolbarInboxTemplate.html',
                    priority: 4,
                    section: ToolbarSection.right,
                    dropdownPosition: ToolbarDropDownPosition.right
                }
            ]);

            const smartEditNavigationToolbarService = toolbarServiceFactory.getToolbarService(
                'smartEditNavigationToolbar'
            );
            smartEditNavigationToolbarService.addItems([
                {
                    key: 'se.cms.shortcut',
                    type: ToolbarItemType.TEMPLATE,
                    include: 'ShortcutLinkWrapperTemplate.html',
                    priority: 1,
                    section: ToolbarSection.left
                }
            ]);

            const smartEditPagesToolbarService = toolbarServiceFactory.getToolbarService(
                'smartEditPagesToolbar'
            );
            smartEditPagesToolbarService.addItems([
                {
                    key: 'se.cms.shortcut',
                    type: ToolbarItemType.TEMPLATE,
                    include: 'ShortcutLinkWrapperTemplate.html',
                    priority: 1,
                    section: ToolbarSection.left
                },
                {
                    key: 'se.cms.trash.page.link',
                    type: ToolbarItemType.TEMPLATE,
                    include: 'trashLinkWrapperTemplate.html',
                    priority: 1,
                    section: ToolbarSection.right
                }
            ]);

            const smartEditTrashPageToolbarService = toolbarServiceFactory.getToolbarService(
                'smartEditTrashPageToolbar'
            );
            smartEditTrashPageToolbarService.addItems([
                {
                    key: 'se.cms.pages.list.link',
                    type: ToolbarItemType.TEMPLATE,
                    include: 'pagesLinkWrapperTemplate.html',
                    priority: 1,
                    section: ToolbarSection.left
                }
            ]);

            catalogDetailsService.addItems([
                {
                    include: 'pageListLinkTemplate.html'
                },
                {
                    include: 'navigationEditorLinkTemplate.html'
                }
            ]);

            catalogDetailsService.addItems(
                [
                    {
                        include: 'catalogDetailsSyncTemplate.html'
                    }
                ],
                CATALOG_DETAILS_COLUMNS.RIGHT
            );

            featureService.register({
                key: 'se.cms.html5DragAndDrop.outer',
                nameI18nKey: 'se.cms.dragAndDrop.name',
                descriptionI18nKey: 'se.cms.dragAndDrop.description',
                enablingCallback: () => {
                    cmsDragAndDropService.register();
                    cmsDragAndDropService.apply();
                },
                disablingCallback: () => {
                    cmsDragAndDropService.unregister();
                }
            });

            perspectiveService.register({
                key: CMSModesService.BASIC_PERSPECTIVE_KEY,
                nameI18nKey: 'se.cms.perspective.basic.name',
                descriptionI18nKey: 'se.hotkey.tooltip',
                features: [
                    'se.contextualMenu',
                    'se.cms.dragandropbutton',
                    'se.cms.remove',
                    'se.cms.edit',
                    'se.cms.componentMenuTemplate',
                    'se.cms.clonePageMenu',
                    'se.cms.pageInfoMenu',
                    'se.emptySlotFix',
                    'se.cms.html5DragAndDrop',
                    'disableSharedSlotEditing',
                    'sharedSlotDisabledDecorator',
                    'se.cms.html5DragAndDrop.outer',
                    'externalComponentDecorator',
                    'externalcomponentbutton',
                    'externalSlotDisabledDecorator',
                    'clonecomponentbutton',
                    'deletePageMenu',
                    'se.cms.pageWorkflowMenu',
                    'se.cms.pageDisplayStatus',
                    'se.cms.pageApprovalSelector',
                    'se.cms.sharedcomponentbutton'
                ],
                perspectives: []
            });

            /* Note: For advance edit mode, the ordering of the entries in the features list will determine the order the buttons will show in the slot contextual menu */
            perspectiveService.register({
                key: CMSModesService.ADVANCED_PERSPECTIVE_KEY,
                nameI18nKey: 'se.cms.perspective.advanced.name',
                descriptionI18nKey: 'se.hotkey.tooltip',
                features: [
                    'se.slotContextualMenu',
                    'se.slotSyncButton',
                    'se.slotSharedButton',
                    'se.slotContextualMenuVisibility',
                    'se.contextualMenu',
                    'se.cms.dragandropbutton',
                    'se.cms.remove',
                    'se.cms.edit',
                    'se.cms.componentMenuTemplate',
                    'se.cms.clonePageMenu',
                    'se.cms.pageInfoMenu',
                    'se.cms.pageSyncMenu',
                    'se.emptySlotFix',
                    'se.cms.html5DragAndDrop',
                    'se.cms.html5DragAndDrop.outer',
                    'syncIndicator',
                    'externalSlotDisabledDecorator',
                    'externalComponentDecorator',
                    'externalcomponentbutton',
                    'clonecomponentbutton',
                    'slotUnsharedButton',
                    'deletePageMenu',
                    'se.cms.pageVersionsMenu',
                    'se.cms.pageWorkflowMenu',
                    'se.cms.pageDisplayStatus',
                    'se.cms.pageApprovalSelector',
                    'se.cms.sharedcomponentbutton'
                ],
                perspectives: []
            });

            perspectiveService.register({
                key: CMSModesService.VERSIONING_PERSPECTIVE_KEY,
                nameI18nKey: 'se.cms.perspective.versioning.name',
                descriptionI18nKey: 'se.cms.perspective.versioning.description',
                features: [
                    'se.cms.pageVersionsMenu',
                    'se.cms.createVersionMenu',
                    'se.cms.rollbackVersionMenu',
                    'se.cms.pageInfoMenu',
                    'disableSharedSlotEditing',
                    'sharedSlotDisabledDecorator',
                    'externalSlotDisabledDecorator',
                    'externalComponentDecorator'
                ],
                perspectives: [],
                permissions: ['se.version.page'],
                isHotkeyDisabled: true
            });

            inViewElementObserver.addSelector(`.${COMPONENT_CLASS}`, () => {
                cmsDragAndDropService.update();
            });
        }
})
export class CmssmarteditContainer {}
