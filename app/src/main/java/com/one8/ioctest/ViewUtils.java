package com.one8.ioctest;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ViewUtils {
    public static void inject(Activity activity){
        inject(new ViewFinder(activity),activity);
    }

    public static void inject(View view){
        inject(new ViewFinder(view),view);
    }

    public static void inject(View view,Object object){
        inject(new ViewFinder(view),object);
    }

    //兼容上面三个方法，统一处理
    private static void inject(ViewFinder finder,Object object){
        injectField(finder,object);
        injectEvent(finder,object);
    }

    //注入事件
    private static void injectEvent(ViewFinder finder, Object object) {
        //1、获取类里面的所有方法
        Class<?> clazz = object.getClass();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            if(onClick!=null){
                int[] valueIds = onClick.value();
                for (int valueId : valueIds) {
                    //3、findViewById找到View
                    View view = finder.findViewById(valueId);

                    //扩展功能-检测网络
                    boolean ifCheckNet = method.getAnnotation(CheckNet.class)!=null;

                    if(view!=null){
                        //4、view设置OnClickListener
                        view.setOnClickListener(new DeclaredOnClickListener(method,object,ifCheckNet));
                    }
                }
            }
        }

    }

    private static class DeclaredOnClickListener implements View.OnClickListener {
        private Object mObject;
        private Method mMethod;
        private boolean mIfCheckNet;
        public DeclaredOnClickListener(Method method, Object object, boolean ifCheckNet) {
            mObject = object;
            mMethod = method;
            mIfCheckNet = ifCheckNet;
        }

        @Override
        public void onClick(View v) {
            //是否需要检查网络
            if(mIfCheckNet){
                if(!NetworkUtil.isNetWorkConnected(v.getContext())){
                    Toast.makeText(v.getContext(),"请检查网络连接",0).show();
                    return;
                }
            }
            try {
                //所有方法都可以 包括私有与公有
                mMethod.setAccessible(true);
                mMethod.invoke(mObject,v);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    //注入属性
    private static void injectField(ViewFinder finder, Object object) {
        //1、获取类里面的所有属性
        Class<?> clazz = object.getClass();
        //获取所有属性，包括公有与私有
        Field[] declaredFields = clazz.getDeclaredFields();
        //2、获取ViewById里的value值
        for (Field field : declaredFields) {
            ViewById viewById = field.getAnnotation(ViewById.class);
            if(viewById!=null){
                //获取注解中的id值->R.id.tv
                int value = viewById.value();
                //3、findViewById找到View
                View view = finder.findViewById(value);
                if(view!=null){
                    //能够注入所有修饰符
                    field.setAccessible(true);
                    //4、动态的注入找到的View
                    try {
                        field.set(object,view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }
}
