///
/// Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
///

import {NgModule} from "@angular/core";
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {SeEntryModule} from "smarteditcommons";
import {MerchandisingExperienceInterceptor} from "../merchandisingsmarteditcommons";

@SeEntryModule("merchandisingsmarteditContainer")
@NgModule({
	imports: [],
	declarations: [],
	entryComponents: [],
	providers: [
		{
			provide: HTTP_INTERCEPTORS,
			useClass: MerchandisingExperienceInterceptor,
			multi: true
		}
	]
})
export class MerchandisingSmartEditContainerModule {}
