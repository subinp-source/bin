/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GatewayProxied, IUrlService, SeDowngradeService, SeInjectable } from 'smarteditcommons';

/** @internal */
@SeDowngradeService(IUrlService)
@GatewayProxied('openUrlInPopup', 'path')
@SeInjectable()
export class UrlService extends IUrlService {}
