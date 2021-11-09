/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GatewayProxied, IAuthenticationService, SeDowngradeService } from 'smarteditcommons';

@SeDowngradeService(IAuthenticationService)
@GatewayProxied()
export class AuthenticationService extends IAuthenticationService {}
