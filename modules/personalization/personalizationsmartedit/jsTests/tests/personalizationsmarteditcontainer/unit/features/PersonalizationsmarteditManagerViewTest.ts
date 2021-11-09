import 'jasmine';
import {promiseHelper} from "testhelpers";
import {PersonalizationsmarteditManagerView} from '../../../../../web/features/personalizationsmarteditcontainer/management/managerView/PersonalizationsmarteditManagerViewService';


describe('PersonalizationsmarteditManagerView', () => {

	const $q = promiseHelper.$q();
	let modalService: jasmine.SpyObj<any>;
	let personalizationsmarteditManagerView: PersonalizationsmarteditManagerView;

	// === SETUP ===
	beforeEach(() => {
		modalService = jasmine.createSpyObj('modalService', ['open']);
		modalService.open.and.callFake(() => {
			const deferred = $q.defer();
			deferred.resolve();
			return deferred.promise;
		});

		personalizationsmarteditManagerView = new PersonalizationsmarteditManagerView(
			modalService
		);

	});

	describe('openManagerAction', () => {

		it('should be defined', () => {
			expect(personalizationsmarteditManagerView.openManagerAction).toBeDefined();
		});

		it('after called it is calling proper services', () => {
			personalizationsmarteditManagerView.openManagerAction();
			expect(personalizationsmarteditManagerView.modalService.open).toHaveBeenCalled();
		});

	});

});
