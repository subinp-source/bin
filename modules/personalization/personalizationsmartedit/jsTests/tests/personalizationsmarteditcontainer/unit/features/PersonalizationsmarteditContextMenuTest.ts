import 'jasmine';
import * as lo from 'lodash';
import {promiseHelper} from 'testhelpers';
import * as angular from 'angular';
import {Observable} from 'rxjs';

import {PersonalizationsmarteditContextMenuServiceProxy} from '../../../../../web/features/personalizationsmarteditcontainer/contextMenu/PersonalizationsmarteditContextMenuServiceOuterProxy';
import {ContextMenuDeleteActionControllerFactory} from '../../../../../web/features/personalizationsmarteditcontainer/contextMenu/PersonalizationsmarteditContextMenuDeleteActionControllerFactory';


describe('personalizationsmarteditContextMenu', () => {

	const $q = promiseHelper.$q();
	let $rootScope: angular.IRootScopeService;
	let $timeout: jasmine.SpyObj<any>;
	const lodash: lo.LoDashStatic = (window as any).smarteditLodash;
	let $translate: jasmine.SpyObj<angular.translate.ITranslateService>;

	let modalManager: jasmine.SpyObj<any>;
	let slotRestrictionsService: jasmine.SpyObj<any>;
	let modalService: jasmine.SpyObj<any>;
	let renderService: jasmine.SpyObj<any>;
	let editorModalService: jasmine.SpyObj<any>;
	let personalizationsmarteditContextService: jasmine.SpyObj<any>;
	let personalizationsmarteditRestService: jasmine.SpyObj<any>;
	let personalizationsmarteditMessageHandler: jasmine.SpyObj<any>;

	let MODAL_BUTTON_ACTIONS: jasmine.SpyObj<any>;
	let MODAL_BUTTON_STYLES: jasmine.SpyObj<any>;

	let personalizationsmarteditContextMenuProxy: PersonalizationsmarteditContextMenuServiceProxy;

	const mockCombinedView = {
		enabled: false
	};
	const mockVariation1 = {
		code: "variation1"
	};
	const mockVariation2 = {
		code: "variation2"
	};
	const mockCustomization = {
		code: "customization1",
		variations: [mockVariation1, mockVariation2]
	};
	const mockCustomize = {
		enabled: false,
		selectedCustomization: mockCustomization.code,
		selectedVariations: [mockVariation1, mockVariation2],
		selectedComponents: ['Section2ASlotHomepageCxContainer', 'cxContainer_NavigationBarSlot_ApparelDECategoryNavComponent_09fe54c5-ea25-421a-95f3-8647f4938383']
	};
	const PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING = [
		{
			borderClass: 'personalizationsmarteditComponentSelected0',
			listClass: 'personalizationsmarteditComponentSelectedList0'
		},
		{
			borderClass: 'personalizationsmarteditComponentSelected1',
			listClass: 'personalizationsmarteditComponentSelectedList1'
		}
	];
	const compType1 = {
		category: "COMPONENT",
		code: "componentType1"
	};
	const compType2 = {
		category: "COMPONENT",
		code: "componentType2"
	};
	const compType3 = {
		category: "COMPONENT",
		code: "componentType3"
	};
	const mockSlotRestrictions = ["componentType1", "componentType2"];
	const restrictedMockComponentTypeList = [compType1, compType2];
	const mockComponentTypeList = [compType1, compType2, compType3];
	const mockComponentList = [{
		typeCode: "componentType1",
		uid: "component1"
	}, {
		typeCode: "componentType1",
		uid: "component2"
	}];

	beforeEach(angular.mock.inject((_$rootScope_: angular.IRootScopeService) => {
		$rootScope = _$rootScope_;

		$timeout = jasmine.createSpy('$timeout');
		$timeout.and.callFake((callback: any, timeout: number) => {
			callback();
		});
		$translate = jasmine.createSpyObj('$translate', ['instant']);

		modalManager = jasmine.createSpyObj('modalManager', ['setButtonHandler', 'enableButton', 'disableButton']);
		modalManager.setButtonHandler.and.callThrough();

		slotRestrictionsService = jasmine.createSpyObj('slotRestrictionsService', ['getSlotRestrictions']);
		slotRestrictionsService.getSlotRestrictions.and.callFake((slotId: any) => {
			const deferred = $q.defer();
			deferred.resolve(
				mockSlotRestrictions
			);
			return deferred.promise;
		});

		modalService = jasmine.createSpyObj('modalService', ['open']);
		modalService.open.and.callFake(() => {
			return {
				afterClosed: new Observable()
			};
		});
		editorModalService = jasmine.createSpyObj('editorModalService', ['open']);
		editorModalService.open.and.callFake(() => {
			const deferred = $q.defer();
			deferred.resolve();
			return deferred.promise;
		});

		personalizationsmarteditContextService = jasmine.createSpyObj('personalizationsmarteditContextService',
			['getCombinedView', 'setCombinedView', 'getCustomize', 'setCustomize']);
		personalizationsmarteditContextService.getCombinedView.and.callFake(() => {
			return mockCombinedView;
		});
		personalizationsmarteditContextService.getCustomize.and.callFake(() => {
			return mockCustomize;
		});

		personalizationsmarteditRestService = jasmine.createSpyObj('personalizationsmarteditRestService', ['getActions', 'getCustomization', 'getNewComponentTypes', 'getComponents', 'getComponent']);
		personalizationsmarteditRestService.getComponents.and.callFake(() => {
			const deferred = $q.defer();
			deferred.resolve({
				response: mockComponentList
			});
			return deferred.promise;
		});
		personalizationsmarteditRestService.getNewComponentTypes.and.callFake(() => {
			const deferred = $q.defer();
			deferred.resolve({
				componentTypes: mockComponentTypeList
			});
			return deferred.promise;
		});
		personalizationsmarteditRestService.getComponent.and.callFake((componentId: any) => { // jshint ignore:line
			const deferred = $q.defer();
			deferred.resolve({});
			return deferred.promise;
		});
		personalizationsmarteditRestService.getCustomization.and.callFake((customizationCode: any) => {
			const deferred = $q.defer();
			deferred.resolve(
				mockCustomization
			);
			return deferred.promise;
		});

		personalizationsmarteditMessageHandler = jasmine.createSpyObj('personalizationsmarteditMessageHandler', ['sendError']);
		renderService = jasmine.createSpyObj('renderService', ['renderSlots']);

		MODAL_BUTTON_ACTIONS = jasmine.createSpyObj('MODAL_BUTTON_ACTIONS', ['NONE', 'CLOSE', 'DISMISS']);
		MODAL_BUTTON_STYLES = jasmine.createSpyObj('MODAL_BUTTON_STYLES', ['DEFAULT', 'PRIMARY', 'SECONDARY']);

		personalizationsmarteditContextMenuProxy = new PersonalizationsmarteditContextMenuServiceProxy(
			modalService,
			renderService,
			editorModalService,
			personalizationsmarteditContextService,
			personalizationsmarteditRestService,
			personalizationsmarteditMessageHandler,
			lodash,
			$translate,
			MODAL_BUTTON_ACTIONS,
			MODAL_BUTTON_STYLES
		);
	});

	describe('proxy', () => {
		describe('openDeleteAction', () => {
			it('should be defined', () => {
				expect(personalizationsmarteditContextMenuProxy.openDeleteAction).toBeDefined();
			});
		});

		describe('openAddAction', () => {
			it('should be defined', () => {
				expect(personalizationsmarteditContextMenuProxy.openAddAction).toBeDefined();
			});
			it('is called proper functions should be called', () => {
				// when
				personalizationsmarteditContextMenuProxy.openAddAction({});
				// then
				expect(modalService.open).toHaveBeenCalled();
			});
		});

		describe('openEditAction', () => {
			it('should be defined', () => {
				expect(personalizationsmarteditContextMenuProxy.openEditAction).toBeDefined();
			});
			it('is called proper functions should be called', () => {
				// when
				personalizationsmarteditContextMenuProxy.openEditAction({});
				// then
				expect(modalService.open).toHaveBeenCalled();
				expect($rootScope.$$watchers).toEqual(null);
			});
		});
	});

	describe('modal', () => {
		describe('modalDeleteActionController', () => {
			const mockConfig = {
				editEnabled: false,
				selectedVariationCode: 'variation1'
			};
			it('is instantiated scope  properly initialized', () => {
				const deleteActionControllerFactory = ContextMenuDeleteActionControllerFactory(mockConfig);
				const deleteActionController = new deleteActionControllerFactory(
					$q,
					modalManager,
					personalizationsmarteditRestService
				);
				expect(deleteActionController.modalManager.setButtonHandler).toHaveBeenCalled();
			});

		});

	});

});
