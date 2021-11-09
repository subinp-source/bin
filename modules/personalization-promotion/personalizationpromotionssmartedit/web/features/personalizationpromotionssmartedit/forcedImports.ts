function importAll(requireContext: any) {
	requireContext
		.keys()
		.forEach((key: any) => {
			requireContext(key);
		});
}

export function doImport() {
	importAll((require as any).context('./', true, /\.js$/));
	importAll((require as any).context('../personalizationpromotionssmarteditcommons', true, /\.js$/));
	importAll((require as any).context('../../', true, /.*\/featureExtensions\/.*\/personalizationpromotionssmartedit\/.*\.js$/));
}