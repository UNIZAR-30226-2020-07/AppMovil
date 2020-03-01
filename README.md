# swagger-android-client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-android-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "io.swagger:swagger-android-client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/swagger-android-client-1.0.0.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import io.swagger.client.api.AlbumApi;

public class AlbumApiExample {

    public static void main(String[] args) {
        AlbumApi apiInstance = new AlbumApi();
        Integer page = 56; // Integer | A page number within the paginated result set.
        try {
            InlineResponse200 result = apiInstance.albumList(page);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AlbumApi#albumList");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://virtserver.swaggerhub.com/UNIZAR-30226-2020-07/songs-api/v1*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*AlbumApi* | [**albumList**](docs/AlbumApi.md#albumList) | **GET** /album/ | 
*AlbumApi* | [**albumRead**](docs/AlbumApi.md#albumRead) | **GET** /album/{id}/ | 
*ArtistApi* | [**artistList**](docs/ArtistApi.md#artistList) | **GET** /artist/ | 
*ArtistApi* | [**artistRead**](docs/ArtistApi.md#artistRead) | **GET** /artist/{id}/ | 
*SongsApi* | [**songsList**](docs/SongsApi.md#songsList) | **GET** /songs/ | 
*SongsApi* | [**songsRead**](docs/SongsApi.md#songsRead) | **GET** /songs/{id}/ | 


## Documentation for Models

 - [Album](docs/Album.md)
 - [Artist](docs/Artist.md)
 - [InlineResponse200](docs/InlineResponse200.md)
 - [InlineResponse2001](docs/InlineResponse2001.md)
 - [InlineResponse2002](docs/InlineResponse2002.md)
 - [Song](docs/Song.md)


## Documentation for Authorization

Authentication schemes defined for the API:
### Basic

- **Type**: HTTP basic authentication


## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author



