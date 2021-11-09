///
/// Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
///

import {Injectable} from "@angular/core";
import {
	HttpEvent,
	HttpHandler,
	HttpInterceptor,
	HttpParams,
	HttpRequest
} from "@angular/common/http";
import {from, Observable} from "rxjs";
import {switchMap} from "rxjs/operators";
import {
	CONTEXT_SITE_ID,
	IExperience,
	ISharedDataService
} from "smarteditcommons";

@Injectable()
export class MerchandisingExperienceInterceptor implements HttpInterceptor {
	private static readonly MERCHCMSWEBSERVICES_PATH: RegExp = /\/merchandisingcmswebservices/;

	constructor(private sharedDataService: ISharedDataService) {}

	intercept(
		request: HttpRequest<any>,
		next: HttpHandler
	): Observable<HttpEvent<any>> {
		if (MerchandisingExperienceInterceptor.MERCHCMSWEBSERVICES_PATH.test(request.url)) {
			return from(this.sharedDataService.get("experience")).pipe(
				switchMap((experience: IExperience) => {
					if (experience) {
						if (request.url.indexOf(CONTEXT_SITE_ID) > -1) {
							let params: HttpParams = request.params;
							if (params) {
								params = params
									.delete("catalogId")
									.delete("catalogVersion")
									.delete("mask");
							}

							const updatedUrlRequest = request.clone({
								url: request.url.replace(
									CONTEXT_SITE_ID,
									experience.catalogDescriptor.siteId
								),
								params
							});
							return next.handle(updatedUrlRequest);
						}
					}
					return next.handle(request);
				})
			);
		} else {
			return next.handle(request);
		}
	}
}
