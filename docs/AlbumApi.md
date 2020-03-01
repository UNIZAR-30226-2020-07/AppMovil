# AlbumApi

All URIs are relative to *https://virtserver.swaggerhub.com/UNIZAR-30226-2020-07/songs-api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**albumList**](AlbumApi.md#albumList) | **GET** /album/ | 
[**albumRead**](AlbumApi.md#albumRead) | **GET** /album/{id}/ | 


<a name="albumList"></a>
# **albumList**
> InlineResponse200 albumList(page)





### Example
```java
// Import classes:
//import io.swagger.client.api.AlbumApi;

AlbumApi apiInstance = new AlbumApi();
Integer page = 56; // Integer | A page number within the paginated result set.
try {
    InlineResponse200 result = apiInstance.albumList(page);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AlbumApi#albumList");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **page** | **Integer**| A page number within the paginated result set. | [optional]

### Return type

[**InlineResponse200**](InlineResponse200.md)

### Authorization

[Basic](../README.md#Basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="albumRead"></a>
# **albumRead**
> Album albumRead(id)





### Example
```java
// Import classes:
//import io.swagger.client.api.AlbumApi;

AlbumApi apiInstance = new AlbumApi();
Integer id = 56; // Integer | A unique integer value identifying this album.
try {
    Album result = apiInstance.albumRead(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AlbumApi#albumRead");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Integer**| A unique integer value identifying this album. |

### Return type

[**Album**](Album.md)

### Authorization

[Basic](../README.md#Basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

