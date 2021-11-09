import 'jasmine';
import {PersonalizationsmarteditCommerceCustomizationService} from "../../../../../../../../web/features/personalizationcommons/PersonalizationsmarteditCommerceCustomizationService";

describe('personalizationsmarteditCommerceCustomizationService', function() {

	const type1 = {
		type: 'type1',
		confProperty: 'type1.enabled'
	};

	const confWithType1Enabled = {
		'type1.enabled': true
	};

	const confWithType1Disabled = {
		'type1.enabled': false
	};

	let personalizationsmarteditCommerceCustomizationService;

	// === SETUP ===
	beforeEach(() => {
		personalizationsmarteditCommerceCustomizationService = new PersonalizationsmarteditCommerceCustomizationService();
	});

	describe('getAvailableTypes', () => {

		it('should be defined', () => {
			expect(personalizationsmarteditCommerceCustomizationService.getAvailableTypes).toBeDefined();
		});

		it('should be empty when configuration is null', () => {
			expect(personalizationsmarteditCommerceCustomizationService.getAvailableTypes(null)).toEqual([]);
		});

		it('should be empty when configuration is undefined', () => {
			expect(personalizationsmarteditCommerceCustomizationService.getAvailableTypes(undefined)).toEqual([]);
		});

		it('should be empty when types are empty', () => {
			expect(personalizationsmarteditCommerceCustomizationService.getAvailableTypes(confWithType1Enabled)).toEqual([]);
		});

		it('should return type when it is enabled in configuration', () => {
			// given
			personalizationsmarteditCommerceCustomizationService.registerType(type1);

			// when
			const availabelTypes = personalizationsmarteditCommerceCustomizationService.getAvailableTypes(confWithType1Enabled);

			// then
			expect(availabelTypes).toContain(type1);
		});

		it('should not return type when it is disabled in configuration', () => {
			// given
			personalizationsmarteditCommerceCustomizationService.registerType(type1);

			// when
			const availabelTypes = personalizationsmarteditCommerceCustomizationService.getAvailableTypes(confWithType1Disabled);

			// then
			expect(availabelTypes).toEqual([]);
		});

		it('should return only enabled types', () => {
			// given
			const configuration = {
				'type1.enabled': false,
				'type2.enabled': true,
				'type3.enabled': true,
				'type4.enabled': false
			};
			const type2 = {
				type: 'type2',
				confProperty: 'type2.enabled'
			};
			const type3 = {
				type: 'type3',
				confProperty: 'type3.enabled'
			};
			const type4 = {
				type: 'type4',
				confProperty: 'type4.enabled'
			};
			personalizationsmarteditCommerceCustomizationService.registerType(type1);
			personalizationsmarteditCommerceCustomizationService.registerType(type2);
			personalizationsmarteditCommerceCustomizationService.registerType(type3);
			personalizationsmarteditCommerceCustomizationService.registerType(type4);

			// when
			const availabelTypes = personalizationsmarteditCommerceCustomizationService.getAvailableTypes(configuration);

			// then
			expect(availabelTypes.length).toBe(2);
			expect(availabelTypes).toContain(type2);
			expect(availabelTypes).toContain(type3);
		});
	});

	describe('isCommerceCustomizationEnabled', () => {
		it('should be defined', () => {
			expect(personalizationsmarteditCommerceCustomizationService.isCommerceCustomizationEnabled).toBeDefined();
		});

		it('should return false when configuration is null', () => {
			expect(personalizationsmarteditCommerceCustomizationService.isCommerceCustomizationEnabled(null)).toBe(false);
		});

		it('should return false when configuration is undefined', function() {
			expect(personalizationsmarteditCommerceCustomizationService.isCommerceCustomizationEnabled(undefined)).toBe(false);
		});

		it('should return false when types are empty', () => {
			expect(personalizationsmarteditCommerceCustomizationService.isCommerceCustomizationEnabled(confWithType1Enabled)).toBe(false);
		});

		it('should return true when at least one type is enabled', () => {
			// given
			personalizationsmarteditCommerceCustomizationService.registerType(type1);

			// when
			const enabled = personalizationsmarteditCommerceCustomizationService.isCommerceCustomizationEnabled(confWithType1Enabled);

			// then
			expect(enabled).toBe(true);
		});

		it('should return false when there is no enabled type', () => {
			// given
			personalizationsmarteditCommerceCustomizationService.registerType(type1);

			// when
			const enabled = personalizationsmarteditCommerceCustomizationService.isCommerceCustomizationEnabled(confWithType1Disabled);

			// then
			expect(enabled).toBe(false);
		});

	});
});
