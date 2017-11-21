package org.develnext.jphp.ext.system.classes;

import php.runtime.Memory;
import org.develnext.jphp.ext.system.classes.Helper;
import org.develnext.jphp.ext.system.DFFIExtension;
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
		this.refval = Helper.ConvertToReference(_type);
    }

	@Signature
    public void __construct(Environment env, String _type, Memory value) {
		this.type = _type;
		this.refval = Helper.ConvertObjectToReference(_type, Memory.unwrap(env, value));
    }
	
	@Signature
    public void setValue(Environment env, Memory value) throws AWTException {
		this.refval = Helper.SetValueToReference(this.refval, Memory.unwrap(env, value));
		
	}

    @Signature
    public Memory getValue() throws AWTException, ClassNotFoundException {
		return Helper.ConvertObjectToMemory(Helper.ConvertToJavaClassByType(this.type), Helper.ConvertReferenceToObject(this.refval));
    }
	
}