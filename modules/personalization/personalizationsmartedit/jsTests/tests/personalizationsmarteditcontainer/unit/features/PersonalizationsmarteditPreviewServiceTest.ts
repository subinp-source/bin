import 'jasmine';
import {promiseHelper} from "testhelpers";
import {PersonalizationsmarteditPreviewService} from "../../../../../web/features/personalizationsmarteditcontainer/service/PersonalizationsmarteditPreviewService";

describe('PersonalizationsmarteditPreviewService', function() {
	// Mock
	const $q = promiseHelper.$q();
	let experienceService: jasmine.SpyObj<any>;

	// Service being tested
	let personalizationsmarteditPreviewService: PersonalizationsmarteditPreviewService;

	beforeEach(() => {
		experienceService = jasmine.createSpyObj('experienceService', ['setCurrentExperience', 'getCurrentExperience']);
		experienceService.getCurrentExperience.and.returnValue($q.defer().promise);
		experienceService.setCurrentExperience.and.returnValue($q.defer().promise);
		personalizationsmarteditPreviewService = new PersonalizationsmarteditPreviewService(experienceService);
		spyOn(personalizationsmarteditPreviewService, 'updatePreviewTicketWithVariations').and.callThrough();
	});

	describe('updatePreviewTicketWithVariations', function() {

		it('should be defined', function() {
			expect(personalizationsmarteditPreviewService.updatePreviewTicketWithVariations).toBeDefined();
		});

		it('should pass proper object to experienceService', function() {
			// when
			personalizationsmarteditPreviewService.updatePreviewTicketWithVariations(['variation1', 'variation2']);
			// then
			expect(experienceService.getCurrentExperience).toHaveBeenCalled();
		});

	});

	describe('removePersonalizationDataFromPreview', function() {

		it('should be defined', function() {
			expect(personalizationsmarteditPreviewService.removePersonalizationDataFromPreview).toBeDefined();
		});

		it('should call proper function with proper arguments', function() {
			// when
			personalizationsmarteditPreviewService.removePersonalizationDataFromPreview();
			// then
			expect(personalizationsmarteditPreviewService.updatePreviewTicketWithVariations).toHaveBeenCalledWith([]);
			expect(experienceService.getCurrentExperience).toHaveBeenCalled();
		});

	});

});
