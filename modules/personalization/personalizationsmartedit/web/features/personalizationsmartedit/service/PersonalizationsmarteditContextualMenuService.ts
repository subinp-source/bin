import {CrossFrameEventService, SeInjectable, TypedMap} from 'smarteditcommons';
import {LoDashStatic} from 'lodash';
import {PersonalizationsmarteditContextService} from 'personalizationsmartedit/service/PersonalizationsmarteditContextServiceInner';
import {PersonalizationsmarteditComponentHandlerService} from 'personalizationsmartedit/service/PersonalizationsmarteditComponentHandlerService';
import {PersonalizationsmarteditUtils} from 'personalizationcommons';
import {PersonalizationsmarteditContextMenuServiceProxy} from 'personalizationsmartedit/contextMenu/PersonalizationsmarteditContextMenuServiceInnerProxy';

@SeInjectable()
export class PersonalizationsmarteditContextualMenuService {

	public static readonly EDIT_PERSONALIZATION_IN_WORKFLOW = 'personalizationsmartedit.editPersonalizationInWorkflow.enabled';

	public contextPersonalization: any;
	public contextCustomize: any;
	public contextCombinedView: any;
	public contextSeData: any;
	private isWorkflowRunningBoolean: boolean = true;

	constructor(
		protected personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		protected personalizationsmarteditComponentHandlerService: PersonalizationsmarteditComponentHandlerService,
		protected personalizationsmarteditUtils: PersonalizationsmarteditUtils,
		protected personalizationsmarteditContextMenuServiceProxy: PersonalizationsmarteditContextMenuServiceProxy,
		protected crossFrameEventService: CrossFrameEventService,
		protected EVENTS: TypedMap<string>,
		private lodash: LoDashStatic) {

		this.init();

	}

	init(): void {
		this.crossFrameEventService.subscribe(this.EVENTS.PAGE_CHANGE, () => {
			this.updateWorkflowStatus();
		});
		this.crossFrameEventService.subscribe(this.EVENTS.PAGE_UPDATED, () => {
			this.updateWorkflowStatus();
		});
		this.updateWorkflowStatus();
	}

	refreshContext() {
		this.contextPersonalization = this.personalizationsmarteditContextService.getPersonalization();
		this.contextCustomize = this.personalizationsmarteditContextService.getCustomize();
		this.contextCombinedView = this.personalizationsmarteditContextService.getCombinedView();
		this.contextSeData = this.personalizationsmarteditContextService.getSeData();
	}

	openDeleteAction(config: any) {
		const configProperties = this.lodash.isString(config.componentAttributes) ? JSON.parse(config.componentAttributes) : config.componentAttributes;
		const configurationToPass: any = {};
		configurationToPass.containerId = config.containerId;
		configurationToPass.containerSourceId = configProperties.smarteditContainerSourceId;
		configurationToPass.slotId = config.slotId;
		configurationToPass.actionId = configProperties.smarteditPersonalizationActionId || null;
		configurationToPass.selectedVariationCode = configProperties.smarteditPersonalizationVariationId || null;
		configurationToPass.selectedCustomizationCode = configProperties.smarteditPersonalizationCustomizationId || null;
		const componentCatalog = configProperties.smarteditCatalogVersionUuid.split('\/');
		configurationToPass.componentCatalog = componentCatalog[0];
		configurationToPass.componentCatalogVersion = componentCatalog[1];
		const contextCustomization = this.getSelectedCustomization(configurationToPass.selectedCustomizationCode);
		configurationToPass.catalog = contextCustomization.catalog;
		configurationToPass.catalogVersion = contextCustomization.catalogVersion;
		configurationToPass.slotsToRefresh = this.getSlotsToRefresh(configProperties.smarteditContainerSourceId);

		this.personalizationsmarteditContextMenuServiceProxy.openDeleteAction(configurationToPass);
	}

	openAddAction(config: any) {
		const configProperties = this.lodash.isString(config.componentAttributes) ? JSON.parse(config.componentAttributes) : config.componentAttributes;
		const configurationToPass: any = {};
		configurationToPass.componentType = config.componentType;
		configurationToPass.componentId = config.componentId;
		configurationToPass.containerId = config.containerId;
		configurationToPass.containerSourceId = configProperties.smarteditContainerSourceId;
		configurationToPass.slotId = config.slotId;
		configurationToPass.actionId = configProperties.smarteditPersonalizationActionId || null;
		configurationToPass.selectedVariationCode = this.getSelectedVariationCode();
		const componentCatalog = configProperties.smarteditCatalogVersionUuid.split('\/');
		configurationToPass.componentCatalog = componentCatalog[0];
		const contextCustomization = this.getSelectedCustomization();
		configurationToPass.catalog = contextCustomization.catalog;
		configurationToPass.selectedCustomizationCode = contextCustomization.code;
		const slot = this.personalizationsmarteditComponentHandlerService.getParentSlotForComponent(config.element);
		const slotCatalog = this.personalizationsmarteditComponentHandlerService.getCatalogVersionUuid(slot).split('\/');
		configurationToPass.slotCatalog = slotCatalog[0];
		configurationToPass.slotsToRefresh = this.getSlotsToRefresh(configProperties.smarteditContainerSourceId);
		configurationToPass.slotsToRefresh.push(config.slotId);

		return this.personalizationsmarteditContextMenuServiceProxy.openAddAction(configurationToPass);
	}

