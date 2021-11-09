/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from 'smarteditcommons';
import './features/contextualMenu.scss';
import { LegacySmarteditServicesModule } from 'smartedit/services';

@SeModule({
    imports: [LegacySmarteditServicesModule]
})
export class SystemModule {}
