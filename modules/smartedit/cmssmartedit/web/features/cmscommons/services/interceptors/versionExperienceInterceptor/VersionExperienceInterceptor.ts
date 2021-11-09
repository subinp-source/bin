/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { from, Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { IExperience, ISharedDataService } from 'smarteditcommons';
import { TYPES_RESOURCE_URI } from 'cmscommons';

@Injectable()
export class VersionExperienceInterceptor implements HttpInterceptor {
    private static MODE_DEFAULT: string = 'DEFAULT';
    private static MODE_PREVIEW_VERSION: string = 'PREVIEWVERSION';
    private static PREVIEW_DATA_TYPE: string = 'PreviewData';

    constructor(private sharedDataService: ISharedDataService) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (this.isGET(request) && this.isPreviewDataTypeResourceEndpoint(request.url)) {
            return from(this.sharedDataService.get('experience')).pipe(
                switchMap((experience: IExperience) => {
                    if (experience.versionId) {
                        const newReq = request.clone({
                            url: request.url.replace(
                                VersionExperienceInterceptor.MODE_DEFAULT,
                                VersionExperienceInterceptor.MODE_PREVIEW_VERSION
                            )
                        });
                        return next.handle(newReq);
                    } else {
                        return next.handle(request);
                    }
                })
            );
        } else {
            return next.handle(request);
        }
    }

    private isGET(request: HttpRequest<any>): boolean {
        return request.method === 'GET';
    }

    private isPreviewDataTypeResourceEndpoint(url: string): boolean {
        return (
            url.indexOf(TYPES_RESOURCE_URI) > -1 &&
            url.indexOf(VersionExperienceInterceptor.PREVIEW_DATA_TYPE) > -1
        );
    }
}
