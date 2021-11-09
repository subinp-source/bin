/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { pageType } from './PageTypeMockData';
import { PageDisplayCondition } from './PageDisplayConditionMockData';
import { UriContextMockData } from './UriContextMockData';

export namespace Unit {
    export class MockData {
        static pageType = pageType;
        static PageDisplayCondition = PageDisplayCondition;
        static UriContext = UriContextMockData;
    }
}
