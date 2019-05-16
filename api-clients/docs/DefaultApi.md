# DefaultApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**listDelete**](DefaultApi.md#listDelete) | **DELETE** list | 
[**listGet**](DefaultApi.md#listGet) | **GET** list | 
[**listPost**](DefaultApi.md#listPost) | **POST** list | 
[**listPut**](DefaultApi.md#listPut) | **PUT** list | 
[**reportGet**](DefaultApi.md#reportGet) | **GET** report | 


<a name="listDelete"></a>
# **listDelete**
> Item listDelete(item)



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
    Item result = apiInstance.listDelete(item);
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

[**Item**](Item.md)

### Authorization

[ApiKey](../README.md#ApiKey)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="listGet"></a>
# **listGet**
> List&lt;Item&gt; listGet(from, limit)



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
    List<Item> result = apiInstance.listGet(from, limit);
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

[**List&lt;Item&gt;**](Item.md)

### Authorization

[ApiKey](../README.md#ApiKey)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="listPost"></a>
# **listPost**
> Item listPost(itemPrototype)



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
ItemPrototype itemPrototype = new ItemPrototype(); // ItemPrototype | 
try {
    Item result = apiInstance.listPost(itemPrototype);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#listPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **itemPrototype** | [**ItemPrototype**](ItemPrototype.md)|  |

### Return type

[**Item**](Item.md)

### Authorization

[ApiKey](../README.md#ApiKey)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="listPut"></a>
# **listPut**
> Item listPut(partialItem)



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
PartialItem partialItem = new PartialItem(); // PartialItem | 
try {
    Item result = apiInstance.listPut(partialItem);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#listPut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **partialItem** | [**PartialItem**](PartialItem.md)|  | [optional]

### Return type

[**Item**](Item.md)

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

