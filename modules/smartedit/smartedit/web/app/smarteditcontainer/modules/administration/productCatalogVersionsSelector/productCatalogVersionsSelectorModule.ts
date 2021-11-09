/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from 'smarteditcommons';

import { ProductCatalogVersionsSelectorComponent } from './productCatalogVersionsSelector';
import { MultiProductCatalogVersionSelectorComponent } from './multiProductCatalogVersionSelector/multiProductCatalogVersionSelector';

@SeModule({
    imports: [
        'smarteditServicesModule',
        'modalServiceModule',
        'yLoDashModule',
        'l10nModule',
        'seDropdownModule'
    ],
    declarations: [
        ProductCatalogVersionsSelectorComponent,
        MultiProductCatalogVersionSelectorComponent
    ]
})
export class ProductCatalogVersionsSelectorModule {}
