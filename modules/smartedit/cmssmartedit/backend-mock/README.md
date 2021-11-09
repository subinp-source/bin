# Backend-mock

Backend-mock is a NestJS server-side application that mimics backend functionality for Contract Testing purposes. For more information on NestJS refer to the documentation page
[here](https://docs.nestjs.com/).

## Functionality

Backend-mock fetches Open API specifications(Contracts) and verifies that the Frontend is compliant with the contracts using the following tools:

-   [Swagger middleware](https://github.com/apigee-127/swagger-tools/blob/master/docs/Middleware.md) that validates requests/responses based on the operations stated in the Contract.
-   [OpenAPI Test Templates](https://github.com/google/oatts) that generates basic unit tests based on the Contract information and runs those against a backend-mock.

Backend-mock runs on http://localhost:3333, any changes to which has to be reflected in `outerGlobalBasePathFetchMock`, and concurently starts on execution of either **grunt e2e**, **grunt e2e_max** or grunt **e2e_dev** commands.

### Fetching contracts

**postinstall.js** script processes properties stated in **config.yml** and fetches contracts from maven repository. **config.yml** contains the following required properties:

| Property         |                 Description                  |
| ---------------- | :------------------------------------------: |
| REPO_URL         |  Maven repository that contains an artifact  |
| ARTIFACT         |              groupId:artifactId              |
| ARTIFACT_VERSION |           Version of the artifact            |
| CONTRACTS        | Space separated list of contracts to extract |

**IMPORTANT**

-   ARTIFACT_VERSION must be a precise version as fetching latest is not possible! For the list of available versions, search for **smartedit-apis** in [SAP Artifactory](https://common.repositories.sap.ondemand.com/artifactory/webapp/#/home).
