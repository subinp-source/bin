/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
function importAll(requireContext: any) {
    requireContext.keys().forEach(function(key: string) {
        requireContext(key);
    });
}

export function doImport() {
    importAll(require.context('../ysmarteditmodulecommons', true, /\.js$/));
    importAll(require.context('./', true, /\.js$/));
}
