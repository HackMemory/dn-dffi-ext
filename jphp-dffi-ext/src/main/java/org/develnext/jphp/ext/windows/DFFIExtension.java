package org.develnext.jphp.ext.windows;

import php.runtime.ext.support.Extension;
import org.develnext.jphp.ext.windows.classes.PDFFI;
import org.develnext.jphp.ext.windows.classes.DFFIStruct;
import org.develnext.jphp.ext.windows.classes.DFFIClassLoader;
import org.develnext.jphp.ext.windows.classes.DFFIReferenceValue;
import php.runtime.env.CompileScope;

import java.awt.*;

public class DFFIExtension extends Extension {
    public static final String NS = "php\\windows";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "windows" };
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, PDFFI.class);
		registerClass(scope, DFFIStruct.class);
		registerClass(scope, DFFIClassLoader.class);
		registerClass(scope, DFFIReferenceValue.class);
    }
}
