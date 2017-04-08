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
import php.runtime.memory.ArrayMemory;

import com.sun.jna.ptr.*;
import com.sun.jna.*;
import java.awt.*;
import java.util.*;
import java.lang.reflect.Field;

@Reflection.Name("DFFIReferenceValue")
@Reflection.Namespace(DFFIExtension.NS)
public class DFFIReferenceValue extends BaseObject {
	
	public ByReference refval;
	public String type;

    public DFFIReferenceValue(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
	
	@Signature
    public void __construct(Environment env, String _type) {
		this.type = _type;
		this.refval = Helper.convertToJRefObject(_type);
    }

	@Signature
    public void __construct(Environment env, String _type, Memory value) {
		this.type = _type;
		this.refval = Helper.setValueToJRefObject(_type, Helper.convertToJValue(value));
    }
	
	@Signature
    public void setValue(Memory value) throws AWTException {
		this.refval = Helper.setValueToJRefValue(this.refval, Helper.convertToJValue(value));
		
	}

    @Signature
    public Memory getValue() throws AWTException {
		return Helper.setValueToPObject(Helper.convertToJType(this.type), Helper.getValueToJRefValue(this.refval));
    }
	
}