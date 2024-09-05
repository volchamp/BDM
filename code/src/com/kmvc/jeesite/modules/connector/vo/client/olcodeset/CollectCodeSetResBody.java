package com.kmvc.jeesite.modules.connector.vo.client.olcodeset;

import java.util.*;

public class CollectCodeSetResBody
{
    private List<CollectCodeSetData> codeEntityCollection;

    public List<CollectCodeSetData> getCodeEntityCollection() {
        return this.codeEntityCollection;
    }

    public void setCodeEntityCollection(final List<CollectCodeSetData> codeEntityCollection) {
        this.codeEntityCollection = codeEntityCollection;
    }
}
