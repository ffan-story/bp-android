package com.wbtech.ums.model;

/**
 * Created by dupengfei on 15/8/5.
 * <p/>
 * 对应新需求中得城市Id,商圈id，商户，商品id 用于sendevent方法参数的整理成对象
 * <p/>
 * 现使用单例模式，通过对这个实力重复赋值 要不然涉及类过多，该变量过大
 * <p/>
 * 在一个事件保存在本地之后clearMerchantAndProductId，此段逻辑在SaveInfo中又用到
 */
public class EventLogIds {

  // 当前类对象
  private static EventLogIds eventLogIds;

  // 城市id
  private String city_id;
  // 商圈id
  private String plaza_id = "";
  // 商户id
  private String merchant_id;
  // 商品Id
  private String product_id;

  // 品牌id
  private String brand_id;
  // 品牌故事
  private String story_id;
  // 登录用户id
  private String user_id;

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

  // 实例化
  public static EventLogIds getInstance() {

    if (eventLogIds == null) eventLogIds = new EventLogIds();

    return eventLogIds;

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

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
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

  // 把商户和商品id清空
  public void clearMerchantAndProductId() {

    eventLogIds.setMerchant_id("");
    eventLogIds.setProduct_id("");
    eventLogIds.setBrandId("");
    eventLogIds.setBrandStoryId("");
    eventLogIds.setUser_id("");
    eventLogIds.setFilm_id("");
    eventLogIds.setRound_id("");
    eventLogIds.setCinema_id("");
    eventLogIds.setAliasName("");
    eventLogIds.setOrderby_id("");
  }


}
