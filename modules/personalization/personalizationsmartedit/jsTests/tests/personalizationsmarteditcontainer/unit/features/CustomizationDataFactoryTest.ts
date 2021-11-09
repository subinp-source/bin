import 'jasmine';
import {promiseHelper} from "testhelpers";
import {CustomizationDataFactory} from "../../../../../web/features/personalizationsmarteditcontainer/dataFactory/CustomizationDataFactory";

describe('CustomizationDataFactory', () => {

	const $q = promiseHelper.$q();
	let personalizationsmarteditRestService: jasmine.SpyObj<any>;
	let personalizationSmartEditUtils: jasmine.SpyObj<any>;
	let customizationDataFactory: CustomizationDataFactory;

	beforeEach(() => {
		personalizationsmarteditRestService = jasmine.createSpyObj('personalizationsmarteditRestService', ['getCustomizations']);
		personalizationsmarteditRestService.getCustomizations.and.returnValue($q.defer().promise);
		personalizationSmartEditUtils = jasmine.createSpyObj('personalizationSmartEditUtils', ['uniqueArray']);

		customizationDataFactory = new CustomizationDataFactory(
			personalizationsmarteditRestService,
			personalizationSmartEditUtils
		);

		spyOn(customizationDataFactory, 'getCustomizations').and.callThrough();
	});

	describe('getCustomizations', () => {
		it('should be defined', () => {
			expect(customizationDataFactory.getCustomizations).toBeDefined();
		});

		it('should pass proper object to personalizationsmarteditRestService', () => {
			// when
			customizationDataFactory.updateData({}, () => "mockSuccessCallbackFunction", () => "mockErrorCallbackFunction");
			customizationDataFactory.getCustomizations({});
			// then
			expect(personalizationsmarteditRestService.getCustomizations).toHaveBeenCalledWith({});
		});
	});

	describe('updateData', () => {
		it('should be defined', () => {
			expect(customizationDataFactory.updateData).toBeDefined();
		});

		// when
		it('should en up with calling getCustomizations', () => {
			customizationDataFactory.updateData({}, () => "mockSuccessCallbackFunction", () => "mockErrorCallbackFunction");
			// then
			expect(customizationDataFactory.getCustomizations).toHaveBeenCalled();
		});
	});

	describe('refreshData', () => {
		it('should be defined', () => {
			expect(customizationDataFactory.refreshData).toBeDefined();
		});
	});

	describe('resetData', () => {
		it('should be defined', () => {
			expect(customizationDataFactory.resetData).toBeDefined();
		});
	});


});
