import 'jasmine';
import {PersonalizationsmarteditSharedSlotDecoratorController} from "../../../../../web/features/personalizationsmartedit/sharedSlotDecorator/PersonalizationsmarteditSharedSlotDecoratorModule";

describe('PersonalizationsmarteditSharedSlotDecoratorController', () => {

	let personalizationsmarteditSharedSlotDecoratorController: PersonalizationsmarteditSharedSlotDecoratorController;
	let slotSharedService: jasmine.SpyObj<any>;
	let $element: any;

	beforeEach(() => {

		slotSharedService = jasmine.createSpyObj('slotSharedService', ['isSlotShared']);
		$element = jasmine.createSpyObj('$element', ['offset', 'find']);

		personalizationsmarteditSharedSlotDecoratorController = new PersonalizationsmarteditSharedSlotDecoratorController(
			slotSharedService,
			$element
		);

	});

	describe('Controller API', () => {
		it('should have proper api when initialized', () => {
			expect(personalizationsmarteditSharedSlotDecoratorController.positionPanel).toBeDefined();
			expect(personalizationsmarteditSharedSlotDecoratorController.$onInit).toBeDefined();
			expect(personalizationsmarteditSharedSlotDecoratorController.$onChanges).toBeDefined();
		});
	});

});