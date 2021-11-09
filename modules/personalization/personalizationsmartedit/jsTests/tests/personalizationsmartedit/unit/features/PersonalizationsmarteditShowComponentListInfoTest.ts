import 'jasmine';
import {promiseHelper} from "testhelpers";
import {IPermissionService} from 'smarteditcommons';
import {PersonalizationsmarteditContextService} from "../../../../../web/features/personalizationsmartedit/service/PersonalizationsmarteditContextServiceInner";
import {PersonalizationsmarteditContextualMenuService} from "../../../../../web/features/personalizationsmartedit/service/PersonalizationsmarteditContextualMenuService";
import {PersonalizationsmarteditComponentHandlerService} from "../../../../../web/features/personalizationsmartedit/service/PersonalizationsmarteditComponentHandlerService";
import {PersonalizationsmarteditShowComponentInfoListComponent} from "../../../../../web/features/personalizationsmartedit/contextMenu/ShowComponentInfoList/PersonalizationsmarteditShowComponentInfoList";

describe('showComponentInfoListModule', () => {

	// ======= Injected mocks =======
	const $q = promiseHelper.$q();
	let personalizationsmarteditContextService: PersonalizationsmarteditContextService;
	let personalizationsmarteditContextualMenuService: PersonalizationsmarteditContextualMenuService;
	let personalizationSmartEditUtils: jasmine.SpyObj<any>;
	let personalizationsmarteditRestService: jasmine.SpyObj<any>;
	let personalizationsmarteditMessageHandler: jasmine.SpyObj<any>;
	let $translate: jasmine.SpyObj<angular.translate.ITranslateService>;
	let personalizationsmarteditComponentHandlerService: jasmine.SpyObj<PersonalizationsmarteditComponentHandlerService>;
	let permissionService: jasmine.SpyObj<IPermissionService>;

	const CONTAINER_ID = '1234';

	const mockActions = {
		actions: [{
			actionCatalog: '1234',
			actionCatalogVersion: 'Staged'
		}, {
			actionCatalog: '1234',
			actionCatalogVersion: 'Online'
		}],
		pagination: {
			count: 2,
			page: 0,
			totalCount: 2,
			totalPages: 1
		}
	};

	// Service being tested
	let showComponentInfoListModule: PersonalizationsmarteditShowComponentInfoListComponent;


	beforeEach(() => {
		personalizationsmarteditContextService = jasmine.createSpyObj('personalizationsmarteditContextService', ['getSeData']);
		personalizationsmarteditContextualMenuService = jasmine.createSpyObj('personalizationsmarteditContextualMenuService', ['isPersonalizationAllowedInWorkflow', 'isContextualMenuInfoEnabled']);
		personalizationSmartEditUtils = jasmine.createSpyObj('personalizationSmartEditUtils', ['getAndSetCatalogVersionNameL10N', 'uniqueArray']);
		personalizationsmarteditRestService = jasmine.createSpyObj('personalizationsmarteditRestService', ['getCxCmsAllActionsForContainer']);
		personalizationsmarteditMessageHandler = jasmine.createSpyObj('personalizationsmarteditMessageHandler', ['sendError']);
		$translate = jasmine.createSpyObj('$translate', ['instant']);
		personalizationsmarteditComponentHandlerService = jasmine.createSpyObj('personalizationsmarteditComponentHandlerService', ['getContainerSourceIdForContainerId']);
		permissionService = jasmine.createSpyObj('permissionService', ['isPermitted']);

		personalizationsmarteditComponentHandlerService.getContainerSourceIdForContainerId.and.returnValue(CONTAINER_ID);
		personalizationsmarteditRestService.getCxCmsAllActionsForContainer.and.returnValue($q.when(mockActions));
		personalizationSmartEditUtils.getAndSetCatalogVersionNameL10N.and.callThrough();
		personalizationSmartEditUtils.uniqueArray.and.callFake((arr1, arr2) => {
			Array.prototype.push.apply(arr1, arr2);
		});
		permissionService.isPermitted.and.returnValue($q.when(false));
		personalizationsmarteditContextualMenuService.isContextualMenuInfoEnabled.and.returnValue($q.when(false));
		personalizationsmarteditContextualMenuService.isPersonalizationAllowedInWorkflow.and.returnValue($q.when(false));

		showComponentInfoListModule = new PersonalizationsmarteditShowComponentInfoListComponent(
			personalizationsmarteditContextService,
			personalizationsmarteditContextualMenuService,
			personalizationSmartEditUtils,
			personalizationsmarteditRestService,
			personalizationsmarteditMessageHandler,
			$translate,
			personalizationsmarteditComponentHandlerService,
			permissionService
		);
	});

	describe('Component API', function() {

		it('should have proper api when initialized without parameters', function() {
			expect(showComponentInfoListModule.isContainerIdEmpty).not.toBeDefined();
			expect(showComponentInfoListModule.actions).not.toBeDefined();
			expect(showComponentInfoListModule.moreCustomizationsRequestProcessing).not.toBeDefined();
			expect(showComponentInfoListModule.$onInit).toBeDefined();
		});

		it('should have proper api when initialized with parameters', function() {
			showComponentInfoListModule.containerId = CONTAINER_ID;
			showComponentInfoListModule.$onInit();

			expect(showComponentInfoListModule.isContainerIdEmpty).toBeDefined();
			expect(showComponentInfoListModule.moreCustomizationsRequestProcessing).toBe(false);
			expect(showComponentInfoListModule.$onInit).toBeDefined();
		});

		it('should have actions when initialized with parameters', function() {
			showComponentInfoListModule.containerId = CONTAINER_ID;
			showComponentInfoListModule.$onInit();

			showComponentInfoListModule.addMoreItems();
			expect(showComponentInfoListModule.isContainerIdEmpty).toBeDefined();
			expect(showComponentInfoListModule.pagination.getTotalCount()).toEqual(2);
			expect(showComponentInfoListModule.actions.length).toEqual(2);
			expect(showComponentInfoListModule.moreCustomizationsRequestProcessing).toBe(false);
		});
	});

});
