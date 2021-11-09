import {SeModule} from 'smarteditcommons';
import {PaginationHelper} from './PaginationHelper';
import {
	PersonalizationsmarteditDateUtils,
	PERSONALIZATION_DATE_FORMATS_PROVIDER
} from './PersonalizationsmarteditDateUtils';
import {
	PersonalizationsmarteditContextUtils
} from './PersonalizationsmarteditContextUtils';
import {
	COMBINED_VIEW_TOOLBAR_ITEM_KEY_PROVIDER,
	CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY_PROVIDER,
	PersonalizationsmarteditUtils,
	PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING_PROVIDER,
	PERSONALIZATION_MODEL_STATUS_CODES_PROVIDER,
	PERSONALIZATION_VIEW_STATUS_MAPPING_CODES_PROVIDER
} from './PersonalizationsmarteditUtils';
import {PersonalizationsmarteditMessageHandler} from './PersonalizationsmarteditMessageHandler';
import {PersonalizationsmarteditCommerceCustomizationService} from './PersonalizationsmarteditCommerceCustomizationService';
import {IsDateValidOrEmptyDirective} from "./IsDateValidOrEmptyDirective";
import {DateTimePickerRangeComponent} from "./DateTimePickerRangeComponent";
import {PersonalizationsmarteditScrollZoneComponent} from "personalizationcommons/personalizationsmarteditScrollZone/PersonalizationsmarteditScrollZoneComponent";
import {PersonalizationCurrentElementDirective} from "./PersonalizationCurrentElementDirective";
import {PersonalizationInfiniteScrollDirectiveOld} from "./PersonalizationInfiniteScrollDirectiveOld";

@SeModule({
	imports: [
		'personalizationcommonsTemplates',
		'smarteditServicesModule',
		'yjqueryModule',
		'l10nModule',
		'alertServiceModule'
	],
	providers: [
		PaginationHelper,
		{
			provide: 'PaginationHelper',
			useFactory: () => {
				return (initialData: any) => {
					return new PaginationHelper(initialData);
				};
			}
		},
		PersonalizationsmarteditDateUtils,
		PersonalizationsmarteditContextUtils,
		PERSONALIZATION_DATE_FORMATS_PROVIDER,
		PERSONALIZATION_MODEL_STATUS_CODES_PROVIDER,
		PERSONALIZATION_VIEW_STATUS_MAPPING_CODES_PROVIDER,
		PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING_PROVIDER,
		COMBINED_VIEW_TOOLBAR_ITEM_KEY_PROVIDER,
		CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY_PROVIDER,
		PersonalizationsmarteditUtils,
		PersonalizationsmarteditMessageHandler,
		PersonalizationsmarteditCommerceCustomizationService
	],
	declarations: [
		IsDateValidOrEmptyDirective,
		PersonalizationCurrentElementDirective,
		PersonalizationInfiniteScrollDirectiveOld,
		DateTimePickerRangeComponent,
		PersonalizationsmarteditScrollZoneComponent
	]
})
export class PersonalizationsmarteditCommonsModule {}
