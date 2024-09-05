package com.kmvc.jeesite.modules.codesetmrg.entity;

import com.thinkgem.jeesite.common.persistence.*;
import java.util.*;
import org.hibernate.validator.constraints.*;

public class SplitMerge extends DataEntity<SplitMerge>
{
    private static final long serialVersionUID = 1L;
    private String oldItemId;
    private String oldItemCode;
    private String oldItemName;
    private String oldKindCode;
    private String newItemId;
    private String newItemCode;
    private String newItemName;
    private String newKindCode;
    private String codeSetId;
    private String codeSetNo;
    private String codeSetName;
    private List<String> itemIdList;

    public SplitMerge() {
    }

    public SplitMerge(final String id) {
        super(id);
    }

    public SplitMerge(final String oldItemId, final String oldItemCode, final String oldItemName, final String oldKindCode, final String newItemId, final String newItemCode, final String newItemName, final String newKindCode, final String codeSetId, final String codeSetNo, final String codeSetName) {
        this.oldItemId = oldItemId;
        this.oldItemCode = oldItemCode;
        this.oldItemName = oldItemName;
        this.oldKindCode = oldKindCode;
        this.newItemId = newItemId;
        this.newItemCode = newItemCode;
        this.newItemName = newItemName;
        this.newKindCode = newKindCode;
        this.codeSetId = codeSetId;
        this.codeSetNo = codeSetNo;
        this.codeSetName = codeSetName;
    }

    @Length(min = 0, max = 64, message = "old_item_id\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getOldItemId() {
        return this.oldItemId;
    }

    public void setOldItemId(final String oldItemId) {
        this.oldItemId = oldItemId;
    }

    @Length(min = 0, max = 100, message = "old_item_code\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
    public String getOldItemCode() {
        return this.oldItemCode;
    }

    public void setOldItemCode(final String oldItemCode) {
        this.oldItemCode = oldItemCode;
    }

    @Length(min = 0, max = 256, message = "old_item_name\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 256 \u4e4b\u95f4")
    public String getOldItemName() {
        return this.oldItemName;
    }

    public void setOldItemName(final String oldItemName) {
        this.oldItemName = oldItemName;
    }

    @Length(min = 0, max = 100, message = "old_kind_code\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
    public String getOldKindCode() {
        return this.oldKindCode;
    }

    public void setOldKindCode(final String oldKindCode) {
        this.oldKindCode = oldKindCode;
    }

    @Length(min = 0, max = 64, message = "new_item_id\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getNewItemId() {
        return this.newItemId;
    }

    public void setNewItemId(final String newItemId) {
        this.newItemId = newItemId;
    }

    @Length(min = 0, max = 100, message = "new_item_code\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
    public String getNewItemCode() {
        return this.newItemCode;
    }

    public void setNewItemCode(final String newItemCode) {
        this.newItemCode = newItemCode;
    }

    @Length(min = 0, max = 256, message = "new_item_name\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 256 \u4e4b\u95f4")
    public String getNewItemName() {
        return this.newItemName;
    }

    public void setNewItemName(final String newItemName) {
        this.newItemName = newItemName;
    }

    @Length(min = 0, max = 64, message = "new_kind_code\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getNewKindCode() {
        return this.newKindCode;
    }

    public void setNewKindCode(final String newKindCode) {
        this.newKindCode = newKindCode;
    }

    public List<String> getItemIdList() {
        return this.itemIdList;
    }

    public void setItemIdList(final List<String> itemIdList) {
        this.itemIdList = itemIdList;
    }

    public String getCodeSetId() {
        return this.codeSetId;
    }

    public void setCodeSetId(final String codeSetId) {
        this.codeSetId = codeSetId;
    }

    public String getCodeSetNo() {
        return this.codeSetNo;
    }

    public void setCodeSetNo(final String codeSetNo) {
        this.codeSetNo = codeSetNo;
    }

    public String getCodeSetName() {
        return this.codeSetName;
    }

    public void setCodeSetName(final String codeSetName) {
        this.codeSetName = codeSetName;
    }
}
