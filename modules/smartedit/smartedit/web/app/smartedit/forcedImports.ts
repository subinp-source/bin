/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
function importAll(requireContext: any) {
    requireContext.keys().forEach(function(key: string) {
        requireContext(key);
    });
}

/* @internal */
export function doImport() {
    importAll(require.context('../../../', true, /templates.js$/));
    importAll(require.context('./directives', true, /\.js$/));
    importAll(require.context('./services', true, /\.js$/));
    importAll(require.context('./modules', true, /\.js$/));
}
