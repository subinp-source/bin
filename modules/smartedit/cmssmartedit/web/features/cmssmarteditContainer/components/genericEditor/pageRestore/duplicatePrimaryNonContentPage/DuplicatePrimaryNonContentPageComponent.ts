/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ISeComponent, SeComponent } from 'smarteditcommons';

@SeComponent({
    templateUrl: 'duplicatePrimaryNonContentPageTemplate.html',
    inputs: ['model']
})
export class DuplicatePrimaryNonContentPageComponent implements ISeComponent {
    // ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------
    public PRODUCT_PAGE = 'ProductPage';

    // ------------------------------------------------------------------------
    // Variables
    // ------------------------------------------------------------------------
    public model: any;
    public ge: any;
    public label: string;

    // ------------------------------------------------------------------------
    // Lifecycle methods
    // ------------------------------------------------------------------------
    $onInit(): void {
        this.model.replace = true;
        this.label =
            this.model.typeCode === 'PRODUCT_PAGE'
                ? 'se.cms.page.restore.category.duplicate.primaryforvariation.error'
                : 'se.cms.page.restore.product.duplicate.primaryforvariation.error';
    }
}
