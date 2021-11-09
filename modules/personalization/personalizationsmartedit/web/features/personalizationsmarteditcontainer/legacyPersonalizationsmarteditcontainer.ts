import * as angular from 'angular';
import {doImport as doImport1} from './forcedImports';
doImport1();

import {CrossFrameEventService, IFeatureService, SeModule} from 'smarteditcommons';
import {PersonalizationsmarteditManageCustomizationViewModule} from 'personalizationsmarteditcontainer/management/manageCustomizationView/PersonalizationsmarteditManageCustomizationViewModule';
import {
	PersonalizationsmarteditCommonsModule,
	PersonalizationsmarteditContextUtils,
	PersonalizationsmarteditDateUtils
} from 'personalizationcommons';
import {PersonalizationsmarteditManagementModule} from 'personalizationsmarteditcontainer/management/PersonalizationsmarteditManagementModule';
import {PersonalizationsmarteditServicesModule} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditServicesModule';
import {PersonalizationsmarteditContextService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter';
import {PersonalizationsmarteditContextServiceReverseProxy} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuterReverseProxy';
import {VersioningModule} from 'personalizationsmarteditcontainer/versioning/VersioningModule';
import {VersionCheckerService} from 'personalizationsmarteditcontainer/versioning/VersionCheckerService';
import {PersonalizationsmarteditRestService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditRestService';
import {PersonalizationsmarteditCustomizeViewModule} from 'personalizationsmarteditcontainer/customizeView/PersonalizationsmarteditCustomizeViewModule';
import {PersonalizationsmarteditDataFactory} from 'personalizationsmarteditcontainer/dataFactory/PersonalizationsmarteditDataFactoryModule';
import {PersonalizationsmarteditCommonComponentsModule} from 'personalizationsmarteditcontainer/commonComponents/PersonalizationsmarteditCommonComponentsModule';
import {LegacyPersonalizationsmarteditContextMenuModule} from 'personalizationsmarteditcontainer/contextMenu/LegacyPersonalizationsmarteditContextMenuModule';
import {PersonalizationsmarteditCombinedViewModule} from 'personalizationsmarteditcontainer/combinedView/PersonalizationsmarteditCombinedViewModule';
import {PersonalizationsmarteditContextMenuServiceProxy} from 'personalizationsmarteditcontainer/contextMenu/PersonalizationsmarteditContextMenuServiceOuterProxy';
import {PersonalizationsmarteditToolbarContextModule} from 'personalizationsmarteditcontainer/toolbarContext/PersonalizationsmarteditToolbarContextModule';
import {PersonalizationsmarteditSegmentViewModule} from 'personalizationsmarteditcontainer/management/manageCustomizationView/segmentView/PersonalizationsmarteditSegmentViewModule';
import {PersonalizationsmarteditManagerViewModule} from 'personalizationsmarteditcontainer/management/managerView/PersonalizationsmarteditManagerViewModule';


@SeModule({
	imports: [
		'personalizationsmarteditcontainerTemplates',
		'ui.bootstrap',
		'functionsModule',
		'seConstantsModule',
		'yjqueryModule',
		'smarteditCommonsModule',
		'smarteditRootModule',
		'smarteditServicesModule',
		PersonalizationsmarteditCombinedViewModule,
		PersonalizationsmarteditCustomizeViewModule,
		PersonalizationsmarteditToolbarContextModule,
		LegacyPersonalizationsmarteditContextMenuModule,
		PersonalizationsmarteditManageCustomizationViewModule,
		PersonalizationsmarteditCommonsModule,
		PersonalizationsmarteditManagementModule,
		PersonalizationsmarteditServicesModule,
		VersioningModule,
		PersonalizationsmarteditDataFactory,
		PersonalizationsmarteditCommonComponentsModule,
		PersonalizationsmarteditSegmentViewModule,
		PersonalizationsmarteditManagerViewModule
	],
	config: ($provide: angular.auto.IProvideService) => {
		'ngInject';

		$provide.decorator('rollbackPageVersionService', (
			$delegate: any,
			$log: angular.ILogService,
			versionCheckerService: VersionCheckerService
		) => {

			const rollbackCallback = $delegate.rollbackPageVersion;
			function rollbackWrapper() {
				versionCheckerService.setVersion(arguments[0]);
				return rollbackCallback.apply($delegate, arguments);
			}
			$delegate.rollbackPageVersion = rollbackWrapper;

			const modalCallback = $delegate.showConfirmationModal;
			function modalWrapper() {
				const targetArguments = arguments;

				return versionCheckerService.provideTranlationKey(targetArguments[1]).then(
					(text: string) => {
						targetArguments[1] = text;
						return modalCallback.apply($delegate, targetArguments);
					});
			}
			$delegate.showConfirmationModal = modalWrapper;

			return $delegate;
		});

	},
	initialize: (
		$log: angular.ILogService,
		yjQuery: any,
		domain: any,
		$q: angular.IQService,
		personalizationsmarteditContextServiceReverseProxy: PersonalizationsmarteditContextServiceReverseProxy,
		personalizationsmarteditContextService: PersonalizationsmarteditContextService, // dont remove
		personalizationsmarteditContextMenuServiceProxy: PersonalizationsmarteditContextMenuServiceProxy,
		personalizationsmarteditContextUtils: PersonalizationsmarteditContextUtils,
		personalizationsmarteditPreviewService: any,
		personalizationsmarteditMessageHandler: any,
		personalizationsmarteditRestService: PersonalizationsmarteditRestService,
		personalizationsmarteditUtils: any,
		EVENTS: any,
		SWITCH_LANGUAGE_EVENT: any,
		EVENT_PERSPECTIVE_UNLOADING: any,
		EVENT_PERSPECTIVE_ADDED: any,
		SHOW_TOOLBAR_ITEM_CONTEXT: any,
		crossFrameEventService: CrossFrameEventService,
		featureService: IFeatureService,
		perspectiveService: any,
		smarteditBootstrapGateway: any,
		systemEventService: any,
		experienceService: any,
		httpBackendService: any,
		personalizationsmarteditDateUtils: PersonalizationsmarteditDateUtils,
		CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY: any,
		COMBINED_VIEW_TOOLBAR_ITEM_KEY: any
	) => {
		'ngInject';

		if (!httpBackendService.backends.PATCH) {
			httpBackendService.backends.PATCH = [];
		}

		const PERSONALIZATION_PERSPECTIVE_KEY: string = 'personalizationsmartedit.perspective';

		const loadCSS = (href: string) => {
			const cssLink = yjQuery("<link rel='stylesheet' type='text/css' href='" + href + "'>");
			yjQuery("head").append(cssLink);
		};
		loadCSS(domain + "/personalizationsmartedit/css/style.css");

		featureService.addToolbarItem({
			toolbarId: 'smartEditPerspectiveToolbar',
			key: CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY,
			type: 'HYBRID_ACTION',
			nameI18nKey: 'personalization.toolbar.pagecustomizations',
			priority: 4,
			section: 'left',
			include: 'personalizationsmarteditCustomizeViewWrapperTemplate.html',
			contextTemplateUrl: 'personalizationsmarteditCustomizeToolbarContextWrapperTemplate.html',
			keepAliveOnClose: false,
			iconClassName: 'sap-icon--palette se-toolbar-menu-ddlb--button__icon',
			permissions: ['se.edit.page', 'se.personalization.page']
		});
		featureService.addToolbarItem({
			toolbarId: 'smartEditPerspectiveToolbar',
			key: COMBINED_VIEW_TOOLBAR_ITEM_KEY,
			type: 'HYBRID_ACTION',
			nameI18nKey: 'personalization.toolbar.combinedview.name',
			priority: 6,
			section: 'left',
			include: 'personalizationsmarteditCombinedViewMenuWrapperTemplate.html',
			contextTemplateUrl: 'personalizationsmarteditCombinedViewToolbarContextWrapperTemplate.html',
			keepAliveOnClose: false,
			iconClassName: 'sap-icon--switch-views se-toolbar-menu-ddlb--button__icon',
			permissions: ['se.read.page', 'se.personalization.page']
		});
		featureService.addToolbarItem({
			toolbarId: 'smartEditPerspectiveToolbar',
			key: 'personalizationsmartedit.container.manager.toolbar',
			type: 'HYBRID_ACTION',
			nameI18nKey: 'personalization.toolbar.library.name',
			priority: 8,
			section: 'left',
			include: 'manageCustomizationViewMenuWrapperTemplate.html',
			keepAliveOnClose: false,
			iconClassName: 'sap-icon--bbyd-active-sales se-toolbar-menu-ddlb--button__icon',
			permissions: ['se.edit.page']
		});
		featureService.register({
			key: 'personalizationsmartedit.context.service',
			nameI18nKey: 'personalization.context.service.name',
			descriptionI18nKey: 'personalization.context.service.description',
			enablingCallback: () => {
				const personalization = personalizationsmarteditContextService.getPersonalization();
				personalization.enabled = true;
				personalizationsmarteditContextService.setPersonalization(personalization);
			},
			disablingCallback: () => {
				const personalization = personalizationsmarteditContextService.getPersonalization();
				personalization.enabled = false;
				personalizationsmarteditContextService.setPersonalization(personalization);
			},
			permissions: ['se.edit.page']
		});

		perspectiveService.register({
			key: PERSONALIZATION_PERSPECTIVE_KEY,
			nameI18nKey: 'personalization.perspective.name',
			features: ['personalizationsmartedit.context.service',
				CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY,
				'personalizationsmartedit.container.manager.toolbar',
				COMBINED_VIEW_TOOLBAR_ITEM_KEY,
				'personalizationsmarteditSharedSlot',
				'personalizationsmarteditComponentLightUp',
				'personalizationsmarteditCombinedViewComponentLightUp',
				'personalizationsmartedit.context.add.action',
				'personalizationsmartedit.context.edit.action',
				'personalizationsmartedit.context.delete.action',
				'personalizationsmartedit.context.info.action',
				'personalizationsmartedit.context.component.edit.action',
				'personalizationsmartedit.context.show.action.list',
				'externalcomponentbutton',
				'personalizationsmarteditExternalComponentDecorator',
				'se.contextualMenu',
				'se.emptySlotFix',
				'se.cms.pageDisplayStatus'
			],
			perspectives: [],
			permissions: ['se.personalization.open']
		});

		const clearAllContextsAndReloadPreview = () => {
			personalizationsmarteditContextUtils.clearCombinedViewCustomizeContext(personalizationsmarteditContextService);
			personalizationsmarteditContextUtils.clearCustomizeContextAndReloadPreview(personalizationsmarteditPreviewService, personalizationsmarteditContextService);
			personalizationsmarteditContextUtils.clearCombinedViewContextAndReloadPreview(personalizationsmarteditPreviewService, personalizationsmarteditContextService);
		};

		crossFrameEventService.subscribe(EVENT_PERSPECTIVE_UNLOADING, (eventId: any, unloadingPerspective: string) => {
			if (unloadingPerspective === PERSONALIZATION_PERSPECTIVE_KEY) {
				clearAllContextsAndReloadPreview();
			}
		});

		const clearAllContexts = () => {
			personalizationsmarteditContextUtils.clearCombinedViewCustomizeContext(personalizationsmarteditContextService);
			personalizationsmarteditContextUtils.clearCustomizeContext(personalizationsmarteditContextService);
			personalizationsmarteditContextUtils.clearCombinedViewContext(personalizationsmarteditContextService);
		};

		systemEventService.subscribe(EVENTS.EXPERIENCE_UPDATE, () => {
			clearAllContexts();
			personalizationsmarteditContextService.setCustomizeFiltersState({});
			return $q.when();
		});

		systemEventService.subscribe(EVENT_PERSPECTIVE_ADDED, () => {
			personalizationsmarteditPreviewService.removePersonalizationDataFromPreview();
			return $q.when();
		});

		systemEventService.subscribe(SWITCH_LANGUAGE_EVENT, () => {
			const combinedView = personalizationsmarteditContextService.getCombinedView();
			angular.forEach(combinedView.selectedItems, function(item: any) {
				personalizationsmarteditUtils.getAndSetCatalogVersionNameL10N(item.variation);
			});
			personalizationsmarteditContextService.setCombinedView(combinedView);
			return $q.when();
		});

		smarteditBootstrapGateway.subscribe("smartEditReady", (eventId: any, data: any) => {

			crossFrameEventService.publish(SHOW_TOOLBAR_ITEM_CONTEXT, CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY);
			crossFrameEventService.publish(SHOW_TOOLBAR_ITEM_CONTEXT, COMBINED_VIEW_TOOLBAR_ITEM_KEY);

			const customize = personalizationsmarteditContextService.getCustomize().selectedCustomization;
			const combinedView = personalizationsmarteditContextService.getCombinedView().customize.selectedCustomization;
			const combinedViewCustomize = personalizationsmarteditContextService.getCombinedView().selectedItems;
			experienceService.getCurrentExperience().then((experience: any) => {
				if (!experience.variations && (customize || combinedView || combinedViewCustomize)) {
					clearAllContexts();
				}
			});

			personalizationsmarteditContextService.refreshExperienceData().then(() => {
				const experience = personalizationsmarteditContextService.getSeData().seExperienceData;
				const activePerspective = perspectiveService.getActivePerspective() || {};
				if (activePerspective.key === PERSONALIZATION_PERSPECTIVE_KEY && experience.pageContext.catalogVersionUuid !== experience.catalogDescriptor.catalogVersionUuid) {
					const warningConf = {
						message: 'personalization.warning.pagefromparent',
						timeout: -1
					};
					personalizationsmarteditMessageHandler.sendWarning(warningConf);
				}
			}).finally(() => {
				personalizationsmarteditContextService.applySynchronization();
			});
		});

	}
})
export class Personalizationsmarteditcontainermodule {}
