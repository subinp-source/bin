import {Component, Inject} from '@angular/core';
import {
	ItemComponentData,
	ITEM_COMPONENT_DATA_TOKEN
} from "smarteditcommons";

@Component({
	selector: 'catalog-version-filter-item-printer',
	template: `
        <div>
            <span class="perso__globe-icon sap-icon--globe"
                  *ngIf="data.item.isCurrentCatalog === false">
            </span>
            <span *ngIf="data.item.isCurrentCatalog === false">
                {{data.item.catalogName}} - {{data.item.catalogVersionId}}
            </span>
            <span *ngIf="data.item.isCurrentCatalog === true" [translate]="'personalization.filter.catalog.current'"></span>
        </div>
    `
})
export class CatalogVersionFilterItemPrinterComponent {
	constructor(@Inject(ITEM_COMPONENT_DATA_TOKEN) public data: ItemComponentData) {}
}
