import 'jasmine';
import {TriggerTabService} from '../../../../../web/features/personalizationsmarteditcontainer/management/manageCustomizationView/multipleTriggersComponent/TriggerTabService';
import {ITriggerTab} from '../../../../../web/features/personalizationsmarteditcontainer/management/manageCustomizationView/multipleTriggersComponent/ITriggerTab';

describe('TriggerTabService', () => {

	// Service being tested
	let triggerService: TriggerTabService;

	// === SETUP ===
	beforeEach(() => {
		triggerService = new TriggerTabService();
	});

	it('Public API', () => {
		expect(triggerService.getTriggersTabs).toBeDefined();
		expect(triggerService.addTriggerTab).toBeDefined();
		expect(triggerService.removeTriggerTab).toBeDefined();
		expect(triggerService.getTriggerDataState).toBeDefined();
	});

	describe('getTriggersTabs', () => {

		it('should be defined', () => {
			expect(triggerService.getTriggersTabs).toBeDefined();
		});

		it('should return empty array after init', () => {
			expect(triggerService.getTriggersTabs()).toEqual([]);
		});

	});

	describe('addTriggerTab', () => {

		it('should be defined', () => {
			expect(triggerService.addTriggerTab).toBeDefined();
		});

		it('should add only one trigger with same id', () => {
			const testTrigger: ITriggerTab = {
				id: "test",
				title: "testTitle",
				templateUrl: "testTemplate.html",
				isTriggerDefined: () => {
					return true;
				},
				isValidOrEmpty: () => {
					return true;
				}
			};
			const testTrigger2: ITriggerTab = {
				id: "test2",
				title: "testTitle2",
				templateUrl: "testTemplate2.html",
				isTriggerDefined: () => {
					return true;
				},
				isValidOrEmpty: () => {
					return true;
				}
			};
			triggerService.addTriggerTab(testTrigger);
			triggerService.addTriggerTab(testTrigger);
			triggerService.addTriggerTab(testTrigger2);

			expect(triggerService.getTriggersTabs().length).toBe(2);
			expect(triggerService.getTriggersTabs()[0]).toBe(testTrigger);
			expect(triggerService.getTriggersTabs()[1]).toBe(testTrigger2);
		});

	});

	describe('removeTriggerTab', () => {

		it('should be defined', () => {
			expect(triggerService.removeTriggerTab).toBeDefined();
		});

		it('should add only one trigger with same id', () => {
			const testTrigger: ITriggerTab = {
				id: "test",
				title: "testTitle",
				templateUrl: "testTemplate.html",
				isTriggerDefined: () => {
					return true;
				},
				isValidOrEmpty: () => {
					return true;
				}
			};
			const testTrigger2: ITriggerTab = {
				id: "test2",
				title: "testTitle2",
				templateUrl: "testTemplate2.html",
				isTriggerDefined: () => {
					return true;
				},
				isValidOrEmpty: () => {
					return true;
				}
			};
			const testTrigger3: ITriggerTab = {
				id: "test3",
				title: "testTitle3",
				templateUrl: "testTemplate3.html",
				isTriggerDefined: () => {
					return true;
				},
				isValidOrEmpty: () => {
					return true;
				}
			};
			triggerService.addTriggerTab(testTrigger);
			triggerService.addTriggerTab(testTrigger2);
			triggerService.addTriggerTab(testTrigger3);

			triggerService.removeTriggerTab(testTrigger2);

			expect(triggerService.getTriggersTabs().length).toBe(2);
			expect(triggerService.getTriggersTabs()[0]).toBe(testTrigger);
			expect(triggerService.getTriggersTabs()[1]).toBe(testTrigger3);
		});

	});

	describe('getTriggerDataState', () => {

		it('should be defined', () => {
			expect(triggerService.getTriggerDataState).toBeDefined();
		});

	});

});
