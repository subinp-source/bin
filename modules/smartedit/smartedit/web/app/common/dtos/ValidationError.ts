/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc object
 * @name ValidationError.object:ValidationError
 * @description
 * An object representing the backend response for any erroy of type "ValidationError"
 */
export interface ValidationError {
    language?: string;
    message: string;
    reason: string;
    subject: string;
    subjectType: string;
    errorCode: string;
    type: 'ValidationError';
}
