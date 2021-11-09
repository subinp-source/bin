import {SeModule} from 'smarteditcommons';
import {COMPONENT_CONTAINER_TYPE_PROVIDER, CONTAINER_SOURCE_ID_ATTR_PROVIDER, PersonalizationsmarteditComponentHandlerService} from "personalizationsmartedit/service/PersonalizationsmarteditComponentHandlerService";
import {PersonalizationsmarteditCustomizeViewHelper} from "personalizationsmartedit/service/PersonalizationsmarteditCustomizeViewHelper";
import {PersonalizationsmarteditContextService} from "personalizationsmartedit/service/PersonalizationsmarteditContextServiceInner";
import {PersonalizationsmarteditContextServiceProxy} from "personalizationsmartedit/service/PersonalizationsmarteditContextServiceInnerProxy";
import {PersonalizationsmarteditContextServiceReverseProxy} from "personalizationsmartedit/service/PersonalizationsmarteditContextServiceInnerReverseProxy";
import {PersonalizationsmarteditContextualMenuService} from "personalizationsmartedit/service/PersonalizationsmarteditContextualMenuService";
import {
	ACTIONS_DETAILS_PROVIDER,
	PersonalizationsmarteditRestService
} from "personalizationsmartedit/service/PersonalizationsmarteditRestService";
import {PersonalizationsmarteditCommonsModule} from "personalizationcommons";
import {PersonalizationsmarteditContextMenuServiceProxy} from 'personalizationsmartedit/contextMenu/PersonalizationsmarteditContextMenuServiceInnerProxy';

@SeModule({
	imports: [
		'smarteditServicesModule',
		'yLoDashModule',
		PersonalizationsmarteditCommonsModule
	],
	providers: [
		PersonalizationsmarteditComponentHandlerService,
		PersonalizationsmarteditCustomizeViewHelper,
		PersonalizationsmarteditContextService,
		PersonalizationsmarteditContextServiceProxy,
		PersonalizationsmarteditContextServiceReverseProxy,
		PersonalizationsmarteditRestService,
		PersonalizationsmarteditContextualMenuService,
		PersonalizationsmarteditContextMenuServiceProxy,
		COMPONENT_CONTAINER_TYPE_PROVIDER,
		CONTAINER_SOURCE_ID_ATTR_PROVIDER,
		ACTIONS_DETAILS_PROVIDER
	]
})
export class PersonalizationsmarteditServicesModule {}
