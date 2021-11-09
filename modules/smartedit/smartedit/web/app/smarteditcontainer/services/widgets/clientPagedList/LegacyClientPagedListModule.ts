/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { LegacySmarteditCommonsModule, SeModule } from 'smarteditcommons';
import { LegacyClientPagedListComponent } from './LegacyClientPagedListComponent';
import { LegacySmarteditServicesModule } from 'smarteditcontainer/services/LegacySmarteditServicesModule';

/**
 * @ngdoc overview
 * @name LegacyClientPagedListModule
 * @deprecated since 2005, use {@link ClientPagedListModule ClientPagedListModule}
 */
@SeModule({
    imports: [
        'pascalprecht.translate',
        'ui.bootstrap',
        LegacySmarteditCommonsModule,
        LegacySmarteditServicesModule
    ],
    declarations: [LegacyClientPagedListComponent]
})
export class ClientPagedListModule {}