	openEditAction(config: any) {
		const configProperties = this.lodash.isString(config.componentAttributes) ? JSON.parse(config.componentAttributes) : config.componentAttributes;
		const configurationToPass: any = {};
		configurationToPass.componentType = config.componentType;
		configurationToPass.componentId = config.componentId;
		configurationToPass.containerId = config.containerId;
		configurationToPass.containerSourceId = configProperties.smarteditContainerSourceId;
		configurationToPass.slotId = config.slotId;
		configurationToPass.actionId = configProperties.smarteditPersonalizationActionId || null;
		configurationToPass.selectedVariationCode = configProperties.smarteditPersonalizationVariationId || null;
		configurationToPass.selectedCustomizationCode = configProperties.smarteditPersonalizationCustomizationId || null;
		configurationToPass.componentUuid = configProperties.smarteditComponentUuid || null;
		configurationToPass.slotsToRefresh = this.getSlotsToRefresh(configProperties.smarteditContainerSourceId);

		return this.personalizationsmarteditContextMenuServiceProxy.openEditAction(configurationToPass);
	}

	openEditComponentAction(config: any) {
		const configProperties = this.lodash.isString(config.componentAttributes) ? JSON.parse(config.componentAttributes) : config.componentAttributes;
		const configurationToPass: any = {};
		configurationToPass.smarteditComponentType = configProperties.smarteditComponentType;
		configurationToPass.smarteditComponentUuid = configProperties.smarteditComponentUuid;
		configurationToPass.smarteditCatalogVersionUuid = configProperties.smarteditCatalogVersionUuid;
		return this.personalizationsmarteditContextMenuServiceProxy.openEditComponentAction(configurationToPass);
	}

	isCustomizeObjectValid(customize: any): boolean {
		return this.lodash.isObject(customize.selectedCustomization) && this.lodash.isObject(customize.selectedVariations) && !this.lodash.isArray(customize.selectedVariations);
	}

	isContextualMenuEnabled(): boolean {
		return this.isCustomizeObjectValid(this.contextCustomize) || (this.contextCombinedView.enabled && this.isCustomizeObjectValid(this.contextCombinedView.customize));
	}

	isElementHighlighted(config: any): boolean {
		if (this.contextCombinedView.enabled) {
			return this.lodash.indexOf(this.contextCombinedView.customize.selectedComponents, config.componentAttributes.smarteditContainerSourceId) > -1;
		} else {
			return this.lodash.indexOf(this.contextCustomize.selectedComponents, config.componentAttributes.smarteditContainerSourceId) > -1;
		}
	}

	isSlotInCurrentCatalog(config: any): boolean {
		const slot = this.personalizationsmarteditComponentHandlerService.getParentSlotForComponent(config.element);
		const catalogUuid = this.personalizationsmarteditComponentHandlerService.getCatalogVersionUuid(slot);
		const experienceCV = this.contextSeData.seExperienceData.catalogDescriptor.catalogVersionUuid;
		return experienceCV === catalogUuid;
	}

	isComponentInCurrentCatalog(config: any): boolean {
		const experienceCV = this.contextSeData.seExperienceData.catalogDescriptor.catalogVersionUuid;
		const componentCV = config.componentAttributes.smarteditCatalogVersionUuid;
		return experienceCV === componentCV;
	}

	isSelectedCustomizationFromCurrentCatalog(): boolean {
		const customization = this.contextCustomize.selectedCustomization || this.contextCombinedView.customize.selectedCustomization;
		if (customization) {
			return this.personalizationsmarteditUtils.isItemFromCurrentCatalog(customization, this.personalizationsmarteditContextService.getSeData());
		}
		return false;
	}

	isCustomizationFromCurrentCatalog(config: any): boolean {
		const items = this.contextCombinedView.selectedItems || [];
		let foundItem = items.filter((item: any) => {
			return item.customization.code === config.componentAttributes.smarteditPersonalizationCustomizationId && item.variation.code === config.componentAttributes.smarteditPersonalizationVariationId;
		});
		foundItem = foundItem.shift();
		if (foundItem) {
			return this.personalizationsmarteditUtils.isItemFromCurrentCatalog(foundItem.customization, this.personalizationsmarteditContextService.getSeData());
		}
		return false;
	}

