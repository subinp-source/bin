import * as angular from "angular";
import {LoDashStatic} from 'lodash';
import {SeInjectable} from 'smarteditcommons';
import {PersonalizationsmarteditContextService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter';
import {PaginationHelper} from 'personalizationcommons/PaginationHelper';
import {PersonalizationsmarteditMessageHandler} from 'personalizationcommons/PersonalizationsmarteditMessageHandler';
import {PersonalizationsmarteditUtils} from 'personalizationcommons/PersonalizationsmarteditUtils';
import {CustomizationDataFactory} from 'personalizationsmarteditcontainer/dataFactory/CustomizationDataFactory';

/* @internal  */
interface PersonalizationsmarteditCombinedViewConfigureControllerScope extends angular.IScope {
	selectionArray: any[];
	selectedElement: any;
	catalogFilter: any;
	selectedItems: any;
}

@SeInjectable()
export class PersonalizationsmarteditCombinedViewConfigureController {

	public init: () => void;

	public pagination: PaginationHelper;
	public moreCustomizationsRequestProcessing: boolean;
	public combinedView: any;
	public customizationFilter: any;
	public customizationPageFilter: any;

	constructor(
		protected $translate: angular.translate.ITranslateService,
		protected customizationDataFactory: CustomizationDataFactory,
		protected personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		protected personalizationsmarteditMessageHandler: PersonalizationsmarteditMessageHandler,
		protected personalizationsmarteditUtils: PersonalizationsmarteditUtils,
		protected componentMenuService: any,
		protected PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER: any,
		protected PERSONALIZATION_VIEW_STATUS_MAPPING_CODES: any,
		private $scope: PersonalizationsmarteditCombinedViewConfigureControllerScope,
		private $q: angular.IQService,
		private lodash: LoDashStatic,
		private modalManager: any
	) {

		this.init = () => {

			this.$scope.selectionArray = [];
			this.customizationDataFactory.resetData();
			this.pagination = new PaginationHelper({});
			this.pagination.reset();

			this.combinedView = this.personalizationsmarteditContextService.getCombinedView();
			this.$scope.selectedItems = [];
			this.$scope.selectedItems = this.lodash.cloneDeep(this.combinedView.selectedItems || []);
			this.$scope.selectedElement = {};

			this.moreCustomizationsRequestProcessing = false;

			this.customizationFilter = {
				name: ''
			};
			this.modalManager.setButtonHandler(this.buttonHandlerFn);

			this.$scope.$watch('selectedItems', (newValue: any, oldValue: any) => {
				this.modalManager.disableButton("confirmOk");
				if (newValue !== oldValue) {
					const combinedView = this.personalizationsmarteditContextService.getCombinedView();
					let arrayEquals = (combinedView.selectedItems || []).length === 0 && this.$scope.selectedItems.length === 0;
					arrayEquals = arrayEquals || this.lodash.isEqual(combinedView.selectedItems, this.$scope.selectedItems);
					if (!arrayEquals) {
						this.modalManager.enableButton("confirmOk");
					}
				}
			}, true);
		};

	}

	getCustomizations = (categoryFilter: any) => {
		const params = {
			filter: categoryFilter,
			dataArrayName: 'customizations'
		};
		this.customizationDataFactory.updateData(params, this.successCallback, this.errorCallback);
	}

	addMoreItems = () => {
		if (this.pagination.getPage() < this.pagination.getTotalPages() - 1 && !this.moreCustomizationsRequestProcessing) {
			this.moreCustomizationsRequestProcessing = true;
			this.getCustomizations(this.getCustomizationsFilterObject());
		}
	}

	selectElement = (item: any) => {
		if (!item) {
			return;
		}
		this.$scope.selectedItems.push(item);

		this.componentMenuService.getValidContentCatalogVersions().then((catalogVersions: any[]) => {
			const catalogsUuids = catalogVersions.map((elem: any) => {
				return elem.id;
			});
			this.$scope.selectedItems.sort((a: any, b: any) => {
				const aCatalogUuid = a.customization.catalog + "/" + a.customization.catalogVersion;
				const bCatalogUuid = b.customization.catalog + "/" + b.customization.catalogVersion;
				if (aCatalogUuid === bCatalogUuid) {
					return a.customization.rank - b.customization.rank;
				}
				return catalogsUuids.indexOf(bCatalogUuid) - catalogsUuids.indexOf(aCatalogUuid);
			});
		});

		this.$scope.selectedElement = null;
		this.searchInputKeypress(null, '');
	}

	initUiSelect = (uiSelectController: any) => {
		uiSelectController.isActive = () => {
			return false;
		};
	}

	removeSelectedItem = (item: any) => {
		this.$scope.selectedItems.splice(this.$scope.selectedItems.indexOf(item), 1);
		this.$scope.selectedElement = null;
		this.searchInputKeypress(null, '');
	}

	getClassForElement = (index: any) => {
		return this.personalizationsmarteditUtils.getClassForElement(index);
	}

	getLetterForElement = (index: any) => {
		return this.personalizationsmarteditUtils.getLetterForElement(index);
	}

	isItemInSelectDisabled = (item: any) => {
		return this.$scope.selectedItems.find((currentItem: any) => {
			return currentItem.customization.code === item.customization.code;
		});
	}

	isItemSelected = (item: any) => {
		return this.$scope.selectedItems.find((currentItem: any) => {
			return currentItem.customization.code === item.customization.code && currentItem.variation.code === item.variation.code;
		});
	}

	searchInputKeypress = (keyEvent: any, searchObj: any) => {
		if (keyEvent && ([37, 38, 39, 40].indexOf(keyEvent.which) > -1)) { // keyleft, keyup, keyright, keydown
			return;
		}
		this.pagination.reset();
		this.customizationFilter.name = searchObj;
		this.customizationDataFactory.resetData();
		this.addMoreItems();
	}

	buttonHandlerFn = (buttonId: any) => {
		const deferred = this.$q.defer();
		if (buttonId === 'confirmOk') {
			const combinedView = this.personalizationsmarteditContextService.getCombinedView();
			combinedView.selectedItems = this.$scope.selectedItems;

			if (combinedView.enabled && combinedView.customize.selectedVariations !== null && this.isCombinedViewContextPersRemoved(combinedView)) {
				combinedView.customize.selectedCustomization = null;
				combinedView.customize.selectedVariations = null;
				combinedView.customize.selectedComponents = null;
			}

			this.personalizationsmarteditContextService.setCombinedView(combinedView);
			return deferred.resolve();
		}
		return deferred.reject();
	}

	pageFilterChange = (itemId: any) => {
		this.customizationPageFilter = itemId;
		this.pagination.reset();
		this.customizationDataFactory.resetData();
		this.addMoreItems();
	}

	catalogFilterChange = (itemId: any) => {
		this.$scope.catalogFilter = itemId;
		this.pagination.reset();
		this.customizationDataFactory.resetData();
		this.addMoreItems();
	}

	isItemFromCurrentCatalog = (item: any) => {
		return this.personalizationsmarteditUtils.isItemFromCurrentCatalog(item, this.personalizationsmarteditContextService.getSeData());
	}

	getAndSetCatalogVersionNameL10N = (customization: any) => {
		return this.personalizationsmarteditUtils.getAndSetCatalogVersionNameL10N(customization);
	}

	successCallback = (response: any) => {
		this.pagination = new PaginationHelper(response.pagination);
		this.$scope.selectionArray.length = 0;
		this.customizationDataFactory.items.forEach((customization) => {
			customization.variations.filter((variation: any) => {
				return this.personalizationsmarteditUtils.isItemVisible(variation);
			}).forEach((variation: any) => {
				this.$scope.selectionArray.push({
					customization: {
						code: customization.code,
						name: customization.name,
						rank: customization.rank,
						catalog: customization.catalog,
						catalogVersion: customization.catalogVersion
					},
					variation: {
						code: variation.code,
						name: variation.name,
						catalog: variation.catalog,
						catalogVersion: variation.catalogVersion
					}
				});
			});
		});
		this.moreCustomizationsRequestProcessing = false;
	}

	errorCallback = () => {
		this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingcustomizations'));
		this.moreCustomizationsRequestProcessing = false;
	}

	private getDefaultStatus() {
		return this.personalizationsmarteditUtils.getStatusesMapping().filter((elem: any) => {
			return elem.code === this.PERSONALIZATION_VIEW_STATUS_MAPPING_CODES.ALL;
		})[0];
	}

	private getCustomizationsFilterObject() {
		const ret: any = {
			currentSize: this.pagination.getCount(),
			currentPage: this.pagination.getPage() + 1,
			name: this.customizationFilter.name,
			statuses: this.getDefaultStatus().modelStatuses,
			catalogs: this.$scope.catalogFilter
		};
		if (this.customizationPageFilter === this.PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER.ONLY_THIS_PAGE) {
			ret.pageId = this.personalizationsmarteditContextService.getSeData().pageId;
			ret.pageCatalogId = this.personalizationsmarteditContextService.getSeData().seExperienceData.pageContext.catalogId;
		}
		return ret;
	}

	private isCombinedViewContextPersRemoved(combinedView: any) {
		return combinedView.selectedItems.filter((item: any) => {
			return item.customization.code === combinedView.customize.selectedCustomization.code && item.variation.code === combinedView.customize.selectedVariations.code;
		}).length === 0;
	}

}