/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

export interface IPagination {
    count: number;
    hasNext?: boolean;
    hasPrevious?: boolean;
    page: number;
    totalCount: number;
    totalPages: number;
}
