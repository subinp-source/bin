import {GatewayProxied, SeInjectable} from 'smarteditcommons';
import {PersonalizationsmarteditContextService} from "personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter";

@GatewayProxied('applySynchronization', 'isCurrentPageActiveWorkflowRunning')
@SeInjectable()
export class PersonalizationsmarteditContextServiceReverseProxy {

	public static readonly WORKFLOW_RUNNING_STATUS = "RUNNING";

	constructor(
		protected personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		protected workflowService: any,
		protected pageInfoService: any) {
	}

	applySynchronization(): void {
		this.personalizationsmarteditContextService.applySynchronization();
	}

	isCurrentPageActiveWorkflowRunning(): Promise<boolean> {
		return this.pageInfoService.getPageUUID().then((pageUuid: string) => {
			return this.workflowService.getActiveWorkflowForPageUuid(pageUuid).then((workflow: any) => {
				if (workflow == null) {
					return false;
				}
				return workflow.status === PersonalizationsmarteditContextServiceReverseProxy.WORKFLOW_RUNNING_STATUS;
			});
		});
	}

}
