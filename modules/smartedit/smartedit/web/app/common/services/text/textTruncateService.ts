/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lodash from 'lodash';
import { TruncatedText } from '../../dtos/TruncatedText';
import { SeDowngradeService } from '../../di';

/**
 * @internal
 *
 * @name TextTruncateService
 *
 * @description
 * Service containing truncate string functions.
 */
@SeDowngradeService()
export class TextTruncateService {
    /**
     * @name TextTruncateService#truncateToNearestWord
     * @methodOf TextTruncateService
     *
     * @description
     * Truncates text to the nearest word depending on character length. Truncates below character length.
     *
     * @param {number} limit index in text to truncate to
     * @param {string} text text to be truncated
     * @return {TruncatedText}
     */
    public truncateToNearestWord(
        limit: number,
        text: string,
        ellipsis: string = ''
    ): TruncatedText {
        if (lodash.isNil(text) || limit > text.length) {
            return new TruncatedText(text, text, false);
        }
        const regexp: RegExp = /(\s)/g;
        const truncatedGroups: RegExpMatchArray = text.match(regexp);

        let truncateIndex = 0;
        if (!truncatedGroups) {
            truncateIndex = limit;
        } else {
            for (let i = 0; i < truncatedGroups.length; i++) {
                const nextPosition: number = this.getPositionOfCharacters(
                    text,
                    truncatedGroups[i],
                    i + 1
                );
                if (nextPosition > limit) {
                    break;
                }
                truncateIndex = nextPosition;
            }
        }
        const truncated: string = text.substr(0, truncateIndex);
        return new TruncatedText(text, truncated, true, ellipsis);
    }

    private getPositionOfCharacters(
        searchString: string,
        characters: string,
        index: number
    ): number {
        return searchString.split(characters, index).join(characters).length;
    }
}
