import {SeModule} from 'smarteditcommons';
import {PersonalizationsmarteditCombinedViewCommonsService} from 'personalizationsmarteditcontainer/combinedView/PersonalizationsmarteditCombinedViewCommonsService';
import {PersonalizationsmarteditCombinedViewMenuComponent} from 'personalizationsmarteditcontainer/combinedView/PersonalizationsmarteditCombinedViewMenuComponent';
import {PersonalizationsmarteditCommonsModule} from 'personalizationcommons';
import {PersonalizationsmarteditServicesModule} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditServicesModule';
import {PersonalizationsmarteditCommonComponentsModule} from 'personalizationsmarteditcontainer/commonComponents/PersonalizationsmarteditCommonComponentsModule';
import {PersonalizationsmarteditCustomizeViewModule} from 'personalizationsmarteditcontainer/customizeView/PersonalizationsmarteditCustomizeViewModule';

@SeModule({
	imports: [
		'seConstantsModule',
		'personalizationsmarteditManageCustomizationViewModule',
		'smarteditCommonsModule',
		'modalServiceModule',
		PersonalizationsmarteditCommonComponentsModule,
		PersonalizationsmarteditCommonsModule,
		PersonalizationsmarteditServicesModule,
		PersonalizationsmarteditCustomizeViewModule
	],
	providers: [
		PersonalizationsmarteditCombinedViewCommonsService
	],
	declarations: [
		PersonalizationsmarteditCombinedViewMenuComponent
	]
})
export class PersonalizationsmarteditCombinedViewModule {}