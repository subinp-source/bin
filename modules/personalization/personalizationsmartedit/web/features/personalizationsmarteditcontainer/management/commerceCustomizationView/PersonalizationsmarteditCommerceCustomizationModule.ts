import {SeModule} from 'smarteditcommons';

import {PersonalizationsmarteditCommonsModule} from 'personalizationcommons';
import {PersonalizationsmarteditServicesModule} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditServicesModule';
import {PersonalizationsmarteditCommerceCustomizationView} from 'personalizationsmarteditcontainer/management/commerceCustomizationView/PersonalizationsmarteditCommerceCustomizationViewService';
import {ActionsDataFactory, PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES_PROVIDER} from 'personalizationsmarteditcontainer/management/commerceCustomizationView/ActionsDataFactory';

@SeModule({
	imports: [
		'modalServiceModule',
		'smarteditCommonsModule',
		'confirmationModalServiceModule',
		PersonalizationsmarteditCommonsModule,
		PersonalizationsmarteditServicesModule,
	],
	providers: [
		PersonalizationsmarteditCommerceCustomizationView,
		ActionsDataFactory,
		PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES_PROVIDER
	]
})
export class PersonalizationsmarteditCommerceCustomizationModule {}