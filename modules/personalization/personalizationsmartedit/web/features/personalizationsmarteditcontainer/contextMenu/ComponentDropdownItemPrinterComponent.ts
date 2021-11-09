import {Component, Inject} from '@angular/core';
import {
	ItemComponentData,
	ITEM_COMPONENT_DATA_TOKEN
} from "smarteditcommons";

@Component({
	selector: 'component-item-printer',
	template: `
        <div class="pe-customize-component__se-select-choices-layout">
            <div class="pe-customize-component__se-select-choices-col1">
                <div class="perso-wrap-ellipsis" title="{{ data.item.name }}">{{ data.item.name }}</div>
            </div>
            <div class="pe-customize-component__se-select-choices-col2">
                <div class="perso-wrap-ellipsis" title="{{ data.item.typeCode }}">{{ data.item.typeCode }}</div>
            </div>
        </div>
    `
})
export class ComponentDropdownItemPrinterComponent {
	constructor(@Inject(ITEM_COMPONENT_DATA_TOKEN) public data: ItemComponentData) {}
}
