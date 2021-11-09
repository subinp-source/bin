/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { CallHandler, ExecutionContext, Injectable, NestInterceptor } from '@nestjs/common';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { StorageService } from '../services/storage.service';
import { cloneDeep } from 'lodash';

@Injectable()
export class FixtureAdjustmentInterceptor implements NestInterceptor {
    constructor(private readonly storageService: StorageService) {}

    intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
        return next.handle().pipe(
            map((body: any) => {
                const request = context.switchToHttp().getRequest();
                const replacementBody = this.storageService.replaceFixture(request.originalUrl);
                if (replacementBody) {
                    return replacementBody;
                }
                const cloneBody = cloneDeep(body);
                this.storageService.modifyFixture(request.originalUrl, cloneBody);
                return cloneBody;
            })
        );
    }
}
