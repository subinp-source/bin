/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

import * as angular from 'angular';
import {SeComponent} from 'smarteditcommons';
import {PersonalizationsmarteditRestService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditRestService';
import {PersonalizationsmarteditMessageHandler} from 'personalizationcommons/PersonalizationsmarteditMessageHandler';
import {PaginationHelper} from 'personalizationcommons/PaginationHelper';
import {TriggerTabService} from '../multipleTriggersComponent/TriggerTabService';
import {PersonalizationsmarteditUtils} from 'personalizationcommons/PersonalizationsmarteditUtils';

@SeComponent({
	templateUrl: 'personalizationsmarteditSegmentViewTemplate.html',
	inputs: [
		'triggers',
		'expression'
	]
})
export class PersonalizationsmarteditSegmentViewComponent {

	public triggers: any;
	public expression: any;

	public elementToScroll: any;
	public scrollZoneVisible: any;
	public actions = this.personalizationsmarteditTriggerService.actions;
	public segments: [] = [];
	public segmentPagination: PaginationHelper = new PaginationHelper({});
	public highlightedContainer: any;
	public singleSegment: any;

	public treeOptions = {
		dragStart: () => {
			this.scrollZoneVisible = this.isScrollZoneVisible();
		},
		dropped: (e: any) => {
			this.scrollZoneVisible = false;
			this.removeDropzoneItem(e.dest.nodesScope.$modelValue);
			this.$timeout(() => {
				this.fixEmptyContainer(this.expression);
			}, 0);
		},
		dragMove: (e: any) => {
			this.highlightedContainer = e.dest.nodesScope.$nodeScope.$modelValue.$$hashKey;
			if (this.isScrollZoneVisible() !== this.scrollZoneVisible) {
				this.scrollZoneVisible = this.isScrollZoneVisible();
			} else if (Math.abs(this.elementToScrollHeight - this.elementToScroll.get(0).scrollHeight) > 10) {
				this.elementToScrollHeight = this.elementToScroll.get(0).scrollHeight;
				this.scrollZoneVisible = false;
			}
		}
	};

	private moreSegmentRequestProcessing: boolean = false;
	private dropzoneItem = {
		type: 'dropzone'
	};
	private initExpression = [{
		type: 'container',
		operation: this.actions[0],
		nodes: [this.dropzoneItem]
	}];
	private segmentFilter = {
		code: ''
	};
	private elementToDuplicate: any = null;
	private _elementToScrollHeight = 0;

	get elementToScrollHeight(): any {
		return this._elementToScrollHeight;
	}

	set elementToScrollHeight(newVal) {
		this._elementToScrollHeight = newVal;
	}

	constructor(
		private personalizationsmarteditRestService: PersonalizationsmarteditRestService,
		private personalizationsmarteditMessageHandler: PersonalizationsmarteditMessageHandler,
		private personalizationsmarteditTriggerService: any,
		private personalizationsmarteditUtils: PersonalizationsmarteditUtils,
		private $timeout: any,
		protected $translate: angular.translate.ITranslateService,
		private confirmationModalService: any,
		private triggerTabService: TriggerTabService
	) {
		this.segmentPagination.reset();
	}

	$onInit(): void {
		const triggerDataState = (this.triggerTabService.getTriggerDataState() as any);
		this.triggers = this.triggers || (triggerDataState.selectedVariation || {}).triggers;
		this.expression = this.expression || triggerDataState.expression;

		if (this.triggers && this.triggers.length > 0) {
			this.expression = this.personalizationsmarteditTriggerService.buildData(this.triggers);
		} else {
			this.expression = angular.copy(this.initExpression);
		}
		this.fixEmptyContainer(this.expression);

		triggerDataState.expression = this.expression;
		this.elementToScroll = angular.element(".se-slider-panel__body");
	}

	getElementToScroll() { // todo: consider transformation to getter/setter
		return this.elementToScroll;
	}
	removeItem(scope: any) {
		if (this.personalizationsmarteditTriggerService.isNotEmptyContainer(scope.$modelValue) && !this.isContainerWithDropzone(scope.$modelValue)) {
			this.confirmationModalService.confirm({
				description: this.$translate.instant('personalization.modal.customizationvariationmanagement.targetgrouptab.segments.removecontainerconfirmation')
			}).then(() => {
				scope.remove();
				this.$timeout(() => {
					this.fixEmptyContainer(this.expression);
				}, 0);
			});
		} else {
			scope.remove();
			this.$timeout(() => {
				this.fixEmptyContainer(this.expression);
			}, 0);
		}
	}
	duplicateItem(elementToDuplicate: any) {
		this.elementToDuplicate = elementToDuplicate;
		this.expression[0].nodes.some(this.findElementAndDuplicate, this);
	}
	toggle(scope: any) {
		scope.toggle();
	}
	newSubItem(scope: any, type: any) {
		const nodeData = scope.$modelValue;
		this.removeDropzoneItem(nodeData.nodes);
		nodeData.nodes.unshift({
			type,
			operation: (type === 'item' ? '' : this.actions[0]),
			nodes: [this.dropzoneItem]
		});
		scope.expand();
		this.$timeout(() => {
			const childArray = scope.childNodes();
			childArray[0].expand();
		}, 0);
	}
	segmentSearchInputKeypress(keyEvent: any, searchObj: any) {
		if (keyEvent && ([37, 38, 39, 40].indexOf(keyEvent.which) > -1)) { // keyleft, keyup, keyright, keydown
			return;
		}
		this.segmentPagination.reset();
		this.segmentFilter.code = searchObj;
		this.segments.length = 0;
		this.addMoreSegmentItems();
	}
	segmentSelectedEvent(item: any) {
		if (!item) {
			return;
		}
		this.expression[0].nodes.unshift({
			type: 'item',
			operation: '',
			selectedSegment: item,
			nodes: []
		});
		this.singleSegment = null;
		this.highlightedContainer = this.expression[0].$$hashKey;
		this.removeDropzoneItem(this.expression[0].nodes);
	}
	addMoreSegmentItems() {
		if (this.segmentPagination.getPage() < this.segmentPagination.getTotalPages() - 1 && !this.moreSegmentRequestProcessing) {
			this.moreSegmentRequestProcessing = true;
			this.personalizationsmarteditRestService.getSegments(this.getSegmentsFilterObject()).then((response: any) => {
				this.personalizationsmarteditUtils.uniqueArray(this.segments, response.segments || []);
				this.segmentPagination = new PaginationHelper(response.pagination);
				this.moreSegmentRequestProcessing = false;
			}, () => { // callback
				this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingsegments'));
				this.moreSegmentRequestProcessing = false;
			});
		}
	}
	isTopContainer(element: any) {
		return angular.equals(this.expression[0], element.node);
	}
	isContainerWithDropzone(element: any) {
		return this.isContainer(element) && element.nodes.length === 1 && this.isDropzone(element.nodes[0]);
	}
	isItem(element: any) {
		return this.personalizationsmarteditTriggerService.isItem(element);
	}
	isDropzone(element: any) {
		return this.personalizationsmarteditTriggerService.isDropzone(element);
	}
	isContainer(element: any) {
		return this.personalizationsmarteditTriggerService.isContainer(element);
	}
	private getSegmentsFilterObject(): any {
		return {
			code: this.segmentFilter.code,
			pageSize: this.segmentPagination.getCount(),
			currentPage: this.segmentPagination.getPage() + 1
		};
	}
	private findElementAndDuplicate(element: any, index: any, array: any[]): boolean {
		// 'this' is additional argument passed to function so in this case it is the component's 'this'
		if (this.elementToDuplicate === element) {
			array.splice(index, 0, angular.copy(this.elementToDuplicate));
			return true;
		}
		if (this.isContainer(element)) {
			element.nodes.some(this.findElementAndDuplicate, this); // recursive call to check all sub containers
		}
		return false;
	}
	private removeDropzoneItem(nodes: []) {
		nodes.forEach((element, index, array) => {
			if (this.isDropzone(element)) {
				array.splice(index, 1);
			}
		});
	}
	private fixEmptyContainer(nodes: any) {
		nodes.forEach((element: any) => {
			if (this.isEmptyContainer(element)) {
				element.nodes.push(this.dropzoneItem);
			}
			if (this.isContainer(element)) {
				this.fixEmptyContainer(element.nodes);
			}
		});
	}
	private isScrollZoneVisible(): boolean {
		return this.elementToScroll.get(0).scrollHeight > this.elementToScroll.get(0).clientHeight;
	}
	private isEmptyContainer(element: any) {
		return this.isContainer(element) && element.nodes.length === 0;
	}
}