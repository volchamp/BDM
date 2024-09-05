package com.kmvc.jeesite.modules.common;

import org.springframework.stereotype.*;
import java.text.*;
import java.util.*;

@Repository("methods")
public class Methods
{
    public String getDate(final String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        final Date date = new Date();
        return sdf.format(date);
    }
}
