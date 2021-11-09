/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Controller, Get, Param } from '@nestjs/common';
import { userGroupsList } from 'fixtures/constants/userGroups';
import { IUserGroups } from 'fixtures/entities/userGroups';

@Controller()
export class UserGroupsController {
    @Get('cmswebservices/v1/usergroups/:userGroupId')
    getUserGroupByID(@Param('userGroupId') userGroupId: string) {
        const resultUserGroup: IUserGroups | undefined = userGroupsList.find(
            (userGroup: IUserGroups) => userGroup.uid === userGroupId
        );
        return resultUserGroup ? resultUserGroup : userGroupsList[0];
    }

    @Get('cmswebservices/v1/usergroups*')
    getUserGroups() {
        return {
            pagination: {
                count: userGroupsList.length,
                page: 1,
                totalCount: userGroupsList.length,
                totalPages: 1
            },
            userGroups: userGroupsList
        };
    }
}
