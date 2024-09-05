package com.kmvc.jeesite.modules.syncPortal.global;

public interface Global
{
    public interface User
    {
        public static final String DefName = "\u7edf\u4e00\u5e73\u53f0\u672a\u547d\u540d";
        public static final Integer DefType = 1;
        public static final Integer DefStatus = 1;

        public interface Sex
        {
            public static final String IMALE = "MALE";
            public static final String IFEMALE = "FEMALE";
            public static final Integer OMALE = 1;
            public static final Integer OFEMALE = 2;
        }

        public interface Status
        {
            public static final String ION = "1";
            public static final String IOFF = "0";
        }
    }

    public interface Web
    {
        public interface Session
        {
            public static final String SSOSysLoginName_k = "ssoSysLoginName";
            public static final String SSOLOGIN1 = "SSOLOGIN1";
            public static final String SSOLOGIN2 = "SSOLOGIN1";
        }
    }

    public interface Portal
    {
        public interface ResultType
        {
            public static final String YES = "1";
            public static final String NO = "0";
        }

        public interface ChangeType
        {
            public static final String ADD = "add";
            public static final String UPD = "update";
            public static final String DEL = "delete";
        }
    }


}
