///
/// Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
///

import * as angular from "angular";
import "merchandisingsmartedit/merchandisingsmartedit_bundle.js";

angular
	.module("merchandisingsmartedit", ["smarteditServicesModule"])
	.run(function(contextualMenuService: any, sharedDataService: any) {
		"ngInject";
		const setUpContextualMenu = function() {
			contextualMenuService.addItems({
				MerchandisingCarouselComponent: [
					{
						key: "MerchandisingCarouselComponent",
						i18nKey: "Edit Strategy",
						action: {
							callback(configuration: any, event: any) {
								sharedDataService
									.get("contextDrivenServicesMerchandisingUrl")
									.then(
										function(url: string) {
											const appUrl = "https://" + url;
											window.open(appUrl);
										}.bind(this)
									);
							}
						},
						displayClass: "icon-activate"
					}
				]
			});
		};
		setUpContextualMenu();
	});
