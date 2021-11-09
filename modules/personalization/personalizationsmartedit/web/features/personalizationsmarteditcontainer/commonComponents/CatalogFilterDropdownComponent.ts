import * as angular from "angular";
import {SeComponent, SeValueProvider, TypedMap} from 'smarteditcommons';

export const PERSONALIZATION_CATALOG_FILTER_PROVIDER: SeValueProvider = {
	provide: "PERSONALIZATION_CATALOG_FILTER",
	useValue: {
		ALL: 'all',
		CURRENT: 'current',
		PARENTS: 'parents'
	}
};

@SeComponent({
	templateUrl: 'pageFilterDropdownTemplate.html',
	inputs: [
		'initialValue',
		'onSelectCallback: &'
	]
})
export class CatalogFilterDropdownComponent {

	public initialValue: any;
	public onSelectCallback: any;
	public selectedId: any;
	public items: any[];
	public fetchStrategy = {
		fetchAll: () => {
			return this.$q.when(this.items);
		}
	};

	constructor(
		protected $q: angular.IQService,
		protected PERSONALIZATION_CATALOG_FILTER: TypedMap<string>
	) {
		this.onChange = this.onChange.bind(this);
	}

	$onInit(): void {
		this.items = [{
			id: this.PERSONALIZATION_CATALOG_FILTER.ALL,
			label: "personalization.filter.catalog.all"
		}, {
			id: this.PERSONALIZATION_CATALOG_FILTER.CURRENT,
			label: "personalization.filter.catalog.current"
		}, {
			id: this.PERSONALIZATION_CATALOG_FILTER.PARENTS,
			label: "personalization.filter.catalog.parents"
		}];
		this.selectedId = this.initialValue || this.items[1].id;
	}

	onChange(changes: any): void {
		this.onSelectCallback({
			value: this.selectedId
		});
	}

}