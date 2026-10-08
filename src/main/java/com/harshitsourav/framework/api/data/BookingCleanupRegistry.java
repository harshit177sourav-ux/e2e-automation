package com.harshitsourav.framework.api.data;

import java.util.ArrayList;
import java.util.List;

public class BookingCleanupRegistry {
    private static final ThreadLocal<List<Integer>> IDS = new ThreadLocal<List<Integer>>() {
        @Override
        protected List<Integer> initialValue() {
            return new ArrayList<>();
        }
    };

    private BookingCleanupRegistry() {

    }

    public static void add(int id) {
        IDS.get().add(id);
    }

    public static void remove(int id) {
        IDS.get().remove(Integer.valueOf(id));
    }

    public static List<Integer> drain() {
        List<Integer> ids = new ArrayList<>(IDS.get());
        IDS.remove();
        return ids;
    }

    public static List<Integer> getAllCurrentIds() {
        return IDS.get();
    }
}
