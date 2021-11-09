import {SeModule} from 'smarteditcommons';
import {PersonalizationsmarteditCommonsModule} from 'personalizationcommons';
import {PersonalizationsmarteditServicesModule} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditServicesModule';
import {PersonalizationsmarteditCustomizeViewComponent} from 'personalizationsmarteditcontainer/customizeView/PersonalizationsmarteditCustomizeViewComponent';
import {PersonalizationsmarteditCustomizeViewServiceProxy} from 'personalizationsmarteditcontainer/customizeView/PersonalizationsmarteditCustomizeViewServiceOuterProxy';
import {PersonalizationsmarteditCommonComponentsModule} from 'personalizationsmarteditcontainer/commonComponents/PersonalizationsmarteditCommonComponentsModule';
import {CustomizationDataFactory} from 'personalizationsmarteditcontainer/dataFactory/CustomizationDataFactory';
import {CustomizationsListComponent} from 'personalizationsmarteditcontainer/customizeView/customizationsList/CustomizationsListComponent';

@SeModule({
	imports: [
		'seConstantsModule',
		'personalizationsmarteditManageCustomizationViewModule',
		'smarteditCommonsModule',
		PersonalizationsmarteditCommonComponentsModule,
		PersonalizationsmarteditCommonsModule,
		PersonalizationsmarteditServicesModule
	],
	providers: [
		PersonalizationsmarteditCustomizeViewServiceProxy,
		CustomizationDataFactory
	],
	declarations: [
		PersonalizationsmarteditCustomizeViewComponent,
		CustomizationsListComponent
	]
})
export class PersonalizationsmarteditCustomizeViewModule {}