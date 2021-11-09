/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// tslint:disable:max-classes-per-file
import { Component, NgModule } from '@angular/core';
import { SeDowngradeComponent, SeEntryModule, SharedComponentsModule } from 'smarteditcommons';

import { CommonModule } from '@angular/common';

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div style="position: absolute; z-index: 999; width: 100vw; height: 100px; padding: 10px">
            <se-more-text
                id="yMoreTextLimitLessThenText"
                [limit]="10"
                [text]="'hello, how are you? What time is it now?'"
                [ellipsis]="'.....'"
            ></se-more-text>
            <br />
            <se-more-text
                id="yMoreTextLimitMoreThenText"
                [limit]="100"
                [text]="'hello, how are you? What time is it now?'"
                [moreLabelI18nKey]="'se.moretext.custom.more.link'"
                [lessLabelI18nKey]="'se.moretext.custom.less.link'"
            ></se-more-text>
            <br />
            <se-more-text
                id="yMoreTextWithCustomLinks"
                [limit]="10"
                [text]="'hello, how are you? What time is it now?'"
                [moreLabelI18nKey]="'se.moretext.custom.more.link'"
                [lessLabelI18nKey]="'se.moretext.custom.less.link'"
            ></se-more-text>
        </div>
    `
})
export class AppRootComponent {}

@SeEntryModule('moreTextApp')
@NgModule({
    imports: [SharedComponentsModule, CommonModule],
    declarations: [AppRootComponent],
    entryComponents: [AppRootComponent]
})
export class MoreTextAppNg {}
