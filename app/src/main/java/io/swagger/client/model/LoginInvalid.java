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
 * LoginInvalid
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2020-03-14T18:16:12.217Z")
public class LoginInvalid {
  @SerializedName("username")
  private List<String> username = null;

  @SerializedName("email")
  private List<String> email = null;

  @SerializedName("password")
  private List<String> password = null;

  @SerializedName("non_field_errors")
  private List<String> nonFieldErrors = null;

  public LoginInvalid username(List<String> username) {
    this.username = username;
    return this;
  }

  public LoginInvalid addUsernameItem(String usernameItem) {
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

  public LoginInvalid email(List<String> email) {
    this.email = email;
    return this;
  }

  public LoginInvalid addEmailItem(String emailItem) {
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

  public LoginInvalid password(List<String> password) {
    this.password = password;
    return this;
  }

  public LoginInvalid addPasswordItem(String passwordItem) {
    if (this.password == null) {
      this.password = new ArrayList<String>();
    }
    this.password.add(passwordItem);
    return this;
  }

   /**
   * Get password
   * @return password
  **/
  @ApiModelProperty(value = "")
  public List<String> getPassword() {
    return password;
  }

  public void setPassword(List<String> password) {
    this.password = password;
  }

  public LoginInvalid nonFieldErrors(List<String> nonFieldErrors) {
    this.nonFieldErrors = nonFieldErrors;
    return this;
  }

  public LoginInvalid addNonFieldErrorsItem(String nonFieldErrorsItem) {
    if (this.nonFieldErrors == null) {
      this.nonFieldErrors = new ArrayList<String>();
    }
    this.nonFieldErrors.add(nonFieldErrorsItem);
    return this;
  }

   /**
   * Get nonFieldErrors
   * @return nonFieldErrors
  **/
  @ApiModelProperty(value = "")
  public List<String> getNonFieldErrors() {
    return nonFieldErrors;
  }

  public void setNonFieldErrors(List<String> nonFieldErrors) {
    this.nonFieldErrors = nonFieldErrors;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginInvalid loginInvalid = (LoginInvalid) o;
    return Objects.equals(this.username, loginInvalid.username) &&
        Objects.equals(this.email, loginInvalid.email) &&
        Objects.equals(this.password, loginInvalid.password) &&
        Objects.equals(this.nonFieldErrors, loginInvalid.nonFieldErrors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, email, password, nonFieldErrors);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginInvalid {\n");
    
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    nonFieldErrors: ").append(toIndentedString(nonFieldErrors)).append("\n");
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

