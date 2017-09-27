package com.alienlab.njmuseum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ContentInfo.
 */
@Entity
@Table(name = "content_info")
public class ContentInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "info_title")
    private String infoTitle;

    @Column(name = "info_cover")
    private String infoCover;

    @Column(name = "info_text")
    private String infoText;

    @Column(name = "info_image")
    private String infoImage;

    @Column(name = "info_sort")
    private Integer infoSort;

    @Column(name = "info_sub_title")
    private String infoSubTitle;

    @Column(name = "info_item_desc")
    private String infoItemDesc;

    @Column(name = "info_field_1")
    private String infoField1;

    @ManyToOne
    private UnitContent unitContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInfoTitle() {
        return infoTitle;
    }

    public ContentInfo infoTitle(String infoTitle) {
        this.infoTitle = infoTitle;
        return this;
    }

    public void setInfoTitle(String infoTitle) {
        this.infoTitle = infoTitle;
    }

    public String getInfoCover() {
        return infoCover;
    }

    public ContentInfo infoCover(String infoCover) {
        this.infoCover = infoCover;
        return this;
    }

    public void setInfoCover(String infoCover) {
        this.infoCover = infoCover;
    }

    public String getInfoText() {
        return infoText;
    }

    public ContentInfo infoText(String infoText) {
        this.infoText = infoText;
        return this;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }

    public String getInfoImage() {
        return infoImage;
    }

    public ContentInfo infoImage(String infoImage) {
        this.infoImage = infoImage;
        return this;
    }

    public void setInfoImage(String infoImage) {
        this.infoImage = infoImage;
    }

    public Integer getInfoSort() {
        return infoSort;
    }

    public ContentInfo infoSort(Integer infoSort) {
        this.infoSort = infoSort;
        return this;
    }

    public void setInfoSort(Integer infoSort) {
        this.infoSort = infoSort;
    }

    public String getInfoSubTitle() {
        return infoSubTitle;
    }

    public ContentInfo infoSubTitle(String infoSubTitle) {
        this.infoSubTitle = infoSubTitle;
        return this;
    }

    public void setInfoSubTitle(String infoSubTitle) {
        this.infoSubTitle = infoSubTitle;
    }

    public String getInfoItemDesc() {
        return infoItemDesc;
    }

    public ContentInfo infoItemDesc(String infoItemDesc) {
        this.infoItemDesc = infoItemDesc;
        return this;
    }

    public void setInfoItemDesc(String infoItemDesc) {
        this.infoItemDesc = infoItemDesc;
    }

    public String getInfoField1() {
        return infoField1;
    }

    public ContentInfo infoField1(String infoField1) {
        this.infoField1 = infoField1;
        return this;
    }

    public void setInfoField1(String infoField1) {
        this.infoField1 = infoField1;
    }

    public UnitContent getUnitContent() {
        return unitContent;
    }

    public ContentInfo unitContent(UnitContent unitContent) {
        this.unitContent = unitContent;
        return this;
    }

    public void setUnitContent(UnitContent unitContent) {
        this.unitContent = unitContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContentInfo contentInfo = (ContentInfo) o;
        if (contentInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contentInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContentInfo{" +
            "id=" + getId() +
            ", infoTitle='" + getInfoTitle() + "'" +
            ", infoCover='" + getInfoCover() + "'" +
            ", infoText='" + getInfoText() + "'" +
            ", infoImage='" + getInfoImage() + "'" +
            ", infoSort='" + getInfoSort() + "'" +
            ", infoSubTitle='" + getInfoSubTitle() + "'" +
            ", infoItemDesc='" + getInfoItemDesc() + "'" +
            ", infoField1='" + getInfoField1() + "'" +
            "}";
    }
}
