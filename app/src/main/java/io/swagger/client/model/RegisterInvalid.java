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
import java.util.ArrayList;
import java.util.List;

/**
 * RegisterInvalid
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2020-03-15T22:58:22.381Z")
public class RegisterInvalid {
  @SerializedName("username")
  private List<String> username = null;

  @SerializedName("email")
  private List<String> email = null;

  @SerializedName("password1")
  private List<String> password1 = null;

  @SerializedName("password2")
  private List<String> password2 = null;

  public RegisterInvalid username(List<String> username) {
    this.username = username;
    return this;
  }

  public RegisterInvalid addUsernameItem(String usernameItem) {
    if (this.username == null) {
      this.username = new ArrayList<String>();
    }
    this.username.add(usernameItem);
    return this;
  }

   /**
   * Get username
   * @return username
  **/
  @ApiModelProperty(value = "")
  public List<String> getUsername() {
    return username;
  }

  public void setUsername(List<String> username) {
    this.username = username;
  }

  public RegisterInvalid email(List<String> email) {
    this.email = email;
    return this;
  }

  public RegisterInvalid addEmailItem(String emailItem) {
    if (this.email == null) {
      this.email = new ArrayList<String>();
    }
    this.email.add(emailItem);
    return this;
  }

   /**
   * Get email
   * @return email
  **/
  @ApiModelProperty(value = "")
  public List<String> getEmail() {
    return email;
  }

  public void setEmail(List<String> email) {
    this.email = email;
  }

  public RegisterInvalid password1(List<String> password1) {
    this.password1 = password1;
    return this;
  }

  public RegisterInvalid addPassword1Item(String password1Item) {
    if (this.password1 == null) {
      this.password1 = new ArrayList<String>();
    }
    this.password1.add(password1Item);
    return this;
  }

   /**
   * Get password1
   * @return password1
  **/
  @ApiModelProperty(value = "")
  public List<String> getPassword1() {
    return password1;
  }

  public void setPassword1(List<String> password1) {
    this.password1 = password1;
  }

  public RegisterInvalid password2(List<String> password2) {
    this.password2 = password2;
    return this;
  }

  public RegisterInvalid addPassword2Item(String password2Item) {
    if (this.password2 == null) {
      this.password2 = new ArrayList<String>();
    }
    this.password2.add(password2Item);
    return this;
  }

   /**
   * Get password2
   * @return password2
  **/
  @ApiModelProperty(value = "")
  public List<String> getPassword2() {
    return password2;
  }

  public void setPassword2(List<String> password2) {
    this.password2 = password2;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegisterInvalid registerInvalid = (RegisterInvalid) o;
    return Objects.equals(this.username, registerInvalid.username) &&
        Objects.equals(this.email, registerInvalid.email) &&
        Objects.equals(this.password1, registerInvalid.password1) &&
        Objects.equals(this.password2, registerInvalid.password2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, email, password1, password2);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegisterInvalid {\n");
    
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password1: ").append(toIndentedString(password1)).append("\n");
    sb.append("    password2: ").append(toIndentedString(password2)).append("\n");
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
