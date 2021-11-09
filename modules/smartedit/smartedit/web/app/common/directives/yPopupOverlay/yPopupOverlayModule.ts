/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from '../../di';
import { SeConstantsModule } from '../../services/SeConstantsModule';
import { FunctionsModule } from '../../utils';
import { LegacySmarteditCommonsModule } from '../../LegacySmarteditCommonsModule';
import { YPopupEngineService } from './yPopupEngineService';
import { YPopupOverlayDirective } from './yPopupOverlayDirective';
import { YPopupOverlayUtilsClickOrderService } from './yPopupOverlayUtilsClickOrderService';
import { YPopupOverlayUtilsDOMCalculations } from './yPopupOverlayUtilsDOMCalculations';

@SeModule({
    imports: [LegacySmarteditCommonsModule, FunctionsModule, SeConstantsModule],
    providers: [
        YPopupEngineService,
        YPopupOverlayUtilsClickOrderService,
        YPopupOverlayUtilsDOMCalculations
    ],
    declarations: [YPopupOverlayDirective]
})
export class YPopupOverlayModule {}
