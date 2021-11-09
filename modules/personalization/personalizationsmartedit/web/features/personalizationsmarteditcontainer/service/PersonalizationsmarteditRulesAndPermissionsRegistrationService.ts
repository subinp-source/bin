import {
	IPermissionService,
	SeInjectable
} from 'smarteditcommons';
import {PersonalizationsmarteditContextService} from './PersonalizationsmarteditContextServiceOuter';
import {PersonalizationsmarteditRestService} from './PersonalizationsmarteditRestService';

@SeInjectable()
export class PersonalizationsmarteditRulesAndPermissionsRegistrationService {

	constructor(
		private personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		private personalizationsmarteditRestService: PersonalizationsmarteditRestService,
		private permissionService: IPermissionService,
		private catalogVersionPermissionService: any,
		private pageService: any,
		private $q: angular.IQService) {
	}

	public registerRules() {

		this.permissionService.registerRule({
			names: ['se.access.personalization'],
			verify: () => {
				return this.catalogVersionPermissionService.hasReadPermissionOnCurrent().then(() => {
					return this.personalizationsmarteditContextService.refreshExperienceData().then(() => {
						return this.personalizationsmarteditRestService.getCustomizations(this.getCustomizationFilter()).then(() => {
							return this.$q.resolve(true);
						}, (errorResp: any) => {
							if (errorResp.status === 403) {
								// Forbidden status on GET /customizations - user doesn't have permission to personalization perspective
								return this.$q.resolve(false);
							} else {
								// other errors will be handled with personalization perspective turned on
								return this.$q.resolve(true);
							}
						});
					});
				});
			}
		});

		this.permissionService.registerRule({
			names: ['se.access.personalization.page'],
			verify: () => {
				return this.pageService.getCurrentPageInfo().then((info: any) => {
					return this.$q.resolve(info.typeCode !== 'EmailPage');
				});
			}
		});

		// Permissions
		this.permissionService.registerPermission({
			aliases: ['se.personalization.open'],
			rules: ['se.access.personalization']
		});

		this.permissionService.registerPermission({
			aliases: ['se.personalization.page'],
			rules: ['se.access.personalization.page']
		});

	}

	private getCustomizationFilter() {
		return {
			currentPage: 0,
			currentSize: 1
		};
	}
}