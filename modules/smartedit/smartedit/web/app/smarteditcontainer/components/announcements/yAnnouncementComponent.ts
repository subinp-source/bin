/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeComponent } from 'smarteditcommons';
import { IAnnouncement } from 'smarteditcontainer/services/announcement/AnnouncementServiceOuter';

/**
 * @deprecated since 2005, use AnnouncementComponent
 */
@SeComponent({
    templateUrl: 'yAnnouncementComponentTemplate.html',
    inputs: ['announcement']
})
export class YAnnouncementComponent {
    public announcement: IAnnouncement;
}
