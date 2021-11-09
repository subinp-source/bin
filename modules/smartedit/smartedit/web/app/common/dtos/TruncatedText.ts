/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @internal
 *
 * @name TruncatedText
 *
 * @description
 * Model containing truncated text properties.
 */
export class TruncatedText {
    constructor(
        private text: string = '',
        private truncatedText: string = '',
        private truncated: boolean,
        private ellipsis: string = ''
    ) {
        // if text/truncatedText is null, then set its value to ""
        this.text = this.text || '';
        this.truncatedText = this.truncatedText || '';
    }

    public getUntruncatedText(): string {
        return this.text;
    }

    public getTruncatedText(): string {
        return this.truncatedText + this.ellipsis;
    }

    public isTruncated(): boolean {
        return this.truncated;
    }
}
