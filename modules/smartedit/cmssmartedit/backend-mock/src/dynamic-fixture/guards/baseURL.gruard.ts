/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { CanActivate, ExecutionContext, Injectable } from '@nestjs/common';
import { Observable } from 'rxjs';
import { ConfigService } from '../services/config.service';

@Injectable()
export class BaseURLGuard implements CanActivate {
    constructor(private readonly configService: ConfigService) {}

    canActivate(context: ExecutionContext): boolean | Promise<boolean> | Observable<boolean> {
        const baseURL = this.configService.getBaseURL();
        const currentBaseURL = context
            .switchToHttp()
            .getRequest()
            .path.split('/')[1];
        return baseURL === currentBaseURL;
    }
}
