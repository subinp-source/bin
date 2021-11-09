/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

export interface IRestrictionsStepHandler {
    hideStep(): void;
    showStep(): void;
    isStepValid(): boolean;
    save(): angular.IPromise<void>;
    getStepId(): string;
    goToStep(): void;
}
