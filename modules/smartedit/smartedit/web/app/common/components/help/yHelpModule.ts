/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeModule } from '../../di';
import { YHelpComponent } from './yHelp';

@SeModule({
    imports: ['yPopoverModule'],
    declarations: [YHelpComponent]
})
export class YHelpModule {}
