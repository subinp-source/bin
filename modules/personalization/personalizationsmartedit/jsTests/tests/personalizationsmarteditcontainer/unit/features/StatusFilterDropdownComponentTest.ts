import 'jasmine';
import {promiseHelper} from "testhelpers";
import {PersonalizationsmarteditUtils} from "../../../../../web/features/personalizationcommons/PersonalizationsmarteditUtils";
import {StatusFilterDropdownComponent} from "../../../../../web/features/personalizationsmarteditcontainer/commonComponents/StatusFilterDropdownComponent";

describe('StatusFilterDropdownComponent', () => {
	const $q = promiseHelper.$q();
	let personalizationsmarteditUtils: PersonalizationsmarteditUtils;
	let statusFilterDropdownComponent: StatusFilterDropdownComponent;

	beforeEach(() => {
		const translate = jasmine.createSpy('translate');
		const PERSONALIZATION_MODEL_STATUS_CODES = jasmine.createSpyObj('PERSONALIZATION_MODEL_STATUS_CODES', ['ENABLED', 'DISABLED']);
		const PERSONALIZATION_VIEW_STATUS_MAPPING_CODES = jasmine.createSpyObj('PERSONALIZATION_VIEW_STATUS_MAPPING_CODES', ['ENABLED', 'DISABLED']);
		const PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING = jasmine.createSpyObj('PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING', ['A', 'B']);
		const l10nFilter = jasmine.createSpyObj('l10nFilter', ['debug']);
		const catalogService = jasmine.createSpyObj('catalogService', ['debug']);

		personalizationsmarteditUtils = new PersonalizationsmarteditUtils(
			$q,
			translate,
			l10nFilter,
			PERSONALIZATION_MODEL_STATUS_CODES,
			PERSONALIZATION_VIEW_STATUS_MAPPING_CODES,
			PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING,
			catalogService
		);
		statusFilterDropdownComponent = new StatusFilterDropdownComponent($q, personalizationsmarteditUtils);
	});

	describe('Component API', () => {

		it('should have proper api before initialized', () => {
			expect(statusFilterDropdownComponent.items).not.toBeDefined();
			expect(statusFilterDropdownComponent.selectedId).not.toBeDefined();
			expect(statusFilterDropdownComponent.onSelectCallback).not.toBeDefined();
			expect(statusFilterDropdownComponent.$onInit).toBeDefined();
			expect(statusFilterDropdownComponent.onChange).toBeDefined();
			expect(statusFilterDropdownComponent.fetchStrategy).toBeDefined();
		});

		it('should have proper api after initialized', () => {
			statusFilterDropdownComponent.onSelectCallback = (): any => undefined;
			statusFilterDropdownComponent.$onInit();
			expect(statusFilterDropdownComponent.items.length).toBe(3);
			expect(statusFilterDropdownComponent.selectedId).toBe(statusFilterDropdownComponent.items[0].id);
			expect(statusFilterDropdownComponent.$onInit).toBeDefined();
			expect(statusFilterDropdownComponent.onChange).toBeDefined();
			expect(statusFilterDropdownComponent.fetchStrategy).toBeDefined();
		});

	});

});
