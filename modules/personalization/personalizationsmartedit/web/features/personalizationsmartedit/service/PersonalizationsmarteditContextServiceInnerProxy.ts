import {CrossFrameEventService, GatewayProxied, SeInjectable} from 'smarteditcommons';
import {PersonalizationsmarteditContextService} from "personalizationsmartedit/service/PersonalizationsmarteditContextServiceInner";
import {Customize} from "personalizationcommons/dtos/Customize";
import {CombinedView} from "personalizationcommons/dtos/CombinedView";
import {SeData} from "personalizationcommons/dtos/SeData";
import {Personalization} from "personalizationcommons/dtos/Personalization";

@GatewayProxied('setPersonalization', 'setCustomize', 'setCombinedView', 'setSeData')
@SeInjectable()
export class PersonalizationsmarteditContextServiceProxy {

	constructor(
		protected personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		protected crossFrameEventService: CrossFrameEventService) {
	}

	setPersonalization(newPersonalization: Personalization): void {
		this.personalizationsmarteditContextService.setPersonalization(newPersonalization);
	}

	setCustomize(newCustomize: Customize): void {
		this.personalizationsmarteditContextService.setCustomize(newCustomize);
		this.crossFrameEventService.publish('PERSONALIZATION_CUSTOMIZE_CONTEXT_SYNCHRONIZED');
	}

	setCombinedView(newCombinedView: CombinedView): void {
		this.personalizationsmarteditContextService.setCombinedView(newCombinedView);
		this.crossFrameEventService.publish('PERSONALIZATION_COMBINEDVIEW_CONTEXT_SYNCHRONIZED');
	}

	setSeData(newSeData: SeData): void {
		this.personalizationsmarteditContextService.setSeData(newSeData);
	}

}
