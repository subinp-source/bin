/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@angular/core';
import { GatewayProxied, IAlertService, SeDowngradeService } from 'smarteditcommons';

/** @internal */
@SeDowngradeService(IAlertService)
@GatewayProxied()
@Injectable()
export class AlertService extends IAlertService {}
