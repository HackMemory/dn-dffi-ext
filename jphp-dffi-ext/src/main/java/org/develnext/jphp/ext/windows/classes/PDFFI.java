package org.develnext.jphp.ext.windows.classes;

import php.runtime.Memory;
import org.develnext.jphp.ext.windows.classes.Helper;
import org.develnext.jphp.ext.windows.DFFIExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseObject;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.awt.*;
import java.util.*;

@Reflection.Name("DFFI")
@Reflection.Namespace(DFFIExtension.NS)
public class PDFFI extends BaseObject {

    public PDFFI(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() {
    }

    @Signature
    public static Memory callFunction(Environment env, TraceInfo trace, String lib, String returnType, String functionName, Memory args, Memory types) throws AWTException, ClassNotFoundException {
		return Helper.callFunction_impl(env, trace, lib, returnType, functionName, args, types);
    }
	
	@Signature
    public static void addSearchPath(String lib, String path) throws AWTException {
		Helper.addSearchPath_impl(lib, path);
    }
	
}