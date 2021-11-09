import 'jasmine';
import {promiseHelper} from "testhelpers";
import {CatalogFilterDropdownComponent} from "../../../../../web/features/personalizationsmarteditcontainer/commonComponents/CatalogFilterDropdownComponent";

describe('CatalogFilterDropdownComponent', () => {
	const $q = promiseHelper.$q();
	let PERSONALIZATION_CATALOG_FILTER: jasmine.SpyObj<any>;
	let catalogFilterDropdownComponent: CatalogFilterDropdownComponent;

	beforeEach(() => {
		PERSONALIZATION_CATALOG_FILTER = jasmine.createSpyObj('PERSONALIZATION_CATALOG_FILTER', ['ALL', 'CURRENT', 'PARENTS']);
		catalogFilterDropdownComponent = new CatalogFilterDropdownComponent($q, PERSONALIZATION_CATALOG_FILTER);
	});

	describe('Component API', () => {

		it('should have proper api before initialized', () => {
			expect(catalogFilterDropdownComponent.items).not.toBeDefined();
			expect(catalogFilterDropdownComponent.selectedId).not.toBeDefined();
			expect(catalogFilterDropdownComponent.onSelectCallback).not.toBeDefined();
			expect(catalogFilterDropdownComponent.$onInit).toBeDefined();
			expect(catalogFilterDropdownComponent.onChange).toBeDefined();
			expect(catalogFilterDropdownComponent.fetchStrategy).toBeDefined();
		});

		it('should have proper api after initialized', () => {
			catalogFilterDropdownComponent.onSelectCallback = (): any => undefined;
			catalogFilterDropdownComponent.$onInit();
			expect(catalogFilterDropdownComponent.items.length).toBe(3);
			expect(catalogFilterDropdownComponent.selectedId).toBe(catalogFilterDropdownComponent.items[1].id);
			expect(catalogFilterDropdownComponent.$onInit).toBeDefined();
			expect(catalogFilterDropdownComponent.onChange).toBeDefined();
			expect(catalogFilterDropdownComponent.fetchStrategy).toBeDefined();
		});

	});

});
