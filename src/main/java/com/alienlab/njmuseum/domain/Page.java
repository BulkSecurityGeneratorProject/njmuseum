package com.alienlab.njmuseum.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Page.
 */
@Entity
@Table(name = "page")
public class Page implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "page_name")
    private String pageName;

    @Column(name = "page_memo")
    private String pageMemo;

    @Column(name = "page_image")
    private String pageImage;

    @Column(name = "page_sort")
    private Integer pageSort;

    @OneToMany(mappedBy = "page")
    @JsonIgnore
    private Set<PageUnit> units = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPageName() {
        return pageName;
    }

    public Page pageName(String pageName) {
        this.pageName = pageName;
        return this;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageMemo() {
        return pageMemo;
    }

    public Page pageMemo(String pageMemo) {
        this.pageMemo = pageMemo;
        return this;
    }

    public void setPageMemo(String pageMemo) {
        this.pageMemo = pageMemo;
    }

    public String getPageImage() {
        return pageImage;
    }

    public Page pageImage(String pageImage) {
        this.pageImage = pageImage;
        return this;
    }

    public void setPageImage(String pageImage) {
        this.pageImage = pageImage;
    }

    public Integer getPageSort() {
        return pageSort;
    }

    public Page pageSort(Integer pageSort) {
        this.pageSort = pageSort;
        return this;
    }

    public void setPageSort(Integer pageSort) {
        this.pageSort = pageSort;
    }

    public Set<PageUnit> getUnits() {
        return units;
    }

    public Page units(Set<PageUnit> pageUnits) {
        this.units = pageUnits;
        return this;
    }

    public Page addUnits(PageUnit pageUnit) {
        this.units.add(pageUnit);
        pageUnit.setPage(this);
        return this;
    }

    public Page removeUnits(PageUnit pageUnit) {
        this.units.remove(pageUnit);
        pageUnit.setPage(null);
        return this;
    }

    public void setUnits(Set<PageUnit> pageUnits) {
        this.units = pageUnits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Page page = (Page) o;
        if (page.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), page.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Page{" +
            "id=" + getId() +
            ", pageName='" + getPageName() + "'" +
            ", pageMemo='" + getPageMemo() + "'" +
            ", pageImage='" + getPageImage() + "'" +
            ", pageSort='" + getPageSort() + "'" +
            "}";
    }
}
