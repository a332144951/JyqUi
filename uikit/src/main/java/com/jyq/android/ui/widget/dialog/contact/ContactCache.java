package com.jyq.android.ui.widget.dialog.contact;

/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
           佛祖保佑       永无BUG
 */

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jyq.android.common.cache.ACache;
import com.jyq.android.common.cache.CacheKit;
import com.jyq.android.net.cache.HttpCache;
import com.jyq.android.net.modal.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/3/29.
 */

class ContactCache {
    private static final ContactCache ourInstance = new ContactCache();

    static ContactCache getInstance() {
        return ourInstance;
    }

    ACache aCache;
    final String KEY_CACHE_RECENT = "cache_recent_contacts";

    private ContactCache() {
        aCache = CacheKit.getInstance().getExternalCache();
    }

    public void updateRecentContacts(ArrayList<User> contact) {
        ArrayList<User> cache = getRecentContacts(null);
        cache.addAll(contact);
        aCache.put(KEY_CACHE_RECENT+"_"+ HttpCache.getInstance().getLoginUser().logicId, new Gson().toJson(cache));
    }

    public ArrayList<User> getRecentContacts(final List<String> filterRoles) {
        ArrayList<User> cache = new Gson().fromJson(aCache.getAsJSONArray(KEY_CACHE_RECENT+"_"+ HttpCache.getInstance().getLoginUser().logicId), new TypeToken<ArrayList<User>>() {
        }.getType());
        final ArrayList<User> contacts=HttpCache.getInstance().getContacts();
        return Lists.newArrayList(Collections2.filter(cache, new Predicate<User>() {
                    @Override
                    public boolean apply(User input) {
                        if (!"baby".equals(input.role)){
                            if (!contacts.contains(input)){
                                return false;
                            }
                        }
                        if (filterRoles == null) {
                            return true;
                        }
                        return filterRoles.contains(input.role);
                    }
                })
        );
    }
}
