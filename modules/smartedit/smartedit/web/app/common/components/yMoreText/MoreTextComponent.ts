/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Input } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { zip, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

import { SeDowngradeComponent } from '../../di';
import { TextTruncateService } from '../../services';
import { TruncatedText } from '../../dtos';

/**
 * @ngdoc component
 * @name SmarteditCommonsModule.component:MoreTextComponent
 * @element se-more-text
 * @description
 * The component for truncating strings and adding an ellipsis.
 * If the limit is less then the string length then the string is truncated and 'more'/'less' buttons
 * are displayed to expand or collapse the string.
 *
 * @param {< String} text the text to be displayed
 * @param {< String =} limit index in text to truncate to. Default value is 100.
 * @param {< String =} moreLabelI18nKey the label property value for a more button. Default value is 'more'.
 * @param {< String =} lessLabelI18nKey the label property value for a less button. Default value is 'less'.
 * @param {< String =} ellipsis the ellipsis for a truncated text. Default value is an empty string.
 */

@SeDowngradeComponent()
@Component({
    selector: 'se-more-text',
    template: `
        <span id="y-more-text-payload" class="se-more-text__payload">{{ text }}</span>
        <span
            id="y-more-text-toggle"
            class="se-more-text__toggle"
            (click)="showHideMoreText()"
            *ngIf="isTruncated"
            >{{ linkLabel }}</span
        >
    `,
    providers: [TextTruncateService]
})
export class MoreTextComponent {
    @Input() text: string;
    @Input() limit?: number;
    @Input() moreLabelI18nKey?: string;
    @Input() lessLabelI18nKey?: string;
    @Input() ellipsis?: string;

    public linkLabel: string;
    public isTruncated: boolean = false;

    private showingMore: boolean = false;
    private moreLabel: string;
    private lessLabel: string;
    private truncatedText: TruncatedText;

    constructor(
        private textTruncateService: TextTruncateService,
        private translate: TranslateService
    ) {}

    ngOnInit() {
        this.limit = this.limit || 100;
        this.moreLabelI18nKey = this.moreLabelI18nKey || 'se.moretext.more.link';
        this.lessLabelI18nKey = this.lessLabelI18nKey || 'se.moretext.less.link';
        this.truncatedText = this.textTruncateService.truncateToNearestWord(
            this.limit,
            this.text,
            this.ellipsis
        );
        this.isTruncated = this.truncatedText.isTruncated();

        this.translateLabels().subscribe(() => {
            this.showHideMoreText();
        });
    }

    public showHideMoreText(): void {
        if (!this.isTruncated) {
            return;
        }

        this.text = this.showingMore
            ? this.truncatedText.getUntruncatedText()
            : this.truncatedText.getTruncatedText();
        this.linkLabel = this.showingMore ? this.lessLabel : this.moreLabel;
        this.showingMore = !this.showingMore;
    }

    private translateLabels(): Observable<string[]> {
        return zip(
            this.translate.get(this.moreLabelI18nKey),
            this.translate.get(this.lessLabelI18nKey)
        ).pipe(
            tap(([more, less]: [string, string]) => {
                this.moreLabel = more;
                this.lessLabel = less;
            })
        );
    }
}
