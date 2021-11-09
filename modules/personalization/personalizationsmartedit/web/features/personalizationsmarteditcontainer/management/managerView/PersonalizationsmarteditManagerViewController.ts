import * as angular from "angular";
import {LoDashStatic} from 'lodash';
import {SeInjectable} from 'smarteditcommons';
import {PersonalizationsmarteditContextService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter';
import {PersonalizationsmarteditMessageHandler} from 'personalizationcommons/PersonalizationsmarteditMessageHandler';
import {PersonalizationsmarteditUtils} from 'personalizationcommons/PersonalizationsmarteditUtils';
import {PersonalizationsmarteditManagerViewUtilsService} from 'personalizationsmarteditcontainer/management/managerView/PersonalizationsmarteditManagerViewUtilsService';
import {PersonalizationsmarteditCommerceCustomizationService} from 'personalizationcommons/PersonalizationsmarteditCommerceCustomizationService';
import {PaginationHelper} from 'personalizationcommons/PaginationHelper';
import {PersonalizationsmarteditManager} from 'personalizationsmarteditcontainer/management/manageCustomizationView/PersonalizationsmarteditManager';
import {PersonalizationsmarteditCommerceCustomizationView} from 'personalizationsmarteditcontainer/management/commerceCustomizationView/PersonalizationsmarteditCommerceCustomizationViewService';
import {PersonalizationsmarteditDateUtils} from 'personalizationcommons/PersonalizationsmarteditDateUtils';

/* @internal  */
export interface PersonalizationsmarteditManagerViewControllerScope extends angular.IScope {
	scrollZoneElement: any;
	scrollZoneVisible: boolean;
	filteredCustomizationsCount: number;
}

@SeInjectable()
export class PersonalizationsmarteditManagerViewController {

	public init: () => void;

	public treeOptions: any;
	public catalogName: string;
	public statuses: any;
	public moreCustomizationsRequestProcessing: boolean;
	public pagination: PaginationHelper;
	public customizations: any;
	public search: any;
	public state: any = {
		uncollapsedCustomizations: []
	};
	public context: any;

	constructor(
		public $scope: PersonalizationsmarteditManagerViewControllerScope,
		private $translate: angular.translate.ITranslateService,
		private $q: angular.IQService,
		private lodash: LoDashStatic,
		private personalizationsmarteditManagerViewUtilsService: PersonalizationsmarteditManagerViewUtilsService,
		private personalizationsmarteditMessageHandler: PersonalizationsmarteditMessageHandler,
		private personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		private personalizationsmarteditUtils: PersonalizationsmarteditUtils,
		private personalizationsmarteditManager: PersonalizationsmarteditManager,
		private personalizationsmarteditCommerceCustomizationService: PersonalizationsmarteditCommerceCustomizationService,
		private personalizationsmarteditCommerceCustomizationView: PersonalizationsmarteditCommerceCustomizationView,
		private personalizationsmarteditDateUtils: PersonalizationsmarteditDateUtils,
		private PERSONALIZATION_VIEW_STATUS_MAPPING_CODES: any,
		public PERSONALIZATION_MODEL_STATUS_CODES: any,
		private waitDialogService: any,
		private $timeout: any,
		private systemEventService: any
	) {

		const seExperienceData = this.personalizationsmarteditContextService.getSeData().seExperienceData;
		const currentLanguageIsocode = seExperienceData.languageDescriptor.isocode;
		this.$scope.scrollZoneVisible = false;
		this.catalogName = seExperienceData.catalogDescriptor.name[currentLanguageIsocode];
		this.catalogName += " - " + seExperienceData.catalogDescriptor.catalogVersion;
		this.statuses = this.personalizationsmarteditUtils.getStatusesMapping();
		this.customizations = [];
		this.$scope.filteredCustomizationsCount = 0;

		this.search = {
			name: '',
			status: this.getDefaultStatus()
		};

		this.moreCustomizationsRequestProcessing = false;
		this.pagination = new PaginationHelper();
		this.pagination.reset();

		this.treeOptions = {
			dragStart: (e: any) => {
				this.$scope.scrollZoneVisible = this.isScrollZoneVisible();
				e.source.nodeScope.$modelValue.isDragging = true;
			},
			dragStop: (e: any) => {
				e.source.nodeScope.$modelValue.isDragging = undefined;
			},
			dropped: (e: any) => {
				this.$scope.scrollZoneVisible = false;
				const item = e.source.nodeScope.$modelValue;

				if (e.source.index === e.dest.index) {
					return; // Element didn't change position
				} else if (item.variations) { // Customization
					this.droppedCustomization(item, e);
				} else { // Target group
					this.droppedTargetGroup(item, e);
				}
			},
			accept: (sourceNodeScope: any, destNodesScope: any) => {
				if (this.lodash.isArray(destNodesScope.$modelValue) && destNodesScope.$modelValue.indexOf(sourceNodeScope.$modelValue) > -1) {
					return true;
				}
				return false;
			}
		};

		this.init = () => {
			this.systemEventService.subscribe('CUSTOMIZATIONS_MODIFIED', () => {
				this.refreshGrid();
				return this.$q.when();
			});
		};

		this.state.uncollapsedCustomizations = [];
	}

	public searchInputKeypress = (keyEvent: any) => {
		if (keyEvent.which === 13 || this.search.name.length > 2 || this.search.name.length === 0) {
			this.refreshGrid();
		}
	}

	public addMoreItems = () => {
		if (this.pagination.getPage() < this.pagination.getTotalPages() - 1 && !this.moreCustomizationsRequestProcessing) {
			this.moreCustomizationsRequestProcessing = true;
			this.getCustomizations(this.getCustomizationsFilterObject());
		}
	}

	public getElementToScroll = () => {
		return this.$scope.scrollZoneElement.children();
	}

	public setScrollZoneElement = (element: any) => {
		this.$scope.scrollZoneElement = element;
	}

	public openNewModal = () => {
		this.personalizationsmarteditManager.openCreateCustomizationModal();
	}

	public isSearchGridHeaderHidden = () => {
		return this.$scope.scrollZoneElement.children().scrollTop() >= 120;
	}

	public scrollZoneReturnToTop = () => {
		this.$scope.scrollZoneElement.children().animate({
			scrollTop: 0
		}, 500);
	}

	public isReturnToTopButtonVisible = () => {
		return this.$scope.scrollZoneElement.children().scrollTop() > 50;
	}

	public isFilterEnabled = () => {
		return this.search.name !== '' || this.search.status !== this.getDefaultStatus();
	}

	public setCustomizationRank = (customization: any, increaseValue: number) => {
		this.personalizationsmarteditManagerViewUtilsService.setCustomizationRank(customization, increaseValue, this.customizations);
	}

	public setVariationRank = (customization: any, variation: any, increaseValue: number) => {
		this.personalizationsmarteditManagerViewUtilsService.setVariationRank(customization, variation, increaseValue);
	}

	public refreshGrid = () => {
		this.pagination.reset();
		this.customizations.length = 0;
		this.addMoreItems();
	}

	public editCustomizationAction = (customization: any) => {
		this.personalizationsmarteditManager.openEditCustomizationModal(customization.code, null);
	}

	public editVariationAction = (customization: any, variation: any) => {
		this.personalizationsmarteditManager.openEditCustomizationModal(customization.code, variation.code);
	}

	public deleteCustomizationAction = (customization: any) => {
		this.personalizationsmarteditManagerViewUtilsService.deleteCustomizationAction(customization, this.customizations);
	}

	public isCommerceCustomizationEnabled = () => {
		return this.personalizationsmarteditCommerceCustomizationService.isCommerceCustomizationEnabled(this.personalizationsmarteditContextService.getSeData().seConfigurationData);
	}

	public manageCommerceCustomization = (customization: any, variation: any) => {
		this.personalizationsmarteditCommerceCustomizationView.openCommerceCustomizationAction(customization, variation);
	}

	public isDeleteVariationEnabled = (customization: any) => {
		return this.personalizationsmarteditUtils.getVisibleItems(customization.variations).length > 1;
	}

	public deleteVariationAction = (customization: any, variation: any, $event: any) => {
		if (this.isDeleteVariationEnabled(customization)) {
			this.personalizationsmarteditManagerViewUtilsService.deleteVariationAction(customization, variation);
		} else {
			$event.stopPropagation();
		}
	}

	public toogleVariationActive = (customization: any, variation: any) => {
		this.personalizationsmarteditManagerViewUtilsService.toogleVariationActive(customization, variation);
	}

	public customizationClickAction = (customization: any) => {
		this.personalizationsmarteditManagerViewUtilsService.customizationClickAction(customization);
	}

	public hasCommerceActions = (variation: any) => {
		return this.personalizationsmarteditUtils.hasCommerceActions(variation);
	}

	public getCommerceCustomizationTooltip = (variation: any) => {
		return this.personalizationsmarteditUtils.getCommerceCustomizationTooltipHTML(variation);
	}

	public getFormattedDate = (myDate: any) => {
		if (myDate) {
			return this.personalizationsmarteditDateUtils.formatDate(myDate, null);
		} else {
			return "";
		}
	}

	public getEnablementTextForCustomization = (customization: any) => {
		return this.personalizationsmarteditUtils.getEnablementTextForCustomization(customization, 'personalization.modal.manager');
	}

	public getEnablementTextForVariation = (variation: any) => {
		return this.personalizationsmarteditUtils.getEnablementTextForVariation(variation, 'personalization.modal.manager');
	}

	public getEnablementActionTextForVariation = (variation: any) => {
		return this.personalizationsmarteditUtils.getEnablementActionTextForVariation(variation, 'personalization.modal.manager');
	}

	public getActivityStateForCustomization = (customization: any) => {
		return this.personalizationsmarteditUtils.getActivityStateForCustomization(customization);
	}

	public getActivityStateForVariation = (customization: any, variation: any) => {
		return this.personalizationsmarteditUtils.getActivityStateForVariation(customization, variation);
	}

	public allCustomizationsCollapsed = () => {
		return this.customizations.map((elem: any) => {
			return elem.isCollapsed;
		}).reduce((previousValue: any, currentValue: any) => {
			return previousValue && currentValue;
		}, true);
	}

	public statusNotDeleted = (variation: any) => {
		return this.personalizationsmarteditUtils.isItemVisible(variation);
	}

	public initCustomization = (customization: any) => {
		if (this.state.uncollapsedCustomizations.includes(customization.code)) {
			customization.isCollapsed = false;
			this.customizationClickAction(customization);
		} else {
			customization.isCollapsed = true;
		}
	}

	public customizationCollapseAction = (customization: any) => {
		customization.isCollapsed = !customization.isCollapsed;
		if (customization.isCollapsed === false) {
			this.state.uncollapsedCustomizations.push(customization.code);
		} else {
			this.state.uncollapsedCustomizations.splice(this.state.uncollapsedCustomizations.indexOf(customization.code), 1);
		}
	}

	public getPage = () => {
		this.addMoreItems();
	}

	private isScrollZoneVisible = () => {
		return this.$scope.scrollZoneElement.children().get(0).scrollHeight > this.$scope.scrollZoneElement.children().get(0).clientHeight;
	}

	private getCustomizations = (filter: any) => {
		this.personalizationsmarteditManagerViewUtilsService.getCustomizations(filter)
			.then((response: any) => {
				this.personalizationsmarteditUtils.uniqueArray(this.customizations, response.customizations || []);
				this.$scope.filteredCustomizationsCount = response.pagination.totalCount;
				this.pagination = new PaginationHelper(response.pagination);
				this.moreCustomizationsRequestProcessing = false;
			}, () => {
				this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingcustomizations'));
				this.moreCustomizationsRequestProcessing = false;
			});
	}

	private getCustomizationsFilterObject = () => {
		return {
			active: "all",
			name: this.search.name,
			currentSize: this.pagination.getCount(),
			currentPage: this.pagination.getPage() + 1,
			statuses: this.search.status.modelStatuses
		};
	}

	private getDefaultStatus = () => {
		return this.statuses.filter((elem: any) => {
			return elem.code === this.PERSONALIZATION_VIEW_STATUS_MAPPING_CODES.ALL;
		})[0];
	}

	private updateCustomizationRank = (customizationCode: string, increaseValue: number) => {
		return this.personalizationsmarteditManagerViewUtilsService.updateCustomizationRank(customizationCode, increaseValue);
	}

	private updateVariationRank = (customization: any, variationCode: string, increaseValue: number) => {
		return this.personalizationsmarteditManagerViewUtilsService.updateVariationRank(customization, variationCode, increaseValue);
	}

	private updateRanks = (item: any, nextItem: any, itemsArray: any[], event: any, increaseValue: number) => {
		const startIndex = (increaseValue > 0) ? event.source.index : event.dest.index;
		const endIndex = (increaseValue > 0) ? event.dest.index : event.source.index;
		itemsArray[event.dest.index].rank = nextItem.rank;
		for (let i = startIndex; i <= endIndex; i++) {
			if (i !== event.dest.index) {
				itemsArray[i].rank += (increaseValue > 0) ? (-1) : 1;
			}
		}
	}

	private valideRanks = (itemsArray: any) => {
		for (let j = 0; j < itemsArray.length - 1; j++) {
			if (itemsArray[j].rank > itemsArray[j + 1].rank) {
				this.refreshGrid();
				break;
			}
		}
	}

	private droppedCustomization = (item: any, e: any) => {
		const nextItem = this.customizations[e.dest.index];
		const increaseValue = nextItem.rank - item.rank;
		if (increaseValue !== 0) {
			this.waitDialogService.showWaitModal();
			this.updateCustomizationRank(item.code, increaseValue)
				.then(() => {
					this.updateRanks(item, nextItem, this.customizations, e, increaseValue);
					this.$timeout(() => {
						this.valideRanks(this.customizations);
					}, 100);
					this.waitDialogService.hideWaitModal();
				}).catch(() => {
					this.waitDialogService.hideWaitModal();
				});
		}
	}

	private droppedTargetGroup = (item: any, e: any) => {
		const variationsArray = e.source.nodesScope.$modelValue;
		const nextItem = variationsArray[e.dest.index];
		const increaseValue = nextItem.rank - item.rank;
		const customization = e.source.nodesScope.$parent.$modelValue;
		if (increaseValue !== 0) {
			this.waitDialogService.showWaitModal();
			this.updateVariationRank(customization, item.code, increaseValue)
				.then(() => {
					this.updateRanks(item, nextItem, customization.variations, e, increaseValue);
					this.$timeout(() => {
						this.valideRanks(customization.variations);
					}, 100);
				}).finally(() => {
					this.waitDialogService.hideWaitModal();
				});
		}
	}


}
