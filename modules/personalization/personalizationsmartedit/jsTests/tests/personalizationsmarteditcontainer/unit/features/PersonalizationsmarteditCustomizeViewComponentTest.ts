import 'jasmine';
import {promiseHelper} from "testhelpers";
import {PersonalizationsmarteditCustomizeViewComponent} from '../../../../../web/features/personalizationsmarteditcontainer/customizeView/PersonalizationsmarteditCustomizeViewComponent';
import {PersonalizationsmarteditMessageHandler} from '../../../../../web/features/personalizationcommons/PersonalizationsmarteditMessageHandler';

describe('PersonalizationsmarteditCustomizeViewComponent', () => {

	// ======= Injected mocks =======
	const $q = promiseHelper.$q();
	let $translate: jasmine.SpyObj<angular.translate.ITranslateService>;
	const PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER = {
		ALL: 'all',
		ONLY_THIS_PAGE: 'onlythispage'
	};
	let personalizationsmarteditContextService: jasmine.SpyObj<any>;
	let personalizationsmarteditUtils: jasmine.SpyObj<any>;
	let personalizationsmarteditRestService: jasmine.SpyObj<any>;
	let customizationDataFactory: jasmine.SpyObj<any>;
	let personalizationsmarteditMessageHandler: PersonalizationsmarteditMessageHandler;

	let personalizationsmarteditCustomizeViewComponent: PersonalizationsmarteditCustomizeViewComponent;

	const mockVariation = {
		code: "testVariation"
	};
	const mockCustomization = {
		code: "testCustomization",
		variations: [mockVariation],
		status: "stat1"
	};

	// === SETUP ===
	beforeEach(() => {
		$translate = jasmine.createSpyObj('$translate', ['instant']);
		personalizationsmarteditRestService = jasmine.createSpyObj('personalizationsmarteditRestService', ['getCustomizations']);
		personalizationsmarteditUtils = jasmine.createSpyObj('personalizationsmarteditUtils', ['getStatusesMapping']);
		customizationDataFactory = jasmine.createSpyObj('customizationDataFactory', ['updateData', 'resetData', 'items']);
		personalizationsmarteditContextService = jasmine.createSpyObj('personalizationsmarteditContextService', ['getCustomizeFiltersState', 'setCustomizeFiltersState', 'refreshExperienceData']);
		personalizationsmarteditMessageHandler = jasmine.createSpyObj('personalizationsmarteditMessageHandler', ['sendError']);
		personalizationsmarteditCustomizeViewComponent = new PersonalizationsmarteditCustomizeViewComponent(
			$translate,
			customizationDataFactory,
			personalizationsmarteditContextService,
			personalizationsmarteditMessageHandler,
			personalizationsmarteditUtils,
			PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER
		);

		customizationDataFactory.updateData.and.callFake(() => {
			personalizationsmarteditRestService.getCustomizations().then((resp: any) => {
				personalizationsmarteditCustomizeViewComponent.customizationsList = resp.customizations;
			});
		});

		personalizationsmarteditRestService.getCustomizations.and.callFake(() => {
			const deferred = $q.defer();
			const retCustomizatons = [mockCustomization, mockCustomization];

			deferred.resolve({
				customizations: retCustomizatons,
				pagination: {
					count: 5,
					page: 0,
					totalCount: 5,
					totalPages: 1
				}
			});
			return deferred.promise;
		});

		personalizationsmarteditUtils.getStatusesMapping.and.callFake(() => {
			return [{
				modelStatuses: {},
				code: "all"
			}];
		});

		personalizationsmarteditContextService.getCustomizeFiltersState.and.callFake(() => {
			return {
				catalogFilter: "catalogMock",
				pageFilter: "pageMock",
				statusFilter: "statusMock",
				nameFilter: "nameMock"
			};
		});

	});

	describe('Component API', () => {

		it('should have proper api when initialized without parameters', () => {
			expect(personalizationsmarteditCustomizeViewComponent.catalogFilterChange).toBeDefined();
			expect(personalizationsmarteditCustomizeViewComponent.pageFilterChange).toBeDefined();
			expect(personalizationsmarteditCustomizeViewComponent.statusFilterChange).toBeDefined();
			expect(personalizationsmarteditCustomizeViewComponent.nameInputKeypress).toBeDefined();
			expect(personalizationsmarteditCustomizeViewComponent.addMoreCustomizationItems).toBeDefined();
			expect(personalizationsmarteditCustomizeViewComponent.$onInit).toBeDefined();
			expect(personalizationsmarteditCustomizeViewComponent.$onDestroy).toBeDefined();
		});
	});

	describe('customizationsList', () => {

		it('should be instantianed and empty', () => {
			personalizationsmarteditCustomizeViewComponent.$onInit();
			expect(personalizationsmarteditCustomizeViewComponent.customizationsList).toBeDefined();
			expect(personalizationsmarteditCustomizeViewComponent.customizationsList.length).toBe(0);
		});

	});

	describe('addMoreCustomizationItems', () => {

		it('after called array ctrl.customizationsOnPage should contain objects return by REST service', () => {

			// when
			personalizationsmarteditCustomizeViewComponent.$onInit();
			personalizationsmarteditCustomizeViewComponent.statusFilter = "all";
			personalizationsmarteditCustomizeViewComponent.addMoreCustomizationItems();

			// then
			expect(personalizationsmarteditCustomizeViewComponent.customizationsList).toBeDefined();
			expect(personalizationsmarteditCustomizeViewComponent.customizationsList.length).toBe(2);
			expect(personalizationsmarteditCustomizeViewComponent.customizationsList).toContain(mockCustomization);
		});

	});

	describe('onInit', () => {

		it('should call proper service on init', () => {

			personalizationsmarteditCustomizeViewComponent.$onInit();

			expect(personalizationsmarteditCustomizeViewComponent.catalogFilter).toBe("catalogMock");
			expect(personalizationsmarteditCustomizeViewComponent.pageFilter).toBe("pageMock");
			expect(personalizationsmarteditCustomizeViewComponent.statusFilter).toBe("statusMock");
			expect(personalizationsmarteditCustomizeViewComponent.nameFilter).toBe("nameMock");
		});

	});

	describe('onDestroy', () => {

		it('should call proper service on destroy', () => {
			const mockFilter = {
				catalogFilter: "catalog",
				pageFilter: "page",
				statusFilter: "status",
				nameFilter: "name"
			};
			personalizationsmarteditCustomizeViewComponent.catalogFilter = mockFilter.catalogFilter;
			personalizationsmarteditCustomizeViewComponent.pageFilter = mockFilter.pageFilter;
			personalizationsmarteditCustomizeViewComponent.statusFilter = mockFilter.statusFilter;
			personalizationsmarteditCustomizeViewComponent.nameFilter = mockFilter.nameFilter;

			personalizationsmarteditCustomizeViewComponent.$onDestroy();

			expect(personalizationsmarteditContextService.setCustomizeFiltersState).toHaveBeenCalledWith(mockFilter);
		});

	});

});
