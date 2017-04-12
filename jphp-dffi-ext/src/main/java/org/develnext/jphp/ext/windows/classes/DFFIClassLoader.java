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
import php.runtime.memory.KeyValueMemory;
import php.runtime.memory.ReferenceMemory;

import com.sun.jna.ptr.*;
import com.sun.jna.*;
import java.awt.*;
import java.util.*;
import java.lang.reflect.*;
import java.net.*;
import java.io.File;

@Reflection.Name("DFFIClassLoader")
@Reflection.Namespace(DFFIExtension.NS)
public class DFFIClassLoader extends BaseObject {
	
	public Class loader;
	public Class[] con_classes = {};
	public Object[] con_args = {};
	public Object instance = null;

    public DFFIClassLoader(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
	
	public DFFIClassLoader(Environment env, Class _loader, Object _instance) {
        super(env);
		this.loader = _loader;
		this.instance = _instance;
    }
	
	
	@Signature
    public void __construct(Environment env, TraceInfo trace, String libPath, String className, ArrayMemory args, ArrayMemory types) throws Exception, MalformedURLException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException {		
		if(libPath == null || libPath == ""){
			this.loader = Class.forName (className);
		}else{
			URLClassLoader child = URLClassLoader.newInstance(new URL[] {new File(libPath).toURL()});
			this.loader = Class.forName (className, true, child);
		}
		

		int size1 = args.size();
		int size2 = types.size();
				
		if (size1 != size2) {
			env.warning(trace, "DFFIClassLoader::__construct(): Both parameters should have an equal number of elements");
		}
				
		ForeachIterator iterator_args = args.getNewIterator(env, false, false);
		ForeachIterator iterator_types = types.getNewIterator(env, false, false);
		while (iterator_args.next()) {
			iterator_types.next();
			
			Memory value = iterator_args.getValue();
			String type_value = iterator_types.getValue().toString().toUpperCase();
			
			Object[] result = DFFIClassLoader.getArgs(value, type_value);
			this.con_args = Helper.addItemToObject(this.con_args, (Object)result[0]);
			this.con_classes = Helper.addItemToClass(this.con_classes, (Class)result[1]);
		}
		
		try {
			if(con_classes.length != 0 && con_args.length != 0){ this.instance = this.loader.getConstructor(con_classes).newInstance(con_args); }
			else{ this.instance = this.loader.newInstance(); }
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof Exception) {
				throw (Exception) e.getCause();
			}
		}
    }
	
	
	@Signature
    public Memory callMethod(Environment env, TraceInfo trace, String functionName, String returnType, ArrayMemory args, ArrayMemory types) throws AWTException, Exception, NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
		Object[] obj_args = {};
		Class[] classes = {};
		Memory returnValue = Memory.NULL;
		
		int size1 = args.size();
		int size2 = types.size();
				
		if (size1 != size2) {
			env.warning(trace, "DFFIClassLoader::callMethod(): Both parameters should have an equal number of elements");
			return Memory.NULL;
		}
				
		ForeachIterator iterator_args = args.getNewIterator(env, false, false);
		ForeachIterator iterator_types = types.getNewIterator(env, false, false);
		while (iterator_args.next()) {
			iterator_types.next();
			
			boolean can = true;
			Memory value = iterator_args.getValue();
			String type_value = iterator_types.getValue().toString().toUpperCase();
			
			Object[] result = DFFIClassLoader.getArgs(value, type_value);
			obj_args = Helper.addItemToObject(obj_args, (Object)result[0]);
			classes = Helper.addItemToClass(classes, (Class)result[1]);
		}
		
		try {
			Class type = Helper.convertToJType(returnType);	
			Method method = this.loader.getMethod(functionName, classes);
			Object response = method.invoke(this.instance, obj_args); 
			
			if(response != null){returnValue = Helper.setValueToPObject(env, type, response); }
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof Exception) {
				throw (Exception) e.getCause();
			}
		}
		
		return returnValue;
	}
	
	
	
	@Signature
    public static Memory callStaticMethod(Environment env, TraceInfo trace, String libPath, String className, String functionName, String returnType, ArrayMemory args, ArrayMemory types) throws Exception, AWTException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, MalformedURLException {
		Object[] obj_args = {};
		Class[] classes = {};
		Memory returnValue = Memory.NULL;
		
		Class sloader = null;
		if(libPath == null || libPath == ""){
			sloader = Class.forName (className);
		}else{
			URLClassLoader child = URLClassLoader.newInstance(new URL[] {new File(libPath).toURL()});
			sloader = Class.forName (className, true, child);
		}
		
		
		int size1 = args.size();
		int size2 = types.size();
				
		if (size1 != size2) {
			env.warning(trace, "DFFIClassLoader::callMethod(): Both parameters should have an equal number of elements");
			return Memory.NULL;
		}
				
		ForeachIterator iterator_args = args.getNewIterator(env, false, false);
		ForeachIterator iterator_types = types.getNewIterator(env, false, false);
		while (iterator_args.next()) {
			iterator_types.next();
			Memory value = iterator_args.getValue();
			String type_value = iterator_types.getValue().toString().toUpperCase();

			Object[] result = DFFIClassLoader.getArgs(value, type_value);
			obj_args = Helper.addItemToObject(obj_args, (Object)result[0]);
			classes = Helper.addItemToClass(classes, (Class)result[1]);
		}
		
		try {
			Class type = Helper.convertToJType(returnType);		
			Method method = sloader.getMethod(functionName, classes);
			Object response = method.invoke(null, obj_args); 
				
			if(response != null){returnValue = Helper.setValueToPObject(env, type, response); }
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof Exception) {
				throw (Exception) e.getCause();
			}
		}
		
		return returnValue;
	}
	
	
	
	public static Object[] getArgs(Memory value, String type_value) throws ClassNotFoundException {
		Object obj_arg = null;
		Class _class = null;
		Object[] objArray = {}; 
		
		switch(type_value){
		case "BOOL":
			obj_arg = value.toBoolean();
			break;
			
		case "INT":
			obj_arg = value.toInteger();
			break;
			
		case "CHAR":
			obj_arg = value.toString().charAt(0);
			break;
		
		case "DOUBLE":
			obj_arg = value.toDouble();
			break;
			
		case "FLOAT":
			obj_arg = value.toFloat();
			break;
			
		case "STRING":
			obj_arg = value.toString();
			break;
			
		case "CLASS":
			_class = (Class)value.toObject(DFFIClassLoader.class).loader;
			obj_arg = (Object)value.toObject(DFFIClassLoader.class).instance;
			break;
		}
		
		if(_class == null){ _class = Helper.convertToJType(type_value); }
		
		
		objArray = Helper.addItemToObject(objArray, obj_arg);
		objArray = Helper.addItemToObject(objArray, _class);
		
		return objArray;
	}
}