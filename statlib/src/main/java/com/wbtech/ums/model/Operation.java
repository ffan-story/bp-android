package com.wbtech.ums.model;

import java.io.Serializable;

/**
 * Created by dupengfei on 15-7-10.
 */
public class Operation implements Serializable {
  // 用户登录ID
  private String user_id;
  // 事件发生时间戳
  private String event_time;
  // 事件ID
  private String event_id;
  // 城市Id
  private String city_id;
  // 商圈Id
  private String plaza_id;
  // 商户Id
  private String merchant_id;
  // 商品ID
  private String product_id;
  // 品牌id
  private String brand_id;
  // 品牌故事id
  private String story_id;
  //电影id
  private String film_id;
  //场次ID
  private String round_id;
  //影院ID
  private String cinema_id;
  //专题资源位id
  private String aliasName;
  //默认排序id
  private String orderby_id;

  public String getUser_id() {
    return user_id;
  }

  public String getEvent_time() {
    return event_time;
  }

  public String getEvent_id() {
    return event_id;
  }

  public String getCity_id() {
    return city_id;
  }

  public void setCity_id(String city_id) {
    this.city_id = city_id;
  }

  public String getPlaza_id() {
    return plaza_id;
  }

  public void setPlaza_id(String plaza_id) {
    this.plaza_id = plaza_id;
  }

  public String getMerchant_id() {
    return merchant_id;
  }

  public void setMerchant_id(String merchant_id) {
    this.merchant_id = merchant_id;
  }

  public String getProduct_id() {
    return product_id;
  }

  public void setProduct_id(String product_id) {
    this.product_id = product_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }

  public void setEvent_time(String event_time) {
    this.event_time = event_time;
  }

  public void setEvent_id(String event_id) {
    this.event_id = event_id;
  }

  public String getBrandId() {
    return brand_id;
  }

  public void setBrandId(String brand_id) {
    this.brand_id = brand_id;
  }

  public String getBrandstoryId() {
    return story_id;
  }

  public void setBrandStoryId(String story_id) {
    this.story_id = story_id;
  }

  public String getFilm_id() {
    return film_id;
  }

  public void setFilm_id(String film_id) {
    this.film_id = film_id;
  }

  public String getRound_id() {
    return round_id;
  }

  public void setRound_id(String round_id) {
    this.round_id = round_id;
  }

  public String getCinema_id() {
    return cinema_id;
  }

  public void setCinema_id(String cinema_id) {
    this.cinema_id = cinema_id;
  }

  public String getAliasName() {
    return aliasName;
  }

  public void setAliasName(String aliasName) {
    this.aliasName = aliasName;
  }

  public String getOrderby_id() {
    return orderby_id;
  }

  public void setOrderby_id(String orderby_id) {
    this.orderby_id = orderby_id;
  }
}
