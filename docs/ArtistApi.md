# ArtistApi

All URIs are relative to *https://virtserver.swaggerhub.com/UNIZAR-30226-2020-07/songs-api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**artistList**](ArtistApi.md#artistList) | **GET** /artist/ | 
[**artistRead**](ArtistApi.md#artistRead) | **GET** /artist/{id}/ | 


<a name="artistList"></a>
# **artistList**
> InlineResponse2001 artistList(page)





### Example
```java
// Import classes:
//import io.swagger.client.api.ArtistApi;

ArtistApi apiInstance = new ArtistApi();
Integer page = 56; // Integer | A page number within the paginated result set.
try {
    InlineResponse2001 result = apiInstance.artistList(page);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ArtistApi#artistList");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **page** | **Integer**| A page number within the paginated result set. | [optional]

### Return type

[**InlineResponse2001**](InlineResponse2001.md)

### Authorization

[Basic](../README.md#Basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="artistRead"></a>
# **artistRead**
> Artist artistRead(id)





### Example
```java
// Import classes:
//import io.swagger.client.api.ArtistApi;

ArtistApi apiInstance = new ArtistApi();
Integer id = 56; // Integer | A unique integer value identifying this artist.
try {
    Artist result = apiInstance.artistRead(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ArtistApi#artistRead");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Integer**| A unique integer value identifying this artist. |

### Return type

[**Artist**](Artist.md)

### Authorization

[Basic](../README.md#Basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

