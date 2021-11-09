import {IModalService, SeInjectable} from 'smarteditcommons';
import {PersonalizationsmarteditManagerViewController} from 'personalizationsmarteditcontainer/management/managerView/PersonalizationsmarteditManagerViewController';

@SeInjectable()
export class PersonalizationsmarteditManagerView {

	constructor(
		private modalService: IModalService) {
	}

	openManagerAction = (customization: any, variation: any) => {
		this.modalService.open({
			title: "personalization.modal.manager.title",
			templateUrl: 'personalizationsmarteditManagerViewTemplate.html',
			controller: PersonalizationsmarteditManagerViewController,
			size: 'fullscreen',
			cssClasses: 'perso-library'
		}).then(() => {
			// success
		}, () => {
			// error
		});
	}
}