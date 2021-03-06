/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
function importAll(requireContext: any) {
	requireContext
		.keys()
		.forEach((key: string) => {
			requireContext(key);
		});
}

export function doImport() {
	importAll(require.context('./', true, /\.js$/));
	importAll(require.context('../personalizationcommons', true, /\.js$/));
	importAll(require.context('./', true, /\.scss$/));
}
