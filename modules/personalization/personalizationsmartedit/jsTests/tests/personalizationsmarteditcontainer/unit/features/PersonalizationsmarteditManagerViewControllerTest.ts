import 'jasmine';
import * as lo from 'lodash';
import {promiseHelper} from "testhelpers";
import {PersonalizationsmarteditManagerViewController} from '../../../../../web/features/personalizationsmarteditcontainer/management/managerView/PersonalizationsmarteditManagerViewController';


describe('PersonalizationsmarteditManagerViewController', () => {

	const $q = promiseHelper.$q();
	const lodash: lo.LoDashStatic = (window as any).smarteditLodash;
	const $timeout: any = jasmine.createSpy('$timeout');
	let $scope: jasmine.SpyObj<angular.IScope>;
	let $translate: jasmine.SpyObj<angular.translate.ITranslateService>;
	let personalizationsmarteditManagerViewUtilsService: jasmine.SpyObj<any>;
	let personalizationsmarteditMessageHandler: jasmine.SpyObj<any>;
	let personalizationsmarteditContextService: jasmine.SpyObj<any>;
	let personalizationsmarteditUtils: jasmine.SpyObj<any>;
	let personalizationsmarteditManager: jasmine.SpyObj<any>;
	let personalizationsmarteditCommerceCustomizationService: jasmine.SpyObj<any>;
	let personalizationsmarteditCommerceCustomizationView: jasmine.SpyObj<any>;
	let personalizationsmarteditDateUtils: jasmine.SpyObj<any>;
	let PERSONALIZATION_VIEW_STATUS_MAPPING_CODES: jasmine.SpyObj<any>;
	let PERSONALIZATION_MODEL_STATUS_CODES: jasmine.SpyObj<any>;
	let waitDialogService: jasmine.SpyObj<any>;
	let systemEventService: jasmine.SpyObj<any>;

	let personalizationsmarteditManagerViewController: PersonalizationsmarteditManagerViewController;

	// === SETUP ===
	beforeEach(() => {

		$scope = jasmine.createSpyObj('$scope', ['scrollZoneElement', 'scrollZoneVisible', 'filteredCustomizationsCount']);
		$translate = jasmine.createSpyObj('$translate', ['instant']);
		personalizationsmarteditManagerViewUtilsService = jasmine.createSpyObj('personalizationsmarteditManagerViewUtilsService', ['deleteVariationAction', 'deleteCustomizationAction']);
		personalizationsmarteditMessageHandler = jasmine.createSpyObj('personalizationsmarteditMessageHandler', ['sendError']);
		personalizationsmarteditContextService = jasmine.createSpyObj('personalizationsmarteditContextService', ['getSeData']);
		personalizationsmarteditContextService.getSeData.and.callFake(() => {
			return {
				seExperienceData: {
					languageDescriptor: {
						isocode: "AA"
					},
					catalogDescriptor: {
						name: {
							AA: "aaa"
						},
						catalogVersion: "catalogVersion"
					}
				}
			};
		});
		personalizationsmarteditUtils = jasmine.createSpyObj('personalizationsmarteditUtils', ['getVisibleItems', 'getStatusesMapping']);
		personalizationsmarteditUtils.getStatusesMapping.and.callFake(() => {
			return [];
		});
		personalizationsmarteditUtils.getVisibleItems.and.callFake(() => {
			return ["a", "b"];
		});
		personalizationsmarteditManager = jasmine.createSpyObj('personalizationsmarteditManager', ['openCreateCustomizationModal']);
		personalizationsmarteditCommerceCustomizationService = jasmine.createSpyObj('personalizationsmarteditCommerceCustomizationService', ['isCommerceCustomizationEnabled']);
		personalizationsmarteditCommerceCustomizationView = jasmine.createSpyObj('personalizationsmarteditCommerceCustomizationView', ['openCommerceCustomizationAction']);
		personalizationsmarteditDateUtils = jasmine.createSpyObj('personalizationsmarteditDateUtils', ['formatDate']);
		PERSONALIZATION_VIEW_STATUS_MAPPING_CODES = jasmine.createSpyObj('PERSONALIZATION_VIEW_STATUS_MAPPING_CODES', ['mock']);
		PERSONALIZATION_MODEL_STATUS_CODES = jasmine.createSpyObj('PERSONALIZATION_MODEL_STATUS_CODES', ['mock']);
		waitDialogService = jasmine.createSpyObj('waitDialogService', ['showWaitModal']);
		systemEventService = jasmine.createSpyObj('systemEventService', ['registerEventHandler']);

		personalizationsmarteditManagerViewController = new PersonalizationsmarteditManagerViewController(
			$scope,
			$translate,
			$q,
			lodash,
			personalizationsmarteditManagerViewUtilsService,
			personalizationsmarteditMessageHandler,
			personalizationsmarteditContextService,
			personalizationsmarteditUtils,
			personalizationsmarteditManager,
			personalizationsmarteditCommerceCustomizationService,
			personalizationsmarteditCommerceCustomizationView,
			personalizationsmarteditDateUtils,
			PERSONALIZATION_VIEW_STATUS_MAPPING_CODES,
			PERSONALIZATION_MODEL_STATUS_CODES,
			waitDialogService,
			$timeout,
			systemEventService
		);

	});

	it('Public API', () => {
		expect(personalizationsmarteditManagerViewController.searchInputKeypress).toBeDefined();
		expect(personalizationsmarteditManagerViewController.addMoreItems).toBeDefined();
		expect(personalizationsmarteditManagerViewController.getElementToScroll).toBeDefined();
		expect(personalizationsmarteditManagerViewController.setScrollZoneElement).toBeDefined();
		expect(personalizationsmarteditManagerViewController.openNewModal).toBeDefined();
		expect(personalizationsmarteditManagerViewController.isSearchGridHeaderHidden).toBeDefined();
		expect(personalizationsmarteditManagerViewController.scrollZoneReturnToTop).toBeDefined();
		expect(personalizationsmarteditManagerViewController.isReturnToTopButtonVisible).toBeDefined();
		expect(personalizationsmarteditManagerViewController.isFilterEnabled).toBeDefined();
		expect(personalizationsmarteditManagerViewController.setCustomizationRank).toBeDefined();
		expect(personalizationsmarteditManagerViewController.setVariationRank).toBeDefined();
		expect(personalizationsmarteditManagerViewController.refreshGrid).toBeDefined();
		expect(personalizationsmarteditManagerViewController.editCustomizationAction).toBeDefined();
		expect(personalizationsmarteditManagerViewController.editVariationAction).toBeDefined();
		expect(personalizationsmarteditManagerViewController.deleteCustomizationAction).toBeDefined();
		expect(personalizationsmarteditManagerViewController.isCommerceCustomizationEnabled).toBeDefined();
		expect(personalizationsmarteditManagerViewController.manageCommerceCustomization).toBeDefined();
		expect(personalizationsmarteditManagerViewController.isDeleteVariationEnabled).toBeDefined();
		expect(personalizationsmarteditManagerViewController.deleteVariationAction).toBeDefined();
		expect(personalizationsmarteditManagerViewController.toogleVariationActive).toBeDefined();
		expect(personalizationsmarteditManagerViewController.customizationClickAction).toBeDefined();
		expect(personalizationsmarteditManagerViewController.hasCommerceActions).toBeDefined();
		expect(personalizationsmarteditManagerViewController.getCommerceCustomizationTooltip).toBeDefined();
		expect(personalizationsmarteditManagerViewController.getFormattedDate).toBeDefined();
		expect(personalizationsmarteditManagerViewController.getEnablementTextForCustomization).toBeDefined();
		expect(personalizationsmarteditManagerViewController.getEnablementTextForVariation).toBeDefined();
		expect(personalizationsmarteditManagerViewController.getEnablementActionTextForVariation).toBeDefined();
		expect(personalizationsmarteditManagerViewController.getActivityStateForCustomization).toBeDefined();
		expect(personalizationsmarteditManagerViewController.getActivityStateForVariation).toBeDefined();
		expect(personalizationsmarteditManagerViewController.allCustomizationsCollapsed).toBeDefined();
		expect(personalizationsmarteditManagerViewController.statusNotDeleted).toBeDefined();
		expect(personalizationsmarteditManagerViewController.treeOptions).toBeDefined();
		expect(personalizationsmarteditManagerViewController.catalogName).toBeDefined();
		expect(personalizationsmarteditManagerViewController.statuses).toBeDefined();
		expect(personalizationsmarteditManagerViewController.moreCustomizationsRequestProcessing).toBeDefined();
		expect(personalizationsmarteditManagerViewController.pagination).toBeDefined();
		expect(personalizationsmarteditManagerViewController.customizations).toBeDefined();
		expect(personalizationsmarteditManagerViewController.search.name).toBeDefined();
		expect(personalizationsmarteditManagerViewController.$scope).toBeDefined();
		expect(personalizationsmarteditManagerViewController.$scope.scrollZoneElement).toBeDefined();
		expect(personalizationsmarteditManagerViewController.$scope.scrollZoneVisible).toBeDefined();
		expect(personalizationsmarteditManagerViewController.$scope.filteredCustomizationsCount).toBeDefined();
	});

});
