import {GatewayProxied, SeInjectable} from 'smarteditcommons';

@GatewayProxied('applySynchronization', 'isCurrentPageActiveWorkflowRunning')
@SeInjectable()
export class PersonalizationsmarteditContextServiceReverseProxy {

	applySynchronization(): void {
		'proxyFunction';
		return undefined;
	}

	isCurrentPageActiveWorkflowRunning(): Promise<boolean> {
		'proxyFunction';
		return undefined;
	}

}
