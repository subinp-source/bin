/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata.builders.meta.payload

import static de.hybris.platform.integrationservices.util.JsonBuilder.json

class ScriptPayloadBuilder {

    private String code
    private String scriptType
    private String description
    private String content
    private Boolean autoDisabling
    private Boolean disabled

    static def script() {
        new ScriptPayloadBuilder()
    }

    def withCode(String code) {
        tap { this.code = code }
    }

    def withScriptType(String scriptType) {
        tap { this.scriptType = scriptType }
    }

    def withDescription(String description) {
        tap { this.description = description }
    }

    def withContent(String content) {
        tap { this.content = content }
    }

    def withAutoDisabling(boolean autoDisabling) {
        tap { this.autoDisabling = autoDisabling }
    }

    def withDisabled(boolean disabled) {
        tap { this.disabled = disabled }
    }

    def build() {
        def payload = json()
        if (code) {
            payload.withCode(code)
        }
        if (content) {
            payload.withField("content", content)
        }
        if (description) {
            payload.withField("description", description)
        }
        if (autoDisabling != null) {
            payload.withField("autodisabling", autoDisabling)
        }
        if (disabled != null) {
            payload.withField("disabled", disabled)
        }
        if (scriptType) {
            payload.withField("scriptType", json().withCode(scriptType))
        }
        payload.build()
    }
}