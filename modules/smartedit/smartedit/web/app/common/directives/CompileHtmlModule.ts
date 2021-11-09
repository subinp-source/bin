/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import { NgIncludeDirective } from './NgIncludeDirective';
import { CompileHtmlDirective } from './CompileHtmlDirective';

@NgModule({
    declarations: [NgIncludeDirective, CompileHtmlDirective],
    exports: [NgIncludeDirective, CompileHtmlDirective]
})
export class CompileHtmlModule {}
