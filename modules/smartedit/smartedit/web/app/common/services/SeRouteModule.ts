/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from 'smarteditcommons/di/SeModule';
import { SeRouteService } from './SeRouteService';

@SeModule({
    providers: [SeRouteService]
})
export class SeRouteModule {}
