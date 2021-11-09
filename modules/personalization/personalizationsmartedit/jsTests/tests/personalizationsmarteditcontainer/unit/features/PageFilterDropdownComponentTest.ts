import 'jasmine';
import {promiseHelper} from "testhelpers";
import {PageFilterDropdownComponent} from "../../../../../web/features/personalizationsmarteditcontainer/commonComponents/PageFilterDropdownComponent";

describe('PageFilterDropdownComponent', () => {
	const $q = promiseHelper.$q();
	let PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER: jasmine.SpyObj<any>;
	let pageFilterDropdownComponent: PageFilterDropdownComponent;

	beforeEach(() => {
		PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER = jasmine.createSpyObj('PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER', ['ALL', 'ONLY_THIS_PAGE']);
		pageFilterDropdownComponent = new PageFilterDropdownComponent($q, PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER);
	});

	describe('Component API', () => {

		it('should have proper api before initialized', () => {
			expect(pageFilterDropdownComponent.items).not.toBeDefined();
			expect(pageFilterDropdownComponent.selectedId).not.toBeDefined();
			expect(pageFilterDropdownComponent.onSelectCallback).not.toBeDefined();
			expect(pageFilterDropdownComponent.$onInit).toBeDefined();
			expect(pageFilterDropdownComponent.onChange).toBeDefined();
			expect(pageFilterDropdownComponent.fetchStrategy).toBeDefined();
		});

		it('should have proper api after initialized', () => {
			pageFilterDropdownComponent.onSelectCallback = (): any => undefined;
			pageFilterDropdownComponent.$onInit();
			expect(pageFilterDropdownComponent.items.length).toBe(2);
			expect(pageFilterDropdownComponent.selectedId).toBe(pageFilterDropdownComponent.items[1].id);
			expect(pageFilterDropdownComponent.$onInit).toBeDefined();
			expect(pageFilterDropdownComponent.onChange).toBeDefined();
			expect(pageFilterDropdownComponent.fetchStrategy).toBeDefined();
		});

	});

});
