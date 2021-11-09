/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@nestjs/common';

@Injectable()
export class WorkflowsService {
    createDecisionComment(origActionCode: string, actionCode: string, description?: string) {
        const decisionComment = {
            authorName: 'CMS Manager',
            code: actionCode,
            createdAgoInMillis: 75752260,
            creationtime: '2019-01-23T19:37:03.567z',
            decisionCode: 'Reject',
            decisionName: 'Reject',
            originalActionCode: origActionCode
        };

        if (description) {
            Object.assign(decisionComment, { text: description });
        }

        return decisionComment;
    }

    createRegularComment(actionCode: string, description: string) {
        return {
            authorName: 'CMS Translator',
            code: actionCode,
            createdAgoInMillis: 521011030,
            text: description
        };
    }

    createAction(
        code: string,
        type: string,
        actionStatus: string,
        isCurrentActionUserParticipant: boolean
    ) {
        return {
            actionType: type,
            code,
            decisions: [
                {
                    code: code + 'Approve',
                    description: 'Approve For ' + code,
                    name: 'Approve'
                },
                {
                    code: code + 'Reject',
                    description: 'Reject For ' + code,
                    name: 'Reject'
                }
            ],
            description: 'This is ' + code,
            isCurrentUserParticipant: isCurrentActionUserParticipant,
            startedAgoInMillis: 86841180,
            name: code,
            status: actionStatus
        };
    }
}
