package org.develnext.jphp.ext.windows.classes;

import php.runtime.Memory;
import php.runtime.env.TraceInfo;
import php.runtime.env.Environment;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.DoubleMemory;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.KeyValueMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.ext.support.compile.FunctionsContainer;
import org.develnext.jphp.ext.windows.classes.DFFIStruct;
import org.develnext.jphp.ext.windows.classes.DFFIReferenceValue;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

import com.sun.jna.*;
import com.sun.jna.ptr.*;
import java.awt.*;
import java.util.*;

import static org.develnext.jphp.zend.ext.standard.ArrayConstants.*;
import static php.runtime.Memory.Type.ARRAY;


public class Helper extends FunctionsContainer {
	
	/* Class DFFI */
	
	public static Memory callFunction_impl(Environment env, TraceInfo trace, String lib, String returnType, String functionName, Memory args, Memory types) throws AWTException, ClassNotFoundException {
		Memory returnValue = Memory.NULL;
		if (expecting(env, trace, 1, args, ARRAY)) {
			Object[] obj_args = {};
			
			if(args != null){
				ArrayMemory _args = args.toValue(ArrayMemory.class);
				ArrayMemory _types = types.toValue(ArrayMemory.class);
				int size1 = _args.size();
				int size2 = _types.size();
				
				if (size1 != size2) {
					env.warning(trace, "DFFI::callFunction(): Both parameters should have an equal number of elements");
					return Memory.NULL;
				}
				
				ForeachIterator iterator_args = _args.getNewIterator(env, false, false);
				ForeachIterator iterator_types = _types.getNewIterator(env, false, false);
				
				while (iterator_args.next()) {
					iterator_types.next();
					
					Memory value = iterator_args.getValue();
					String type_value = iterator_types.getValue().toString().toUpperCase();

					switch(type_value){
					case "BOOL":
						obj_args = addItemToObject(obj_args, value.toBoolean());
						break;
						
					case "INT":
						obj_args = addItemToObject(obj_args, value.toInteger());
						break;
					
					case "DOUBLE":
						obj_args = addItemToObject(obj_args, value.toDouble());
						break;
						
					case "FLOAT":
						obj_args = addItemToObject(obj_args, value.toFloat());
						break;
						
					case "STRING":
						obj_args = addItemToObject(obj_args, value.toString());
						break;
					
					case "WSTRING":
						obj_args = addItemToObject(obj_args, new WString(value.toString()));
						break;
						
					case "STRUCT":
						Structure struct = (Structure)value.toObject(DFFIStruct.class).struct;
						obj_args = addItemToObject(obj_args, struct);
						break;
						
					case "REFERENCE":
						ByReference reference = (ByReference)value.toObject(DFFIReferenceValue.class).refval;
						obj_args = addItemToObject(obj_args, reference);
						break;
					}
				}
			}
			
			
			Class type = convertToJType(returnType);
			Function _function = NativeLibrary.getInstance(lib).getFunction(functionName);
			Object _returnValue = _function.invoke(type, obj_args);
			returnValue = setValueToPObject(type, _returnValue);
		}
		
		return returnValue;
	}
	
	public static void addSearchPath_impl(String lib, String path) throws AWTException {
		NativeLibrary.addSearchPath(lib, path);
	}
	
	/* Class DFFI END*/
	
	
	
	
	
	
	
	
	public static Memory setValueToPObject(Class<?> type, Object value){
		Memory returnValue = Memory.NULL;
		
		if (type==boolean.class || type==Boolean.class) {
			if((boolean)value == true){ returnValue = Memory.TRUE; } else { returnValue = Memory.FALSE; }
		} else if (type==int.class || type==Integer.class || type==Pointer.class) {
			returnValue = new LongMemory((int)value);
		} else if (type==long.class || type==Long.class) {
			returnValue = new LongMemory((long)value);
		} else if (type==float.class || type==Float.class) {
			returnValue = new DoubleMemory((float)value);
		} else if (type==double.class || type==Double.class) {
			returnValue = new DoubleMemory((double)value);
		} else if (type==String.class) {
			returnValue = new StringMemory((String)value);
		}
		
		return returnValue;
	}
	
