import {GatewayProxied, SeInjectable} from 'smarteditcommons';

@GatewayProxied('openDeleteAction', 'openAddAction', 'openEditAction', 'openEditComponentAction')
@SeInjectable()
export class PersonalizationsmarteditContextMenuServiceProxy {

	openDeleteAction(config: any): void {
		'proxyFunction';
		return undefined;
	}
	openAddAction(config: any): void {
		'proxyFunction';
		return undefined;
	}
	openEditAction(config: any): void {
		'proxyFunction';
		return undefined;
	}
	openEditComponentAction(config: any): void {
		'proxyFunction';
		return undefined;
	}
}