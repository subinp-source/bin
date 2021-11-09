/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * EventMessageData represents the data that can optionally be passed to the event service
 * when firing an event to show a EventMessageComponent
 */
export interface EventMessageData {
    /**
     * The new description to be displayed in the se-message
     */
    description?: string;

    /**
     * The new title to be displayed in the se-message
     */
    title?: string;
}
