package com.alienlab.njmuseum.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PageUnit.
 */
@Entity
@Table(name = "page_unit")
public class PageUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "unit_memo")
    private String unitMemo;

    @Column(name = "unit_title")
    private String unitTitle;

    @Column(name = "unit_sort")
    private Integer unitSort;

    @ManyToOne
    private Page page;

    @OneToMany(mappedBy = "pageUnit")
    @JsonIgnore
    private Set<UnitContent> contents = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public PageUnit unitName(String unitName) {
        this.unitName = unitName;
        return this;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitMemo() {
        return unitMemo;
    }

    public PageUnit unitMemo(String unitMemo) {
        this.unitMemo = unitMemo;
        return this;
    }

    public void setUnitMemo(String unitMemo) {
        this.unitMemo = unitMemo;
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    public PageUnit unitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
        return this;
    }

    public void setUnitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
    }

    public Integer getUnitSort() {
        return unitSort;
    }

    public PageUnit unitSort(Integer unitSort) {
        this.unitSort = unitSort;
        return this;
    }

    public void setUnitSort(Integer unitSort) {
        this.unitSort = unitSort;
    }

    public Page getPage() {
        return page;
    }

    public PageUnit page(Page page) {
        this.page = page;
        return this;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Set<UnitContent> getContents() {
        return contents;
    }

    public PageUnit contents(Set<UnitContent> unitContents) {
        this.contents = unitContents;
        return this;
    }

    public PageUnit addContents(UnitContent unitContent) {
        this.contents.add(unitContent);
        unitContent.setPageUnit(this);
        return this;
    }

    public PageUnit removeContents(UnitContent unitContent) {
        this.contents.remove(unitContent);
        unitContent.setPageUnit(null);
        return this;
    }

    public void setContents(Set<UnitContent> unitContents) {
        this.contents = unitContents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PageUnit pageUnit = (PageUnit) o;
        if (pageUnit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pageUnit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PageUnit{" +
            "id=" + getId() +
            ", unitName='" + getUnitName() + "'" +
            ", unitMemo='" + getUnitMemo() + "'" +
            ", unitTitle='" + getUnitTitle() + "'" +
            ", unitSort='" + getUnitSort() + "'" +
            "}";
    }
}
