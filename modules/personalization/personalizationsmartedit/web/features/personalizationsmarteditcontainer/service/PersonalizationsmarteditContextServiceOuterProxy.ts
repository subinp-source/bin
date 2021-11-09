import {GatewayProxied, SeInjectable} from 'smarteditcommons';
import {Personalization} from "personalizationcommons/dtos/Personalization";
import {Customize} from "personalizationcommons/dtos/Customize";
import {CombinedView} from "personalizationcommons/dtos/CombinedView";
import {SeData} from "personalizationcommons/dtos/SeData";

@GatewayProxied('setPersonalization', 'setCustomize', 'setCombinedView', 'setSeData')
@SeInjectable()
export class PersonalizationsmarteditContextServiceProxy {

	setPersonalization(personalization: Personalization): void {
		'proxyFunction';
		return undefined;
	}

	setCustomize(customize: Customize): void {
		'proxyFunction';
		return undefined;
	}

	setCombinedView(combinedView: CombinedView): void {
		'proxyFunction';
		return undefined;
	}

	setSeData(seData: SeData): void {
		'proxyFunction';
		return undefined;
	}
}
