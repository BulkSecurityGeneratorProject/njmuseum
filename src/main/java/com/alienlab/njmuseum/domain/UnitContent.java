package com.alienlab.njmuseum.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A UnitContent.
 */
@Entity
@Table(name = "unit_content")
public class UnitContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_name")
    private String contentName;

    @Column(name = "content_memo")
    private String contentMemo;

    @Column(name = "content_sort")
    private Integer contentSort;

    @ManyToOne
    private PageUnit pageUnit;

    @OneToMany(mappedBy = "unitContent")
    @JsonIgnore
    private Set<ContentInfo> infos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentName() {
        return contentName;
    }

    public UnitContent contentName(String contentName) {
        this.contentName = contentName;
        return this;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentMemo() {
        return contentMemo;
    }

    public UnitContent contentMemo(String contentMemo) {
        this.contentMemo = contentMemo;
        return this;
    }

    public void setContentMemo(String contentMemo) {
        this.contentMemo = contentMemo;
    }

    public Integer getContentSort() {
        return contentSort;
    }

    public UnitContent contentSort(Integer contentSort) {
        this.contentSort = contentSort;
        return this;
    }

    public void setContentSort(Integer contentSort) {
        this.contentSort = contentSort;
    }

    public PageUnit getPageUnit() {
        return pageUnit;
    }

    public UnitContent pageUnit(PageUnit pageUnit) {
        this.pageUnit = pageUnit;
        return this;
    }

    public void setPageUnit(PageUnit pageUnit) {
        this.pageUnit = pageUnit;
    }

    public Set<ContentInfo> getInfos() {
        return infos;
    }

    public UnitContent infos(Set<ContentInfo> contentInfos) {
        this.infos = contentInfos;
        return this;
    }

    public UnitContent addInfos(ContentInfo contentInfo) {
        this.infos.add(contentInfo);
        contentInfo.setUnitContent(this);
        return this;
    }

    public UnitContent removeInfos(ContentInfo contentInfo) {
        this.infos.remove(contentInfo);
        contentInfo.setUnitContent(null);
        return this;
    }

    public void setInfos(Set<ContentInfo> contentInfos) {
        this.infos = contentInfos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UnitContent unitContent = (UnitContent) o;
        if (unitContent.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), unitContent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UnitContent{" +
            "id=" + getId() +
            ", contentName='" + getContentName() + "'" +
            ", contentMemo='" + getContentMemo() + "'" +
            ", contentSort='" + getContentSort() + "'" +
            "}";
    }
}
