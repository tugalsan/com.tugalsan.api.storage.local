package com.tugalsan.api.storage.local.client;

import com.google.gwt.storage.client.*;
import com.tugalsan.api.callable.client.TGS_CallableType0Void;

import com.tugalsan.api.list.client.*;
import com.tugalsan.api.log.client.*;
import com.tugalsan.api.thread.client.*;
import java.util.*;
import java.util.stream.*;

public class TGC_StorageLocalUtils {

    final private static TGC_Log d = TGC_Log.of(TGC_StorageLocalUtils.class);

    public static boolean supported() {
        return Storage.isLocalStorageSupported();
    }

    public static int size() {
        return of().getLength();
    }

    public static List<String> names() {
        List<String> names = TGS_ListUtils.of();
        IntStream.range(0, size()).forEachOrdered(i -> names.add(of().key(i)));
        return names;
    }

    public static void clear() {
        of().clear();
    }

    public static void clear(CharSequence startsWith) {
        var str = startsWith.toString();
        names().forEach(param -> {
            if (param.startsWith(str)) {
                del(param);
            }
        });
    }

    public static void del(CharSequence label) {
        of().removeItem(label.toString());
    }

    public static String get(CharSequence label) {
        return of().getItem(label.toString());
    }

    public static void set(CharSequence param, CharSequence value) {
        of().setItem(param.toString(), value.toString());
    }

    public static void afterSet(CharSequence param, TGS_CallableType0Void exe) {
        var duration = 1;
        TGC_ThreadUtils.run_afterSeconds_afterGUIUpdate(t -> {
            var val = get(param);
            if (val == null) {
                t.run_afterSeconds(duration);
                return;
            }
            d.ci("afterSet", param, val);
            exe.run();
        }, duration);
    }

    private static Storage of() {
        return Storage.getLocalStorageIfSupported();
    }
//    public static native String native_localStorage() /*-{
//        return "" + localStorage;
//    }-*/;
//
}
