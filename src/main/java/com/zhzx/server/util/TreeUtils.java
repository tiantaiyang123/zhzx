package com.zhzx.server.util;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 王志斌
 * @Date: 2021/12/29 上午10:20
 */
public class TreeUtils {
    public TreeUtils() {
    }

    public static <T> List<T> listToTree(List<T> items) {
        try {
            List<T> roots = (List)items.stream().filter((t) -> {
                try {
                    Method method = t.getClass().getMethod("getParentId");
                    Object object = method.invoke(t);
                    return null == object || object.equals(0L);
                } catch (Exception var3) {
                    var3.printStackTrace();
                    return false;
                }
            }).collect(Collectors.toList());
            Iterator var2 = roots.iterator();

            while(var2.hasNext()) {
                T root = (T) var2.next();
                setChildren(root, items);
            }

            return roots;
        } catch (Exception var4) {
            return null;
        }
    }

    public static <T> void setChildren(T root, List<T> items) {
        Class<?> clazz = root.getClass();
        List<T> children = (List)items.stream().filter((t) -> {
            try {
                Method getParentId = t.getClass().getMethod("getParentId");
                Object parentId = getParentId.invoke(t);
                Method getId = clazz.getMethod("getId");
                Object id = getId.invoke(root);
                return id.equals(parentId);
            } catch (Exception var7) {
                var7.printStackTrace();
                return false;
            }
        }).collect(Collectors.toList());
        if (children != null && !children.isEmpty()) {
            try {
                Method setChildren = clazz.getMethod("setChildren", List.class);
                setChildren.invoke(root, children);
                Iterator var5 = children.iterator();

                while(var5.hasNext()) {
                    T child = (T) var5.next();
                    setChildren(child, items);
                }
            } catch (Exception var7) {
                return;
            }
        }

    }
}