	isEditPersonalizationInWorkflowAllowed(condition: boolean): boolean {
		const seConfigurationData = this.personalizationsmarteditContextService.getSeData().seConfigurationData || [];
		const isEditPersonalizationInWorkflowPropertyEnabled = seConfigurationData[PersonalizationsmarteditContextualMenuService.EDIT_PERSONALIZATION_IN_WORKFLOW] === true;
		if (isEditPersonalizationInWorkflowPropertyEnabled) {
			return condition;
		} else {
			return condition && !this.isWorkflowRunningBoolean;
		}
	}

	isPersonalizationAllowedInWorkflow(): boolean {
		this.refreshContext();
		return this.isEditPersonalizationInWorkflowAllowed(this.contextPersonalization.enabled);
	}

	isContextualMenuAddItemEnabled(config: any): boolean {
		this.refreshContext();
		let isEnabled = this.isContextualMenuEnabled();
		isEnabled = isEnabled && (!this.isElementHighlighted(config));
		isEnabled = isEnabled && this.isSlotInCurrentCatalog(config);
		isEnabled = isEnabled && this.isSelectedCustomizationFromCurrentCatalog();
		return this.isEditPersonalizationInWorkflowAllowed(isEnabled);
	}

	isContextualMenuEditItemEnabled(config: any): boolean {
		this.refreshContext();
		let isEnabled = this.contextPersonalization.enabled;
		isEnabled = isEnabled && !this.lodash.isUndefined(config.componentAttributes.smarteditPersonalizationActionId);
		isEnabled = isEnabled && this.isSlotInCurrentCatalog(config);
		isEnabled = isEnabled && (this.isSelectedCustomizationFromCurrentCatalog() || this.isCustomizationFromCurrentCatalog(config));
		return this.isEditPersonalizationInWorkflowAllowed(isEnabled);
	}

	isContextualMenuDeleteItemEnabled(config: any): boolean {
		return this.isContextualMenuEditItemEnabled(config);
	}

	isContextualMenuShowActionListEnabled(config: any): boolean {
		this.refreshContext();
		let isEnabled = !this.lodash.isUndefined(config.componentAttributes.smarteditPersonalizationActionId);
		isEnabled = isEnabled && this.contextCombinedView.enabled;
		isEnabled = isEnabled && !this.contextCombinedView.customize.selectedCustomization;
		return isEnabled;
	}

	isContextualMenuInfoEnabled(): boolean {
		this.refreshContext();
		let isEnabled = this.contextPersonalization.enabled;
		isEnabled = isEnabled && !this.lodash.isObject(this.contextCustomize.selectedVariations);
		isEnabled = isEnabled || this.lodash.isArray(this.contextCustomize.selectedVariations);
		isEnabled = isEnabled && !this.contextCombinedView.enabled;

		return isEnabled;
	}

	isContextualMenuInfoItemEnabled(): boolean {
		const isEnabled = this.isContextualMenuInfoEnabled();
		return isEnabled || !this.isEditPersonalizationInWorkflowAllowed(this.contextPersonalization.enabled);
	}

	isContextualMenuEditComponentItemEnabled(config: any): boolean {
		this.refreshContext();
		let isEnabled = this.contextPersonalization.enabled;
		isEnabled = isEnabled && !this.contextCombinedView.enabled && this.isComponentInCurrentCatalog(config);
		return isEnabled;
	}

	private getSelectedVariationCode() {
		if (this.personalizationsmarteditContextService.getCombinedView().enabled) {
			return this.personalizationsmarteditContextService.getCombinedView().customize.selectedVariations.code;
		}
		return this.personalizationsmarteditContextService.getCustomize().selectedVariations.code;
	}

	private getSelectedCustomization(customizationCode?: any) {
		if (this.personalizationsmarteditContextService.getCombinedView().enabled) {
			let customization = this.personalizationsmarteditContextService.getCombinedView().customize.selectedCustomization;
			if (!customization && customizationCode) {
				customization = this.personalizationsmarteditContextService.getCombinedView().selectedItems.filter((elem: any) => {
					return elem.customization.code === customizationCode;
				})[0].customization;
			}
			return customization;
		}
		return this.personalizationsmarteditContextService.getCustomize().selectedCustomization;
	}

	private getSlotsToRefresh(containerSourceId: any) {
		let slotsSelector = this.personalizationsmarteditComponentHandlerService.getAllSlotsSelector();
		slotsSelector += ' [data-smartedit-container-source-id="' + containerSourceId + '"]'; // space at beginning is important
		const slots = this.personalizationsmarteditComponentHandlerService.getFromSelector(slotsSelector);
		const slotIds = Array.prototype.slice.call(this.lodash.map(slots, (el: any) => {
			return this.personalizationsmarteditComponentHandlerService.getParentSlotIdForComponent(this.personalizationsmarteditComponentHandlerService.getFromSelector(el));
		}));
		return slotIds;
	}

	private updateWorkflowStatus(): void {
		this.personalizationsmarteditContextService.isCurrentPageActiveWorkflowRunning().then((result: any) => {
			this.isWorkflowRunningBoolean = result;
		});
	}
}
