import {SeModule} from 'smarteditcommons';
import {CatalogFilterDropdownComponent, PERSONALIZATION_CATALOG_FILTER_PROVIDER} from 'personalizationsmarteditcontainer/commonComponents/CatalogFilterDropdownComponent';
import {PageFilterDropdownComponent, PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER_PROVIDER} from 'personalizationsmarteditcontainer/commonComponents/PageFilterDropdownComponent';
import {StatusFilterDropdownComponent} from 'personalizationsmarteditcontainer/commonComponents/StatusFilterDropdownComponent';
import {PersonalizationsmarteditCommonsModule} from 'personalizationcommons';
import {PersonalizationsmarteditServicesModule} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditServicesModule';
@SeModule({
	imports: [
		'ySelectModule',
		'componentMenuServiceModule',
		'l10nModule',
		PersonalizationsmarteditCommonsModule,
		PersonalizationsmarteditServicesModule
	],
	declarations: [
		CatalogFilterDropdownComponent,
		PageFilterDropdownComponent,
		StatusFilterDropdownComponent
	],
	providers: [
		PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER_PROVIDER,
		PERSONALIZATION_CATALOG_FILTER_PROVIDER
	]
})
export class PersonalizationsmarteditCommonComponentsModule {}
