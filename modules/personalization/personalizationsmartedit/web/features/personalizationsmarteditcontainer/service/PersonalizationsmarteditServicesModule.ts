import {SeModule} from 'smarteditcommons';
import {PersonalizationsmarteditContextService} from "personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter";
import {PersonalizationsmarteditContextServiceProxy} from "personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuterProxy";
import {PersonalizationsmarteditContextServiceReverseProxy} from "personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuterReverseProxy";
import {PersonalizationsmarteditRestService} from "personalizationsmarteditcontainer/service/PersonalizationsmarteditRestService";
import {PersonalizationsmarteditPreviewService} from "personalizationsmarteditcontainer/service/PersonalizationsmarteditPreviewService";
import {PersonalizationsmarteditRulesAndPermissionsRegistrationService} from './PersonalizationsmarteditRulesAndPermissionsRegistrationService';
import {PersonalizationsmarteditCommonsModule} from "personalizationcommons";

@SeModule({
	imports: [
		'loadConfigModule',
		'smarteditServicesModule',
		'yjqueryModule',
		'permissionServiceModule',
		'catalogVersionPermissionModule',
		PersonalizationsmarteditCommonsModule,
	],
	providers: [
		PersonalizationsmarteditRestService,
		PersonalizationsmarteditContextService,
		PersonalizationsmarteditContextServiceProxy,
		PersonalizationsmarteditContextServiceReverseProxy,
		PersonalizationsmarteditPreviewService,
		PersonalizationsmarteditRulesAndPermissionsRegistrationService
	],
	initialize: (
		personalizationsmarteditRulesAndPermissionsRegistrationService: PersonalizationsmarteditRulesAndPermissionsRegistrationService
	) => {
		'ngInject';
		personalizationsmarteditRulesAndPermissionsRegistrationService.registerRules();
	}
})
export class PersonalizationsmarteditServicesModule {}
