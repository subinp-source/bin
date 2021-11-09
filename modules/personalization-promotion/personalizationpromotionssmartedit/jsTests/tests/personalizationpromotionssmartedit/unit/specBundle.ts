function importAll(requireContext: any) {
	requireContext
		.keys()
		.forEach(function(key: string) {
			requireContext(key);
		});
}
importAll(require.context('./features', true, /Test\.ts$/));
importAll(require.context('../../../../jsTarget/web/features/personalizationpromotionssmarteditcommons', true, /Module\.ts$/));
importAll(require.context('../../../../jsTarget/web/features/personalizationpromotionssmartedit', true, /Module\.ts$/));
