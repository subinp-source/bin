import {SeModule} from 'smarteditcommons';
import {PersonalizationsmarteditCommonsModule} from 'personalizationcommons';
import {PersonalizationsmarteditServicesModule} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditServicesModule';
import {PersonalizationsmarteditCommerceCustomizationModule} from 'personalizationsmarteditcontainer/management/commerceCustomizationView/PersonalizationsmarteditCommerceCustomizationModule';
import {PersonalizationsmarteditManageCustomizationViewModule} from 'personalizationsmarteditcontainer/management/manageCustomizationView/PersonalizationsmarteditManageCustomizationViewModule';
import {PersonalizationsmarteditDataFactory} from 'personalizationsmarteditcontainer/dataFactory/PersonalizationsmarteditDataFactoryModule';
import {PersonalizationsmarteditManagerView} from 'personalizationsmarteditcontainer/management/managerView/PersonalizationsmarteditManagerViewService';
import {PersonalizationsmarteditManagerViewUtilsService} from 'personalizationsmarteditcontainer/management/managerView/PersonalizationsmarteditManagerViewUtilsService';


@SeModule({
	imports: [
		'modalServiceModule',
		'confirmationModalServiceModule',
		'smarteditCommonsModule',
		'ui.tree',
		PersonalizationsmarteditCommonsModule,
		PersonalizationsmarteditServicesModule,
		PersonalizationsmarteditManageCustomizationViewModule,
		PersonalizationsmarteditCommerceCustomizationModule,
		PersonalizationsmarteditDataFactory
	],
	providers: [
		PersonalizationsmarteditManagerView,
		PersonalizationsmarteditManagerViewUtilsService
	]
})
export class PersonalizationsmarteditManagerViewModule {}
