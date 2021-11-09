import * as angular from "angular";
import {CrossFrameEventService, IPermissionService, SeComponent} from 'smarteditcommons';
import {PersonalizationsmarteditContextService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter';
import {PersonalizationsmarteditMessageHandler} from 'personalizationcommons/PersonalizationsmarteditMessageHandler';
import {PersonalizationsmarteditUtils} from 'personalizationcommons/PersonalizationsmarteditUtils';
import {PersonalizationsmarteditRestService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditRestService';
import {PersonalizationsmarteditCombinedViewCommonsService} from 'personalizationsmarteditcontainer/combinedView/PersonalizationsmarteditCombinedViewCommonsService';
import {PersonalizationsmarteditContextUtils} from 'personalizationcommons/PersonalizationsmarteditContextUtils';
import {PersonalizationsmarteditPreviewService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditPreviewService';

@SeComponent({
	templateUrl: 'personalizationsmarteditCombinedViewMenuTemplate.html',
	inputs: [
		'isMenuOpen'
	]
})
export class PersonalizationsmarteditCombinedViewMenuComponent {

	public combinedView: any;
	public selectedItems: any;
	public isCombinedViewConfigured: boolean;

	constructor(
		protected $translate: angular.translate.ITranslateService,
		protected personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		protected personalizationsmarteditMessageHandler: PersonalizationsmarteditMessageHandler,
		protected personalizationsmarteditRestService: PersonalizationsmarteditRestService,
		protected personalizationsmarteditContextUtils: PersonalizationsmarteditContextUtils,
		protected personalizationsmarteditUtils: PersonalizationsmarteditUtils,
		protected personalizationsmarteditPreviewService: PersonalizationsmarteditPreviewService,
		protected personalizationsmarteditCombinedViewCommonsService: PersonalizationsmarteditCombinedViewCommonsService,
		protected crossFrameEventService: CrossFrameEventService,
		protected permissionService: IPermissionService,
		protected SHOW_TOOLBAR_ITEM_CONTEXT: any,
		protected COMBINED_VIEW_TOOLBAR_ITEM_KEY: any
	) {
	}

	$onInit(): void {
		this.combinedView = this.personalizationsmarteditContextService.getCombinedView();
		this.selectedItems = this.combinedView.selectedItems || [];
		this.isCombinedViewConfigured = this.selectedItems.length !== 0;
	}

	combinedViewClick(): void {
		this.personalizationsmarteditContextUtils.clearCustomizeContextAndReloadPreview(this.personalizationsmarteditPreviewService, this.personalizationsmarteditContextService);
		this.personalizationsmarteditCombinedViewCommonsService.openManagerAction();
	}

	getAndSetComponentsForElement(customizationId: any, variationId: any, catalog: any, catalogVersion: any) {
		this.personalizationsmarteditRestService.getComponenentsIdsForVariation(customizationId, variationId, catalog, catalogVersion)
			.then((response: any) => {
				const combinedView = this.personalizationsmarteditContextService.getCombinedView();
				combinedView.customize.selectedComponents = response.components;
				this.personalizationsmarteditContextService.setCombinedView(combinedView);
			}, () => { // error
				this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingcomponentsforvariation'));
			});
	}

	itemClick(item: any) {
		const combinedView = this.personalizationsmarteditContextService.getCombinedView();
		if (!combinedView.enabled) {
			return;
		}

		this.selectedItems.forEach((elem: any) => {
			elem.highlighted = false;
		});
		item.highlighted = true;

		combinedView.customize.selectedCustomization = item.customization;
		combinedView.customize.selectedVariations = item.variation;
		this.personalizationsmarteditContextService.setCombinedView(combinedView);
		this.permissionService.isPermitted([{
			names: ['se.edit.page']
		}]).then((roleGranted: any) => {
			if (roleGranted) {
				this.getAndSetComponentsForElement(item.customization.code, item.variation.code, item.customization.catalog, item.customization.catalogVersion);
			}
		});
		this.personalizationsmarteditCombinedViewCommonsService.updatePreview(this.personalizationsmarteditUtils.getVariationKey(item.customization.code, [item.variation]));
		this.crossFrameEventService.publish(this.SHOW_TOOLBAR_ITEM_CONTEXT, this.COMBINED_VIEW_TOOLBAR_ITEM_KEY);
	}

	getClassForElement(index: any) {
		return this.personalizationsmarteditUtils.getClassForElement(index);
	}

	getLetterForElement(index: any) {
		return this.personalizationsmarteditUtils.getLetterForElement(index);
	}

	isItemFromCurrentCatalog(item: any) {
		return this.personalizationsmarteditCombinedViewCommonsService.isItemFromCurrentCatalog(item);
	}

	clearAllCombinedViewClick() {
		this.selectedItems = [];
		this.combinedView.selectedItems = [];
		this.combinedView.enabled = false;
		this.personalizationsmarteditContextService.setCombinedView(this.combinedView);
		this.personalizationsmarteditCombinedViewCommonsService.combinedViewEnabledEvent(this.combinedView.enabled);
	}

}