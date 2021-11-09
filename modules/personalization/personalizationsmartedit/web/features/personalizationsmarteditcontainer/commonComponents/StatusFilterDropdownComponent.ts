import * as angular from 'angular';
import {SeComponent} from 'smarteditcommons';
import {PersonalizationsmarteditUtils} from 'personalizationcommons';

@SeComponent({
	templateUrl: 'pageFilterDropdownTemplate.html',
	inputs: [
		'initialValue',
		'onSelectCallback: &'
	]
})
export class StatusFilterDropdownComponent {

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
		protected personalizationsmarteditUtils: PersonalizationsmarteditUtils
	) {
		this.onChange = this.onChange.bind(this);
	}

	$onInit(): void {
		this.items = this.personalizationsmarteditUtils.getStatusesMapping().map((elem: any) => {
			return {
				id: elem.code,
				label: elem.text,
				modelStatuses: elem.modelStatuses
			};
		});
		this.selectedId = this.initialValue || this.items[0].id;
	}

	onChange(changes: any): void {
		this.onSelectCallback({
			value: this.selectedId
		});
	}
}

