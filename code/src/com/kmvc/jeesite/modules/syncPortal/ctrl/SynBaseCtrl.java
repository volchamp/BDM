package com.kmvc.jeesite.modules.syncPortal.ctrl;

import org.springframework.http.*;
import org.springframework.util.*;

public class SynBaseCtrl
{
    public <T> ResponseEntity<T> returnByResponseEntity(final T t) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return (ResponseEntity<T>)new ResponseEntity((Object)t, (MultiValueMap)headers, HttpStatus.OK);
    }
}
