/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule, YHelpModule } from 'smarteditcommons';
import { SynchronizationPanelComponent } from './synchronizationPanel';

@SeModule({
    imports: [YHelpModule],
    declarations: [SynchronizationPanelComponent]
})
export class SynchronizationPanelModule {}
