/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from '../../di';
import { YPopoverDirective } from './yPopoverDirective';
import { YPopoverPopupComponent } from './yPopoverPopupComponent';
import { SeConstantsModule } from '../../services/SeConstantsModule';
import { FunctionsModule } from '../../utils';
import { LegacySmarteditCommonsModule } from '../../LegacySmarteditCommonsModule';

@SeModule({
    imports: [
        'coretemplates',
        'yPopupOverlayModule',
        LegacySmarteditCommonsModule,
        FunctionsModule,
        SeConstantsModule
    ],
    declarations: [YPopoverDirective, YPopoverPopupComponent]
})
export class YPopoverModule {}
