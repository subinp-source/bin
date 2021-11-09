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
import {PersonalizationsmarteditCustomizeToolbarContextComponent} from '../../../../../web/features/personalizationsmarteditcontainer/toolbarContext/PersonalizationsmarteditCustomizeToolbarContextComponent';
import {PersonalizationsmarteditCombinedViewToolbarContextComponent} from '../../../../../web/features/personalizationsmarteditcontainer/toolbarContext/PersonalizationsmarteditCombinedViewToolbarContextComponent';
import {PersonalizationsmarteditManageCustomizationViewMenuComponent} from '../../../../../web/features/personalizationsmarteditcontainer/toolbarContext/PersonalizationsmarteditManageCustomizationViewMenuComponent';

describe('PersonalizationsmarteditToolbarContextModule', () => {

	let $rootScope: angular.IRootScopeService;
	let personalizationsmarteditCombinedViewCommonsService: jasmine.SpyObj<any>;
	let personalizationsmarteditContextService: jasmine.SpyObj<any>;
	let personalizationsmarteditContextUtils: jasmine.SpyObj<any>;
	let personalizationsmarteditManager: jasmine.SpyObj<any>;
	let personalizationsmarteditManagerView: jasmine.SpyObj<any>;
	let crossFrameEventService: jasmine.SpyObj<any>;
	const personalizationsmarteditPreviewService = jasmine.createSpy('personalizationsmarteditPreviewService');
	const SHOW_TOOLBAR_ITEM_CONTEXT = 'SHOW_TOOLBAR_ITEM_CONTEXT';
	const HIDE_TOOLBAR_ITEM_CONTEXT = 'HIDE_TOOLBAR_ITEM_CONTEXT';
	const CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY = 'CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY';
	const COMBINED_VIEW_TOOLBAR_ITEM_KEY = 'COMBINED_VIEW_TOOLBAR_ITEM_KEY';

	// Components being tested
	let customizeToolbarContextComponent: PersonalizationsmarteditCustomizeToolbarContextComponent;
	let combinedViewToolbarContextComponent: PersonalizationsmarteditCombinedViewToolbarContextComponent;
	let manageCustomizationViewMenuComponent: PersonalizationsmarteditManageCustomizationViewMenuComponent;

	// === SETUP ===
	beforeEach(angular.mock.inject((_$rootScope_: angular.IRootScopeService) => {
		$rootScope = _$rootScope_;

		personalizationsmarteditCombinedViewCommonsService = jasmine.createSpyObj('personalizationsmarteditCombinedViewCommonsService', ['updatePreview']);
		personalizationsmarteditContextService = jasmine.createSpyObj('personalizationsmarteditContextService', ['getCustomize', 'getCombinedView']);
		personalizationsmarteditContextUtils = jasmine.createSpyObj('personalizationsmarteditContextUtils', ['clearCustomizeContextAndReloadPreview', 'clearCombinedViewCustomizeContext', 'clearCombinedViewContextAndReloadPreview']);
		personalizationsmarteditManager = jasmine.createSpyObj('personalizationsmarteditManager', ['openCreateCustomizationModal']);
		personalizationsmarteditManagerView = jasmine.createSpyObj('personalizationsmarteditManagerView', ['openManagerAction']);
		crossFrameEventService = jasmine.createSpyObj('crossFrameEventService', ['publish']);

		customizeToolbarContextComponent = new PersonalizationsmarteditCustomizeToolbarContextComponent(
			$rootScope,
			personalizationsmarteditCombinedViewCommonsService,
			personalizationsmarteditContextService,
			personalizationsmarteditContextUtils,
			personalizationsmarteditPreviewService,
			crossFrameEventService,
			SHOW_TOOLBAR_ITEM_CONTEXT,
			HIDE_TOOLBAR_ITEM_CONTEXT,
			CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY
		);
		combinedViewToolbarContextComponent = new PersonalizationsmarteditCombinedViewToolbarContextComponent(
			$rootScope,
			personalizationsmarteditCombinedViewCommonsService,
			personalizationsmarteditContextService,
			personalizationsmarteditContextUtils,
			crossFrameEventService,
			SHOW_TOOLBAR_ITEM_CONTEXT,
			HIDE_TOOLBAR_ITEM_CONTEXT,
			COMBINED_VIEW_TOOLBAR_ITEM_KEY
		);
		manageCustomizationViewMenuComponent = new PersonalizationsmarteditManageCustomizationViewMenuComponent(
			personalizationsmarteditContextService,
			personalizationsmarteditContextUtils,
			personalizationsmarteditPreviewService,
			personalizationsmarteditManager,
			personalizationsmarteditManagerView
		);
	});

	it('Public API', () => {
		expect(customizeToolbarContextComponent.$onInit).toBeDefined();
		expect(customizeToolbarContextComponent.clear).toBeDefined();

		expect(combinedViewToolbarContextComponent.$onInit).toBeDefined();
		expect(combinedViewToolbarContextComponent.clear).toBeDefined();

		expect(manageCustomizationViewMenuComponent.createCustomizationClick).toBeDefined();
		expect(manageCustomizationViewMenuComponent.managerViewClick).toBeDefined();
	});

	it('manageCustomizationViewMenuComponent.createCustomizationClick triggers proper modal', () => {
		manageCustomizationViewMenuComponent.createCustomizationClick();
		expect(personalizationsmarteditManager.openCreateCustomizationModal).toHaveBeenCalled();
	});

	it('manageCustomizationViewMenuComponent.managerViewClick triggers proper modal', () => {
		manageCustomizationViewMenuComponent.managerViewClick();
		expect(personalizationsmarteditManagerView.openManagerAction).toHaveBeenCalled();
	});


});
