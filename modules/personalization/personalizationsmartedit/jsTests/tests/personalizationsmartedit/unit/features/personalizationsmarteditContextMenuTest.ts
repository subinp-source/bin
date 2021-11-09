import {coreAnnotationsHelper, promiseHelper} from 'testhelpers';
import * as lo from 'lodash';
import {PersonalizationsmarteditContextMenuServiceProxy} from "../../../../../web/features/personalizationsmartedit/contextMenu/PersonalizationsmarteditContextMenuServiceInnerProxy";
import {PersonalizationsmarteditContextualMenuService} from "../../../../../web/features/personalizationsmartedit/service/PersonalizationsmarteditContextualMenuService";
import {PersonalizationsmarteditComponentHandlerService} from "../../../../../web/features/personalizationsmartedit/service/PersonalizationsmarteditComponentHandlerService";

describe('personalizationsmarteditContextMenu', () => {

	let personalizationsmarteditContextMenu: jasmine.SpyObj<PersonalizationsmarteditContextMenuServiceProxy>;
	const $q = promiseHelper.$q();

	const mockConfigProperties = {
		name: "name",
		smarteditPersonalizationActionId: "actionId",
		smarteditPersonalizationVariationId: "variationCode",
		smarteditPersonalizationCustomizationId: "customizationCode",
		smarteditComponentUuid: "componentUuid",
		smarteditContainerSourceId: "containerSourceId",
		smarteditCatalogVersionUuid: "catalogName/Online",
		smarteditComponentType: "type"
	};
	const mockConfig = {
		componentAttributes: JSON.stringify(mockConfigProperties),
		componentId: "id",
		componentType: "type",
		containerId: "containerId",
		slotId: "slotId"
	};
	const mockCustomization1 = {
		code: "mockNameCustomization1",
		catalog: "catalogName",
		catalogVersion: "Online"
	};
	const mockCustomization2 = {
		code: "mockNameCustomization2",
		catalog: "catalogName2",
		catalogVersion: "Online"
	};
	const mockCustomization3 = {
		code: "customizationCode",
		catalog: "catalogName3",
		catalogVersion: "Online"
	};
	const mockCustomize = {
		selectedCustomization: mockCustomization1,
		selectedVariations: {
			code: "mockNameVariation"
		},
		selectedComponents: null
	};

	describe('inner proxy', () => {
		let personalizationsmarteditContextMenuProxy: PersonalizationsmarteditContextMenuServiceProxy;

		beforeEach(() => {
			coreAnnotationsHelper.init();
			personalizationsmarteditContextMenuProxy = new PersonalizationsmarteditContextMenuServiceProxy();
		});

		describe('openDeleteAction', () => {
			it('should be defined', () => {
				expect(personalizationsmarteditContextMenuProxy.openDeleteAction).toBeDefined();
			});
		});
		describe('openAddAction', () => {
			it('should be defined', () => {
				expect(personalizationsmarteditContextMenuProxy.openAddAction).toBeDefined();
			});
		});
		describe('openEditAction', () => {
			it('should be defined', () => {
				expect(personalizationsmarteditContextMenuProxy.openEditAction).toBeDefined();
			});
		});
		describe('openEditComponentAction', () => {
			it('should be defined', () => {
				expect(personalizationsmarteditContextMenuProxy.openEditComponentAction).toBeDefined();
			});
		});
	});

	describe('ContextualMenuService', () => {
		const lodash: lo.LoDashStatic = (window as any).smarteditLodash;
		let crossFrameEventService: jasmine.SpyObj<any>;
		let EVENTS: jasmine.SpyObj<any>;
		let personalizationsmarteditContextualMenuService: PersonalizationsmarteditContextualMenuService;
		let personalizationsmarteditContextService: jasmine.SpyObj<any>;
		let personalizationsmarteditUtils: jasmine.SpyObj<any>;
		let personalizationsmarteditComponentHandlerService: jasmine.SpyObj<PersonalizationsmarteditComponentHandlerService>;

		beforeEach(() => {
			crossFrameEventService = jasmine.createSpyObj('crossFrameEventService', ['subscribe']);
			EVENTS = jasmine.createSpyObj('EVENTS', ['PAGE_CHANGE', 'PAGE_UPDATED']);
			personalizationsmarteditContextService = jasmine.createSpyObj('personalizationsmarteditContextService', ['getCombinedView', 'getCustomize', 'isCurrentPageActiveWorkflowRunning']);
			personalizationsmarteditContextService.getCustomize.and.callFake(() => {
				return mockCustomize;
			});
			personalizationsmarteditContextService.getCombinedView.and.callFake(() => {
				return {
					enabled: false,
					customize: {
						selectedCustomization: mockCustomization2
					}
				};
			});
			personalizationsmarteditContextService.isCurrentPageActiveWorkflowRunning.and.callFake(() => {
				const deferred = $q.defer();
				deferred.resolve({});
				return deferred.promise;
			});

			personalizationsmarteditComponentHandlerService = jasmine.createSpyObj('personalizationsmarteditComponentHandlerService',
				['getParentSlotForComponent', 'getCatalogVersionUuid', 'getAllSlotsSelector', 'getFromSelector', 'getParentSlotIdForComponent']);
			personalizationsmarteditUtils = jasmine.createSpyObj('personalizationSmartEditUtils', ['isItemFromCurrentCatalog']);
			personalizationsmarteditContextMenu = jasmine.createSpyObj('personalizationsmarteditContextMenu', ['openDeleteAction', 'openAddAction', 'openEditAction', 'openEditComponentAction']);
			personalizationsmarteditContextualMenuService = new PersonalizationsmarteditContextualMenuService(
				personalizationsmarteditContextService,
				personalizationsmarteditComponentHandlerService,
				personalizationsmarteditUtils,
				personalizationsmarteditContextMenu,
				crossFrameEventService,
				EVENTS,
				lodash
			);
		});

		describe('openDeleteAction', () => {
			it('should be defined', () => {
				expect(personalizationsmarteditContextualMenuService.openDeleteAction).toBeDefined();
			});
			it('should call proper service with parameters if customization in context', () => {
				personalizationsmarteditContextualMenuService.openDeleteAction(mockConfig);
				expect(personalizationsmarteditContextMenu.openDeleteAction).toHaveBeenCalledWith({
					containerId: 'containerId',
					containerSourceId: 'containerSourceId',
					slotId: 'slotId',
					actionId: "actionId",
					selectedVariationCode: "variationCode",
					selectedCustomizationCode: "customizationCode",
					catalog: "catalogName",
					catalogVersion: "Online",
					componentCatalog: "catalogName",
					componentCatalogVersion: "Online",
					slotsToRefresh: []
				});
			});
			it('should call proper service with parameters if combined view enabled and customization in context', () => {
				personalizationsmarteditContextService.getCombinedView.and.callFake(() => {
					return {
						enabled: true,
						customize: {
							selectedCustomization: mockCustomization2
						}
					};
				});
				personalizationsmarteditContextualMenuService.openDeleteAction(mockConfig);
				expect(personalizationsmarteditContextMenu.openDeleteAction).toHaveBeenCalledWith({
					containerId: 'containerId',
					containerSourceId: 'containerSourceId',
					slotId: 'slotId',
					actionId: "actionId",
					selectedVariationCode: "variationCode",
					selectedCustomizationCode: "customizationCode",
					catalog: "catalogName2",
					catalogVersion: "Online",
					componentCatalog: "catalogName",
					componentCatalogVersion: "Online",
					slotsToRefresh: []
				});
			});
			it('should call proper service with parameters if combined view enabled and customization not in context', () => {
				personalizationsmarteditContextService.getCombinedView.and.callFake(() => {
					return {
						enabled: true,
						customize: {
							selectedCustomization: null
						},
						selectedItems: [{
							customization: mockCustomization2
						},
						{
							customization: mockCustomization3
						},
						]
					};
				});
				personalizationsmarteditContextualMenuService.openDeleteAction(mockConfig);
				expect(personalizationsmarteditContextMenu.openDeleteAction).toHaveBeenCalledWith({
					containerId: 'containerId',
					containerSourceId: 'containerSourceId',
					slotId: 'slotId',
					actionId: "actionId",
					selectedVariationCode: "variationCode",
					selectedCustomizationCode: "customizationCode",
					catalog: "catalogName3",
					catalogVersion: "Online",
					componentCatalog: "catalogName",
					componentCatalogVersion: "Online",
					slotsToRefresh: []
				});
			});
		});

		describe('openAddAction', () => {
			it('should be defined', () => {
				expect(personalizationsmarteditContextualMenuService.openAddAction).toBeDefined();
			});
			it('should call proper service with parameters', () => {
				personalizationsmarteditContextService.selectedCustomizations = {
					code: "mockNameCustomization",
				};
				personalizationsmarteditContextService.selectedVariations = {
					code: "mockNameVariation",
				};
				personalizationsmarteditComponentHandlerService.getParentSlotForComponent.and.callFake(() => {
					return {};
				});
				personalizationsmarteditComponentHandlerService.getCatalogVersionUuid.and.callFake(() => {
					return "slotCatalogName";
				});

				personalizationsmarteditContextualMenuService.openAddAction(mockConfig);

				expect(personalizationsmarteditContextMenu.openAddAction).toHaveBeenCalledWith({
					componentType: "type",
					componentId: "id",
					containerId: 'containerId',
					containerSourceId: 'containerSourceId',
					slotId: 'slotId',
					actionId: "actionId",
					selectedVariationCode: "mockNameVariation",
					selectedCustomizationCode: "mockNameCustomization1",
					catalog: "catalogName",
					slotCatalog: "slotCatalogName",
					componentCatalog: "catalogName",
					slotsToRefresh: ['slotId']
				});
			});
		});

		describe('openEditAction', () => {
			it('should be defined', () => {
				expect(personalizationsmarteditContextualMenuService.openEditAction).toBeDefined();
			});
			it('should call proper service', () => {
				personalizationsmarteditContextualMenuService.openEditAction(mockConfig);
				expect(personalizationsmarteditContextMenu.openEditAction).toHaveBeenCalledWith({
					componentType: "type",
					componentId: "id",
					containerId: 'containerId',
					containerSourceId: 'containerSourceId',
					slotId: 'slotId',
					actionId: "actionId",
					selectedVariationCode: "variationCode",
					selectedCustomizationCode: "customizationCode",
					componentUuid: "componentUuid",
					slotsToRefresh: []
				});
			});
		});

		describe('openEditComponentAction', () => {
			it('should be defined', () => {
				expect(personalizationsmarteditContextualMenuService.openEditComponentAction).toBeDefined();
			});
			it('should call proper service', () => {
				personalizationsmarteditContextualMenuService.openEditComponentAction(mockConfig);
				expect(personalizationsmarteditContextMenu.openEditComponentAction).toHaveBeenCalledWith({
					smarteditComponentType: "type",
					smarteditComponentUuid: "componentUuid",
					smarteditCatalogVersionUuid: "catalogName/Online"
				});
			});
		});
	});
});
