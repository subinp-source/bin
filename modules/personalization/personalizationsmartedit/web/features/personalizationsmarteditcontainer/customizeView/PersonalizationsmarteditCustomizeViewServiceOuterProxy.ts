import {GatewayProxied, SeInjectable} from 'smarteditcommons';
import {Dictionary} from 'lodash';

@GatewayProxied('getSourceContainersInfo')
@SeInjectable()
export class PersonalizationsmarteditCustomizeViewServiceProxy {
	getSourceContainersInfo(): Dictionary<number> {
		'proxyFunction';
		return null;
	}
}