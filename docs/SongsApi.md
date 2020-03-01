# SongsApi

All URIs are relative to *https://virtserver.swaggerhub.com/UNIZAR-30226-2020-07/songs-api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**songsList**](SongsApi.md#songsList) | **GET** /songs/ | 
[**songsRead**](SongsApi.md#songsRead) | **GET** /songs/{id}/ | 


<a name="songsList"></a>
# **songsList**
> InlineResponse2002 songsList(page)





### Example
```java
// Import classes:
//import io.swagger.client.api.SongsApi;

SongsApi apiInstance = new SongsApi();
Integer page = 56; // Integer | A page number within the paginated result set.
try {
    InlineResponse2002 result = apiInstance.songsList(page);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SongsApi#songsList");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **page** | **Integer**| A page number within the paginated result set. | [optional]

### Return type

[**InlineResponse2002**](InlineResponse2002.md)

### Authorization

[Basic](../README.md#Basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="songsRead"></a>
# **songsRead**
> Song songsRead(id)





### Example
```java
// Import classes:
//import io.swagger.client.api.SongsApi;

SongsApi apiInstance = new SongsApi();
Integer id = 56; // Integer | A unique integer value identifying this song.
try {
    Song result = apiInstance.songsRead(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SongsApi#songsRead");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Integer**| A unique integer value identifying this song. |

### Return type

[**Song**](Song.md)

### Authorization

[Basic](../README.md#Basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

