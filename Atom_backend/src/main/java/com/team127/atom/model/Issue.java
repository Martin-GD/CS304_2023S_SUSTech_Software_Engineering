package com.team127.atom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Data
@AllArgsConstructor
public class Issue {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue.id
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue.opener
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    private String opener;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue.title
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    private String title;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue.star
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    private Integer star=0;


    private Integer love=0;

    private Integer isDraft;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue.created_at
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    private Long createdAt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column issue.content
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    private String content;

    private Reply reply;

    private boolean isLike = false;

    private boolean isStar = false;

    public double points=0;

    public int replyNum=0;

    private boolean isFollow=false;


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table issue
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table issue
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table issue
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    public Issue() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue.id
     *
     * @return the value of issue.id
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue.id
     *
     * @param id the value for issue.id
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue.opener
     *
     * @return the value of issue.opener
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    public String getOpener() {
        return opener;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue.opener
     *
     * @param opener the value for issue.opener
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    public void setOpener(String opener) {
        this.opener = opener;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue.title
     *
     * @return the value of issue.title
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue.title
     *
     * @param title the value for issue.title
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue.star
     *
     * @return the value of issue.star
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    public Integer getStar() {
        return star;
    }


    public void setStar(Integer star) {
        this.star = star;
    }


    public Integer getLove() {
        return love;
    }
    public void setLove(Integer love) {
        this.love = love;
    }

    public Integer getIsDraft() {
        return isDraft;
    }
    public void setIsDraft(Integer isDraft) {
        this.isDraft = isDraft;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue.created_at
     *
     * @return the value of issue.created_at
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    public Long getCreatedAt() {
        return createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue.created_at
     *
     * @param createdAt the value for issue.created_at
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column issue.content
     *
     * @return the value of issue.content
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column issue.content
     *
     * @param content the value for issue.content
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }

    public boolean getIsLike(){
        return this.isLike;
    }

    public void setIsStar(boolean isStar) {
        this.isStar = isStar;
    }

    public boolean getIsStar(){
        return this.isStar;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }
}