	public static ByReference setValueToJRefObject(String cls, Object value){
		ByReference c = null;
		
		cls = cls.toUpperCase();
		switch(cls){
			case "INT":		c = new IntByReference((int)value); 			break;
			case "LONG":	c = new LongByReference((long)value);			break;
			case "DOUBLE":	c = new DoubleByReference((double)value);		break;
			case "FLOAT":   c = new FloatByReference((float)value); 		break;
			case "STRING": 	c = new StringByReference((String)value); 		break;
			case "POINTER": c = new PointerByReference((Pointer)value); 	break;
			
		}
		
		return c;
	}
	
	
	public static Class<?> convertToJType(String cls){
		Class c = Class.class;
		
		cls = cls.toUpperCase();
		switch(cls){
			case "INT":		c = int.class; 			break;
			case "BOOL":	c = boolean.class; 		break;
			case "LONG":	c = long.class;			break;
			case "DOUBLE":	c = double.class;		break;
			case "FLOAT":   c = float.class; 		break;
			case "STRING": 	c = String.class; 		break;
			case "POINTER": c = Pointer.class; 		break;
			
		}
		
		return c;
	}
	
	
	public static Object convertToJValue(Memory value){
		Object val = null;
		
		switch(value.getRealType()){
			case INT:		val = value.toInteger();	break;
			case BOOL:		val = value.toBoolean(); 	break;
			case DOUBLE:	val = value.toDouble();		break;
			case STRING: 	val = value.toString();		break;
		}
		
		return val;
	}
	
	
	public static ByReference convertToJRefObject(String cls){
		ByReference c = null;
		
		cls = cls.toUpperCase();
		switch(cls){
			case "INT":		c = new IntByReference(); 			break;
			case "LONG":	c = new LongByReference();			break;
			case "DOUBLE":	c = new DoubleByReference();		break;
			case "FLOAT":   c = new FloatByReference(); 		break;
			case "STRING": 	c = new StringByReference(); 		break;
			case "POINTER": c = new PointerByReference(); 		break;
			
		}
		
		return c;
	}
	
	
	public static ByReference setValueToJRefValue(ByReference refval, Object value){
		ByReference returnValue = null;
		Class cls = refval.getClass();
		
		if(cls==IntByReference.class){
			IntByReference _refval = (IntByReference)refval;
			_refval.setValue((int)value);
			returnValue = _refval;
		}else if(cls==LongByReference.class){
			LongByReference _refval = (LongByReference)refval;
			_refval.setValue((long)value);
			returnValue = _refval;
		}else if(cls==DoubleByReference.class){
			DoubleByReference _refval = (DoubleByReference)refval;
			_refval.setValue((double)value);
			returnValue = _refval;
		}else if(cls==FloatByReference.class){
			FloatByReference _refval = (FloatByReference)refval;
			_refval.setValue((float)value);
			returnValue = _refval;
		}else if(cls==StringByReference.class){
			StringByReference _refval = (StringByReference)refval;
			_refval.setValue((String)value);
			returnValue = _refval;
		}else if(cls==PointerByReference.class){
			PointerByReference _refval = (PointerByReference)refval;
			_refval.setValue((Pointer)value);
			returnValue = _refval;
		}

		return returnValue;
	}
	
	
	
	public static Object getValueToJRefValue(ByReference refval){
		Object returnValue = null;
		Class cls = refval.getClass();
		
		if(cls==IntByReference.class){
			IntByReference _refval = (IntByReference)refval;
			returnValue = _refval.getValue();
		}else if(cls==LongByReference.class){
			LongByReference _refval = (LongByReference)refval;
			returnValue = _refval.getValue();
		}else if(cls==DoubleByReference.class){
			DoubleByReference _refval = (DoubleByReference)refval;
			returnValue = _refval.getValue();
		}else if(cls==FloatByReference.class){
			FloatByReference _refval = (FloatByReference)refval;
			returnValue = _refval.getValue();
		}else if(cls==StringByReference.class){
			StringByReference _refval = (StringByReference)refval;
			returnValue = _refval.getValue();
		}else if(cls==PointerByReference.class){
			PointerByReference _refval = (PointerByReference)refval;
			returnValue = _refval.getValue();;
		}
		
		return returnValue;
	}
	
	
	
	
	
	
	
	
	
	public static Object[] addItemToObject(Object[] obj, Object newObj) {
		ArrayList<Object> temp = new ArrayList<Object>(Arrays.asList(obj));
		temp.add(newObj);
		return temp.toArray();
	}
	
	
	public static Class[] addItemToClass(Class[] cls, Class newCls) {
		ArrayList<Class> temp = new ArrayList<Class>(Arrays.asList(cls));
		temp.add(newCls);
		return temp.toArray(cls);
	}
	
	
	
	
	
	
	
	/*** StringByReference ***/
	public static class StringByReference extends ByReference {
		public StringByReference() {
			this(0);
		}

		public StringByReference(int size) {
			super(size < 4 ? 4 : size);
			getPointer().clear(size < 4 ? 4 : size);
		}

		public StringByReference(String str) {
			super(str.length() < 4 ? 4 : str.length() + 1);
			setValue(str);
		}

		private void setValue(String str) {
			getPointer().setString(0, str);
		}

		public String getValue() {
			return getPointer().getString(0);
		}
	}	
	/*** StringByReference ***/
	
	
	
	
	
	/* Class DFFIStruct*/
	
