/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { annotationService, GatewayProxied } from 'smarteditcommons';
import { UrlService } from 'smartedit/services';

describe('test urlService ', () => {
    it('url service inits a private gateway', function() {
        expect(annotationService.getClassAnnotation(UrlService, GatewayProxied)).toEqual([
            'openUrlInPopup',
            'path'
        ]);
    });
});
