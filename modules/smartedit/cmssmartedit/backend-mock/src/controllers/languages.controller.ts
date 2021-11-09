/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Controller, Get } from '@nestjs/common';

@Controller()
export class LanguagesController {
    @Get('cmswebservices/v1/sites/:siteUID/languages')
    getLanguages() {
        return {
            languages: [
                {
                    nativeName: 'English',
                    isocode: 'en',
                    name: 'English',
                    required: true
                },
                {
                    nativeName: 'French',
                    isocode: 'fr',
                    required: false
                },
                {
                    nativeName: 'Italian',
                    isocode: 'it'
                },
                {
                    nativeName: 'Polish',
                    isocode: 'pl'
                },
                {
                    nativeName: 'Hindi',
                    isocode: 'hi'
                }
            ]
        };
    }

    @Get('smarteditwebservices/v1/i18n/languages')
    getI18NLanguages() {
        return {
            languages: [
                {
                    isoCode: 'en',
                    name: 'English'
                },
                {
                    isoCode: 'fr',
                    name: 'French'
                }
            ]
        };
    }
}
