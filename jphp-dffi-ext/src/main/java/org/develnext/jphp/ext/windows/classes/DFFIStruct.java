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
import php.runtime.lang.ForeachIterator;

import com.sun.jna.*;
import java.awt.*;
import java.util.*;
import java.lang.reflect.Field;

@Reflection.Name("DFFIStruct")
@Reflection.Namespace(DFFIExtension.NS)
public class DFFIStruct extends BaseObject {
	
	public Structure struct;

    public DFFIStruct(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

	@Signature
    public void __construct(Environment env, String name, ArrayMemory types) throws ClassNotFoundException {
		Class[] classes = {};
		ForeachIterator iterator = types.getNewIterator(env, false, false);
		while (iterator.next()) {
			classes = Helper.addItemToClass(classes, Helper.convertToJType(iterator.getValue().toString()));
		}
		
		this.struct = Helper.createStructure(name, classes);
    }

    @Signature
    public ArrayMemory getResponse() throws AWTException, IllegalAccessException {
		ArrayMemory fields_arr = new ArrayMemory();
		Field[] fields = this.struct.getClass().getDeclaredFields();
		//Integer index = 0;
		
		for(Field itm : fields){
			Object value = itm.get(this.struct);
			fields_arr.add(Helper.setValueToPObject(value.getClass(), value));
			//index++;
		}
		
		return fields_arr;
    }
	
}