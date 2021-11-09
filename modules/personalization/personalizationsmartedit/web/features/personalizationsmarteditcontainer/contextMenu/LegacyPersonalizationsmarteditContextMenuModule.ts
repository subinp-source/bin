import {SeModule} from 'smarteditcommons';
import {PersonalizationsmarteditContextMenuServiceProxy} from 'personalizationsmarteditcontainer/contextMenu/PersonalizationsmarteditContextMenuServiceOuterProxy';
import {PersonalizationsmarteditServicesModule} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditServicesModule';
import {PersonalizationsmarteditCommonsModule} from 'personalizationcommons/PersonalizationsmarteditCommonsModule';
import {PersonalizationsmarteditCommonComponentsModule} from 'personalizationsmarteditcontainer/commonComponents/PersonalizationsmarteditCommonComponentsModule';


@SeModule({
	imports: [
		PersonalizationsmarteditServicesModule,
		PersonalizationsmarteditCommonsModule,
		PersonalizationsmarteditCommonComponentsModule,
		'ui.select',
		'genericEditorModule',
		'cmsSmarteditServicesModule',
		'smarteditRootModule',
		'renderServiceModule',
		'slotRestrictionsServiceModule',
		'modalServiceModule',
	],
	providers: [
		PersonalizationsmarteditContextMenuServiceProxy
	],
})
export class LegacyPersonalizationsmarteditContextMenuModule {}