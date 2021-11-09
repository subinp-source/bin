import * as angular from "angular";
import {SeComponent} from 'smarteditcommons';
import {PersonalizationsmarteditContextService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter';
import {PaginationHelper} from 'personalizationcommons/PaginationHelper';
import {PersonalizationsmarteditMessageHandler} from 'personalizationcommons/PersonalizationsmarteditMessageHandler';
import {PersonalizationsmarteditUtils} from 'personalizationcommons/PersonalizationsmarteditUtils';
import {CustomizationDataFactory} from "personalizationsmarteditcontainer/dataFactory/CustomizationDataFactory";


@SeComponent({
	templateUrl: 'personalizationsmarteditCustomizeViewTemplate.html',
	inputs: [
		'isMenuOpen'
	]
})
export class PersonalizationsmarteditCustomizeViewComponent {

	public pagination: PaginationHelper;
	public moreCustomizationsRequestProcessing: boolean;
	public customizationsList: any;
	public filters: any;
	public catalogFilter: any;
	public pageFilter: any;
	public statusFilter: any;
	public nameFilter: any;
	public context: any;

	constructor(
		protected $translate: angular.translate.ITranslateService,
		protected customizationDataFactory: CustomizationDataFactory,
		protected personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		protected personalizationsmarteditMessageHandler: PersonalizationsmarteditMessageHandler,
		protected personalizationsmarteditUtils: PersonalizationsmarteditUtils,
		protected PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER: any
	) {
	}

	$onInit(): void {
		this.personalizationsmarteditContextService.refreshExperienceData();
		this.moreCustomizationsRequestProcessing = false;
		this.customizationsList = this.customizationDataFactory.items;
		this.customizationDataFactory.resetData();
		this.pagination = new PaginationHelper({});
		this.pagination.reset();

		this.filters = this.personalizationsmarteditContextService.getCustomizeFiltersState();
		this.catalogFilter = this.filters.catalogFilter;
		this.pageFilter = this.filters.pageFilter;
		this.statusFilter = this.filters.statusFilter;
		this.nameFilter = this.filters.nameFilter;
	}

	$onDestroy(): void {
		const filters = this.personalizationsmarteditContextService.getCustomizeFiltersState();
		filters.catalogFilter = this.catalogFilter;
		filters.pageFilter = this.pageFilter;
		filters.statusFilter = this.statusFilter;
		filters.nameFilter = this.nameFilter;
		this.personalizationsmarteditContextService.setCustomizeFiltersState(filters);
	}

	$onChanges(changes: any): void {
		if (changes.isMenuOpen && !changes.isMenuOpen.isFirstChange() && changes.isMenuOpen.currentValue) {
			this.refreshList();
		}
	}

	// Private methods
	errorCallback(): any {
		this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingcustomizations'));
		this.moreCustomizationsRequestProcessing = false;
	}

	successCallback(response: any): any {
		this.pagination = new PaginationHelper(response.pagination);
		this.moreCustomizationsRequestProcessing = false;
	}

	getStatus(): any {
		if (this.statusFilter === undefined) {
			return this.personalizationsmarteditUtils.getStatusesMapping()[0]; // all elements
		}
		return this.personalizationsmarteditUtils.getStatusesMapping().filter((elem: any) => {
			return elem.code === this.statusFilter;
		})[0];
	}

	getCustomizations(categoryFilter: any): void {
		const params = {
			filter: categoryFilter,
			dataArrayName: 'customizations'
		};
		this.customizationDataFactory.updateData(params, this.successCallback.bind(this), this.errorCallback.bind(this));
	}

	getCustomizationsFilterObject(): any {
		const ret = {
			currentSize: this.pagination.getCount(),
			currentPage: this.pagination.getPage() + 1,
			name: this.nameFilter,
			statuses: this.getStatus().modelStatuses,
			catalogs: this.catalogFilter
		} as any;
		if (this.pageFilter === this.PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER.ONLY_THIS_PAGE) {
			ret.pageId = this.personalizationsmarteditContextService.getSeData().pageId;
			ret.pageCatalogId = (this.personalizationsmarteditContextService.getSeData().seExperienceData.pageContext || {}).catalogId;
		}
		return ret;
	}

	refreshList(): any {
		if (this.moreCustomizationsRequestProcessing === false) {
			this.moreCustomizationsRequestProcessing = true;
			this.pagination.reset();
			this.customizationDataFactory.resetData();
			this.getCustomizations(this.getCustomizationsFilterObject());
		}
	}

	// Properties
	catalogFilterChange(itemId: string) {
		this.catalogFilter = itemId;
		this.refreshList();
	}

	pageFilterChange(itemId: string) {
		this.pageFilter = itemId;
		this.refreshList();
	}

	statusFilterChange(itemId: string) {
		this.statusFilter = itemId;
		this.refreshList();
	}

	nameInputKeypress(keyEvent: any) {
		if (keyEvent.which === 13 || this.nameFilter.length > 2 || this.nameFilter.length === 0) {
			this.refreshList();
		}
	}

	addMoreCustomizationItems() {
		if (this.pagination.getPage() < this.pagination.getTotalPages() - 1 && !this.moreCustomizationsRequestProcessing) {
			this.moreCustomizationsRequestProcessing = true;
			this.getCustomizations(this.getCustomizationsFilterObject());
		}
	}

	getPage() {
		this.context.addMoreCustomizationItems();
	}

}