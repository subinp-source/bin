import 'jasmine';
import * as lo from 'lodash';
import {promiseHelper} from "testhelpers";
import {PersonalizationsmarteditCombinedViewCommonsService} from '../../../../../web/features/personalizationsmarteditcontainer/combinedView/PersonalizationsmarteditCombinedViewCommonsService';
import {PersonalizationsmarteditCombinedViewMenuComponent} from '../../../../../web/features/personalizationsmarteditcontainer/combinedView/PersonalizationsmarteditCombinedViewMenuComponent';
import {PersonalizationsmarteditCombinedViewConfigureController} from '../../../../../web/features/personalizationsmarteditcontainer/combinedView/PersonalizationsmarteditCombinedViewConfigureController';

describe('Test Personalizationsmartedit Combined View Module', () => {
	let modalService: jasmine.SpyObj<any>;
	let $translate: jasmine.SpyObj<angular.translate.ITranslateService>;
	let $scope: jasmine.SpyObj<angular.IScope>;
	let personalizationsmarteditContextService: jasmine.SpyObj<any>;
	let personalizationsmarteditRestService: jasmine.SpyObj<any>;
	let personalizationsmarteditPreviewService: jasmine.SpyObj<any>;
	let personalizationsmarteditUtils: jasmine.SpyObj<any>;
	let personalizationsmarteditMessageHandler: jasmine.SpyObj<any>;
	let personalizationsmarteditContextUtils: jasmine.SpyObj<any>;
	let customizationDataFactory: jasmine.SpyObj<any>;
	let crossFrameEventService: jasmine.SpyObj<any>;
	let permissionService: jasmine.SpyObj<any>;
	let modalManager: jasmine.SpyObj<any>;
	let MODAL_BUTTON_ACTIONS: jasmine.SpyObj<any>;
	let MODAL_BUTTON_STYLES: jasmine.SpyObj<any>;
	let componentMenuService: jasmine.SpyObj<any>;

	let PERSONALIZATION_VIEW_STATUS_MAPPING_CODES: jasmine.SpyObj<any>;
	let PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER: jasmine.SpyObj<any>;
	const SHOW_TOOLBAR_ITEM_CONTEXT = 'SHOW_TOOLBAR_ITEM_CONTEXT';
	const COMBINED_VIEW_TOOLBAR_ITEM_KEY = 'personalizationsmartedit.container.combinedview.toolbar';
	const lodash: lo.LoDashStatic = (window as any).smarteditLodash;


	const $q = promiseHelper.$q();
	const mockCombinedView = {
		enabled: false,
		selectedItems: []
	};
	const mockExperienceData = {
		catalogDescriptor: {
			catalogId: "myId"
		}
	};
	const mockVariation = {
		code: "testVariation"
	};
	const mockCustomization = {
		code: "testCustomization",
		variations: [mockVariation]
	};

	let personalizationsmarteditCombinedViewCommonsService: PersonalizationsmarteditCombinedViewCommonsService;
	let personalizationsmarteditCombinedViewMenuComponent: PersonalizationsmarteditCombinedViewMenuComponent;
	let personalizationsmarteditCombinedViewConfigureController: PersonalizationsmarteditCombinedViewConfigureController;

	// === SETUP ===
	beforeEach(() => {
		$translate = jasmine.createSpyObj('$translate', ['instant']);
		$scope = jasmine.createSpyObj('$scope', ['$watch']);

		modalService = jasmine.createSpyObj('modalService', ['open']);
		modalManager = jasmine.createSpyObj('modalManager', ['setButtonHandler', 'enableButton', 'disableButton']);
		MODAL_BUTTON_ACTIONS = jasmine.createSpyObj('MODAL_BUTTON_ACTIONS', ['NONE', 'CLOSE', 'DISMISS']);
		MODAL_BUTTON_STYLES = jasmine.createSpyObj('MODAL_BUTTON_STYLES', ['DEFAULT', 'PRIMARY', 'SECONDARY']);
		PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER = jasmine.createSpyObj('PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER', ['ALL', 'ONLY_THIS_PAGE']);
		PERSONALIZATION_VIEW_STATUS_MAPPING_CODES = jasmine.createSpyObj('PERSONALIZATION_VIEW_STATUS_MAPPING_CODES', ['ENABLED', 'DISABLED']);
		componentMenuService = jasmine.createSpyObj('componentMenuService', ['getValidContentCatalogVersions']);

		personalizationsmarteditContextService = jasmine.createSpyObj('personalizationsmarteditContextService', ['getCombinedView', 'getSeExperienceData', 'setCombinedView']);
		personalizationsmarteditRestService = jasmine.createSpyObj('personalizationsmarteditRestService', ['getCustomizations']);
		personalizationsmarteditMessageHandler = jasmine.createSpyObj('personalizationsmarteditMessageHandler', ['sendError']);
		customizationDataFactory = jasmine.createSpyObj('customizationDataFactory', ['updateData', 'resetData']);
		crossFrameEventService = jasmine.createSpyObj('crossFrameEventService', ['publish']);
		permissionService = jasmine.createSpyObj('permissionService', ['isPermitted']);

		modalService.open.and.callFake(() => {
			const deferred = $q.defer();
			deferred.resolve();
			return deferred.promise;
		});
		personalizationsmarteditContextService.getCombinedView.and.callFake(() => {
			return mockCombinedView;
		});
		personalizationsmarteditContextService.getSeExperienceData.and.callFake(() => {
			return mockExperienceData;
		});
		personalizationsmarteditRestService.getCustomizations.and.callFake(() => {
			const deferred = $q.defer();
			deferred.resolve({
				customizations: [mockCustomization, mockCustomization],
				pagination: {
					count: 5,
					page: 0,
					totalCount: 5,
					totalPages: 1
				}
			});
			return deferred.promise;
		});
		personalizationsmarteditPreviewService = jasmine.createSpyObj('personalizationsmarteditPreviewService', ['updatePreviewTicketWithVariations']);

		personalizationsmarteditContextUtils = jasmine.createSpyObj('personalizationsmarteditContextUtils', ['clearCombinedViewCustomizeContext']);
		personalizationsmarteditUtils = jasmine.createSpyObj('personalizationsmarteditUtils', ['isItemFromCurrentCatalog']);

		personalizationsmarteditCombinedViewCommonsService = new PersonalizationsmarteditCombinedViewCommonsService(
			$q,
			personalizationsmarteditContextUtils,
			personalizationsmarteditContextService,
			personalizationsmarteditPreviewService,
			personalizationsmarteditUtils,
			personalizationsmarteditRestService,
			modalService,
			MODAL_BUTTON_ACTIONS,
			MODAL_BUTTON_STYLES
		);
		personalizationsmarteditCombinedViewMenuComponent = new PersonalizationsmarteditCombinedViewMenuComponent(
			$translate,
			personalizationsmarteditContextService,
			personalizationsmarteditMessageHandler,
			personalizationsmarteditRestService,
			personalizationsmarteditContextUtils,
			personalizationsmarteditUtils,
			personalizationsmarteditPreviewService,
			personalizationsmarteditCombinedViewCommonsService,
			crossFrameEventService,
			permissionService,
			PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER,
			SHOW_TOOLBAR_ITEM_CONTEXT,
			COMBINED_VIEW_TOOLBAR_ITEM_KEY
		);
		personalizationsmarteditCombinedViewConfigureController = new PersonalizationsmarteditCombinedViewConfigureController(
			$translate,
			customizationDataFactory,
			personalizationsmarteditContextService,
			personalizationsmarteditMessageHandler,
			personalizationsmarteditUtils,
			componentMenuService,
			PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER,
			PERSONALIZATION_VIEW_STATUS_MAPPING_CODES,
			$scope,
			$q,
			lodash,
			modalManager
		);
	});

	it('GIVEN that personalizationsmarteditCombinedViewCommonsService is instantiated it contains proper functions', () => {
		expect(personalizationsmarteditCombinedViewCommonsService.openManagerAction).toBeDefined();
		expect(personalizationsmarteditCombinedViewCommonsService.updatePreview).toBeDefined();
		expect(personalizationsmarteditCombinedViewCommonsService.getVariationsForPreviewTicket).toBeDefined();
		expect(personalizationsmarteditCombinedViewCommonsService.combinedViewEnabledEvent).toBeDefined();
	});

	it('GIVEN that modal for configure combined view is open, proper functions should be called', () => {
		personalizationsmarteditCombinedViewCommonsService.openManagerAction();
		expect(modalService.open).toHaveBeenCalled();
	});

	it('GIVEN that personalizationsmarteditCombinedViewMenuComponent properties are instantiated properly', () => {
		personalizationsmarteditCombinedViewMenuComponent.$onInit();

		expect(personalizationsmarteditCombinedViewMenuComponent.combinedView).toBeDefined();
		expect(personalizationsmarteditCombinedViewMenuComponent.selectedItems).toBeDefined();
		expect(personalizationsmarteditCombinedViewMenuComponent.getClassForElement).toBeDefined();
		expect(personalizationsmarteditCombinedViewMenuComponent.combinedView).toEqual(mockCombinedView);
	});

	it('GIVEN that personalizationsmarteditCombinedViewConfigureController properties in scope are instantiated properly', () => {

		personalizationsmarteditCombinedViewCommonsService.openManagerAction();

		expect(personalizationsmarteditCombinedViewConfigureController.init).toBeDefined();
		personalizationsmarteditCombinedViewConfigureController.init();

		expect(personalizationsmarteditCombinedViewConfigureController.combinedView).toBeDefined();
		expect(personalizationsmarteditCombinedViewConfigureController.$scope.selectedItems).toBeDefined();
		expect(personalizationsmarteditCombinedViewConfigureController.$scope.selectedElement).toBeDefined();
		expect(personalizationsmarteditCombinedViewConfigureController.$scope.selectionArray).toBeDefined();
		expect(personalizationsmarteditContextService.getCombinedView).toHaveBeenCalled();
		expect(personalizationsmarteditCombinedViewConfigureController.combinedView).toEqual(mockCombinedView);
	});
});
