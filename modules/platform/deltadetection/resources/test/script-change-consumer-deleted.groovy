/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import de.hybris.deltadetection.enums.ChangeType

log.info('input resource (Media code): ' + cronjob.job.input.code)
if (change.changeType == ChangeType.DELETED) {
    changeDetectionService.consumeChanges([change])
    log.info('Consumed: ' + change)
    return true
}
log.info('Not consumed: ' + change)
true
