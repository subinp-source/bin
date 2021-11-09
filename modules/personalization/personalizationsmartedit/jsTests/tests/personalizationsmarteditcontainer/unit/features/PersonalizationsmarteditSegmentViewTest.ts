/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

import 'jasmine';
import * as angular from 'angular';
import {promiseHelper} from "../../../../../smartedit-build/test/unit/promiseHelper";
import {PersonalizationsmarteditSegmentViewComponent} from "../../../../../web/features/personalizationsmarteditcontainer/management/manageCustomizationView/segmentView/PersonalizationsmarteditSegmentViewComponent";
import {TriggerTabService} from "../../../../../web/features/personalizationsmarteditcontainer/management/manageCustomizationView/multipleTriggersComponent/TriggerTabService";

describe('Test Personalizationsmartedit Segment View Module', () => {
	const $q = promiseHelper.$q();

	const mockSegment1 = {
		code: "testSegment1"
	};
	const mockSegment2 = {
		code: "testSegment2"
	};
	const mockSegment3 = {
		code: "testSegment3"
	};
	const mockBuildData = [{
		type: 'container',
		operation: {
			id: 'OR',
			name: 'personalization.modal.customizationvariationmanagement.targetgrouptab.expression.or'
		},
		nodes: [{
			type: 'item',
			operation: '',
			selectedSegment: {
				code: 'SegmentA'
			},
			nodes: []
		}, {
			type: 'item',
			operation: '',
			selectedSegment: {
				code: 'SegmentB'
			},
			nodes: []
		}]
	}];
	const $timeout: any = jasmine.createSpy('$timeout');
	let $translate: jasmine.SpyObj<angular.translate.ITranslateService>;
	let $scope: jasmine.SpyObj<angular.IScope>;

	let personalizationsmarteditRestService: jasmine.SpyObj<any>;
	let personalizationsmarteditMessageHandler: jasmine.SpyObj<any>;
	let personalizationsmarteditTriggerService: jasmine.SpyObj<any>;
	let personalizationSmartEditUtils: jasmine.SpyObj<any>;
	const triggerTabService = new TriggerTabService();
	let confirmationModalService: jasmine.SpyObj<any>;

	let segmentViewComponent: PersonalizationsmarteditSegmentViewComponent;

	beforeEach(() => {
		$scope = jasmine.createSpyObj('$scope', ['$digest', '$apply']);
		$translate = jasmine.createSpyObj('$translate', ['instant']);
		personalizationsmarteditMessageHandler = jasmine.createSpyObj('personalizationsmarteditMessageHandler', ['sendError']);
		personalizationsmarteditTriggerService = jasmine.createSpyObj('personalizationsmarteditTriggerService', ['actions', 'buildData', 'isItem', 'isDropzone', 'isContainer']);
		personalizationsmarteditTriggerService.buildData.and.callFake(() => {
			return mockBuildData;
		});
		personalizationSmartEditUtils = jasmine.createSpyObj('personalizationSmartEditUtils', ['uniqueArray']);
		personalizationSmartEditUtils.uniqueArray.and.callFake((arr1, arr2) => {
			Array.prototype.push.apply(arr1, arr2);
		});
		confirmationModalService = jasmine.createSpyObj('confirmationModalService', ['confirm']);
		personalizationsmarteditRestService = jasmine.createSpyObj('personalizationsmarteditRestService', ['getSegments']);
		personalizationsmarteditRestService.getSegments.and.callFake(() => {
			const deferred = $q.defer();
			deferred.resolve({
				segments: [mockSegment1, mockSegment2, mockSegment3],
				pagination: {
					count: 3,
					page: 0,
					totalCount: 3,
					totalPages: 1
				}
			});
			return deferred.promise;
		});
		segmentViewComponent = new PersonalizationsmarteditSegmentViewComponent(
			personalizationsmarteditRestService,
			personalizationsmarteditMessageHandler,
			personalizationsmarteditTriggerService,
			personalizationSmartEditUtils,
			$timeout,
			$translate,
			confirmationModalService,
			triggerTabService
		);
	});

	describe('Component API', () => {

		it('should have proper api when initialized without parameters', () => {

			expect(segmentViewComponent.actions).toBeDefined();
			expect(segmentViewComponent.treeOptions).toBeDefined();
			expect(segmentViewComponent.isScrollZoneVisible).toBeDefined();
			expect(segmentViewComponent.getElementToScroll).toBeDefined();
			expect(segmentViewComponent.removeItem).toBeDefined();
			expect(segmentViewComponent.duplicateItem).toBeDefined();
			expect(segmentViewComponent.toggle).toBeDefined();
			expect(segmentViewComponent.newSubItem).toBeDefined();
			expect(segmentViewComponent.segments).toBeDefined();
			expect(segmentViewComponent.segmentPagination).toBeDefined();
			expect(segmentViewComponent.segmentFilter).toBeDefined();
			expect(segmentViewComponent.segmentSearchInputKeypress).toBeDefined();
			expect(segmentViewComponent.segmentSelectedEvent).toBeDefined();
			expect(segmentViewComponent.moreSegmentRequestProcessing).toBeDefined();
			expect(segmentViewComponent.addMoreSegmentItems).toBeDefined();
			expect(segmentViewComponent.isTopContainer).toBeDefined();
			expect(segmentViewComponent.isEmptyContainer).toBeDefined();
			expect(segmentViewComponent.isItem).toBeDefined();
			expect(segmentViewComponent.isContainer).toBeDefined();
			expect(segmentViewComponent.isDropzone).toBeDefined();
			expect(segmentViewComponent.$onInit).toBeDefined();
			expect(segmentViewComponent.expression).not.toBeDefined();

		});

		it('should have proper api when initialized with parameters', () => {
			segmentViewComponent.triggers = [];
			segmentViewComponent.expression = {};

			segmentViewComponent.$onInit();

			expect(segmentViewComponent.actions).toBeDefined();
			expect(segmentViewComponent.treeOptions).toBeDefined();
			expect(segmentViewComponent.isScrollZoneVisible).toBeDefined();
			expect(segmentViewComponent.getElementToScroll).toBeDefined();
			expect(segmentViewComponent.removeItem).toBeDefined();
			expect(segmentViewComponent.duplicateItem).toBeDefined();
			expect(segmentViewComponent.toggle).toBeDefined();
			expect(segmentViewComponent.newSubItem).toBeDefined();
			expect(segmentViewComponent.segments).toBeDefined();
			expect(segmentViewComponent.segmentPagination).toBeDefined();
			expect(segmentViewComponent.segmentFilter).toBeDefined();
			expect(segmentViewComponent.segmentSearchInputKeypress).toBeDefined();
			expect(segmentViewComponent.segmentSelectedEvent).toBeDefined();
			expect(segmentViewComponent.moreSegmentRequestProcessing).toBeDefined();
			expect(segmentViewComponent.addMoreSegmentItems).toBeDefined();
			expect(segmentViewComponent.isTopContainer).toBeDefined();
			expect(segmentViewComponent.isEmptyContainer).toBeDefined();
			expect(segmentViewComponent.isItem).toBeDefined();
			expect(segmentViewComponent.isContainer).toBeDefined();
			expect(segmentViewComponent.isDropzone).toBeDefined();
			expect(segmentViewComponent.$onInit).toBeDefined();
			expect(segmentViewComponent.expression).toBeDefined();

		});

	});

	describe('addMoreSegmentItems', () => {

		it('should be defined', () => {
			expect(segmentViewComponent.addMoreSegmentItems).toBeDefined();
		});

		it('should set properties if called', () => {
			// when
			segmentViewComponent.addMoreSegmentItems();
			$scope.$digest();

			// then
			expect(segmentViewComponent.segments).toEqual([mockSegment1, mockSegment2, mockSegment3]);
			expect(segmentViewComponent.segmentPagination.count).toBe(3);
			expect(segmentViewComponent.segmentPagination.page).toBe(0);
			expect(segmentViewComponent.segmentPagination.totalCount).toBe(3);
			expect(segmentViewComponent.segmentPagination.totalPages).toBe(1);
		});

	});

	describe('treeOptions', () => {
		it('should be defined with proper values', () => {
			expect(segmentViewComponent.treeOptions).toBeDefined();
			expect(segmentViewComponent.treeOptions.dragStart).toBeDefined();
			expect(segmentViewComponent.treeOptions.dropped).toBeDefined();
			expect(segmentViewComponent.treeOptions.dragMove).toBeDefined();
		});
	});

	describe('$onInit', () => {

		it('should be defined', () => {
			expect(segmentViewComponent.$onInit).toBeDefined();
		});

		it('should properly initialize expression if no triggers object', () => {
			// given
			segmentViewComponent.triggers = [];
			const dropzoneItem = {
				type: 'dropzone'
			};
			const initExpression = [{
				type: 'container',
				operation: segmentViewComponent.actions[0],
				nodes: [dropzoneItem]
			}];
			// when
			segmentViewComponent.$onInit();

			// then
			expect(segmentViewComponent.expression).toEqual(initExpression);
		});

		it('should properly initialize expression if triggers object passed', () => {
			const trigger = {
				type: 'segmentTriggerData',
				code: 'code',
				groupBy: 'OR',
				segments: [{
					code: 'SegmentA'
				}, {
					code: 'SegmentB'
				}]
			};

			// given
			segmentViewComponent.triggers = [trigger];

			// when
			segmentViewComponent.$onInit();

			// then
			expect(segmentViewComponent.expression).toEqual(mockBuildData);
		});

	});

});
