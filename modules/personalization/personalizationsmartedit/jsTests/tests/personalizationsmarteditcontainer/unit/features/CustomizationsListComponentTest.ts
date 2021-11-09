import 'jasmine';
import * as lo from 'lodash';
import {coreAnnotationsHelper, promiseHelper} from 'testhelpers';
import {PersonalizationsmarteditUtils} from "../../../../../web/features/personalizationcommons/PersonalizationsmarteditUtils";
import {PersonalizationsmarteditContextService} from "../../../../../web/features/personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter";
import {CustomizationsListComponent} from "../../../../../web/features/personalizationsmarteditcontainer/customizeView/customizationsList/CustomizationsListComponent";
import {PersonalizationsmarteditContextUtils} from "../../../../../web/features/personalizationcommons/PersonalizationsmarteditContextUtils";
import {PersonalizationsmarteditCommerceCustomizationService} from "../../../../../web/features/personalizationcommons/PersonalizationsmarteditCommerceCustomizationService";
import {PersonalizationsmarteditContextServiceProxy} from "../../../../../web/features/personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuterProxy";

describe('CustomizationsListComponent', () => {
	coreAnnotationsHelper.init();

	const $q = promiseHelper.$q();
	const lodash: lo.LoDashStatic = (window as any).smarteditLodash;
	let $translate: jasmine.SpyObj<angular.translate.ITranslateService>;
	let l10nFilter: jasmine.SpyObj<any>;
	let catalogService: jasmine.SpyObj<any>;
	let personalizationsmarteditRestService: jasmine.SpyObj<any>;
	let personalizationsmarteditDateUtils: jasmine.SpyObj<any>;
	let personalizationsmarteditPreviewService: jasmine.SpyObj<any>;
	let personalizationsmarteditManager: jasmine.SpyObj<any>;
	let personalizationsmarteditMessageHandler: jasmine.SpyObj<any>;
	let sharedDataService: jasmine.SpyObj<any>;
	let loadConfigManagerService: jasmine.SpyObj<any>;
	let personalizationsmarteditCustomizeViewServiceProxy: jasmine.SpyObj<any>;
	let crossFrameEventService: jasmine.SpyObj<any>;
	let PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING: jasmine.SpyObj<any>;
	let PERSONALIZATION_MODEL_STATUS_CODES: jasmine.SpyObj<any>;
	let PERSONALIZATION_VIEW_STATUS_MAPPING_CODES: jasmine.SpyObj<any>;
	const systemEventService = jasmine.createSpy('systemEventService');

	const personalizationsmarteditContextUtils = new PersonalizationsmarteditContextUtils();
	const personalizationsmarteditCommerceCustomizationService = new PersonalizationsmarteditCommerceCustomizationService();
	const personalizationsmarteditContextServiceProxy = new PersonalizationsmarteditContextServiceProxy();

	let personalizationsmarteditUtils: PersonalizationsmarteditUtils;
	let personalizationsmarteditContextService: PersonalizationsmarteditContextService;
	let customizationsListComponent: CustomizationsListComponent;

	const SHOW_TOOLBAR_ITEM_CONTEXT = 'SHOW_TOOLBAR_ITEM_CONTEXT';
	const CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY = 'CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY';

	const mockVariation = {
		code: "testVariation"
	};
	const mockCustomization1 = {
		code: "testCustomization1",
		variations: [mockVariation]
	};
	const mockCustomization2 = {
		code: "testCustomization2",
		variations: [mockVariation]
	};
	const mockCustomizationCollapsed = {
		code: "testCustomizationCollapsed",
		variations: [mockVariation],
		collapsed: true
	};

	const mockComponentList = ['component1', 'component2'];
	const mockVariationList = [mockVariation];

	beforeEach(() => {

		$translate = jasmine.createSpyObj('$translate', ['instant']);
		l10nFilter = jasmine.createSpyObj('l10nFilter', ['debug']);
		catalogService = jasmine.createSpyObj('catalogService', ['debug']);
		personalizationsmarteditRestService = jasmine.createSpyObj('personalizationsmarteditRestService',
			['getCustomizations', 'getComponenentsIdsForVariation', 'getVariationsForCustomization', 'getCxCmsActionsOnPageForCustomization']);
		personalizationsmarteditDateUtils = jasmine.createSpyObj('personalizationsmarteditDateUtils', ['formatDate', 'isDateValidOrEmpty', 'isDateRangeValid']);
		personalizationsmarteditPreviewService = jasmine.createSpyObj('personalizationsmarteditPreviewService', ['updatePreviewTicketWithVariations']);
		personalizationsmarteditManager = jasmine.createSpyObj('personalizationsmarteditManager', ['openCreateCustomizationModal']);
		personalizationsmarteditMessageHandler = jasmine.createSpyObj('personalizationsmarteditMessageHandler', ['sendError']);
		sharedDataService = jasmine.createSpyObj('sharedDataService', ['get']);
		loadConfigManagerService = jasmine.createSpyObj('loadConfigManagerService', ['loadAsObject']);
		personalizationsmarteditCustomizeViewServiceProxy = jasmine.createSpyObj('personalizationsmarteditCustomizeViewServiceProxy', ['getSourceContainersInfo']);
		crossFrameEventService = jasmine.createSpyObj('crossFrameEventService', ['publish']);
		PERSONALIZATION_MODEL_STATUS_CODES = jasmine.createSpyObj('PERSONALIZATION_MODEL_STATUS_CODES', ['ENABLED', 'DISABLED']);
		PERSONALIZATION_VIEW_STATUS_MAPPING_CODES = jasmine.createSpyObj('PERSONALIZATION_VIEW_STATUS_MAPPING_CODES', ['ENABLED', 'DISABLED']);
		PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING = jasmine.createSpyObj('PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING', ['A', 'B']);

		personalizationsmarteditUtils = new PersonalizationsmarteditUtils(
			$q,
			$translate,
			l10nFilter,
			PERSONALIZATION_MODEL_STATUS_CODES,
			PERSONALIZATION_VIEW_STATUS_MAPPING_CODES,
			PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING,
			catalogService
		);
		personalizationsmarteditContextService = new PersonalizationsmarteditContextService(
			$q,
			sharedDataService,
			loadConfigManagerService,
			personalizationsmarteditContextServiceProxy,
			personalizationsmarteditContextUtils
		);

		customizationsListComponent = new CustomizationsListComponent(
			$q,
			$translate,
			personalizationsmarteditContextService,
			personalizationsmarteditRestService,
			personalizationsmarteditCommerceCustomizationService,
			personalizationsmarteditMessageHandler,
			personalizationsmarteditUtils,
			personalizationsmarteditDateUtils,
			personalizationsmarteditContextUtils,
			personalizationsmarteditPreviewService,
			personalizationsmarteditManager,
			personalizationsmarteditCustomizeViewServiceProxy,
			systemEventService,
			crossFrameEventService,
			SHOW_TOOLBAR_ITEM_CONTEXT,
			CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY,
			lodash
		);

		personalizationsmarteditRestService.getComponenentsIdsForVariation.and.callFake(() => {
			const deferred = $q.defer();
			deferred.resolve({
				components: mockComponentList
			});
			return deferred.promise;
		});

		personalizationsmarteditRestService.getVariationsForCustomization.and.callFake(() => {
			const deferred = $q.defer();
			deferred.resolve({
				variations: mockVariationList
			});
			return deferred.promise;
		});

		personalizationsmarteditRestService.getCxCmsActionsOnPageForCustomization.and.callFake(() => {
			return $q.defer().promise;
		});

		personalizationsmarteditPreviewService.updatePreviewTicketWithVariations.and.callFake(() => {
			return $q.defer().promise;
		});

		personalizationsmarteditCustomizeViewServiceProxy.getSourceContainersInfo.and.callFake(() => {
			return $q.defer().promise;
		});
	});

	describe('Component API', () => {
		it('should have proper api when initialized without parameters', () => {
			expect(customizationsListComponent.initCustomization).toBeDefined();
			expect(customizationsListComponent.editCustomizationAction).toBeDefined();
			expect(customizationsListComponent.customizationClick).toBeDefined();
			expect(customizationsListComponent.customizationRowClick).toBeDefined();
			expect(customizationsListComponent.getSelectedVariationClass).toBeDefined();
			expect(customizationsListComponent.variationClick).toBeDefined();
			expect(customizationsListComponent.hasCommerceActions).toBeDefined();
			expect(customizationsListComponent.clearAllSubMenu).toBeDefined();
			expect(customizationsListComponent.getActivityStateForCustomization).toBeDefined();
			expect(customizationsListComponent.getActivityStateForVariation).toBeDefined();
			expect(customizationsListComponent.getEnablementTextForCustomization).toBeDefined();
			expect(customizationsListComponent.getEnablementTextForVariation).toBeDefined();
			expect(customizationsListComponent.isEnabled).toBeDefined();
			expect(customizationsListComponent.getDatesForCustomization).toBeDefined();
			expect(customizationsListComponent.customizationSubMenuAction).toBeDefined();
		});

		it('should have proper api when initialized with parameters', () => {
			customizationsListComponent.customizationsList = [mockCustomization1, mockCustomization2];
			expect(customizationsListComponent.initCustomization).toBeDefined();
			expect(customizationsListComponent.editCustomizationAction).toBeDefined();
			expect(customizationsListComponent.customizationRowClick).toBeDefined();
			expect(customizationsListComponent.customizationClick).toBeDefined();
			expect(customizationsListComponent.getSelectedVariationClass).toBeDefined();
			expect(customizationsListComponent.variationClick).toBeDefined();
			expect(customizationsListComponent.hasCommerceActions).toBeDefined();
			expect(customizationsListComponent.clearAllSubMenu).toBeDefined();
			expect(customizationsListComponent.getActivityStateForCustomization).toBeDefined();
			expect(customizationsListComponent.getActivityStateForVariation).toBeDefined();
			expect(customizationsListComponent.getEnablementTextForCustomization).toBeDefined();
			expect(customizationsListComponent.getEnablementTextForVariation).toBeDefined();
			expect(customizationsListComponent.isEnabled).toBeDefined();
			expect(customizationsListComponent.getDatesForCustomization).toBeDefined();
			expect(customizationsListComponent.customizationSubMenuAction).toBeDefined();
			expect(customizationsListComponent.customizationsList.length).toBe(2);
		});
	});

	describe('customizationClick', () => {
		it('after called all objects in contex service are set properly', () => {
			expect(personalizationsmarteditContextService.getCustomize().selectedCustomization).toBe(null);
			expect(personalizationsmarteditContextService.getCustomize().selectedVariations).toBe(null);
			expect(personalizationsmarteditContextService.getCustomize().selectedComponents).toBe(null);
			// when
			customizationsListComponent.customizationClick(mockCustomization1);
			// then
			expect(personalizationsmarteditContextService.getCustomize().selectedCustomization).toBe(mockCustomization1);
			expect(personalizationsmarteditContextService.getCustomize().selectedVariations[0].code).toBe(mockCustomization1.variations[0].code);
			expect(personalizationsmarteditContextService.getCustomize().selectedComponents).toBe(mockComponentList);
		});
	});

	describe('variationClick', () => {
		it('after called all objects in contex service are set properly', () => {
			expect(personalizationsmarteditContextService.getCustomize().selectedCustomization).toBe(null);
			expect(personalizationsmarteditContextService.getCustomize().selectedVariations).toBe(null);
			expect(personalizationsmarteditContextService.getCustomize().selectedComponents).toBe(null);
			// when
			customizationsListComponent.variationClick(mockCustomization1, mockVariation);
			// then
			expect(personalizationsmarteditContextService.getCustomize().selectedCustomization).toBe(mockCustomization1);
			expect(personalizationsmarteditContextService.getCustomize().selectedVariations).toBe(mockVariation);
			expect(personalizationsmarteditContextService.getCustomize().selectedComponents).toBe(mockComponentList);
		});
	});

	describe('customizationRowClick', () => {
		it('after called all objects in contex service are set properly', () => {
			customizationsListComponent.customizationsList = [mockCustomizationCollapsed];
			// given
			expect(personalizationsmarteditContextService.getCustomize().selectedCustomization).toBe(null);
			expect(personalizationsmarteditContextService.getCustomize().selectedVariations).toBe(null);
			expect(personalizationsmarteditContextService.getCustomize().selectedComponents).toBe(null);
			// when
			customizationsListComponent.customizationRowClick(mockCustomizationCollapsed, true);
			// then
			expect(personalizationsmarteditContextService.getCustomize().selectedCustomization).toBe(mockCustomizationCollapsed);
			expect(personalizationsmarteditContextService.getCustomize().selectedVariations[0].code).toBe(mockCustomizationCollapsed.variations[0].code);
			expect(personalizationsmarteditContextService.getCustomize().selectedVariations[0].numberOfAffectedComponents).toBe(undefined);
			expect(personalizationsmarteditContextService.getCustomize().selectedComponents).toBe(mockComponentList);
		});
	});
});
