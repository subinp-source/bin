import 'jasmine';
import {PersonalizationsmarteditScrollZoneComponent} from "../../../../../../../../web/features/personalizationcommons/personalizationsmarteditScrollZone/PersonalizationsmarteditScrollZoneComponent";

describe('PersonalizationsmarteditScrollZoneComponent', () => {

	const yjQuery: any = jasmine.createSpy('yjQuery');
	const $scope: any = jasmine.createSpy('$scope');
	const $timeout: any = jasmine.createSpy('$timeout');
	const $compile: any = jasmine.createSpy('$compile');

	let scrollZoneComponent: PersonalizationsmarteditScrollZoneComponent;

	// === SETUP ===
	beforeEach(() => {
		scrollZoneComponent = new PersonalizationsmarteditScrollZoneComponent($scope, $timeout, $compile, yjQuery);
	});

	describe('Component API', () => {

		it('should have proper api', () => {

			expect(scrollZoneComponent.scrollZoneTop).toBe(true);
			expect(scrollZoneComponent.scrollZoneBottom).toBe(true);
			expect(scrollZoneComponent.start).toBe(false);
			expect(scrollZoneComponent.elementToScroll).toEqual(null);
			expect(scrollZoneComponent.scrollZoneVisible).toBe(false);
			expect(scrollZoneComponent.stopScroll).toBeDefined();
			expect(scrollZoneComponent.scrollTop).toBeDefined();
			expect(scrollZoneComponent.scrollBottom).toBeDefined();
			expect(scrollZoneComponent.$onInit).toBeDefined();
			expect(scrollZoneComponent.$onDestroy).toBeDefined();
			expect(scrollZoneComponent.$onChanges).toBeDefined();
		});

	});

	describe('$onChanges', function() {

		it('should be defined', function() {
			const ctrl = new PersonalizationsmarteditScrollZoneComponent(null, null, null, null);
			expect(ctrl.$onChanges).toBeDefined();
		});

		it('should not set properties if called without parameters', function() {
			// given
			scrollZoneComponent.start = false;
			scrollZoneComponent.scrollZoneTop = false;
			scrollZoneComponent.scrollZoneBottom = false;
			// when
			scrollZoneComponent.$onChanges({});

			// then
			expect(scrollZoneComponent.start).toBe(false);
			expect(scrollZoneComponent.scrollZoneTop).toBe(false);
			expect(scrollZoneComponent.scrollZoneBottom).toBe(false);
		});

		it('should properly set properties if called with parameters', function() {
			// given
			scrollZoneComponent.start = false;
			scrollZoneComponent.scrollZoneTop = false;
			scrollZoneComponent.scrollZoneBottom = false;
			const changes = {
				scrollZoneVisible: {
					currentValue: true
				}
			};
			// when
			scrollZoneComponent.$onChanges(changes);

			// then
			expect(scrollZoneComponent.start).toBe(true);
			expect(scrollZoneComponent.scrollZoneTop).toBe(true);
			expect(scrollZoneComponent.scrollZoneBottom).toBe(true);
		});
	});
});
