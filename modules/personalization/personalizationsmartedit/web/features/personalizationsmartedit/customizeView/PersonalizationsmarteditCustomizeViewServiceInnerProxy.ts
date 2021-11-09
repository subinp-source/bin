import {GatewayProxied, SeInjectable} from 'smarteditcommons';
import {Dictionary} from 'lodash';
import {PersonalizationsmarteditCustomizeViewHelper} from 'personalizationsmartedit/service/PersonalizationsmarteditCustomizeViewHelper';

@GatewayProxied('getSourceContainersInfo')
@SeInjectable()
export class PersonalizationsmarteditCustomizeViewServiceProxy {

	constructor(
		protected personalizationsmarteditCustomizeViewHelper: PersonalizationsmarteditCustomizeViewHelper
	) {
	}

	getSourceContainersInfo(): Dictionary<number> {
		return this.personalizationsmarteditCustomizeViewHelper.getSourceContainersInfo();
	}

}