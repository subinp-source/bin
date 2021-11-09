import * as angular from "angular";
import {SeComponent, SeValueProvider, TypedMap} from 'smarteditcommons';

export const PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER_PROVIDER: SeValueProvider = {
	provide: "PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER",
	useValue: {
		ALL: 'all',
		ONLY_THIS_PAGE: 'onlythispage'
	}
};

@SeComponent({
	templateUrl: 'pageFilterDropdownTemplate.html',
	inputs: [
		'initialValue',
		'onSelectCallback: &'
	]
})
export class PageFilterDropdownComponent {

	public initialValue: any;
	public onSelectCallback: any;
	public selectedId: any;
	public items: any[];
	fetchStrategy = {
		fetchAll: () => {
			return this.$q.when(this.items);
		}
	};

	constructor(
		protected $q: angular.IQService,
		protected PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER: TypedMap<string>
	) {
		this.onChange = this.onChange.bind(this);
	}

	$onInit(): void {
		this.items = [{
			id: this.PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER.ALL,
			label: "personalization.filter.page.all"
		}, {
			id: this.PERSONALIZATION_CUSTOMIZATION_PAGE_FILTER.ONLY_THIS_PAGE,
			label: "personalization.filter.page.onlythispage"
		}];
		this.selectedId = this.initialValue || this.items[1].id;
	}

	onChange(changes: any): void {
		this.onSelectCallback({
			value: this.selectedId
		});
	}

}