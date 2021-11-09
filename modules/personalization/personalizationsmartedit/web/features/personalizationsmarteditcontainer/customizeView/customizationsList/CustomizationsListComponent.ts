import * as angular from 'angular';
import {LoDashStatic} from 'lodash';
import {CrossFrameEventService, SeComponent} from 'smarteditcommons';
import {PersonalizationsmarteditContextService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter';
import {PersonalizationsmarteditRestService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditRestService';
import {PersonalizationsmarteditMessageHandler} from 'personalizationcommons/PersonalizationsmarteditMessageHandler';
import {PersonalizationsmarteditUtils} from 'personalizationcommons/PersonalizationsmarteditUtils';
import {PersonalizationsmarteditDateUtils} from 'personalizationcommons/PersonalizationsmarteditDateUtils';
import {PersonalizationsmarteditContextUtils} from 'personalizationcommons';
import {PersonalizationsmarteditPreviewService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditPreviewService';
import {PersonalizationsmarteditCustomizeViewServiceProxy} from 'personalizationsmarteditcontainer/customizeView/PersonalizationsmarteditCustomizeViewServiceOuterProxy';

@SeComponent({
	templateUrl: 'customizationsListTemplate.html',
	inputs: [
		'customizationsList',
		'requestProcessing'
	]
})
export class CustomizationsListComponent {

	public customizationsList: any[];
	private sourceContainersComponentsInfo: {};

	constructor(
		protected $q: any,
		protected $translate: angular.translate.ITranslateService,
		protected personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		protected personalizationsmarteditRestService: PersonalizationsmarteditRestService,
		protected personalizationsmarteditCommerceCustomizationService: any,
		protected personalizationsmarteditMessageHandler: PersonalizationsmarteditMessageHandler,
		protected personalizationsmarteditUtils: PersonalizationsmarteditUtils,
		protected personalizationsmarteditDateUtils: PersonalizationsmarteditDateUtils,
		protected personalizationsmarteditContextUtils: PersonalizationsmarteditContextUtils,
		protected personalizationsmarteditPreviewService: PersonalizationsmarteditPreviewService,
		protected personalizationsmarteditManager: any,
		protected personalizationsmarteditCustomizeViewServiceProxy: PersonalizationsmarteditCustomizeViewServiceProxy,
		protected systemEventService: any,
		protected crossFrameEventService: CrossFrameEventService,
		protected SHOW_TOOLBAR_ITEM_CONTEXT: any,
		protected CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY: any,
		private lodash: LoDashStatic
	) {}

	$onInit(): void {
		this.sourceContainersComponentsInfo = {};
		this.systemEventService.subscribe('CUSTOMIZATIONS_MODIFIED', () => {
			this.refreshCustomizeContext();
			return this.$q.when();
		});
	}

	initCustomization(customization: any): void {
		customization.collapsed = true;
		if ((this.personalizationsmarteditContextService.getCustomize().selectedCustomization || {}).code === customization.code) {
			customization.collapsed = false;
			this.updateCustomizationData(customization);
		}
		this.personalizationsmarteditUtils.getAndSetCatalogVersionNameL10N(customization);
	}

	editCustomizationAction(customization: any) {
		this.personalizationsmarteditContextUtils.clearCombinedViewContextAndReloadPreview(
			this.personalizationsmarteditPreviewService, this.personalizationsmarteditContextService);
		this.personalizationsmarteditManager.openEditCustomizationModal(customization.code);
	}

	customizationRowClick(customization: any, select: boolean) {
		this.clearAllSubMenu();
		customization.collapsed = !customization.collapsed;

		if (!customization.collapsed) {
			this.updateCustomizationData(customization);
		}
		if (select) {
			this.customizationClick(customization);
		}

		this.customizationsList.filter((cust) => {
			return customization.code !== cust.code;
		}).forEach((cust) => {
			cust.collapsed = true;
		});
	}

	customizationClick(customization: any) {
		const combinedView = this.personalizationsmarteditContextService.getCombinedView();
		const currentVariations = this.personalizationsmarteditContextService.getCustomize().selectedVariations;
		const visibleVariations = this.getVisibleVariations(customization);
		const customize = this.personalizationsmarteditContextService.getCustomize();
		customize.selectedCustomization = customization;
		customize.selectedVariations = visibleVariations;
		this.personalizationsmarteditContextService.setCustomize(customize);
		if (visibleVariations.length > 0) {
			const allVariations = this.personalizationsmarteditUtils.getVariationCodes(visibleVariations).join(",");
			this.getAndSetComponentsForVariation(customization.code, allVariations, customization.catalog, customization.catalogVersion);
		}

		if ((this.lodash.isObjectLike(currentVariations) && !this.lodash.isArray(currentVariations)) || combinedView.enabled) {
			this.updatePreviewTicket();
		}

		this.personalizationsmarteditContextUtils.clearCombinedViewContext(this.personalizationsmarteditContextService);
		this.crossFrameEventService.publish(this.SHOW_TOOLBAR_ITEM_CONTEXT, this.CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY);
	}

	getSelectedVariationClass(variation: any): string {
		if (this.lodash.isEqual(variation.code, (
			this.personalizationsmarteditContextService.getCustomize().selectedVariations || {}).code)) {
			return "selectedVariation";
		} else {
			return "";
		}
	}

	getSelectedCustomizationClass(customization: any): string {
		if (this.lodash.isEqual(customization.code,
			(this.personalizationsmarteditContextService.getCustomize().selectedCustomization || {}).code) &&
			this.lodash.isArray(this.personalizationsmarteditContextService.getCustomize().selectedVariations)) {
			return "selectedCustomization";
		} else {
			return "";
		}
	}

	variationClick(customization: any, variation: any): void {
		const customize = this.personalizationsmarteditContextService.getCustomize();
		customize.selectedCustomization = customization;
		customize.selectedVariations = variation;
		this.personalizationsmarteditContextService.setCustomize(customize);
		this.personalizationsmarteditContextUtils.clearCombinedViewContext(this.personalizationsmarteditContextService);
		this.getAndSetComponentsForVariation(customization.code, variation.code, customization.catalog, customization.catalogVersion);
		this.updatePreviewTicket(customization.code, [variation]);
		this.crossFrameEventService.publish(this.SHOW_TOOLBAR_ITEM_CONTEXT, this.CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY);
	}

	hasCommerceActions(variation: any): boolean {
		return this.personalizationsmarteditUtils.hasCommerceActions(variation);
	}

	getCommerceCustomizationTooltip(variation: any): string {
		return this.personalizationsmarteditUtils.getCommerceCustomizationTooltipHTML(variation);
	}

	getActivityStateForCustomization(customization: any): string {
		return this.personalizationsmarteditUtils.getActivityStateForCustomization(customization);
	}

	getActivityStateForVariation(customization: any, variation: any): string {
		return this.personalizationsmarteditUtils.getActivityStateForVariation(customization, variation);
	}

	clearAllSubMenu(): void {
		for (const customization of this.customizationsList) {
			customization.subMenu = false;
		}
	}

	getEnablementTextForCustomization(customization: any): string {
		return this.personalizationsmarteditUtils.getEnablementTextForCustomization(
			customization, 'personalization.toolbar.pagecustomizations');
	}

	getEnablementTextForVariation(variation: any): string {
		return this.personalizationsmarteditUtils.getEnablementTextForVariation(
			variation, 'personalization.toolbar.pagecustomizations');
	}

	isEnabled(item: any): boolean {
		return this.personalizationsmarteditUtils.isPersonalizationItemEnabled(item);
	}

	getDatesForCustomization(customization: any): string {
		let activityStr = "";
		let startDateStr = "";
		let endDateStr = "";

		if (customization.enabledStartDate || customization.enabledEndDate) {
			startDateStr = this.personalizationsmarteditDateUtils.formatDateWithMessage(customization.enabledStartDate);
			endDateStr = this.personalizationsmarteditDateUtils.formatDateWithMessage(customization.enabledEndDate);
			if (!customization.enabledStartDate) {
				startDateStr = " ...";
			}
			if (!customization.enabledEndDate) {
				endDateStr = "... ";
			}
			activityStr += " (" + startDateStr + " - " + endDateStr + ") ";
		}
		return activityStr;
	}

	customizationSubMenuAction(customization: any): void {
		if (!customization.subMenu) {
			this.clearAllSubMenu();
		}
		customization.subMenu = !customization.subMenu;
	}

	isCustomizationFromCurrentCatalog(customization: any): boolean {
		return this.personalizationsmarteditUtils.isItemFromCurrentCatalog(customization, this.personalizationsmarteditContextService.getSeData());
	}

	statusNotDeleted(variation: any): boolean {
		return this.personalizationsmarteditUtils.isItemVisible(variation);
	}

	private matchActionForVariation(action: any, variation: any): boolean {
		return ((action.variationCode === variation.code) &&
			(action.actionCatalog === variation.catalog) &&
			(action.actionCatalogVersion === variation.catalogVersion));
	}

	private numberOfAffectedComponentsForActions(actionsForVariation: any[]): number {
		let result = 0;
		actionsForVariation.forEach((action) => {
			result += parseInt((this.sourceContainersComponentsInfo as any)[action.containerId], 10) || 0;
		});
		return result;
	}

	private initSourceContainersComponentsInfo() {
		const deferred = this.$q.defer();
		(this.personalizationsmarteditCustomizeViewServiceProxy.getSourceContainersInfo() as any).then(
			(response: any) => {
				this.sourceContainersComponentsInfo = response;
				deferred.resolve();
			},
			() => { // error callback
				this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingnumberofaffectedcomponentsforvariation'));
				deferred.reject();
			});

		return deferred.promise;
	}

	private paginatedGetAndSetNumberOfAffectedComponentsForVariations(customization: any, currentPage: number) {

		this.personalizationsmarteditRestService.getCxCmsActionsOnPageForCustomization(customization, currentPage).then(
			(response: any) => {
				customization.variations.forEach((variation: any) => {
					const actionsForVariation = response.actions.filter((action: any) => {
						return this.matchActionForVariation(action, variation);
					});
					variation.numberOfAffectedComponents = (currentPage === 0) ? 0 : variation.numberOfAffectedComponents;
					variation.numberOfAffectedComponents += this.numberOfAffectedComponentsForActions(actionsForVariation);
				});

				const nextPage = currentPage + 1;
				if (nextPage < response.pagination.totalPages) {
					this.paginatedGetAndSetNumberOfAffectedComponentsForVariations(customization, nextPage);
				}
			},
			() => { // error callback
				this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingnumberofaffectedcomponentsforvariation'));
			});
	}

	private getAndSetNumberOfAffectedComponentsForVariations(customization: any) {
		const customize = this.personalizationsmarteditContextService.getCustomize();
		const isUpToDate: boolean = (customize.selectedComponents || []).every((componentId: keyof {}) => {
			return this.sourceContainersComponentsInfo[componentId] !== undefined;
		});
		if (!isUpToDate || customize.selectedComponents === null || this.lodash.isEqual(this.sourceContainersComponentsInfo, {})) {
			this.initSourceContainersComponentsInfo().finally(() => {
				this.paginatedGetAndSetNumberOfAffectedComponentsForVariations(customization, 0);
			});
		} else if (isUpToDate) {
			this.paginatedGetAndSetNumberOfAffectedComponentsForVariations(customization, 0);
		}
	}

	private getNumberOfAffectedComponentsForCorrespondingVariation(variationsArray: any[], variationCode: any) {
		const foundVariation = variationsArray.filter((elem) => {
			return elem.code === variationCode;
		});
		return (foundVariation[0] || {}).numberOfAffectedComponents;
	}

	private updateCustomizationData(customization: any): void {
		this.personalizationsmarteditRestService.getVariationsForCustomization(customization.code, customization).then(
			(response: any) => {
				response.variations.forEach((variation: any) => {
					variation.numberOfAffectedComponents = this.getNumberOfAffectedComponentsForCorrespondingVariation(customization.variations, variation.code);
				});
				customization.variations = response.variations || [];
				customization.variations.forEach((variation: any) => {
					variation.numberOfCommerceActions = this.personalizationsmarteditCommerceCustomizationService.getCommerceActionsCount(variation);
					variation.commerceCustomizations = this.personalizationsmarteditCommerceCustomizationService.getCommerceActionsCountMap(variation);
				});
				this.getAndSetNumberOfAffectedComponentsForVariations(customization);
			},
			() => { // error callback
				this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingcustomization'));
			});
	}

	private getVisibleVariations(customization: any) {
		return this.personalizationsmarteditUtils.getVisibleItems(customization.variations);
	}

	private getAndSetComponentsForVariation(customizationId: any, variationId: any, catalog: any, catalogVersion: any) {
		this.personalizationsmarteditRestService.getComponenentsIdsForVariation(customizationId, variationId, catalog, catalogVersion)
			.then((response: any) => {
				const customize = this.personalizationsmarteditContextService.getCustomize();
				customize.selectedComponents = response.components;
				this.personalizationsmarteditContextService.setCustomize(customize);
			}, () => { // error callback
				this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingcomponentsforvariation'));
			});
	}

	private updatePreviewTicket(customizationId?: any, variationArray?: any[]) {
		const variationKeys = this.personalizationsmarteditUtils.getVariationKey(customizationId, variationArray);
		this.personalizationsmarteditPreviewService.updatePreviewTicketWithVariations(variationKeys);
	}

	private refreshCustomizeContext(): void {
		const customize = this.lodash.cloneDeep(this.personalizationsmarteditContextService.getCustomize());
		if (customize.selectedCustomization) {
			this.personalizationsmarteditRestService.getCustomization(customize.selectedCustomization)
				.then((response: any) => {
					customize.selectedCustomization = response;
					if (customize.selectedVariations && !this.lodash.isArray(customize.selectedVariations)) {
						response.variations.filter((item: any) => {
							return customize.selectedVariations.code === item.code;
						}).forEach((variation: any) => {
							customize.selectedVariations = variation;
							if (!this.personalizationsmarteditUtils.isItemVisible(variation)) {
								customize.selectedCustomization = null;
								customize.selectedVariations = null;
								this.personalizationsmarteditPreviewService.removePersonalizationDataFromPreview();
							}
						});
					}
					this.personalizationsmarteditContextService.setCustomize(customize);
				}, () => {
					this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingcustomization'));
				});
		}
	}



}