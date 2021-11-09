/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/*
 each file passed to the karma configuration acts as an entry point for the webpack configuration
 To avoid a resource/time issue, we use one entry point specBundle.ts instead of passing files individually to the karma configuration.
*/
function importAll(requireContext: any) {
    requireContext.keys().forEach(function(key: string) {
        requireContext(key);
    });
}
importAll(require.context('../../../jsTarget/web/app/smartedit/directives', true, /Module\.ts$/));
importAll(require.context('../../../jsTarget/web/app/smartedit/services', true, /Module\.ts$/));
importAll(require.context('./unit', true, /Test\.(js|ts)$/));
