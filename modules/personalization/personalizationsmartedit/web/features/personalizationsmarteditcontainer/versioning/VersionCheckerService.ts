import {SeInjectable} from 'smarteditcommons';
import {PersonalizationsmarteditRestService} from "personalizationsmarteditcontainer/service/PersonalizationsmarteditRestService";

// copy of cmsSmarteditServicesModule.interfaces:IPageVersion
interface IPageVersion {
	uid: string;
	itemUUID: string;
	creationtime: Date;
	label: string;
	description?: string;
}

@SeInjectable()
export class VersionCheckerService {

	// storage for asynchronusly changed page version
	private version: IPageVersion;

	constructor(
		private personalizationsmarteditRestService: PersonalizationsmarteditRestService,
		private pageVersionSelectionService: any
	) {}

	public setVersion(version: IPageVersion) {
		this.version = version;
	}

	public provideTranlationKey(key: string): Promise<string> {
		const TRANSLATE_NS: string = 'personalization.se.cms.actionitem.page.version.rollback.confirmation';

		this.version = this.version || this.pageVersionSelectionService.getSelectedPageVersion();

		if (!!this.version) {
			return this.personalizationsmarteditRestService.checkVersionConflict(this.version.uid).then(
				(response: any) => {
					return response.result ? key : TRANSLATE_NS;
				}, () => {
					return key;
				}
			);
		} else {
			return Promise.resolve(key);
		}
	}

}


