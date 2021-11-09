/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    DragAndDropService,
    GatewayFactory,
    ISharedDataService,
    MessageGateway,
    Payload,
    SeInjectable,
    SystemEventService,
    TypedMap
} from 'smarteditcommons';

@SeInjectable()
export class CmsDragAndDropService {
    private static readonly CMS_DRAG_AND_DROP_ID = 'se.cms.dragAndDrop';
    private static readonly TARGET_SELECTOR = '';
    private static readonly SOURCE_SELECTOR =
        ".smartEditComponent[data-smartedit-component-type!='ContentSlot']";
    private static readonly COMPONENT_SELECTOR = '.smartEditComponent';

    private gateway: MessageGateway;

    constructor(
        private yjQuery: JQueryStatic,
        private dragAndDropService: DragAndDropService,
        private systemEventService: SystemEventService,
        private sharedDataService: ISharedDataService,
        private ID_ATTRIBUTE: string,
        private UUID_ATTRIBUTE: string,
        private TYPE_ATTRIBUTE: string,
        private DRAG_AND_DROP_EVENTS: TypedMap<string>,
        private ENABLE_CLONE_ON_DROP: string,
        private gatewayFactory: GatewayFactory
    ) {
        this.gateway = this.gatewayFactory.createGateway('cmsDragAndDrop');
    }

    register(): void {
        this.dragAndDropService.register({
            id: CmsDragAndDropService.CMS_DRAG_AND_DROP_ID,
            sourceSelector: CmsDragAndDropService.SOURCE_SELECTOR,
            targetSelector: CmsDragAndDropService.TARGET_SELECTOR,
            startCallback: this.onStart.bind(this),
            stopCallback: this.onStop.bind(this),
            enableScrolling: false
        });
    }

    unregister(): void {
        this.dragAndDropService.unregister([CmsDragAndDropService.CMS_DRAG_AND_DROP_ID]);
    }

    apply(): void {
        this.dragAndDropService.apply(null);
    }

    update(): void {
        this.dragAndDropService.update(CmsDragAndDropService.CMS_DRAG_AND_DROP_ID);
    }

    private onStart(event: DragEvent): void {
        this.sharedDataService.get(this.ENABLE_CLONE_ON_DROP).then((cloneOnDrop: boolean) => {
            const component = this.getSelector(event.target).closest(
                CmsDragAndDropService.COMPONENT_SELECTOR
            );

            const dragInfo: Payload = {
                componentId: component.attr(this.ID_ATTRIBUTE),
                componentUuid: component.attr(this.UUID_ATTRIBUTE),
                componentType: component.attr(this.TYPE_ATTRIBUTE),
                slotUuid: null,
                slotId: null,
                cloneOnDrop
            };

            this.gateway.publish(this.DRAG_AND_DROP_EVENTS.DRAG_STARTED, dragInfo);
            this.systemEventService.publishAsync(this.DRAG_AND_DROP_EVENTS.DRAG_STARTED);
        });
    }

    private onStop(): void {
        this.gateway.publish(this.DRAG_AND_DROP_EVENTS.DRAG_STOPPED, null);
    }

    private getSelector(selector: EventTarget): JQuery<EventTarget> {
        return this.yjQuery(selector);
    }
}
