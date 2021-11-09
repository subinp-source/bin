# Dynamic Fixture modification

The goal of the `Dynamic Fixture modification` module is to provide a more flexible mock-backend experience during e2e execution. Frequently, different e2e tests require different responses from the same endpoint, which is why this module was created.

## Prerequisites

-   The module works as an extension of [NestJS](https://nestjs.com/) application. Therefore, it requires an existing NestJS project up and running.

-   Your e2e set up requires a Web API to communicate with the module.

## Getting Started

-   Import current module with the desired base URL property in the main module of your NestJS application. The base URL will serve as a base for all endpoints served in the module.

```javascript
@Module({
    imports: [DynamicFixtureModule.forRoot({baseURL: "my_base_url"})],
    controllers: [AppController],
    providers: [AppService]
})
```

## Usage

The module injects a global interceptor that modifes the response data from your controllers based on the currently stored information.

### Exposed endpoints

Stored information is manipulated via the 4 endpoints.

| Endpoint                         | HttpVerb |                      Description                       |           Return           |
| -------------------------------- | :------: | :----------------------------------------------------: | :------------------------: |
| your_base_url/modify             |   Post   | Store information to modify properties of the response | ID of the received payload |
| your_base_url/replace            |   Post   |       Store information to substitute a response       | ID of the received payload |
| your_base_url/fixture/:fixtureID |  Delete  |                  Remove payload by ID                  |         No return          |
| your_base_url/fixture            |  Delete  |                Discard all information                 |         No return          |

### Payload structure

**Modification and replacement endpoints expect a specificly structured payload.**

| Property |                                                              Description                                                               |
| -------- | :------------------------------------------------------------------------------------------------------------------------------------: |
| url      |                                            List of RegExp used for matching with requestURL                                            |
| replace  | The object which is used as a replacement of the response or its` keys define path to the fixture's property that needs to be modified |

```javascript
{
	"url":["page\/types\/[^?]*$"],
	"replace": {
		"property_1.property_2.property_3": false,
		"property_1.property_2.property_3": "visible"
	}
}
```

If request URL matches at least one RegExp in the URL property, the content will be modified accordingly.

### End-to-end example

Given a controller returns the following fixture for the _page/codes/:codeID_ endpoint.

```javascript
{
    "attributes": [
        {
            "structureType": "Boolean",
            "i18nKey": "visible.name",
            "localized": false,
            "qualifier": "visible",
            "postfix": "postfix.text"
        },
        {
            "structureType": "number",
            "qualifier": "navigation",
            "i18nKey": "navigation.name",
            "localized": false,
            "required": true,
            "editable": true
        }
    ],
    "category": "NotToBeFound",
    "code": "pageCode",
    "name": "Page Component"
}
```

-   To set `editable` property of the response to `false` send the following payload to the `your_base_url/modify`.

```javascript
{
	"url":["page\/codes\/[^?]*$"],
	"replace": {
		"attributes.1.editable": false,
	}
}
```

-   To substitue the response send the following payload to the `your_base_url/replace`.

```javascript
{
	"url":["page\/codes\/[^?]*$"],
	"replace": {
        "attributes": [
            {
                "structureType": "Boolean",
                "i18nKey": "visible.name",
                "localized": false,
                "qualifier": "visible",
                "postfix": "postfix.text"
            },
        ],
        "category": "NewCategory",
        "code": "NewCode",
        "name": "NewName"
    }
}
```

### Notes

-   Make sure to remove the payload from storage if the response adjustment is no longer necessary.
-   If the request URL matches both replacement and modification strategies, the replacement strategy takes priority.
