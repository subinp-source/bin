import * as angular from 'angular';
import {SeModule} from 'smarteditcommons';
import {PersonalizationpromotionssmarteditPromotionsComponent} from './promotionsComponent/PersonalizationpromotionssmarteditPromotionsComponent';
import {PersonalizationpromotionssmarteditIAction, PersonalizationpromotionssmarteditServiceModule} from 'personalizationpromotionssmarteditcommons';

@SeModule({
	imports: [
		'personalizationsmarteditCommonsModule',
		'personalizationsmarteditCommerceCustomizationModule',
		PersonalizationpromotionssmarteditServiceModule,
		'smarteditServicesModule'
	],
	config: ($logProvider: angular.ILogProvider) => {
		'ngInject';
		$logProvider.debugEnabled(false);
	},
	declarations: [PersonalizationpromotionssmarteditPromotionsComponent],
	initialize: (
		personalizationsmarteditCommerceCustomizationService: any,
		$filter: any
	) => {
		'ngInject';
		personalizationsmarteditCommerceCustomizationService.registerType({
			type: 'cxPromotionActionData',
			text: 'personalization.modal.commercecustomization.action.type.promotion',
			template: 'personalizationpromotionssmarteditPromotionsWrapperTemplate.html',
			confProperty: 'personalizationsmartedit.commercecustomization.promotions.enabled',
			getName: (action: PersonalizationpromotionssmarteditIAction) => {
				return $filter('translate')('personalization.modal.commercecustomization.promotion.display.name') + " - " + action.promotionId;
			}
		});
	}
})
export class PersonalizationpromotionssmarteditPromotionsModule {}