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

@SeComponent({
	templateUrl: 'personalizationsmarteditSegmentExpressionAsHtmlTemplate.html',
	inputs: [
		'segmentExpression'
	]
})
export class PersonalizationsmarteditSegmentExpressionAsHtmlComponent {

	private _segmentExpression = {};
	private _operators = ['AND', 'OR', 'NOT'];
	private _emptyGroup = '[]';
	private _emptyGroupAndOperators = this.operators.concat(this.emptyGroup);

	constructor(
		private personalizationsmarteditTriggerService: any
	) {
	}

	get segmentExpression(): any {
		return this._segmentExpression;
	}

	set segmentExpression(newVal: any) {
		this._segmentExpression = newVal;
	}

	get operators(): any {
		return this._operators;
	}

	get emptyGroup(): any {
		return this._emptyGroup;
	}

	get emptyGroupAndOperators(): any {
		return this._emptyGroupAndOperators;
	}

	// Methods

	// A segmentExpression parameter can be 'variation.triggers' object or 'segmentExpression' object
	// If variations.triggers is passed it will converted to segmentExpression
	getExpressionAsArray(): [] {
		if (angular.isDefined(this.segmentExpression) && !angular.isDefined(this.segmentExpression.operation)) {
			this.segmentExpression = this.personalizationsmarteditTriggerService.buildData(this.segmentExpression)[0];
		}
		return this.personalizationsmarteditTriggerService.getExpressionAsString(this.segmentExpression).split(" ");
	}

	getLocalizationKeyForOperator(operator: string): string {
		return 'personalization.modal.customizationvariationmanagement.targetgrouptab.expression.' + operator.toLowerCase();
	}
}