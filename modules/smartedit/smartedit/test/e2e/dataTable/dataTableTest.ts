/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { DataTableObject } from './dataTableObject';

describe('Data Table -', () => {
    beforeEach(() => {
        DataTableObject.Actions.navigate();
    });

    describe('General - ', () => {
        it('it renders correct number of headers', () => {
            DataTableObject.Assertions.hasCorrectHeadersCount(3);
        });

        it('it renders correct number of rows', () => {
            DataTableObject.Assertions.hasCorrectRowsCount(3);
        });
    });

    describe('Angular - ', () => {
        it('renders correct number of angular component cells', () => {
            DataTableObject.Assertions.hasCorrectNgComponentsCount(3);
        });
    });

    describe('Legacy Components - ', () => {
        it('renders correct number of legacy component cells', () => {
            DataTableObject.Assertions.hasCorrectLegacyComponentsCount(3);
        });
    });

    describe('Default -', () => {
        it('renders correct number of default cells', () => {
            DataTableObject.Assertions.cellExists('col_3_row_1');
            DataTableObject.Assertions.cellExists('col_3_row_2');
            DataTableObject.Assertions.cellExists('col_3_row_2');
        });
    });
});