	public static Structure createStructure(String className, Class[] classes){
        return allocate(className, classes);
	}
	
	/* Class DFFIStruct END*/
	
	public static class AutoStructure extends Structure {
        @Override
        protected java.util.List getFieldOrder() {
            Field[] declared = getClass().getDeclaredFields();
            ArrayList<String> ordered = new ArrayList<>(declared.length);
            for (Field f : declared)
                ordered.add(f.getName());
            return ordered;
        }
    }
    
    public static Structure allocate(final String name, Class... classes) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            dos.writeInt(0xCAFEBABE);
            dos.writeShort(0);
            dos.writeShort(49); // Java 5

            ByteArrayOutputStream constants = new ByteArrayOutputStream();
            DataOutputStream con = new DataOutputStream(constants);
            AtomicInteger idx = new AtomicInteger(0);

            int superClass = classref(con, idx, AutoStructure.class.getName());
            int thisClass = classref(con, idx, name);

            ByteArrayOutputStream methods = new ByteArrayOutputStream();
            DataOutputStream met = new DataOutputStream(methods);
            met.writeShort(1);
            met.writeShort(0x0001);
            met.writeShort(utf(con, idx, "<init>"));
            met.writeShort(utf(con, idx, "()V"));
            met.writeShort(1);

            met.writeShort(utf(con, idx, "Code"));
            met.writeInt(2 + 2 + 4 + 1 + 1 + 2 + 1 + 2 + 2); // LENGTH
            met.writeShort(1); // 1 stack and locals
            met.writeShort(1);

            met.writeInt(1 + 1 + 2 + 1);
            met.writeByte(42); // aload_0
            met.writeByte(183); // invokespecial super init
            met.writeShort(methodref(con, idx, AutoStructure.class.getName(), "<init>", "()V"));
            met.writeByte(177); // return

            met.writeShort(0); // No exceptions or attributes
            met.writeShort(0);

            ByteArrayOutputStream fields = new ByteArrayOutputStream();
            DataOutputStream fie = new DataOutputStream(fields);

            fie.writeShort(classes.length);
            for (Class d : classes) {
                fie.writeShort(0x0001); // ACC_PUBLIC
                // Random name
                fie.writeShort(utf(con, idx, UUID.randomUUID().toString()));
                HashMap<String, String> typeMap = new HashMap<>();
                typeMap.put("int", "I");
                typeMap.put("boolean", "Z");
                typeMap.put("byte", "B");
                typeMap.put("char", "C");
                typeMap.put("short", "S");
                typeMap.put("double", "D");
                typeMap.put("float", "F");
                typeMap.put("long", "J");

                String descriptor = typeMap.get(d.getName());
                if (descriptor == null)
                    descriptor = String.format("L%s;", d.getName().replace(".", "/"));
                fie.writeShort(utf(con, idx, descriptor));
                fie.writeShort(0); // No attributes
            }

            dos.writeShort(idx.get() + 1);
            dos.write(constants.toByteArray());

            // Class
            dos.writeShort(0x0001); // Public
            dos.writeShort(thisClass);
            dos.writeShort(superClass);
            dos.writeShort(0); // No implemented interfaces
            dos.write(fields.toByteArray());
            dos.write(methods.toByteArray());
            dos.writeShort(0); // No attributes

            return (Structure) new ClassLoader() {
                public Class defineClass(byte[] bytes) {
                    return super.defineClass(name, bytes, 0, bytes.length);
                }
            }.defineClass(baos.toByteArray()).newInstance();
        } catch (IOException | ReflectiveOperationException e) {
            return null;
        }
    }

    private static int classref(DataOutputStream out, AtomicInteger size, String id) throws IOException {
        int utf = utf(out, size, id.replace(".", "/"));
        out.writeByte(7); // Class pointing to ^
        out.writeShort(utf);
        return size.addAndGet(1);
    }

    private static int utf(DataOutputStream out, AtomicInteger size, String id) throws IOException {
        out.writeByte(1); // UTF-8
        out.writeUTF(id);
        return size.addAndGet(1);
    }

    private static int methodref(DataOutputStream out, AtomicInteger size, String clazz, String name, String descriptor) throws IOException {
        int classIdx = classref(out, size, clazz);
        int descIdx = nametype(out, size, name, descriptor);
        out.writeByte(10); // Methodref
        out.writeShort(classIdx);
        out.writeShort(descIdx);
        return size.addAndGet(1);
    }

    private static int nametype(DataOutputStream out, AtomicInteger size, String name, String descriptor) throws IOException {
        int nameIdx = utf(out, size, name);
        int descIdx = utf(out, size, descriptor);
        out.writeByte(12); // Nametype
        out.writeShort(nameIdx);
        out.writeShort(descIdx);
        return size.addAndGet(1);
    }
}