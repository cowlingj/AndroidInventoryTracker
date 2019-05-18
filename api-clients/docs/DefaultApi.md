# DefaultApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**listDelete**](DefaultApi.md#listDelete) | **DELETE** list | 
[**listGet**](DefaultApi.md#listGet) | **GET** list | 
[**listIdGet**](DefaultApi.md#listIdGet) | **GET** list/{id} | 
[**listPost**](DefaultApi.md#listPost) | **POST** list | 
[**listPut**](DefaultApi.md#listPut) | **PUT** list | 
[**reportGet**](DefaultApi.md#reportGet) | **GET** report | 


<a name="listDelete"></a>
# **listDelete**
> tk.jonathancowling.inventorytracker.clients.list.models.Item listDelete(item)



removes an item in the list

### Example
```java
// Import classes:
//import tk.jonathancowling.inventorytracker.clients.list.ApiClient;
//import tk.jonathancowling.inventorytracker.clients.list.ApiException;
//import tk.jonathancowling.inventorytracker.clients.list.Configuration;
//import tk.jonathancowling.inventorytracker.clients.list.auth.*;
//import tk.jonathancowling.inventorytracker.clients.list.api.DefaultApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: ApiKey
ApiKeyAuth ApiKey = (ApiKeyAuth) defaultClient.getAuthentication("ApiKey");
ApiKey.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//ApiKey.setApiKeyPrefix("Token");

DefaultApi apiInstance = new DefaultApi();
String item = "item_example"; // String | 
try {
    tk.jonathancowling.inventorytracker.clients.list.models.Item result = apiInstance.listDelete(item);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#listDelete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **item** | **String**|  | [optional]

### Return type

[**tk.jonathancowling.inventorytracker.clients.list.models.Item**](tk.jonathancowling.inventorytracker.clients.list.models.Item.md)

### Authorization

[ApiKey](../README.md#ApiKey)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="listGet"></a>
# **listGet**
> InlineResponse200 listGet(from, limit)



returns items in the list

### Example
```java
// Import classes:
//import tk.jonathancowling.inventorytracker.clients.list.ApiClient;
//import tk.jonathancowling.inventorytracker.clients.list.ApiException;
//import tk.jonathancowling.inventorytracker.clients.list.Configuration;
//import tk.jonathancowling.inventorytracker.clients.list.auth.*;
//import tk.jonathancowling.inventorytracker.clients.list.api.DefaultApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: ApiKey
ApiKeyAuth ApiKey = (ApiKeyAuth) defaultClient.getAuthentication("ApiKey");
ApiKey.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//ApiKey.setApiKeyPrefix("Token");

DefaultApi apiInstance = new DefaultApi();
String from = "from_example"; // String | get items from this offset
Integer limit = 25; // Integer | limits the number of items returned
try {
    InlineResponse200 result = apiInstance.listGet(from, limit);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#listGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **from** | **String**| get items from this offset | [optional]
 **limit** | **Integer**| limits the number of items returned | [optional] [default to 25]

### Return type

[**InlineResponse200**](InlineResponse200.md)

### Authorization

[ApiKey](../README.md#ApiKey)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="listIdGet"></a>
# **listIdGet**
> tk.jonathancowling.inventorytracker.clients.list.models.Item listIdGet(id)



returns a single item

### Example
```java
// Import classes:
//import tk.jonathancowling.inventorytracker.clients.list.ApiClient;
//import tk.jonathancowling.inventorytracker.clients.list.ApiException;
//import tk.jonathancowling.inventorytracker.clients.list.Configuration;
//import tk.jonathancowling.inventorytracker.clients.list.auth.*;
//import tk.jonathancowling.inventorytracker.clients.list.api.DefaultApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: ApiKey
ApiKeyAuth ApiKey = (ApiKeyAuth) defaultClient.getAuthentication("ApiKey");
ApiKey.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//ApiKey.setApiKeyPrefix("Token");

DefaultApi apiInstance = new DefaultApi();
String id = "id_example"; // String | 
try {
    tk.jonathancowling.inventorytracker.clients.list.models.Item result = apiInstance.listIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#listIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  |

### Return type

[**tk.jonathancowling.inventorytracker.clients.list.models.Item**](tk.jonathancowling.inventorytracker.clients.list.models.Item.md)

### Authorization

[ApiKey](../README.md#ApiKey)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="listPost"></a>
# **listPost**
> tk.jonathancowling.inventorytracker.clients.list.models.Item listPost(tkJonathancowlingInventorytrackerClientsListModelsItemPrototype)



adds an item to the list

### Example
```java
// Import classes:
//import tk.jonathancowling.inventorytracker.clients.list.ApiClient;
//import tk.jonathancowling.inventorytracker.clients.list.ApiException;
//import tk.jonathancowling.inventorytracker.clients.list.Configuration;
//import tk.jonathancowling.inventorytracker.clients.list.auth.*;
//import tk.jonathancowling.inventorytracker.clients.list.api.DefaultApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: ApiKey
ApiKeyAuth ApiKey = (ApiKeyAuth) defaultClient.getAuthentication("ApiKey");
ApiKey.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//ApiKey.setApiKeyPrefix("Token");

DefaultApi apiInstance = new DefaultApi();
tk.jonathancowling.inventorytracker.clients.list.models.ItemPrototype tkJonathancowlingInventorytrackerClientsListModelsItemPrototype = new tk.jonathancowling.inventorytracker.clients.list.models.ItemPrototype(); // tk.jonathancowling.inventorytracker.clients.list.models.ItemPrototype | 
try {
    tk.jonathancowling.inventorytracker.clients.list.models.Item result = apiInstance.listPost(tkJonathancowlingInventorytrackerClientsListModelsItemPrototype);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#listPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tkJonathancowlingInventorytrackerClientsListModelsItemPrototype** | [**tk.jonathancowling.inventorytracker.clients.list.models.ItemPrototype**](tk.jonathancowling.inventorytracker.clients.list.models.ItemPrototype.md)|  |

### Return type

[**tk.jonathancowling.inventorytracker.clients.list.models.Item**](tk.jonathancowling.inventorytracker.clients.list.models.Item.md)

### Authorization

[ApiKey](../README.md#ApiKey)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="listPut"></a>
# **listPut**
> tk.jonathancowling.inventorytracker.clients.list.models.Item listPut(tkJonathancowlingInventorytrackerClientsListModelsPartialItem)



replaces an item in the list

### Example
```java
// Import classes:
//import tk.jonathancowling.inventorytracker.clients.list.ApiClient;
//import tk.jonathancowling.inventorytracker.clients.list.ApiException;
//import tk.jonathancowling.inventorytracker.clients.list.Configuration;
//import tk.jonathancowling.inventorytracker.clients.list.auth.*;
//import tk.jonathancowling.inventorytracker.clients.list.api.DefaultApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: ApiKey
ApiKeyAuth ApiKey = (ApiKeyAuth) defaultClient.getAuthentication("ApiKey");
ApiKey.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//ApiKey.setApiKeyPrefix("Token");

DefaultApi apiInstance = new DefaultApi();
tk.jonathancowling.inventorytracker.clients.list.models.PartialItem tkJonathancowlingInventorytrackerClientsListModelsPartialItem = new tk.jonathancowling.inventorytracker.clients.list.models.PartialItem(); // tk.jonathancowling.inventorytracker.clients.list.models.PartialItem | 
try {
    tk.jonathancowling.inventorytracker.clients.list.models.Item result = apiInstance.listPut(tkJonathancowlingInventorytrackerClientsListModelsPartialItem);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#listPut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tkJonathancowlingInventorytrackerClientsListModelsPartialItem** | [**tk.jonathancowling.inventorytracker.clients.list.models.PartialItem**](tk.jonathancowling.inventorytracker.clients.list.models.PartialItem.md)|  | [optional]

### Return type

[**tk.jonathancowling.inventorytracker.clients.list.models.Item**](tk.jonathancowling.inventorytracker.clients.list.models.Item.md)

### Authorization

[ApiKey](../README.md#ApiKey)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="reportGet"></a>
# **reportGet**
> reportGet()



returns a report on the list over a given period

### Example
```java
// Import classes:
//import tk.jonathancowling.inventorytracker.clients.list.ApiClient;
//import tk.jonathancowling.inventorytracker.clients.list.ApiException;
//import tk.jonathancowling.inventorytracker.clients.list.Configuration;
//import tk.jonathancowling.inventorytracker.clients.list.auth.*;
//import tk.jonathancowling.inventorytracker.clients.list.api.DefaultApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: ApiKey
ApiKeyAuth ApiKey = (ApiKeyAuth) defaultClient.getAuthentication("ApiKey");
ApiKey.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//ApiKey.setApiKeyPrefix("Token");

DefaultApi apiInstance = new DefaultApi();
try {
    apiInstance.reportGet();
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#reportGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization

[ApiKey](../README.md#ApiKey)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

