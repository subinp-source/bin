
import 'jasmine';
import {PersonalizationsmarteditComponentLightUpDecoratorController} from "../../../../../web/features/personalizationsmartedit/componentLightUpDecorator/PersonalizationsmarteditComponentLightUpDecoratorModule";
import {PersonalizationsmarteditContextService} from "../../../../../web/features/personalizationsmartedit/service/PersonalizationsmarteditContextServiceInner";
import {PersonalizationsmarteditComponentHandlerService} from "../../../../../web/features/personalizationsmartedit/service/PersonalizationsmarteditComponentHandlerService";

describe('PersonalizationsmarteditComponentLightUpDecoratorController', () => {

	let personalizationsmarteditComponentLightUpDecoratorController: PersonalizationsmarteditComponentLightUpDecoratorController;
	let personalizationsmarteditContextService: PersonalizationsmarteditContextService;
	let personalizationsmarteditComponentHandlerService: PersonalizationsmarteditComponentHandlerService;
	let crossFrameEventService: jasmine.SpyObj<any>;
	let $element: any;
	const CONTAINER_SOURCE_ID_ATTR: string = 'container_id_attr';
	const OVERLAY_COMPONENT_CLASS: string = 'component_class';
	const CONTAINER_TYPE_ATTRIBUTE: string = 'container_type_attr';
	const ID_ATTRIBUTE: string = 'id_attr';
	const TYPE_ATTRIBUTE: string = 'type_attr';
	const CATALOG_VERSION_UUID_ATTRIBUTE: string = 'uuid_attr';
	const yjQuery: any = jasmine.createSpy('yjQuery');

	beforeEach(() => {

		personalizationsmarteditContextService = jasmine.createSpyObj('personalizationsmarteditContextService', ['getCustomize']);
		personalizationsmarteditComponentHandlerService = jasmine.createSpyObj('personalizationsmarteditComponentHandlerService', ['getParentContainerIdForComponent']);
		crossFrameEventService = jasmine.createSpyObj('crossFrameEventService', ['subscribe']);
		$element = jasmine.createSpyObj('$element', ['parent']);

		personalizationsmarteditComponentLightUpDecoratorController = new PersonalizationsmarteditComponentLightUpDecoratorController(
			personalizationsmarteditContextService,
			personalizationsmarteditComponentHandlerService,
			crossFrameEventService,
			CONTAINER_SOURCE_ID_ATTR,
			OVERLAY_COMPONENT_CLASS,
			CONTAINER_TYPE_ATTRIBUTE,
			ID_ATTRIBUTE,
			TYPE_ATTRIBUTE,
			CATALOG_VERSION_UUID_ATTRIBUTE
		);

	});

	describe('Controller API', () => {
		it('should have proper api when initialized', () => {
			expect(personalizationsmarteditComponentLightUpDecoratorController.isComponentSelected).toBeDefined();
			expect(personalizationsmarteditComponentLightUpDecoratorController.isVariationComponentSelected).toBeDefined();
			expect(personalizationsmarteditComponentLightUpDecoratorController.calculate).toBeDefined();
			expect(personalizationsmarteditComponentLightUpDecoratorController.$onInit).toBeDefined();
			expect(personalizationsmarteditComponentLightUpDecoratorController.$onDestroy).toBeDefined();
		});
	});

});
