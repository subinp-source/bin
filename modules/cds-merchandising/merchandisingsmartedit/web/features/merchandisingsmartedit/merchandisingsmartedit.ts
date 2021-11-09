///
/// Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
///

import {UpgradeModule} from "@angular/upgrade/static";
import {NgModule} from "@angular/core";
import {SeEntryModule} from "smarteditcommons";

@SeEntryModule("merchandisingsmartedit")
@NgModule({
	imports: [UpgradeModule],
	declarations: [],
	entryComponents: [],
	providers: []
})
export class MerchandisingSmartEditModule {}
