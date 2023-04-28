
# API Documentation

This API allows clients to save FX deal details to a database.




## POST /fx-deal/saveDealDetails

Endpoint to save FX Deal details.




## Request Format

The API accepts a JSON payload representing an FxDeal object:

``` json
{
    "dealUniqueId":"test",
    "orderingCurrencyIsoCode":"NEP",
    "targetCurrencyIsoCode":"AME",
    "dealTimestamp":"2018-02-05T11:59:11.332Z",
    "dealAmount": 0.5
}

```
## Response Format

The API returns a JSON payload representing a FinalResponse object:

``` json
{
    "message": "Deal details with unique ID: test has been accepted"
}

```




## Duplicate Request

If the same JSON payload is sent then response will be:

``` json
{
    "errorCode": "DUPLICATE",
    "errorDescription": "FX Deal with unique ID test already exists."
}

```
## Empty Payload Request

If empty JSON payload is sent then reponse will be:

``` json
{
    "orderingCurrencyIsoCode": "Ordering currency code cannot be blank",
    "dealAmount": "Deal amount cannot be null",
    "dealUniqueId": "Deal Unique Id cannot be empty or null",
    "dealTimestamp": "Deal timestamp cannot be null",
    "targetCurrencyIsoCode": "Target currency code cannot be blank"
}

```

## POST /fx-deal/uploadFxDeal
End point to upload csv file containing fx-deals and persist data inside file to database.
Spring Batch is used to process large amount of data, for simplicity job is launched from
this endpoint call, but we can also use scheduler to schedule job to process files from storage
location automatically.
