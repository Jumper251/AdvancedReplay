package me.jumper251.replay.utils;

import org.bukkit.block.Block;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ReflectionHelper {

    private static ReflectionHelper instance;

    private Class<?> materialClass;

    private Class<?> blockClass;

    private Class<?> blockDataClass;


    private Method matchMaterial;

    private Method getBlockData;

    private Method blockDataGetMaterial;

    private ReflectionHelper() {
        this.initializeClasses();

        this.initializeMethods();
    }

    public static ReflectionHelper getInstance() {
        if (instance == null) instance = new ReflectionHelper();

        return instance;
    }

    private void initializeClasses() {
        try {
            this.materialClass = Class.forName("org.bukkit.Material");
            this.blockClass = Class.forName("org.bukkit.block.Block");
            this.blockDataClass = Class.forName("org.bukkit.block.data.BlockData");


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeMethods() {
        try {
            this.matchMaterial = this.materialClass.getMethod("matchMaterial", String.class, boolean.class);
            this.getBlockData = this.blockClass.getMethod("getBlockData");
            this.blockDataGetMaterial = this.blockDataClass.getMethod("getMaterial");

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public Object matchMaterial(String material, boolean legacy) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return this.matchMaterial.invoke(null, material, legacy);
    }

    public Object getBlockData(Block block) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return this.getBlockData.invoke(block);
    }

    public Object getBlockDataMaterial(Object blockData) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return this.blockDataGetMaterial.invoke(blockData);
    }


}
