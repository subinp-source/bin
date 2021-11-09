import * as angular from 'angular';
import {doImport as doImport1} from './forcedImports';
doImport1();

import {IContextualMenuButton, IFeatureService, SeModule} from 'smarteditcommons';
import {PersonalizationsmarteditCommonsModule} from 'personalizationcommons';
import {PersonalizationsmarteditServicesModule} from 'personalizationsmartedit/service/PersonalizationsmarteditServicesModule';
import {PersonalizationsmarteditShowComponentInfoListModule} from 'personalizationsmartedit/contextMenu/PersonalizationsmarteditShowComponentInfoListModule';
import {PersonalizationsmarteditComponentHandlerService} from 'personalizationsmartedit/service/PersonalizationsmarteditComponentHandlerService';
import {PersonalizationsmarteditContextServiceProxy} from 'personalizationsmartedit/service/PersonalizationsmarteditContextServiceInnerProxy';
import {PersonalizationsmarteditShowActionListModule} from 'personalizationsmartedit/contextMenu/PersonalizationsmarteditShowActionListModule';
import {PersonalizationsmarteditCustomizeViewModule} from 'personalizationsmartedit/customizeView/PersonalizationsmarteditCustomizeViewModule';
import {PersonalizationsmarteditCustomizeViewServiceProxy} from 'personalizationsmartedit/customizeView/PersonalizationsmarteditCustomizeViewServiceInnerProxy';
import {PersonalizationsmarteditContextualMenuService} from 'personalizationsmartedit/service/PersonalizationsmarteditContextualMenuService';
import {PersonalizationsmarteditContextMenuModule} from 'personalizationsmartedit/contextMenu/PersonalizationsmarteditContextMenuModule';
import {PersonalizationsmarteditContextMenuServiceProxy} from 'personalizationsmartedit/contextMenu/PersonalizationsmarteditContextMenuServiceInnerProxy';
import {PersonalizationsmarteditSharedSlotDecoratorModule} from 'personalizationsmartedit/sharedSlotDecorator/PersonalizationsmarteditSharedSlotDecoratorModule';
import {PersonalizationsmarteditComponentLightUpDecoratorModule} from 'personalizationsmartedit/componentLightUpDecorator/PersonalizationsmarteditComponentLightUpDecoratorModule';
import {PersonalizationsmarteditCombinedViewComponentLightUpDecoratorModule} from 'personalizationsmartedit/combinedView/PersonalizationsmarteditCombinedViewComponentLightUpDecoratorModule';
import {PersonalizationsmarteditExternalComponentDecoratorModule} from 'personalizationsmartedit/externalComponentDecorator/PersonalizationsmarteditExternalComponentDecorator';


