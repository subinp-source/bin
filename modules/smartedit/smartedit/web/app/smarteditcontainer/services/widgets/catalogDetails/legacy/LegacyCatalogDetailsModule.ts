/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from 'smarteditcommons';

import { LegacyCatalogDetailsComponent } from './components/LegacyCatalogDetailsComponent';
import { LegacyHomePageLinkComponent } from './components/LegacyHomePageLinkComponent';
import { LegacyCatalogVersionDetailsComponent } from './components/LegacyCatalogVersionDetailsComponent';

@SeModule({
    declarations: [
        LegacyCatalogDetailsComponent,
        LegacyHomePageLinkComponent,
        LegacyCatalogVersionDetailsComponent
    ]
})
export class LegacyCatalogDetailsModule {}
