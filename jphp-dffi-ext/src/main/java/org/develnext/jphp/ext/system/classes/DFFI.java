package org.develnext.jphp.ext.system.classes;

import org.develnext.jphp.ext.system.DFFIExtension;
import org.develnext.jphp.ext.system.classes.Helper;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.memory.*;
import php.runtime.memory.helper.*;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseObject;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.awt.*;
import java.util.*;
import com.sun.jna.*;

@Reflection.Name("DFFI")
@Reflection.Namespace(DFFIExtension.NS)
public class DFFI extends BaseObject {

	public String libName;
	public static ArrayMemory functions = new ArrayMemory();
	
	public DFFI(Environment env, ClassEntity clazz)
	{
		super(env, clazz);
	}
	
	@Reflection.Signature
	public void __construct(String lib)
	{
		this.libName = lib;
	}
	
	@Reflection.Signature
	public void bind(String functionName, String returnType, Memory _types)
	{		
		ArrayMemory types = (ArrayMemory)_types.toValue(ArrayMemory.class);
		
		ArrayMemory array = new ArrayMemory();
		array.refOfIndex("lib").assign(libName);
		array.refOfIndex("returnType").assign(returnType);
		array.refOfIndex("types").assign(types);
		
		functions.refOfIndex(functionName).assign(array);
	}
	
	@Reflection.Signature
	public static Memory __callStatic(Environment env, TraceInfo trace, String functionName, Memory... args) throws AWTException, Exception
	{
		Memory returnValue = Memory.NULL;
		Memory _function = functions.valueOfIndex(functionName);
		if(_function != Memory.UNDEFINED){
			ArrayMemory function = _function.toValue(ArrayMemory.class);
			String lib = function.valueOfIndex("lib").toString();
			String returnType = function.valueOfIndex("returnType").toString();
			Memory types = function.valueOfIndex("types");
			
			returnValue = Helper.callFunction(env, lib, returnType, functionName, types, args);
		}
		
		return returnValue;
	}

	@Reflection.Signature
	public static void addSearchPath(String lib, String path) throws AWTException
	{
		NativeLibrary.addSearchPath(lib, path);
	}
	
}