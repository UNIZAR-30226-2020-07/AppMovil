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
 * PasswordChange
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2020-03-14T16:06:26.034Z")
public class PasswordChange {
  @SerializedName("new_password1")
  private String newPassword1 = null;

  @SerializedName("new_password2")
  private String newPassword2 = null;

  public PasswordChange newPassword1(String newPassword1) {
    this.newPassword1 = newPassword1;
    return this;
  }

   /**
   * Get newPassword1
   * @return newPassword1
  **/
  @ApiModelProperty(required = true, value = "")
  public String getNewPassword1() {
    return newPassword1;
  }

  public void setNewPassword1(String newPassword1) {
    this.newPassword1 = newPassword1;
  }

  public PasswordChange newPassword2(String newPassword2) {
    this.newPassword2 = newPassword2;
    return this;
  }

   /**
   * Get newPassword2
   * @return newPassword2
  **/
  @ApiModelProperty(required = true, value = "")
  public String getNewPassword2() {
    return newPassword2;
  }

  public void setNewPassword2(String newPassword2) {
    this.newPassword2 = newPassword2;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PasswordChange passwordChange = (PasswordChange) o;
    return Objects.equals(this.newPassword1, passwordChange.newPassword1) &&
        Objects.equals(this.newPassword2, passwordChange.newPassword2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newPassword1, newPassword2);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PasswordChange {\n");
    
    sb.append("    newPassword1: ").append(toIndentedString(newPassword1)).append("\n");
    sb.append("    newPassword2: ").append(toIndentedString(newPassword2)).append("\n");
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
