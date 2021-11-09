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
import {promiseHelper} from 'testhelpers';
import {ActionsDataFactory} from '../../../../../web/features/personalizationsmarteditcontainer/management/commerceCustomizationView/ActionsDataFactory';
import {PersonalizationsmarteditCommerceCustomizationService} from '../../../../../web/features/personalizationcommons/PersonalizationsmarteditCommerceCustomizationService';
import {PersonalizationsmarteditCommerceCustomizationViewControllerFactory, PersonalizationsmarteditCommerceCustomizationViewControllerScope} from "../../../../../web/features/personalizationsmarteditcontainer/management/commerceCustomizationView/PersonalizationsmarteditCommerceCustomizationViewControllerFactory";

describe('personalizationsmarteditCommerceCustomizationViewController', () => {

	const $q = promiseHelper.$q();
	let $scope: jasmine.SpyObj<PersonalizationsmarteditCommerceCustomizationViewControllerScope>;
	let $translate: jasmine.SpyObj<angular.translate.ITranslateService>;
	let personalizationsmarteditRestService: jasmine.SpyObj<any>;
	let personalizationsmarteditContextService: jasmine.SpyObj<any>;
	let actionsDataFactory: ActionsDataFactory;
	let personalizationsmarteditMessageHandler: jasmine.SpyObj<any>;
	const systemEventService = jasmine.createSpy('systemEventService');
	let personalizationsmarteditCommerceCustomizationService: jasmine.SpyObj<any>;
	let personalizationsmarteditUtils: jasmine.SpyObj<any>;
	let confirmationModalService: jasmine.SpyObj<any>;
	let modalManager: jasmine.SpyObj<any>;
	let commerceCustomizationViewController: PersonalizationsmarteditCommerceCustomizationViewControllerFactory;
	const $log = jasmine.createSpyObj('$log', ['debug']);

	const PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES = {
		OLD: 'old',
		NEW: 'new',
		DELETE: 'delete',
		UPDATE: 'update'
	};
	const mockVariation = {
		code: "testVariation"
	};
	const mockCustomization = {
		code: "testCustomization",
		variations: [mockVariation]
	};
	const mockAction = {
		type: "mock",
		code: "mock"
	};
	const mockActionWrapper = {
		action: mockAction,
		status: PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES.OLD
	};
	const mockPromotionAction = {
		type: "cxPromotionActionData",
		code: "promotion code",
		promotionId: "promotion id"
	};
	const mockPromotionActionWrapper = {
		action: mockPromotionAction
	};
	const mockComparer = (a1: any, a2: any) => {
		return a1.type === a2.type && a1.code === a2.code;
	};

	beforeEach(() => {
		$scope = jasmine.createSpyObj('$scope', ['isItemInSelectedActions', 'actions', 'removeSelectedAction', 'addAction']);
		$translate = jasmine.createSpyObj('$translate', ['instant']);
		actionsDataFactory = new ActionsDataFactory(PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES);
		personalizationsmarteditRestService = jasmine.createSpyObj('personalizationsmarteditRestService', ['getActions']);
		personalizationsmarteditRestService.getActions.and.returnValue(
			$q.defer().promise
		);
		personalizationsmarteditContextService = jasmine.createSpyObj('personalizationsmarteditContextService', ['getSeData']);
		personalizationsmarteditContextService.getSeData.and.callFake(() => {
			return {
				seConfigurationData: {}
			};
		});
		personalizationsmarteditMessageHandler = jasmine.createSpyObj('personalizationsmarteditMessageHandler', ['sendError']);
		personalizationsmarteditCommerceCustomizationService = new PersonalizationsmarteditCommerceCustomizationService();
		personalizationsmarteditUtils = jasmine.createSpyObj('personalizationsmarteditUtils', ['getEnablementTextForCustomization', 'getEnablementTextForVariation', 'getActivityStateForCustomization', 'getActivityStateForVariation']);
		confirmationModalService = jasmine.createSpyObj('confirmationModalService', ['confirm']);
		modalManager = jasmine.createSpyObj('modalManager', ['setButtonHandler', 'enableButton', 'disableButton']);

		const commerceCustomizationViewControllerFactory = PersonalizationsmarteditCommerceCustomizationViewControllerFactory(mockCustomization, mockVariation);
		commerceCustomizationViewController = new commerceCustomizationViewControllerFactory(
			$scope,
			$translate,
			$q,
			actionsDataFactory,
			personalizationsmarteditRestService,
			personalizationsmarteditMessageHandler,
			systemEventService,
			personalizationsmarteditCommerceCustomizationService,
			personalizationsmarteditContextService,
			personalizationsmarteditUtils,
			confirmationModalService,
			PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES,
			modalManager,
			$log
		);

		commerceCustomizationViewController.availableTypes = [{
			type: 'cxPromotionActionData',
			getName: (action: any) => {
				return "P - " + action.promotionId;
			}
		}];
	});

	describe('getActionsToDisplay', () => {
		it('should be empty', () => {
			// then
			expect(commerceCustomizationViewController.getActionsToDisplay).toBeDefined();
		});

		it('should contain action', () => {
			// when
			commerceCustomizationViewController.addAction(mockAction, mockComparer);

			// then
			expect(commerceCustomizationViewController.getActionsToDisplay).toBeDefined();
			expect(commerceCustomizationViewController.getActionsToDisplay().length).toBe(1);
		});

	});

	describe('displayAction', () => {
		it('should use default display', () => {
			expect(commerceCustomizationViewController.displayAction(mockActionWrapper)).toBe('mock');
		});

		it('should use promotion display', () => {
			expect(commerceCustomizationViewController.displayAction(mockPromotionActionWrapper)).toBe('P - promotion id');
		});
	});

	describe('addAction', () => {
		it('should add new item', () => {
			// given
			expect(commerceCustomizationViewController.getActionsToDisplay().length).toBe(0);

			// when
			commerceCustomizationViewController.addAction(mockAction, mockComparer);

			// then
			expect(commerceCustomizationViewController.getActionsToDisplay().length).toBe(1);
		});

		it('should ignore existing item', () => {
			// given
			expect(commerceCustomizationViewController.getActionsToDisplay().length).toBe(0);

			// when
			commerceCustomizationViewController.addAction(mockAction, mockComparer);
			commerceCustomizationViewController.addAction(mockAction, mockComparer);

			// then
			expect(commerceCustomizationViewController.getActionsToDisplay().length).toBe(1);

		});

		it('should restore item from delete queue', () => {
			// given
			expect(commerceCustomizationViewController.getActionsToDisplay().length).toBe(0);
			commerceCustomizationViewController.addAction(mockAction, mockComparer);
			expect(commerceCustomizationViewController.getActionsToDisplay().length).toBe(1);
			commerceCustomizationViewController.removeSelectedAction($scope.actions[0]);
			expect(commerceCustomizationViewController.getActionsToDisplay().length).toBe(0);

			// when
			commerceCustomizationViewController.addAction(mockAction, mockComparer);

			// then
			expect(commerceCustomizationViewController.getActionsToDisplay().length).toBe(1);
			expect(commerceCustomizationViewController.removedActions.length).toBe(0);
		});

	});

	describe('removeSelectedAction', () => {
		it('should delete item', () => {
			// given
			commerceCustomizationViewController.addAction(mockAction, mockComparer);
			expect(commerceCustomizationViewController.getActionsToDisplay().length).toBe(1);

			// when
			commerceCustomizationViewController.removeSelectedAction($scope.actions[0]);

			// then
			expect(commerceCustomizationViewController.getActionsToDisplay().length).toBe(0);
		});
	});

	describe('isDirty', () => {
		it('should not be dirty after adding exiting item', () => {
			// given
			$scope.actions.push(mockActionWrapper);

			// when
			commerceCustomizationViewController.addAction(mockAction, mockComparer);

			// then
			expect(commerceCustomizationViewController.isDirty()).toBe(false);
		});

		it('should not be dirty after removing new item', () => {
			// given
			commerceCustomizationViewController.addAction(mockAction, mockComparer);

			// when
			commerceCustomizationViewController.removeSelectedAction($scope.actions[0]);

			// then
			expect(commerceCustomizationViewController.isDirty()).toBe(false);
		});

		it('should be dirty after adding new item', () => {
			// when
			commerceCustomizationViewController.addAction(mockAction, mockComparer);

			// then
			expect(commerceCustomizationViewController.isDirty()).toBe(true);
		});

		it('should be dirty after removing exiting item', () => {
			// given
			$scope.actions.push(mockActionWrapper);

			// when
			commerceCustomizationViewController.removeSelectedAction($scope.actions[0]);

			// then
			expect(commerceCustomizationViewController.isDirty()).toBe(true);
		});
	});
});
