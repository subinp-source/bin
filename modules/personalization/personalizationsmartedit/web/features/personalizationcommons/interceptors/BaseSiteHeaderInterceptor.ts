/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {from, Observable} from 'rxjs';
import {switchMap} from 'rxjs/operators';
import {IExperience, ISharedDataService} from "smarteditcommons";

@Injectable()
export class BaseSiteHeaderInterceptor implements HttpInterceptor {

	private static HEADER_NAME: string = 'Basesite';
	private static PERSONALIZATION_ENDPOINT: RegExp = /\/personalizationwebservices/;

	constructor(private sharedDataService: ISharedDataService) {}

	intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		if (BaseSiteHeaderInterceptor.PERSONALIZATION_ENDPOINT.test(request.url)) {
			return from(this.sharedDataService.get('experience')).pipe(
				switchMap((experience: IExperience) => {
					if (experience.catalogDescriptor.siteId) {
						const newReq = request.clone({
							headers: request.headers.set(BaseSiteHeaderInterceptor.HEADER_NAME, experience.catalogDescriptor.siteId)
						});
						return next.handle(newReq);
					}
					return next.handle(request);
				})
			);
		} else {
			return next.handle(request);
		}
	}

}