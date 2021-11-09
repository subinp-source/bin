import 'jasmine';
import {PersonalizationsmarteditMessageHandler} from "../../../../../../../../web/features/personalizationcommons/PersonalizationsmarteditMessageHandler";

describe('PersonalizationsmarteditMessageHandler', () => {

	// ======= Injected mocks =======
	let alertService: jasmine.SpyObj<any>;

	// Service being tested
	let personalizationsmarteditMessageHandler: PersonalizationsmarteditMessageHandler;

	beforeEach(() => {
		alertService = jasmine.createSpyObj('alertService', ['showInfo', 'showDanger', 'showWarning', 'showSuccess']);
		alertService.showInfo.and.callThrough();
		alertService.showDanger.and.callThrough();
		alertService.showWarning.and.callThrough();
		alertService.showSuccess.and.callThrough();

		personalizationsmarteditMessageHandler = new PersonalizationsmarteditMessageHandler(alertService);
	});

	describe('sendInformation', function() {

		it('should be defined', function() {
			expect(personalizationsmarteditMessageHandler.sendInformation).toBeDefined();
		});

		it('properly forward message to alertService', function() {
			// when
			personalizationsmarteditMessageHandler.sendInformation("test message");
			// then
			expect(alertService.showInfo).toHaveBeenCalledWith("test message");
		});

	});

	describe('sendError', function() {

		it('should be defined', function() {
			expect(personalizationsmarteditMessageHandler.sendError).toBeDefined();
		});

		it('properly forward message to alertService', function() {
			// when
			personalizationsmarteditMessageHandler.sendError("test error");
			// then
			expect(alertService.showDanger).toHaveBeenCalledWith("test error");
		});

	});

	describe('sendWarning', function() {

		it('should be defined', function() {
			expect(personalizationsmarteditMessageHandler.sendWarning).toBeDefined();
		});

		it('properly forward message to alertService', function() {
			// when
			personalizationsmarteditMessageHandler.sendWarning("test warning");
			// then
			expect(alertService.showWarning).toHaveBeenCalledWith("test warning");
		});

	});

	describe('sendSuccess', function() {

		it('should be defined', function() {
			expect(personalizationsmarteditMessageHandler.sendSuccess).toBeDefined();
		});

		it('properly forward message to alertService', function() {
			// when
			personalizationsmarteditMessageHandler.sendSuccess("test success");
			// then
			expect(alertService.showSuccess).toHaveBeenCalledWith("test success");
		});

	});

});
