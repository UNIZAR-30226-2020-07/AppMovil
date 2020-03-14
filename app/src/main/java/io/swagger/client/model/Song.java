/*
 * API
 * Test version of the api
 *
 * OpenAPI spec version: v1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * Song
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2020-03-14T16:06:26.034Z")
public class Song {
  @SerializedName("id")
  private Integer id = null;

  @SerializedName("title")
  private String title = null;

  @SerializedName("duration")
  private Integer duration = null;

  @SerializedName("stream_url")
  private String streamUrl = null;

  /**
   * Gets or Sets genre
   */
  @JsonAdapter(GenreEnum.Adapter.class)
  public enum GenreEnum {
    _90S("90s"),
    
    CLASSIC("Classic"),
    
    ELECTRONIC("Electronic"),
    
    REGGASE("Reggase"),
    
    R_B("R&B"),
    
    LATIN("Latin"),
    
    OLDIES("Oldies"),
    
    COUNTRY("Country"),
    
    RAP("Rap"),
    
    ROCK("Rock"),
    
    METAL("Metal"),
    
    POP("Pop"),
    
    BLUES("Blues"),
    
    JAZZ("Jazz"),
    
    FOLK("Folk"),
    
    _80S("80s"),
    
    PLAYLIST("Playlist");

    private String value;

    GenreEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static GenreEnum fromValue(String text) {
      for (GenreEnum b : GenreEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<GenreEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final GenreEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public GenreEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return GenreEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("genre")
  private GenreEnum genre = null;

  @SerializedName("album")
  private Integer album = null;

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")
  public Integer getId() {
    return id;
  }

  public Song title(String title) {
    this.title = title;
    return this;
  }

   /**
   * Get title
   * @return title
  **/
  @ApiModelProperty(required = true, value = "")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Song duration(Integer duration) {
    this.duration = duration;
    return this;
  }

   /**
   * Get duration
   * @return duration
  **/
  @ApiModelProperty(required = true, value = "")
  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public Song streamUrl(String streamUrl) {
    this.streamUrl = streamUrl;
    return this;
  }

   /**
   * Get streamUrl
   * @return streamUrl
  **/
  @ApiModelProperty(required = true, value = "")
  public String getStreamUrl() {
    return streamUrl;
  }

  public void setStreamUrl(String streamUrl) {
    this.streamUrl = streamUrl;
  }

  public Song genre(GenreEnum genre) {
    this.genre = genre;
    return this;
  }

   /**
   * Get genre
   * @return genre
  **/
  @ApiModelProperty(required = true, value = "")
  public GenreEnum getGenre() {
    return genre;
  }

  public void setGenre(GenreEnum genre) {
    this.genre = genre;
  }

  public Song album(Integer album) {
    this.album = album;
    return this;
  }

   /**
   * Get album
   * @return album
  **/
  @ApiModelProperty(value = "")
  public Integer getAlbum() {
    return album;
  }

  public void setAlbum(Integer album) {
    this.album = album;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Song song = (Song) o;
    return Objects.equals(this.id, song.id) &&
        Objects.equals(this.title, song.title) &&
        Objects.equals(this.duration, song.duration) &&
        Objects.equals(this.streamUrl, song.streamUrl) &&
        Objects.equals(this.genre, song.genre) &&
        Objects.equals(this.album, song.album);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, duration, streamUrl, genre, album);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Song {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    streamUrl: ").append(toIndentedString(streamUrl)).append("\n");
    sb.append("    genre: ").append(toIndentedString(genre)).append("\n");
    sb.append("    album: ").append(toIndentedString(album)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