@SeModule({
	imports: [
		PersonalizationsmarteditShowActionListModule,
		PersonalizationsmarteditCommonsModule,
		PersonalizationsmarteditServicesModule,
		PersonalizationsmarteditShowComponentInfoListModule,
		PersonalizationsmarteditCustomizeViewModule,
		PersonalizationsmarteditContextMenuModule,
		PersonalizationsmarteditSharedSlotDecoratorModule,
		PersonalizationsmarteditComponentLightUpDecoratorModule,
		PersonalizationsmarteditCombinedViewComponentLightUpDecoratorModule,
		PersonalizationsmarteditExternalComponentDecoratorModule,
		'personalizationsmarteditTemplates',
		'decoratorServiceModule',
		'smarteditServicesModule',
		'yjqueryModule',
		'externalComponentDecoratorModule',
		'externalComponentButtonModule'
	],
	initialize: (
		yjQuery: any,
		domain: any,
		$q: angular.IQService,
		personalizationsmarteditComponentHandlerService: PersonalizationsmarteditComponentHandlerService,
		personalizationsmarteditContextualMenuService: PersonalizationsmarteditContextualMenuService,
		personalizationsmarteditContextServiceProxy: PersonalizationsmarteditContextServiceProxy, // dont remove
		personalizationsmarteditCustomizeViewServiceProxy: PersonalizationsmarteditCustomizeViewServiceProxy, // dont remove
		personalizationsmarteditContextMenuServiceProxy: PersonalizationsmarteditContextMenuServiceProxy,
		decoratorService: any,
		featureService: IFeatureService
	) => {
		'ngInject';

		const loadCSS = (href: string) => {
			const cssLink = yjQuery("<link rel='stylesheet' type='text/css' href='" + href + "'>");
			yjQuery("head").append(cssLink);
		};
		loadCSS(domain + "/personalizationsmartedit/css/style.css");

		decoratorService.addMappings({
			'^.*Slot$': ['personalizationsmarteditSharedSlot']
		});

		decoratorService.addMappings({
			'^.*Component$': ['personalizationsmarteditComponentLightUp', 'personalizationsmarteditCombinedViewComponentLightUp']
		});

		decoratorService.addMappings({
			'^((?!Slot).)*$': ['personalizationsmarteditExternalComponentDecorator']
		});

		featureService.addDecorator({
			key: 'personalizationsmarteditExternalComponentDecorator',
			nameI18nKey: 'personalizationsmarteditExternalComponentDecorator',
			displayCondition: (componentType: string, componentId: string) => {
				const component: JQuery = personalizationsmarteditComponentHandlerService.getOriginalComponent(componentId, componentType);
				const container: any = personalizationsmarteditComponentHandlerService.getParentContainerForComponent(component);
				if (container.length > 0 && container[0].attributes["data-smartedit-personalization-action-id"]) {
					return $q.when(false);
				}
				return $q.when(personalizationsmarteditComponentHandlerService.isExternalComponent(componentId, componentType));
			}
		});

		featureService.addDecorator({
			key: 'personalizationsmarteditComponentLightUp',
			nameI18nKey: 'personalizationsmarteditComponentLightUp'
		});

		featureService.addDecorator({
			key: 'personalizationsmarteditCombinedViewComponentLightUp',
			nameI18nKey: 'personalizationsmarteditCombinedViewComponentLightUp'
		});

		featureService.addDecorator({
			key: 'personalizationsmarteditSharedSlot',
			nameI18nKey: 'personalizationsmarteditSharedSlot'
		});

		featureService.addContextualMenuButton({
			key: "personalizationsmartedit.context.show.action.list",
			i18nKey: 'personalization.context.action.list.show',
			nameI18nKey: 'personalization.context.action.list.show',
			regexpKeys: ['^.*Component$'],
			condition: (config: any) => {
				return personalizationsmarteditContextualMenuService.isContextualMenuShowActionListEnabled(config);
			},
			action: {
				template: '<personalizationsmartedit-show-action-list data-container-id="ctrl.componentAttributes.smarteditContainerId"></personalizationsmartedit-show-action-list>'
			},
			displayClass: "showactionlistbutton",
			displayIconClass: "hyicon hyicon-combinedview cmsx-ctx__icon personalization-ctx__icon",
			displaySmallIconClass: "hyicon hyicon-combinedview cmsx-ctx__icon--small",
			permissions: ['se.read.page'],
			priority: 500
		} as IContextualMenuButton);

		featureService.addContextualMenuButton({
			key: "personalizationsmartedit.context.info.action",
			i18nKey: 'personalization.context.action.info',
			nameI18nKey: 'personalization.context.action.info',
			regexpKeys: ['^.*Component$'],
			condition: (config: any) => {
				return personalizationsmarteditContextualMenuService.isContextualMenuInfoItemEnabled();
			},
			action: {
				template: '<personalizationsmartedit-show-component-info-list data-container-id="ctrl.componentAttributes.smarteditContainerId"></personalizationsmartedit-show-component-info-list>'
			},
			displayClass: "infoactionbutton",
			displayIconClass: "hyicon hyicon-msginfo cmsx-ctx__icon personalization-ctx__icon",
			displaySmallIconClass: "hyicon hyicon-msginfo cmsx-ctx__icon--small",
			permissions: ['se.edit.page'],
			priority: 510
		} as IContextualMenuButton);

		featureService.addContextualMenuButton({
			key: "personalizationsmartedit.context.add.action",
			i18nKey: 'personalization.context.action.add',
			nameI18nKey: 'personalization.context.action.add',
			regexpKeys: ['^.*Component$'],
			condition: (config: any) => {
				return personalizationsmarteditContextualMenuService.isContextualMenuAddItemEnabled(config);
			},
			action: {
				callback: (config: any, $event: any) => {
					personalizationsmarteditContextualMenuService.openAddAction(config);
				}
			},
			displayClass: "addactionbutton",
			displayIconClass: "hyicon hyicon-addlg cmsx-ctx__icon personalization-ctx__icon",
			displaySmallIconClass: "hyicon hyicon-addlg cmsx-ctx__icon--small",
			permissions: ['se.edit.page'],
			priority: 520
		} as IContextualMenuButton);

		featureService.addContextualMenuButton({
			key: "personalizationsmartedit.context.component.edit.action",
			i18nKey: 'personalization.context.component.action.edit',
			nameI18nKey: 'personalization.context.component.action.edit',
			regexpKeys: ['^.*Component$'],
			condition: (config: any) => {
				return personalizationsmarteditContextualMenuService.isContextualMenuEditComponentItemEnabled(config);
			},
			action: {
				callback: (config: any, $event: any) => {
					personalizationsmarteditContextualMenuService.openEditComponentAction(config);
				}
			},
			displayClass: "editbutton",
			displayIconClass: "sap-icon--edit cmsx-ctx__icon",
			displaySmallIconClass: "sap-icon--edit cmsx-ctx__icon--small",
			permissions: ['se.edit.page'],
			priority: 530
		} as IContextualMenuButton);

		featureService.addContextualMenuButton({
			key: "personalizationsmartedit.context.edit.action",
			i18nKey: 'personalization.context.action.edit',
			nameI18nKey: 'personalization.context.action.edit',
			regexpKeys: ['^.*Component$'],
			condition: (config: any) => {
				return personalizationsmarteditContextualMenuService.isContextualMenuEditItemEnabled(config);
			},
			action: {
				callback: (config: any, $event: any) => {
					personalizationsmarteditContextualMenuService.openEditAction(config);
				}
			},
			displayClass: "replaceactionbutton",
			displayIconClass: "hyicon hyicon-change cmsx-ctx__icon personalization-ctx__icon",
			displaySmallIconClass: "hyicon hyicon-change cmsx-ctx__icon--small",
			permissions: ['se.edit.page'],
			priority: 540
		} as IContextualMenuButton);

		featureService.addContextualMenuButton({
			key: "personalizationsmartedit.context.delete.action",
			i18nKey: 'personalization.context.action.delete',
			nameI18nKey: 'personalization.context.action.delete',
			regexpKeys: ['^.*Component$'],
			condition: (config: any) => {
				return personalizationsmarteditContextualMenuService.isContextualMenuDeleteItemEnabled(config);
			},
			action: {
				callback: (config: any, $event: any) => {
					personalizationsmarteditContextualMenuService.openDeleteAction(config);
				}
			},
			displayClass: "removeactionbutton",
			displayIconClass: "hyicon hyicon-removelg cmsx-ctx__icon personalization-ctx__icon",
			displaySmallIconClass: "hyicon hyicon-removelg cmsx-ctx__icon--small",
			permissions: ['se.edit.page'],
			priority: 550
		} as IContextualMenuButton);

	}
})
export class Personalizationsmarteditmodule {}

