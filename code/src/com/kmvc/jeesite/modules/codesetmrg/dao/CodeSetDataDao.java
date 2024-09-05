package com.kmvc.jeesite.modules.codesetmrg.dao;

import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSet;
import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSetData;
import com.thinkgem.jeesite.common.persistence.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;
import java.util.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;

@MyBatisDao
public interface CodeSetDataDao extends CrudDao<CodeSetData>
{
    CodeSetData getByCodeSetIdAndItemCode(final CodeSetData p0);

    void updateStatusById(final Map<String, Object> p0);

    List<CodeSetData> findHistory(final CodeSetData p0);

    int findMaxVersion(final CodeSetData p0);

    void validtimeCheck();

    List<Map<String, Object>> queryMetaList(final CodeSet p0);

    List<Map<String, Object>> selBySql(final CodeSet p0);

    int getTotalPage(final Map<String, Object> p0);

    List<CodeSetData> getCodeSetDataList(final Map<String, Object> p0);

    List<CodeSetData> findListByCodeSetData(final Map<String, Object> p0);

    List<CodeSetData> findBySystemIdAndCodeSetId(final Map<String, Object> p0);

    int batUpdByIds(final CodeSetData p0);
}
