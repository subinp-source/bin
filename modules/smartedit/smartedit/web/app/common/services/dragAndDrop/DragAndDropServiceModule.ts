/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import { DragAndDropService } from './DragAndDropService';
import { DragAndDropScrollingService } from './DragAndDropScrollingService';
import { InViewElementObserver } from './InViewElementObserver';

/**
 * The DragAndDropServiceModule contains a service that provides a rich drag and drop experience tailored for CMS operations.
 */

@NgModule({
    providers: [InViewElementObserver, DragAndDropScrollingService, DragAndDropService]
})
export class DragAndDropServiceModule {}
