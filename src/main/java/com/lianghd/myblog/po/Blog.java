package com.lianghd.myblog.po;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "t_blog")      // 和数据库对应生成的能力(默认生成的表名)
public class Blog {

    @Id                 // 主键
    @GeneratedValue     // 主键生成策略
    private Long id;
    private String title;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;
    private String firstPicture;
    private String flag;
    private Integer views;
    private boolean appreciate;
    private boolean share;
    private boolean comment;
    private boolean publish;  // true发布 false保存
    private boolean recommend;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    // 实体间关系
    @ManyToOne //Blog和Type 多对一 Many端为关系维护端
    private Type type;

    @ManyToMany(cascade = {CascadeType.PERSIST}) ////Blog和 Tag 多对多
    // cascade级联关系（生成新博文时使用新tag则生成对应的新tag）
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne
    private User user;  //Blog和User 多对一

    @OneToMany(mappedBy = "blog")   //Blog和Type 一对多
    private List<Comment> comments = new ArrayList<>();

    @Transient
    private String tagIds;

    private String description;


    public Blog(){
    }

    /*getter and setter*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFirstPicture() {
        return firstPicture;
    }

    public void setFirstPicture(String firstPicture) {
        this.firstPicture = firstPicture;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public boolean isAppreciate() {
        return appreciate;
    }

    public void setAppreciate(boolean appreciate) {
        this.appreciate = appreciate;
    }

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public boolean isComment() {
        return comment;
    }

    public void setComment(boolean comment) {
        this.comment = comment;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    // 初始化 tagIds （不存入数据库）
    public void init(){
        this.tagIds = tagsToIds(this.getTags());
    }

    // tags数组转tagIds
    private String tagsToIds(List<Tag> tags){
        if (!tags.isEmpty()){
            StringBuffer ids = new StringBuffer();
            boolean  flag = false;
            for (Tag tag : tags){
                if (flag){
                    ids.append(",");
                }else{
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        }else {
            return tagIds;
        }
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", firstPicture='" + firstPicture + '\'' +
                ", flag='" + flag + '\'' +
                ", views=" + views +
                ", appreciate=" + appreciate +
                ", share=" + share +
                ", comment=" + comment +
                ", publish=" + publish +
                ", recommend=" + recommend +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", description='" + description + '\'' +
                '}';
    }
}
