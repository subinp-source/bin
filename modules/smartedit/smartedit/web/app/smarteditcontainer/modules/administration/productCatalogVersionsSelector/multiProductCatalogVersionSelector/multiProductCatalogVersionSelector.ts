/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { FetchStrategy, IBaseCatalog, IBaseCatalogVersion, SeComponent } from 'smarteditcommons';

export interface SelectAdaptedCatalogVersion extends IBaseCatalogVersion {
    id: string;
    label: string;
}

export interface SelectAdaptedCatalog extends IBaseCatalog {
    fetchStrategy: FetchStrategy<IBaseCatalogVersion>;
    versions: SelectAdaptedCatalogVersion[];
    selectedItem: string;
}

@SeComponent({
    selector: 'multi-product-catalog-version-selector',
    templateUrl: 'multiProductCatalogVersionSelectorTemplate.html',
    inputs: ['productCatalogs', 'selectedVersions', 'onSelectionChange:&']
})
export class MultiProductCatalogVersionSelectorComponent {
    public productCatalogs: SelectAdaptedCatalog[];

    private selectedVersions: string[];
    private onSelectionChange: (data: { $selectedVersions: string[] }) => void;

    $onInit() {
        this.productCatalogs = this.productCatalogs.map((productCatalog: SelectAdaptedCatalog) => {
            const versions = productCatalog.versions.map(
                (version: SelectAdaptedCatalogVersion) => ({
                    ...version,
                    id: version.uuid,
                    label: version.version
                })
            );
            return {
                ...productCatalog,
                versions,
                fetchStrategy: {
                    fetchAll() {
                        return Promise.resolve(versions);
                    }
                },
                selectedItem: productCatalog.versions.find(
                    (version: SelectAdaptedCatalogVersion) =>
                        this.selectedVersions.indexOf(version.uuid) > -1
                ).uuid
            };
        });
    }

    public updateModel = (): void => {
        const $selectedVersions = this.productCatalogs.map(
            (productCatalog: SelectAdaptedCatalog) => productCatalog.selectedItem
        );

        this.onSelectionChange({
            $selectedVersions
        });
    };
}
