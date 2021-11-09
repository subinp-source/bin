import 'jasmine';
import {promiseHelper} from "testhelpers";
import {PersonalizationsmarteditManager} from "../../../../../web/features/personalizationsmarteditcontainer/management/manageCustomizationView/PersonalizationsmarteditManager";

describe('PersonalizationsmarteditManager', () => {

	const $q = promiseHelper.$q();
	let modalService: jasmine.SpyObj<any>;
	let MODAL_BUTTON_STYLES: jasmine.SpyObj<any>;
	let CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS: jasmine.SpyObj<any>;
	let personalizationsmarteditManager: PersonalizationsmarteditManager;

	// === SETUP ===
	beforeEach(() => {
		modalService = jasmine.createSpyObj('modalService', ['open']);
		MODAL_BUTTON_STYLES = jasmine.createSpyObj('MODAL_BUTTON_STYLES', ['DEFAULT', 'PRIMARY', 'SECONDARY']);
		CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS = jasmine.createSpyObj('CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS', ['CONFIRM_OK', 'CONFIRM_CANCEL', 'CONFIRM_NEXT']);

		modalService.open.and.callFake(() => {
			const deferred = $q.defer();
			deferred.resolve();
			return deferred.promise;
		});

		personalizationsmarteditManager = new PersonalizationsmarteditManager(
			modalService,
			MODAL_BUTTON_STYLES,
			CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS
		);

	});

	describe('openCreateCustomizationModal', () => {

		it('should be defined', () => {
			expect(personalizationsmarteditManager.openCreateCustomizationModal).toBeDefined();
		});

		it('GIVEN that modal for creating customization is open, proper functions should be called', () => {
			personalizationsmarteditManager.openCreateCustomizationModal();
			expect(modalService.open).toHaveBeenCalled();
		});

	});

	describe('openEditCustomizationModal', () => {

		it('should be defined', () => {
			expect(personalizationsmarteditManager.openEditCustomizationModal).toBeDefined();
		});

		it('GIVEN that modal for editing customization is open, proper functions should be called', () => {
			personalizationsmarteditManager.openCreateCustomizationModal();
			expect(modalService.open).toHaveBeenCalled();
		});

	});


});
